package com.rsg.webflux.model;

public interface IResponseBody<T> {
    T getData();

    String getMessage();
}
