package com.rea_lity.state;

import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.Channel;
import org.bsc.langgraph4j.state.Channels;

import java.util.Map;

public class AiAgentContext extends AgentState {
    public static final String CONTEXT_KEY = "context";

    public static final Map<String, Channel<?>> SCHEMA = Map.of();


    public AiAgentContext(Map<String, Object> initData) {
        super(initData);
    }

    public WorkFlowContext context() {
        return (WorkFlowContext) this.data().get(CONTEXT_KEY);
    }
}
