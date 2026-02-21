package com.rea_lity.utils;

import com.rea_lity.modle.enums.MessageTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
public class SseEmitterSendUtil {
    public static void send(SseEmitter sseEmitter, MessageTypeEnum msgType, String msg) {
        if(sseEmitter == null)return;
        try {
            sseEmitter.send(SseEmitter.event()
                    .name(msgType.name())
                    .data(msg));
        } catch (Exception e) {
            sseEmitter.completeWithError(e);
            log.error("SseEmitterSendUtil.send error", e);
        }
    }
}
