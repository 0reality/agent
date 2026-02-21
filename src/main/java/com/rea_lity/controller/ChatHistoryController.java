package com.rea_lity.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rea_lity.modle.dto.ChatHistoryQueryRequest;
import com.rea_lity.modle.entity.ChatHistory;
import com.rea_lity.service.ChatHistoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatHistoryController {

    @Resource
    private ChatHistoryService chatHistoryServiceImpl;

    @PostMapping("/chat")
    public Page<ChatHistory> getChatHistory(@RequestBody ChatHistoryQueryRequest chatHistoryQueryRequest) {
        return chatHistoryServiceImpl.getHistory(chatHistoryQueryRequest);
    }
}
