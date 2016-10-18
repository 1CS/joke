package com.lcs.joke.net;

import com.lcs.joke.net.bean.response.BaseResponse;
import com.lcs.joke.net.bean.response.JokeResponse;
import com.lcs.joke.net.retrofit.RetrofitSender;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Api {
    public static NetTask getLatestJoke(int pageIndex, NetCallback<BaseResponse<JokeResponse>> callback) {
        Subscription task = RetrofitSender.getInstance()
            .getLatestJoke(pageIndex)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callback);
        return new NetTask(task);
    }
}
