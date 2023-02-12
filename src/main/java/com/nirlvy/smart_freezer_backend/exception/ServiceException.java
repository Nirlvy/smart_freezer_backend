package com.nirlvy.smart_freezer_backend.exception;

import com.nirlvy.smart_freezer_backend.common.ResultCode;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

    private Integer code;

    public ServiceException(ResultCode code, Exception e) {
        super(code.getMsg() + (e != null ? ":" + e.getMessage() : ""));
        this.code = code.getCode();
    }
}
