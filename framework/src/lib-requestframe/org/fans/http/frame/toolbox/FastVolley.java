package org.fans.http.frame.toolbox;

import android.os.Looper;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.zitech.framework.BaseApplication;

/**
 * Created by lu on 2014/9/17.
 */
public class FastVolley extends Volley {
    private RequestQueue mRequestQueue;
    private boolean stop = false;
    public static FastVolley instance;

    private FastVolley() {
//        new Builder().httpMethod()
    }

    public static FastVolley getInstance() {
        if (instance == null) {
            synchronized (FastVolley.class) {
                if (instance == null) {
                    instance = new FastVolley();
                }
            }
        }
        return instance;
    }

    /**
     * 获取一个请求队列(单例)
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            synchronized (RequestQueue.class) {
                if (mRequestQueue == null) {
                    mRequestQueue = newRequestQueue(BaseApplication.getInstance());
                }
            }

        }
        return mRequestQueue;
    }


    public void execute(com.android.volley.Request request) {
        checkIfNotInMainThread();
        restartIfStopped();
        getRequestQueue().add(request);
    }

    private void restartIfStopped() {
        if (stop) {
            restart();
        }
    }

    public boolean isStopped() {
        return stop;
    }

    public void restart() {
        getRequestQueue().start();
        stop = false;
    }

    public void stop() {
        try {
            getRequestQueue().stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        stop = true;
    }

    public void cancle(String key) {
        getRequestQueue().cancel(key);
    }

    /**
     * 判断当前是否为主线程调用
     */
    private void checkIfNotInMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("Executor must be call from the main thread.");
        }
    }
}
