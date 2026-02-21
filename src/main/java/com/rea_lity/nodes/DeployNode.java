package com.rea_lity.nodes;

import com.rea_lity.common.SseEmitterContextHolder;
import com.rea_lity.modle.enums.MessageTypeEnum;
import com.rea_lity.service.ChatHistoryService;
import com.rea_lity.state.AiAgentContext;
import com.rea_lity.state.WorkFlowContext;
import com.rea_lity.tools.VueProjectBuilder;
import com.rea_lity.utils.SseEmitterSendUtil;
import dev.langchain4j.model.openai.internal.chat.AssistantMessage;
import dev.langchain4j.model.openai.internal.chat.Message;
import dev.langchain4j.model.openai.internal.chat.SystemMessage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DeployNode implements NodeAction<AiAgentContext> {

    public static final String NODE_NAME = "DeployNode";

    @Resource
    private ChatHistoryService chatHistoryService;

    @Override
    public Map<String, Object> apply(AiAgentContext aiAgentContext) throws Exception {
        WorkFlowContext context = aiAgentContext.context();
        log.info("开始执行部署节点");
        SseEmitter sseEmitter = SseEmitterContextHolder.get(aiAgentContext.context().getConversationId());
        SseEmitterSendUtil.send(sseEmitter, MessageTypeEnum.NODE, "开始执行部署节点");
        chatHistoryService.addHistory(context.getConversationId(),SystemMessage.builder().content("开始执行部署节点").build());
        try {
            Long conversationId = context.getConversationId();
            VueProjectBuilder vueProjectBuilder = new VueProjectBuilder();
            vueProjectBuilder.build(conversationId, "/");
            SseEmitterSendUtil.send(sseEmitter, MessageTypeEnum.NODE, "部署成功");
            chatHistoryService.addHistory(context.getConversationId(),SystemMessage.builder().content("部署成功").build());
        } catch (Exception e) {
            log.error("执行部署节点失败", e);
            chatHistoryService.addHistory(context.getConversationId(),SystemMessage.builder().content("执行部署节点失败").build());
            context.getNodesOutput().add("执行部署节点失败");
        }
        context.setNodeName(NODE_NAME);
        return Map.of(AiAgentContext.CONTEXT_KEY, context);
    }
}
