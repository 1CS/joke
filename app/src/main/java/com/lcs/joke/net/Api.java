package com.lcs.joke.net;

import com.lcs.joke.net.bean.response.BaseResponse;
import com.lcs.joke.net.bean.response.JokeResponse;
import com.lcs.joke.net.retrofit.RetrofitSender;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Api {
    public static NetTask getLatestJoke(NetCallback<BaseResponse<JokeResponse>> callback) {
        Subscription task = RetrofitSender.getInstance()
            .getLatestJoke()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callback);
        return new NetTask(task);
    }

    public static NetTask getLatestPic(NetCallback<BaseResponse<JokeResponse>> callback) {
        Subscription task = RetrofitSender.getInstance()
            .getLatestPic()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callback);
        return new NetTask(task);
    }

    public static NetTask getListJoke(int pageIndex, String time, NetCallback<BaseResponse<JokeResponse>> callback) {
        Subscription task = RetrofitSender.getInstance()
            .getListJoke(pageIndex, time)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callback);
        return new NetTask(task);
    }

    public static NetTask getListPic(int pageIndex, String time, NetCallback<BaseResponse<JokeResponse>> callback) {
        Subscription task = RetrofitSender.getInstance()
            .getListPic(pageIndex, time)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callback);
        return new NetTask(task);
    }
}
