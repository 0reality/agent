package com.rea_lity.controller;

import com.rea_lity.common.SseEmitterContextHolder;
import com.rea_lity.graph.MainGraph;
import com.rea_lity.modle.enums.MessageTypeEnum;
import com.rea_lity.state.AiAgentContext;
import com.rea_lity.state.WorkFlowContext;
import com.rea_lity.utils.SseEmitterSendUtil;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.StateGraph;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.Map;

@RestController
@Slf4j
class WorkFlowController {
    @GetMapping("/workflow")
    public SseEmitter workflow(Long conversationId, String prompt) {

        SseEmitter emitter = new SseEmitter(0L); // 设置为永不超时

        // 创建一个不包含 SseEmitter 的初始数据
        WorkFlowContext initData = WorkFlowContext.builder()
                .nodesOutput(new ArrayList<>())
                .conversationId(conversationId)
                .currentStepCount(0)
                .initPrompt(prompt)
                .stream(true)
                .build();

        // 使用 ThreadLocal 存储 emitter，避免序列化问题
        SseEmitterContextHolder.set(emitter);

        try {
            StateGraph<AiAgentContext> mainGraph = MainGraph.createMianGraph();
            CompiledGraph<AiAgentContext> compile = mainGraph.compile();

            for (var item : compile.stream(Map.of(AiAgentContext.CONTEXT_KEY, initData))) {
                Integer currentStepCount = item.state().context().getCurrentStepCount();

                // 从 ThreadLocal 获取 emitter
                SseEmitter currentEmitter = SseEmitterContextHolder.get();
                if (currentEmitter != null) {
                    SseEmitterSendUtil.send(currentEmitter, MessageTypeEnum.SYSTEM,
                            "第" + currentStepCount + "步：" + item.node() + "开始执行");
                }

                currentStepCount += 1;
                item.state().context().setCurrentStepCount(currentStepCount);
            }
        } catch (Exception e) {
            log.error("工作流执行异常", e);
            SseEmitter currentEmitter = SseEmitterContextHolder.get();
            if (currentEmitter != null) {
                SseEmitterSendUtil.send(currentEmitter, MessageTypeEnum.ERROR, "工作流执行异常: " + e.getMessage());
            }
        } finally {
            // 清理 ThreadLocal
            SseEmitterContextHolder.clear();
        }

        return emitter;
    }

}
