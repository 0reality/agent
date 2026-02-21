package com.rea_lity.common;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SseEmitterContextHolder {

    private static final Map<Long, SseEmitter> CONTEXT_HOLDER = new ConcurrentHashMap<>();
    
    public static void set(Long conversationId,SseEmitter emitter) {
        CONTEXT_HOLDER.put(conversationId,emitter);
    }
    
    public static SseEmitter get(Long conversationId) {
        return CONTEXT_HOLDER.get(conversationId);
    }
    
    public static void clear(Long conversationId) {
        CONTEXT_HOLDER.remove(conversationId);
    }
}