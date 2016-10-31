package com.lcs.joke.net.bean.response;

import com.lcs.joke.net.bean.Joke;

import java.util.ArrayList;

public class BaseResponse {
    public String error_code;
    public String reason;
    public ArrayList<Joke> result;
}
