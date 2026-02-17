package com.rea_lity.nodes;

import com.rea_lity.AiService.RouterNodeService;
import com.rea_lity.factory.RouterNodeServiceFactory;
import com.rea_lity.modle.enums.RouterEnums;
import com.rea_lity.state.AiAgentContext;
import com.rea_lity.state.WorkFlowContext;
import jakarta.annotation.Resource;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 路由节点
 */
@Component
public class RouterNode {

    @Resource
    private RouterNodeService routerNodeService;

    public RouterEnums apply(AiAgentContext aiAgentContext) {
        try {
            WorkFlowContext context = aiAgentContext.context();
            return routerNodeService.route(context.getConversationId(),context.getInitPrompt());
        } catch (Exception e){
            return RouterEnums.ERROR;
        }
    }
}
