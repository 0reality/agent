package com.rea_lity.factory;

import com.rea_lity.config.CustomConfig;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatModelConfig {

    @Resource
    CustomConfig customConfig;
    @Bean
    ChatModel chatModel() {
        return QwenChatModel.builder()
                .apiKey(customConfig.getDASHSCOPE_API_KEY())
                .modelName("qwen-flash")
                .temperature(0.7F)
                .maxTokens(8192)
                .build();
    }
}
