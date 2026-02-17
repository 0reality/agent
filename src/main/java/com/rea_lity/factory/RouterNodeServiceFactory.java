package com.rea_lity.factory;

import com.rea_lity.AiService.RouterNodeService;
import com.rea_lity.config.CustomConfig;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RouterNodeServiceFactory {

    @Resource
    CustomConfig customConfig;

    @Bean
    public RouterNodeService routerNodeService() {
        ChatModel chatModel = QwenChatModel.builder()
                .apiKey(customConfig.getDASHSCOPE_API_KEY())
                .modelName("qwen-max")
                .temperature(0.7F)
                .maxTokens(4096)
                .build();
        return AiServices.builder(RouterNodeService.class)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .chatModel(chatModel)
                .build();
    }
}
