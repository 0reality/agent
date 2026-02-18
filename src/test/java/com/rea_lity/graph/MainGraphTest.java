package com.rea_lity.graph;

import com.rea_lity.state.AiAgentContext;
import com.rea_lity.state.WorkFlowContext;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MainGraphTest {

    @Test
    void createMianGraph() throws GraphStateException {
        StateGraph<AiAgentContext> mianGraph = MainGraph.createMianGraph();
        CompiledGraph<AiAgentContext> compile = mianGraph.compile();
        WorkFlowContext initData = WorkFlowContext.builder()
                .nodesOutput(new ArrayList<>())
                .conversationId(1L)
                .currentStepCount(0)
                .initPrompt("帮我构建一个前端博客项目来展示我的博客")
                .build();
        for (var item : compile.stream( Map.of( AiAgentContext.CONTEXT_KEY, initData ) ) ) {
            System.out.println( item );
        }

        WorkFlowContext initData1 = WorkFlowContext.builder()
                .nodesOutput(new ArrayList<>())
                .conversationId(1L)
                .currentStepCount(0)
                .initPrompt("在帮我设计一个登录页面在里面吧")
                .build();
        for (var item : compile.stream( Map.of( AiAgentContext.CONTEXT_KEY, initData1 ) ) ) {
            System.out.println( item );
        }

        WorkFlowContext initData2 = WorkFlowContext.builder()
                .nodesOutput(new ArrayList<>())
                .conversationId(1L)
                .currentStepCount(0)
                .initPrompt("就根据这个设计帮我构建项目吧")
                .build();
        for (var item : compile.stream( Map.of( AiAgentContext.CONTEXT_KEY, initData2 ) ) ) {
            System.out.println( item );
        }


    }
}