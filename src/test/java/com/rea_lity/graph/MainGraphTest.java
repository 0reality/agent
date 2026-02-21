package com.rea_lity.graph;

import com.rea_lity.state.AiAgentContext;
import com.rea_lity.state.WorkFlowContext;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import jakarta.annotation.Resource;
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

    @Resource
    ChatMemoryProvider chatMemoryProvider;

    @Test
    void test() throws GraphStateException {
        StateGraph<AiAgentContext> mianGraph = MainGraph.createMianGraph();
        CompiledGraph<AiAgentContext> compile = mianGraph.compile();
    }

    @Test
    void createMianGraph() throws GraphStateException {
        StateGraph<AiAgentContext> mianGraph = MainGraph.createMianGraph();
        CompiledGraph<AiAgentContext> compile = mianGraph.compile();
        WorkFlowContext initData = WorkFlowContext.builder()
                .nodesOutput(new ArrayList<>())
                .conversationId(3L)
                .currentStepCount(0)
                .initPrompt("帮我设计一个前端博客项目来展示我的博客")
                .build();
        for (var item : compile.stream( Map.of( AiAgentContext.CONTEXT_KEY, initData ) ) ) {
            System.out.println( item.node() );
        }

        ChatMemory chatMemory = chatMemoryProvider.get(1L);
        chatMemory.messages().forEach(System.out::println);


        WorkFlowContext initData2 = WorkFlowContext.builder()
                .nodesOutput(new ArrayList<>())
                .conversationId(3L)
                .currentStepCount(0)
                .initPrompt("就根据这个设计帮我构建项目吧")
                .build();
        for (var item : compile.stream( Map.of( AiAgentContext.CONTEXT_KEY, initData2 ) )) {
            System.out.println( item.node() );
        }

        chatMemory.messages().forEach(System.out::println);

    }
}