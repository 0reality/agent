package com.rea_lity.nodes;

import com.rea_lity.AiService.ImageCollectionPlanService;
import com.rea_lity.modle.ImageCollectionPlan;
import com.rea_lity.state.AiAgentContext;
import com.rea_lity.state.WorkFlowContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class ImageCollectionPlanNode implements NodeAction<AiAgentContext> {

    public static final String NODE_NAME = "ImageCollectionPlanNode";

    @Resource
    ImageCollectionPlanService imageCollectionPlanService;

    @Override
    public Map<String, Object> apply(AiAgentContext aiAgentContext) throws Exception {
        WorkFlowContext context = aiAgentContext.context();
        try {
            log.info("开始执行图片收集计划节点");
            ImageCollectionPlan imageCollectionPlan = imageCollectionPlanService.planImageCollection(context.getConversationId(), context.getInitPrompt());
            log.info("图片收集计划结果：{}", imageCollectionPlan);
            context.getNodesOutput().add(imageCollectionPlan.toString());
            context.setImageCollectionPlan(imageCollectionPlan);
        } catch (Exception e) {
            log.error("图片收集计划失败", e);
            context.getNodesOutput().add("图片收集计划失败");
        }
        context.setNodeName(NODE_NAME);
        return Map.of(AiAgentContext.CONTEXT_KEY, context);
    }
}
