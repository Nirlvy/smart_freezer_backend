package com.nirlvy.smart_freezer_backend.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nirlvy.smart_freezer_backend.common.Result;

@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 抛出ServiceException,调用此方法
     * 
     * @param se
     * @return Result
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result handle(ServiceException se) {
        return Result.error(se.getCode(), se.getMessage());
    }
}
