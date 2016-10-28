package com.lcs.joke.net.retrofit;

import com.lcs.joke.net.bean.response.BaseResponse;
import com.lcs.joke.net.bean.response.JokeResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface AppServer {
    int PAGE_SIZE = 20;
    String JOKE_KEY = "3527d805ea8acdda62a1fd662cb139bf";

    @GET("joke/content/text.from?key=" + JOKE_KEY + "&page=1&pagesize=" + PAGE_SIZE)
    Observable<BaseResponse<JokeResponse>> getLatestJoke();

    @GET("joke/content/list.from?sort=desc&key=" + JOKE_KEY + "&pagesize=" + PAGE_SIZE)
    Observable<BaseResponse<JokeResponse>> getListJoke(@Query("page") int page, @Query("time") String time);

    @GET("joke/img/list.from?sort=desc&key=" + JOKE_KEY + "&pagesize=" + PAGE_SIZE)
    Observable<BaseResponse<JokeResponse>> getListPic(@Query("page") int page, @Query("time") String time);
}
