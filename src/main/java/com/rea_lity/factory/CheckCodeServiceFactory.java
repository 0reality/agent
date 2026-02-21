package com.rea_lity.factory;

import com.rea_lity.AiService.CheckCodeService;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CheckCodeServiceFactory {
    @Resource
    private ChatModel chatModel;

    @Resource
    private StreamingChatModel streamingChatModel;

    @Bean
    public CheckCodeService checkCodeService() {

        return AiServices.builder(CheckCodeService.class)
                .chatModel(chatModel)
                .build();
    }

    @Bean
    public CheckCodeService checkCodeServiceStream() {
        return AiServices.builder(CheckCodeService.class)
                .streamingChatModel(streamingChatModel)
                .build();
    }
}
