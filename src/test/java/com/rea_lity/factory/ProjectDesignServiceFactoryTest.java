package com.rea_lity.factory;

import ch.qos.logback.core.util.TimeUtil;
import com.rea_lity.AiService.ProjectDesignService;
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

import java.sql.Time;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class ProjectDesignServiceFactoryTest {

    @Resource
    private ProjectDesignService projectDesignService;

    @Resource
    private ProjectDesignService projectDesignServiceStream;

    @Test
    void projectDesignService() {
        String design = projectDesignService.design(1L,"我需要一个博客项目来展示我的博客");
        System.out.println(design);
    }
    
    @Test
    void projectDesignServiceStream() throws InterruptedException {
        TokenStream tokenStream = projectDesignServiceStream.designStream(1L,"我需要一个博客项目来展示我的博客");
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
        TimeUnit.SECONDS.sleep(30);
    }

    private void send(MessageTypeEnum messageType, String message) {
        System.out.println(messageType + ": " + message);;
    }
}