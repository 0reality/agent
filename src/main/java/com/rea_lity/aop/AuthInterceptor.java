package com.rea_lity.aop;

import com.rea_lity.annotations.AuthCheck;
import com.rea_lity.common.ErrorCode;
import com.rea_lity.exception.BusinessException;
import com.rea_lity.modle.entity.User;
import com.rea_lity.modle.enums.UserRoleEnum;
import com.rea_lity.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 权限切面类，用于处理权限拦截
 */
@Aspect
@Component
public class AuthInterceptor{

    @Resource
    UserService userService;

    @Around("@annotation(authCheck)")
    public Object authCheck(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        UserRoleEnum mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();

        // 不需要权限
        if(mustRole == null){
            return joinPoint.proceed();
        }

        // 获取现在登录的用户信息
        User user = userService.getLoginUser(httpServletRequest);
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(user.getUserRole());

        // 没有任何权限
        if(userRoleEnum == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 被禁用的用户没有权限
        if(UserRoleEnum.BAN.equals(userRoleEnum)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 权限不足
        if(UserRoleEnum.ADMIN.equals(mustRole)){
            if(!UserRoleEnum.ADMIN.equals(userRoleEnum)){
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
        }

        return joinPoint.proceed();
    }
}
