package com.rea_lity.common;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class SseEmitterContextHolder {
    
    private static final ThreadLocal<SseEmitter> CONTEXT_HOLDER = new ThreadLocal<>();
    
    public static void set(SseEmitter emitter) {
        CONTEXT_HOLDER.set(emitter);
    }
    
    public static SseEmitter get() {
        return CONTEXT_HOLDER.get();
    }
    
    public static void clear() {
        CONTEXT_HOLDER.remove();
    }
}