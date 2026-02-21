package com.rea_lity.factory;

import com.rea_lity.AiService.ProjectDesignService;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ProjectDesignServiceFactory {

    @Resource
    private ChatModel chatModel;

    @Resource
    private StreamingChatModel streamingChatModel;

    @Resource
    private ChatMemoryProvider chatMemoryProvider;

    @Bean
    public ProjectDesignService projectDesignService() {

        return AiServices.builder(ProjectDesignService.class)
                .chatMemoryProvider(chatMemoryProvider)
                .chatModel(chatModel)
                .build();
    }

    @Bean
    public ProjectDesignService projectDesignServiceStream() {
        return AiServices.builder(ProjectDesignService.class)
                .chatMemoryProvider(chatMemoryProvider)
                .streamingChatModel(streamingChatModel)
                .build();
    }
}
