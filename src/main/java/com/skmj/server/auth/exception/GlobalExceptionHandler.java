package com.skmj.server.auth.exception;

import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public SaResult handleRuntimeException(RuntimeException ex) {
        return SaResult.error(ex.getMessage());
    }
}