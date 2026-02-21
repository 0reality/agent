package com.rea_lity.nodes;

import cn.hutool.core.io.FileUtil;
import com.rea_lity.AiService.CheckCodeService;
import com.rea_lity.AiService.ProjectDesignService;
import com.rea_lity.common.SseEmitterContextHolder;
import com.rea_lity.constant.CommonConstant;
import com.rea_lity.modle.enums.MessageTypeEnum;
import com.rea_lity.state.AiAgentContext;
import com.rea_lity.state.WorkFlowContext;
import com.rea_lity.utils.SseEmitterSendUtil;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.PartialThinking;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.tool.BeforeToolExecution;
import dev.langchain4j.service.tool.ToolExecution;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Component
public class CheckCodeNode implements NodeAction<AiAgentContext> {

    public static final String NODE_NAME = "CheckCodeNode";

    @Resource
    private CheckCodeService checkCodeService;

    @Resource
    private CheckCodeService checkCodeServiceStream;

    private void chatStream(AiAgentContext aiAgentContext, String prompt) {
        SseEmitter sseEmitter = SseEmitterContextHolder.get(aiAgentContext.context().getConversationId());
        SseEmitterSendUtil.send(sseEmitter, MessageTypeEnum.NODE, "开始执行代码审查节点");
        TokenStream tokenStream = checkCodeServiceStream.checkStream(prompt);
        WorkFlowContext context = aiAgentContext.context();
        CountDownLatch latch = new CountDownLatch(1);
        tokenStream
                .onPartialResponse((String partialResponse) -> {
                    log.info("partialResponse: {}", partialResponse);
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
                    String text = response.aiMessage().text();
                    aiAgentContext.context().setHasError(text.length() >= 20);
                    context.setDeployError(text);
                    latch.countDown();

                })
                .onError((Throwable error) -> {
                    SseEmitterSendUtil.send(sseEmitter, MessageTypeEnum.ERROR, error.getMessage());
                    log.error("ProjectDesignNode error", error);
                })
                .start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String,Object> apply(AiAgentContext aiAgentContext) {
        WorkFlowContext context = aiAgentContext.context();
        try {
            log.info("开始执行代码审查节点");
            String prompt = getCode(context.getConversationId());
            if (!context.getStream()) {
                chat(aiAgentContext, prompt);
            } else {
                chatStream(aiAgentContext, prompt);
            }
        } catch (Exception e){
            log.error("代码审查失败", e);
            context.getNodesOutput().add("代码审查失败");
        } finally {
            context.setNodeName(NODE_NAME);
        }
        return Map.of(AiAgentContext.CONTEXT_KEY, context);


    }

    private void chat(AiAgentContext aiAgentContext, String prompt) {
        String checkCode = checkCodeService.checkCode(prompt);
        aiAgentContext.context().setDeployError(checkCode);
        aiAgentContext.context().setHasError(checkCode.length() >= 20);
    }

    public String getCode(Long conversationId) {
        String path = CommonConstant.ROOT_PATH + conversationId;

        // 读取该文件夹下的所有文件 排除 dist node_modules public 文件夹
        List<File> files = getAllFiles(new File(path));

        StringBuilder sb = new StringBuilder();
        for (File f : files) {
            sb.append("文件名称：");
            sb.append(f.getName());
            sb.append("\n文件内容：");
            sb.append(FileUtil.readUtf8String(f));
        }
        return sb.toString();
    }

    private List<File> getAllFiles(File file) {
        File[] files = file.listFiles();
        List<File> subFiles = new ArrayList<>();
        List<String> ignore = List.of("public","node_modules","dist");
        if (files != null) {
            for (File f : files) {
                if (f.isFile()) {
                    subFiles.add(f);
                } else if(!ignore.contains(f.getName())){
                    subFiles.addAll(getAllFiles(f));
                }
            }
        }
        return subFiles;
    }
}
