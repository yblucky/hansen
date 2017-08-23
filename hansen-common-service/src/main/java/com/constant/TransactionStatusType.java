package com.constant;

public enum TransactionStatusType {
    UNCHECKED(1, "未确认"),
    CHECKING(2, "确认中"),
    CHECKED(3, "已确认"),
    ERROR(4, "请求异常"),;

    public int code;
    public String message;

    private TransactionStatusType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
