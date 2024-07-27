package com.rsg.webflux.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse<T> implements IResponseBody<T> {

    private T data;
    private String message;

    @Override
    public T getData() {
        return this.data;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
