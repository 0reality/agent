package com.rea_lity.nodes;

import com.rea_lity.AiService.ProjectDesignService;
import com.rea_lity.AiService.RouterNodeService;
import com.rea_lity.modle.enums.RouterEnums;
import com.rea_lity.state.AiAgentContext;
import com.rea_lity.state.WorkFlowContext;
import dev.langchain4j.service.SystemMessage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class ProjectDesignNode implements NodeAction<AiAgentContext> {

    public static final String NODE_NAME = "ProjectDesignNode";

    @Resource
    private ProjectDesignService projectDesignService;

    public Map<String,Object> apply(AiAgentContext aiAgentContext) {
        WorkFlowContext context = aiAgentContext.context();
        try {
            log.info("开始执行项目设计节点");
            String design = projectDesignService.design(context.getConversationId(), context.getInitPrompt());
            context.getNodesOutput().add(design);
            log.info("项目设计结果：{}", design);
        } catch (Exception e){
            log.error("项目设计失败", e);
            context.getNodesOutput().add("项目设计失败");
        } finally {
            context.setNodeName(NODE_NAME);
        }
        return Map.of(AiAgentContext.CONTEXT_KEY, context);
    }
}
