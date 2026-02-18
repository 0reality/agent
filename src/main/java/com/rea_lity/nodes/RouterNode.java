package com.rea_lity.nodes;

import com.rea_lity.AiService.RouterNodeService;
import com.rea_lity.factory.RouterNodeServiceFactory;
import com.rea_lity.modle.enums.RouterEnums;
import com.rea_lity.state.AiAgentContext;
import com.rea_lity.state.WorkFlowContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 路由节点
 */
@Component
@Slf4j
public class RouterNode implements NodeAction<AiAgentContext>{

    public static final String NODE_NAME = "router";

    @Resource
    private RouterNodeService routerNodeService;

    public Map<String, Object> apply(AiAgentContext aiAgentContext) {
        WorkFlowContext context = aiAgentContext.context();
        try {
            RouterEnums routed = routerNodeService.route(context.getConversationId(), context.getInitPrompt());
            context.setRouter(routed);
        } catch (Exception e){
            context.setRouter(RouterEnums.ERROR);
            log.error("路由发生错误：", e);
        } finally {
            context.setNodeName(NODE_NAME);
        }
        return Map.of(AiAgentContext.CONTEXT_KEY, context);
    }
}
