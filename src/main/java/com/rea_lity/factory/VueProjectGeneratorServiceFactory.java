package com.rea_lity.factory;

import com.rea_lity.AiService.VueProjectGeneratorService;
import com.rea_lity.config.CustomConfig;
import com.rea_lity.tools.DirectoryReader;
import com.rea_lity.tools.ExistTool;
import com.rea_lity.tools.OperationFile;
import com.rea_lity.tools.VueProjectBuilder;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class VueProjectGeneratorServiceFactory {
    @Resource
    private ChatModel chatModel;

    @Resource
    private StreamingChatModel streamingChatModel;

    @Resource
    private ChatMemoryProvider chatMemoryProvider;

    @Bean
    public VueProjectGeneratorService vueProjectGeneratorService() {
        return AiServices.builder(VueProjectGeneratorService.class)
                .chatMemoryProvider(chatMemoryProvider)
                .chatModel(chatModel)
                .tools(
                        new OperationFile(),
                        new DirectoryReader(),
                        new ExistTool()
                )
                .beforeToolExecution(toolExecutionRequest -> {
                    log.info("Executing tool: {}", toolExecutionRequest);
                })
                .afterToolExecution(toolExecutionRequest -> {
                    log.info("Tool execution result: {}", toolExecutionRequest.result());
                })
                .hallucinatedToolNameStrategy(toolExecutionRequest -> ToolExecutionResultMessage.from(
                        toolExecutionRequest, "Error: there is no tool called " + toolExecutionRequest.name()))
                .build();
    }

    @Bean
    public VueProjectGeneratorService vueProjectGeneratorServiceStream() {
        return AiServices.builder(VueProjectGeneratorService.class)
                .chatMemoryProvider(chatMemoryProvider)
                .streamingChatModel(streamingChatModel)
                .tools(
                        new OperationFile(),
                        new DirectoryReader(),
                        new ExistTool()
                )
                .beforeToolExecution(toolExecutionRequest -> {
                    log.info("Executing tool: {}", toolExecutionRequest);
                })
                .afterToolExecution(toolExecutionRequest -> {
                    log.info("Tool execution result: {}", toolExecutionRequest.result());
                })
                .hallucinatedToolNameStrategy(toolExecutionRequest -> ToolExecutionResultMessage.from(
                        toolExecutionRequest, "Error: there is no tool called " + toolExecutionRequest.name()))
                .build();
    }
}
