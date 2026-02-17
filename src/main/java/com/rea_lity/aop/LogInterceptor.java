package com.rea_lity.aop;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.lang.UUID;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

/**
 * 日志切面类，用于处理日志拦截
 */
@Aspect
@Component
@Slf4j
public class LogInterceptor {

    @Around("execution(* com.rea_lity.controller.*.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        // 开启定时器
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 获取请求路径
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        String url = httpServletRequest.getRequestURI();
        String requestId = UUID.randomUUID().toString();
        // 获取请求参数
        Object[] args = joinPoint.getArgs();
        String argsStr = "[" + StringUtil.join(args, ", ") + "]";
        log.info("请求开始，请求路径：{}, 请求参数：{}, 请求ID: {}, 请求IP： {}", url, argsStr, requestId, httpServletRequest.getRemoteHost());

        // 执行方法
        Object result = joinPoint.proceed(args);

        // 停止计时器
        stopWatch.stop();
        log.info("请求结束，请求路径：{}, 请求ID: {}, 执行时间: {}ms", url, requestId, stopWatch.getTotalTimeMillis());
        return result;
    }

}
