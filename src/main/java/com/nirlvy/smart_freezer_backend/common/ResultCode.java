package com.nirlvy.smart_freezer_backend.common;

public enum ResultCode {
    UNKNOWN_ERROR(-1, "未知错误"),
    SUCCESS(200, "成功"),
    INVALID_PARAMS(400, "错误的参数"),
    FORBIDDEN(403, "权限不足"),
    STSTEM_ERROR(500, "系统错误"),
    USERNAME_OR_PASSWORD_INCORRECT(1000, "用户名或密码错误"),
    USERNAME_OR_PASSWORD_IS_BLANK(1001, "用户名或密码为空"),
    USERNAME_ALREADY_EXISTS(1002, "用户名已存在"),
    TOKEN_NOT_EXISTS(1003, "无token,请重新登录"),
    FAKE_TOKEN(1004, "错误的token"),
    UNKNOWN_USER(1005, "用户不存在，请重新登录"),
    TOKEN_ERROR(1006, "token验证失败，请重新登录"),
    ;

    private Integer code;
    private String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
