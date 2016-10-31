package com.lcs.joke.net.retrofit;

import com.lcs.joke.net.bean.response.BaseResponse;

import retrofit2.http.GET;
import rx.Observable;

public interface AppServer {
    String JOKE_KEY = "3527d805ea8acdda62a1fd662cb139bf";

    @GET("joke/randJoke.php?key=" + JOKE_KEY)
    Observable<BaseResponse> getJoke();
}
