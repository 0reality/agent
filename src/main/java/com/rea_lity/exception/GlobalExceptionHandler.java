package com.rea_lity.exception;

import com.rea_lity.common.BaseResponse;
import com.rea_lity.common.ErrorCode;
import com.rea_lity.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<String> handleException(Exception e) {
        log.error("Exception:", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }


    @ExceptionHandler(BusinessException.class)
    public BaseResponse<String> handleBusinessException(Exception e) {
        log.error("BusinessException: ", e);
        return ResultUtils.error(ErrorCode.OPERATION_ERROR, e.getMessage());
    }
}
