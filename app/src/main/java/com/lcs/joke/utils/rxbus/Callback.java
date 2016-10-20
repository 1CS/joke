package com.lcs.joke.utils.rxbus;

import rx.functions.Action1;

public abstract class Callback<T> implements Action1<T> {
    /**
     * 监听到事件时回调接口
     *
     * @param t 返回结果
     */
    public abstract void onEvent(T t);

    @Override
    public void call(T t) {
        onEvent(t);
    }
}
