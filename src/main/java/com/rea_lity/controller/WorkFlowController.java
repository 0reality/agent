package com.rea_lity.controller;

import com.rea_lity.common.SseEmitterContextHolder;
import com.rea_lity.graph.MainGraph;
import com.rea_lity.modle.enums.MessageTypeEnum;
import com.rea_lity.service.ChatHistoryService;
import com.rea_lity.state.AiAgentContext;
import com.rea_lity.state.WorkFlowContext;
import com.rea_lity.utils.SseEmitterSendUtil;
import dev.langchain4j.model.openai.internal.chat.Message;
import dev.langchain4j.model.openai.internal.chat.UserMessage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.StateGraph;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
class WorkFlowController {

    @Resource
    private ChatHistoryService chatHistoryServiceImpl;


    @GetMapping("/workflow")
    public SseEmitter workflow(Long conversationId, String prompt) {

        SseEmitter emitter = new SseEmitter(0L);
        List<Message> messages = new ArrayList<>();
        messages.add(UserMessage.builder().content(prompt).build());
        chatHistoryServiceImpl.addHistory(messages, conversationId);
        WorkFlowContext initData = WorkFlowContext.builder()
                .nodesOutput(new ArrayList<>())
                .conversationId(conversationId)
                .currentStepCount(0)
                .initPrompt(prompt)
                .stream(true)
                .build();

        SseEmitterContextHolder.set(conversationId, emitter);

        CompletableFuture.runAsync(() -> {

            try {
                StateGraph<AiAgentContext> mainGraph = MainGraph.createMianGraph();
                CompiledGraph<AiAgentContext> compile = mainGraph.compile();

                for (var item : compile.stream(
                        Map.of(AiAgentContext.CONTEXT_KEY, initData))) {

                    Integer currentStepCount =
                            item.state().context().getCurrentStepCount();

                    SseEmitter currentEmitter =
                            SseEmitterContextHolder.get(conversationId);

                    if (currentEmitter != null) {
                        SseEmitterSendUtil.send(
                                currentEmitter,
                                MessageTypeEnum.SYSTEM,
                                "第" + currentStepCount + "步：" +
                                        item.node() + "开始执行"
                        );
                    }

                    item.state().context()
                            .setCurrentStepCount(currentStepCount + 1);
                }

                // 👇 正常结束
                SseEmitter currentEmitter =
                        SseEmitterContextHolder.get(conversationId);

                if (currentEmitter != null) {
                    currentEmitter.complete();
                }

            } catch (Exception e) {

                log.error("工作流执行异常", e);

                SseEmitter currentEmitter =
                        SseEmitterContextHolder.get(conversationId);

                if (currentEmitter != null) {
                    SseEmitterSendUtil.send(
                            currentEmitter,
                            MessageTypeEnum.ERROR,
                            "工作流执行异常: " + e.getMessage()
                    );
                    currentEmitter.completeWithError(e);
                }

            } finally {
                SseEmitterContextHolder.clear(conversationId);
            }

        });

        return emitter;
    }

}
