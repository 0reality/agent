package com.rea_lity.nodes;

import com.rea_lity.AiService.ProjectDesignService;
import com.rea_lity.common.SseEmitterContextHolder;
import com.rea_lity.modle.enums.MessageTypeEnum;
import com.rea_lity.service.ChatHistoryService;
import com.rea_lity.state.AiAgentContext;
import com.rea_lity.state.WorkFlowContext;
import com.rea_lity.utils.SseEmitterSendUtil;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.PartialThinking;
import dev.langchain4j.model.openai.internal.chat.AssistantMessage;
import dev.langchain4j.model.openai.internal.chat.Message;
import dev.langchain4j.model.openai.internal.chat.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.tool.BeforeToolExecution;
import dev.langchain4j.service.tool.ToolExecution;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Component
@Slf4j
public class ProjectDesignNode implements NodeAction<AiAgentContext> {

    public static final String NODE_NAME = "ProjectDesignNode";

    @Resource
    private ProjectDesignService projectDesignService;

    @Resource
    private ProjectDesignService projectDesignServiceStream;
    
    @Resource
    private ChatHistoryService chatHistoryService;

    private String chatStream(AiAgentContext aiAgentContext) {
        WorkFlowContext context = aiAgentContext.context();
        SseEmitter sseEmitter = SseEmitterContextHolder.get(context.getConversationId());
        SseEmitterSendUtil.send(sseEmitter, MessageTypeEnum.NODE, "开始执行项目设计节点");
        chatHistoryService.addHistory(context.getConversationId(),SystemMessage.builder().content("开始执行项目设计节点").build());
        TokenStream tokenStream = projectDesignServiceStream.designStream(context.getConversationId(), context.getInitPrompt());
        final String[] design = {null};
        CountDownLatch latch = new CountDownLatch(1);
        tokenStream
                .onPartialResponse((String partialResponse) -> {
                    log.info("ProjectDesignNode partialResponse: {}", partialResponse);
                    SseEmitterSendUtil.send(sseEmitter, MessageTypeEnum.ASSISTANT, partialResponse);
                })
                .onPartialThinking((PartialThinking partialThinking) -> {
                    SseEmitterSendUtil.send(sseEmitter, MessageTypeEnum.THINKING, partialThinking.text());
                })
                // 这将在工具执行之前调用。BeforeToolExecution 包含 ToolExecutionRequest（例如工具名称、工具参数等）
                .beforeToolExecution((BeforeToolExecution beforeToolExecution) -> {
                    SseEmitterSendUtil.send(sseEmitter, MessageTypeEnum.TOOL_CALL, beforeToolExecution.request().name());
                })
                // 这将在工具执行之后调用。ToolExecution 包含 ToolExecutionRequest 和工具执行结果。
                .onToolExecuted((ToolExecution toolExecution) -> {
                    SseEmitterSendUtil.send(sseEmitter, MessageTypeEnum.TOOL_RESPONSE, toolExecution.result());
                })
                .onCompleteResponse((ChatResponse response) -> {
                    chatHistoryService.addHistory(context.getConversationId(),AssistantMessage.builder().content(response.aiMessage().text()).build());
                    design[0] = response.aiMessage().text();
                    latch.countDown();
                })
                .onError((Throwable error) -> {
                    SseEmitterSendUtil.send(sseEmitter, MessageTypeEnum.ERROR, error.getMessage());
                    chatHistoryService.addHistory(context.getConversationId(),SystemMessage.builder().content("设计节点出错：" + error.getMessage()).build());
                    log.error("ProjectDesignNode error", error);
                })
                .start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return design[0];
    }

    public Map<String,Object> apply(AiAgentContext aiAgentContext) {
        WorkFlowContext context = aiAgentContext.context();
        try {
            log.info("开始执行项目设计节点");
            String design = null;
            if (!context.getStream()) {
                design = chat(aiAgentContext);
            } else {
                design = chatStream(aiAgentContext);
            }
            log.info("项目设计结果：{}", design);
        } catch (Exception e){
            log.error("项目设计失败", e);
            context.getNodesOutput().add("项目设计失败");
        } finally {
            context.setNodeName(NODE_NAME);
        }
        return Map.of(AiAgentContext.CONTEXT_KEY, context);


    }

    private String chat(AiAgentContext aiAgentContext) {
        WorkFlowContext context = aiAgentContext.context();
        return projectDesignService.design(context.getConversationId(), context.getInitPrompt());
    }

}
