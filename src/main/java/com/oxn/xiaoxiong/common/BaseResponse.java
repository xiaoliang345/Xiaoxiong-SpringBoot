package com.oxn.xiaoxiong.common;

import com.oxn.xiaoxiong.enums.StatusCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponse<T> implements Serializable {

    private int code;
    private String message;
    private T data;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(StatusCode statusCode) {
        this(statusCode.getCode(), null, statusCode.getMessage());
    }
}
