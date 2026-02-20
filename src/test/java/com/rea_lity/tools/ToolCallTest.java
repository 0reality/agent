package com.rea_lity.tools;

import com.rea_lity.AiService.ToolService;
import com.rea_lity.AiService.VueProjectGeneratorService;
import com.rea_lity.config.CustomConfig;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class ToolCallTest {

    @Resource
    CustomConfig customConfig;

    @Resource
    ChatMemoryProvider chatMemoryProvider;

    @Resource
    ChatModel chatModel;

    @Test
    void testToolCall() {
        VueProjectGeneratorService toolService = AiServices.builder(VueProjectGeneratorService.class)
                .tools(new DirectoryReader(), new OperationFile(), new ExistTool())
                .chatModel(chatModel)
                .beforeToolExecution(toolExecutionRequest -> {
                    log.info("Tool info: {}", toolExecutionRequest);
                })
                .afterToolExecution(toolExecutionRequest -> {
                    log.info("Tool result: {}", toolExecutionRequest);
                })
                .chatMemoryProvider(chatMemoryProvider)
                .hallucinatedToolNameStrategy(toolExecutionRequest -> ToolExecutionResultMessage.from(
                        toolExecutionRequest, "Error: there is no tool called " + toolExecutionRequest.name()))
                .build();
        toolService.generateVueProject(2L, "帮我构建一个博客项目");
    }
}
