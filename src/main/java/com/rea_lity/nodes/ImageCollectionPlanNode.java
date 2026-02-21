package com.rea_lity.nodes;

import com.rea_lity.AiService.ImageCollectionPlanService;
import com.rea_lity.common.SseEmitterContextHolder;
import com.rea_lity.modle.ImageCollectionPlan;
import com.rea_lity.modle.enums.MessageTypeEnum;
import com.rea_lity.service.ChatHistoryService;
import com.rea_lity.state.AiAgentContext;
import com.rea_lity.state.WorkFlowContext;
import com.rea_lity.utils.SseEmitterSendUtil;
import dev.langchain4j.model.openai.internal.chat.Message;
import dev.langchain4j.model.openai.internal.chat.SystemMessage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ImageCollectionPlanNode implements NodeAction<AiAgentContext> {

    public static final String NODE_NAME = "ImageCollectionPlanNode";

    @Resource
    ImageCollectionPlanService imageCollectionPlanService;
    
    @Resource
    private ChatHistoryService chatHistoryService;

    @Override
    public Map<String, Object> apply(AiAgentContext aiAgentContext) throws Exception {
        WorkFlowContext context = aiAgentContext.context();
        SseEmitter sseEmitter = SseEmitterContextHolder.get(aiAgentContext.context().getConversationId());
        SseEmitterSendUtil.send(sseEmitter, MessageTypeEnum.NODE, "开始执行图片收集计划节点");
        try {
            log.info("开始执行图片收集计划节点");
            chatHistoryService.addHistory(context.getConversationId(),SystemMessage.builder().content("开始执行图片收集计划节点").build());
            ImageCollectionPlan imageCollectionPlan = imageCollectionPlanService.planImageCollectionByMemoryId(context.getConversationId(),context.getInitPrompt());
            log.info("图片收集计划结果：{}", imageCollectionPlan);
            SseEmitterSendUtil.send(sseEmitter, MessageTypeEnum.NODE, "图片收集计划结果：" + imageCollectionPlan);
            chatHistoryService.addHistory(context.getConversationId(),SystemMessage.builder().content("图片收集计划结果：" + imageCollectionPlan).build());
            context.setImageCollectionPlan(imageCollectionPlan);
        } catch (Exception e) {
            log.error("图片收集计划失败", e);
            context.getNodesOutput().add("图片收集计划失败");
            SseEmitterSendUtil.send(sseEmitter, MessageTypeEnum.NODE, "图片收集计划失败");
            chatHistoryService.addHistory(context.getConversationId(),SystemMessage.builder().content("图片收集计划失败").build());
        }
        context.setNodeName(NODE_NAME);
        return Map.of(AiAgentContext.CONTEXT_KEY, context);
    }
}
