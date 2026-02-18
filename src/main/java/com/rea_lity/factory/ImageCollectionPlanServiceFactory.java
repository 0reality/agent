package com.rea_lity.factory;

import com.rea_lity.AiService.ImageCollectionPlanService;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageCollectionPlanServiceFactory {

    @Resource
    private ChatModel chatModel;

    @Resource
    private ChatMemoryProvider chatMemoryProvider;

    @Bean
    public ImageCollectionPlanService imageCollectionPlanService() {
        return AiServices.builder(ImageCollectionPlanService.class)
                .chatMemoryProvider(chatMemoryProvider)
                .chatModel(chatModel)
                .build();
    }
}
