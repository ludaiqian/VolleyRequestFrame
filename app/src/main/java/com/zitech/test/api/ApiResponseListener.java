package com.zitech.test.api;

import com.android.volley.HttpError;

/**
 * Created by lu on 2016/3/24.
 */
public abstract class ApiResponseListener<T> {
    /**
     * 当Activity者Fragment未销毁
     */
    public abstract void onResponseInActive(T response);

    public abstract void onError(HttpError error);

    /**
     * 当Activity者Fragment销毁
     */
    public void onResponseAfterDestoried(T response) {

    }
}