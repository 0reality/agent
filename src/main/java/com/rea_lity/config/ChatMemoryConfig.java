package com.rea_lity.config;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Configuration
public class ChatMemoryConfig {
    
    // 全局共享的内存存储
    private final Map<Long, ChatMemory> globalMemoryStore = new ConcurrentHashMap<>();
    
    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> globalMemoryStore.computeIfAbsent((Long) memoryId,
            id -> MessageWindowChatMemory.withMaxMessages(10));
    }
}
