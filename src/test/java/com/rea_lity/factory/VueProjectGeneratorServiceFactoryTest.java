package com.rea_lity.factory;

import com.rea_lity.AiService.VueProjectGeneratorService;
import com.rea_lity.modle.enums.MessageTypeEnum;
import com.rea_lity.utils.SseEmitterSendUtil;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.PartialThinking;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.tool.BeforeToolExecution;
import dev.langchain4j.service.tool.ToolExecution;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VueProjectGeneratorServiceFactoryTest {

    @Resource
    VueProjectGeneratorService vueProjectGeneratorService;
    
    @Resource
    VueProjectGeneratorService vueProjectGeneratorServiceStream;

    @Test
    void vueProjectGeneratorService() {
        vueProjectGeneratorService.generateVueProject(1L, "构建一个博客项目");
    }
    
    @Test
    void vueProjectGeneratorServiceStream() throws InterruptedException {
        TokenStream tokenStream = vueProjectGeneratorServiceStream.generateVueProjectStream(4L, "构建一个博客项目");
        tokenStream
                .onPartialResponse((String partialResponse) -> {
                    send(MessageTypeEnum.ASSISTANT, partialResponse);
                })
                .onPartialThinking((PartialThinking partialThinking) -> {
                    send(MessageTypeEnum.THINKING, partialThinking.text());
                })
                // 这将在工具执行之前调用。BeforeToolExecution 包含 ToolExecutionRequest（例如工具名称、工具参数等）
                .beforeToolExecution((BeforeToolExecution beforeToolExecution) -> {
                    send(MessageTypeEnum.TOOL_CALL, beforeToolExecution.request().name());
                })
                // 这将在工具执行之后调用。ToolExecution 包含 ToolExecutionRequest 和工具执行结果。
                .onToolExecuted((ToolExecution toolExecution) -> {
                    send(MessageTypeEnum.TOOL_RESPONSE, toolExecution.result());
                })
                .onCompleteResponse((ChatResponse response) -> {
                    System.out.println(response.aiMessage().text());
                })
                .onError((Throwable error) -> {
                    send(MessageTypeEnum.ERROR, error.getMessage());
                })
                .start();
        TimeUnit.SECONDS.sleep(120);
    }

    private void send(MessageTypeEnum messageType, String message) {
        System.out.println(messageType + ": " + message);
        System.out.flush();
    }
}