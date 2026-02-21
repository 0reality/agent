package com.rea_lity.config;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rea_lity.constant.CommonConstant;
import com.rea_lity.modle.dto.ChatHistoryQueryRequest;
import com.rea_lity.modle.entity.ChatHistory;
import com.rea_lity.service.ChatHistoryService;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.internal.chat.ToolMessage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Configuration
@Slf4j
public class ChatMemoryConfig {

    @Resource
    private ChatHistoryService chatHistoryService;
    
    // 全局共享的内存存储
    private final Map<Long, ChatMemory> globalMemoryStore = new ConcurrentHashMap<>();
    
    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> globalMemoryStore.computeIfAbsent((Long) memoryId,
                this::create);
    }

    private ChatMemory create(Long memoryId) {
        MessageWindowChatMemory messageWindowChatMemory = MessageWindowChatMemory.withMaxMessages(50);
        ChatHistoryQueryRequest chatHistoryQueryRequest = new ChatHistoryQueryRequest();
        chatHistoryQueryRequest.setAppId(memoryId);
        chatHistoryQueryRequest.setCurrent(1);
        chatHistoryQueryRequest.setPageSize(200);
        chatHistoryQueryRequest.setSortField("createTime");
        chatHistoryQueryRequest.setSortOrder(CommonConstant.SORT_ORDER_DESC);
        Page<ChatHistory> history = chatHistoryService.getHistory(chatHistoryQueryRequest);
        List<ChatHistory> records = history.getRecords().reversed();
        int total = 0;
        for (ChatHistory record : records) {
            if("SYSTEM".equals(record.getMessageType()))continue;
            messageWindowChatMemory.add(UserMessage.from(record.getMessage()));
            total ++;
        }
        log.info("load memoryId: {}, total: {}", memoryId, total);
        return messageWindowChatMemory;
    }
}
