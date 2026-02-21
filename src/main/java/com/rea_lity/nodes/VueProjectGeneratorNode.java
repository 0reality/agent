package com.rea_lity.nodes;

import cn.hutool.core.util.StrUtil;
import com.rea_lity.AiService.VueProjectGeneratorService;
import com.rea_lity.common.SseEmitterContextHolder;
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
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Slf4j
@Component
public class VueProjectGeneratorNode implements NodeAction<AiAgentContext> {

    public static final String NODE_NAME = "VueProjectGeneratorNode";

    @Resource
    private VueProjectGeneratorService vueProjectGeneratorService;

    @Resource
    private VueProjectGeneratorService vueProjectGeneratorServiceStream;

    private String chat(AiAgentContext aiAgentContext, String prompt) {
        WorkFlowContext context = aiAgentContext.context();
        return vueProjectGeneratorService.generateVueProject(context.getConversationId(), prompt);
    }

    @Override
    public Map<String, Object> apply(AiAgentContext aiAgentContext) throws Exception {
        WorkFlowContext context = aiAgentContext.context();
        try {
            log.info("VueProjectGeneratorNode start");
            log.info("当前对话ID: {}", context.getConversationId());
            log.info("图片资源数量: {}", context.getImageResources() != null ? context.getImageResources().size() : 0);
            log.info("部署错误信息: {}", context.getDeployError());
            
            // 构造 prompt - 确保不为空且安全
            String prompt = "请根据用户的要求构建/修改项目，使得他正确的执行。";
            String imageInfo = context.getImageResources() != null ? context.getImageResources().toString() : "无图片资源";
            prompt += "静态资源图片如下：" + imageInfo;
            
            if(StrUtil.isNotBlank(context.getDeployError())) {
                prompt += "部署报错信息如下：" + context.getDeployError();
                context.setDeployError("");
            }
            
            log.info("构造的用户提示词长度: {}", prompt.length());
            log.info("提示词预览: {}", prompt.substring(0, Math.min(100, prompt.length())));
            log.info("准备调用 AI 服务，conversationId: {}", context.getConversationId());

            String s = null;
            if(!context.getStream()) {
                s = chat(aiAgentContext ,prompt);
            } else {
                s = chatStream(aiAgentContext ,prompt);
            }

            log.info("VueProjectGeneratorNode result length: {}", s != null ? s.length() : 0);
            context.getNodesOutput().add(s != null ? s : "生成结果为空");
        } catch (Exception e) {
            log.error("VueProjectGeneratorNode error", e);
            String errorMsg = "Vue项目生成失败: " + e.getMessage();
            context.getNodesOutput().add(errorMsg);
            
            // 如果是配置相关的错误，给出更明确的提示
            if (e instanceof IllegalStateException && e.getMessage().contains("API Key")) {
                log.error("请检查 application.yml 配置文件，确保已正确配置 dashscope.api-key");
            }
        }
        context.setNodeName(NODE_NAME);
        return Map.of(AiAgentContext.CONTEXT_KEY, context);
    }

    private String chatStream(AiAgentContext aiAgentContext, String prompt) {
        WorkFlowContext context = aiAgentContext.context();
        SseEmitter sseEmitter = SseEmitterContextHolder.get();
        TokenStream tokenStream = vueProjectGeneratorServiceStream.generateVueProjectStream(context.getConversationId(), prompt);
        final String[] s = {null};
        tokenStream
                .onPartialResponse((String partialResponse) -> {
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
                    s[0] = response.aiMessage().text();
                })
                .onError((Throwable error) -> {
                    SseEmitterSendUtil.send(sseEmitter, MessageTypeEnum.ERROR, error.getMessage());
                    log.error("ProjectDesignNode error", error);
                })
                .start();
        return s[0];
    }
}
