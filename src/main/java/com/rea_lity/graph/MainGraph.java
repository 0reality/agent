package com.rea_lity.graph;

import com.rea_lity.AiService.ImageCollectionPlanService;
import com.rea_lity.modle.enums.RouterEnums;
import com.rea_lity.nodes.*;
import com.rea_lity.state.AiAgentContext;
import com.rea_lity.state.WorkFlowContext;
import com.rea_lity.utils.SpringContextUtils;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.EdgeAction;

import java.util.Map;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

public class MainGraph {

    public static StateGraph<AiAgentContext> createMianGraph() throws GraphStateException {

        ProjectDesignNode projectDesignNode = SpringContextUtils.getBean(ProjectDesignNode.class);
        RouterNode routerNode = SpringContextUtils.getBean(RouterNode.class);
        ImageCollectionPlanNode imageCollectionPlanNode = SpringContextUtils.getBean(ImageCollectionPlanNode.class);
        ImageCollectionNode imageCollectionNode = SpringContextUtils.getBean(ImageCollectionNode.class);
        VueProjectGeneratorNode vueProjectGeneratorNode = SpringContextUtils.getBean(VueProjectGeneratorNode.class);

        EdgeAction<AiAgentContext> edgeAction = aiAgentContext -> {
            WorkFlowContext context = aiAgentContext.context();
            RouterEnums router = context.getRouter();
            return router.toString();
        };

        return new StateGraph<>(AiAgentContext.SCHEMA, AiAgentContext::new)
                .addNode(ProjectDesignNode.NODE_NAME, node_async(projectDesignNode))
                .addNode(RouterNode.NODE_NAME, node_async(routerNode))
                .addNode(ImageCollectionPlanNode.NODE_NAME, node_async(imageCollectionPlanNode))
                .addNode(ImageCollectionNode.NODE_NAME, node_async(imageCollectionNode))
                .addNode(VueProjectGeneratorNode.NODE_NAME, node_async(vueProjectGeneratorNode))

                // Define edges
                .addEdge(START, RouterNode.NODE_NAME)
                .addConditionalEdges(RouterNode.NODE_NAME, edge_async(edgeAction), Map.of(
                        RouterEnums.ERROR.toString(), END,
                        RouterEnums.EXECUTE.toString(), ImageCollectionPlanNode.NODE_NAME,
                        RouterEnums.PLAN.toString(), ProjectDesignNode.NODE_NAME
                ))
                .addEdge(ProjectDesignNode.NODE_NAME, END)
                .addEdge(ImageCollectionPlanNode.NODE_NAME, ImageCollectionNode.NODE_NAME)
                .addEdge(ImageCollectionNode.NODE_NAME, VueProjectGeneratorNode.NODE_NAME)
                .addEdge(VueProjectGeneratorNode.NODE_NAME, END)
                ;
    }
}
