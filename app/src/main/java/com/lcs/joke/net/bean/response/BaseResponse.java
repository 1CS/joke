package com.lcs.joke.net.bean.response;

public class BaseResponse<T> {
    public String error_code;
    public String reason;
    public T result;
}
