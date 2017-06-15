package com.zitech.test;


import com.mozillaonline.providers.DownloadManager;
import com.shizhefei.mvc.MVCHelper;
import com.zitech.framework.BaseApplication;
import com.zitech.test.api.ZitechApi;
import com.zitech.test.widget.MyLoadViewFactory;

public class TestApplication extends BaseApplication {
    private DownloadManager mDownloadManager;
    @Override
    public void onCreate() {
        super.onCreate();
        //推荐在Application里初始化
        ZitechApi.init();
        // 设置LoadView的factory，用于创建使用者自定义的加载失败，加载中，加载更多等布局,写法参照DeFaultLoadViewFactory
        MVCHelper.setLoadViewFractory(new MyLoadViewFactory());
    }

    public DownloadManager getDownloadManager() {
        if (mDownloadManager == null) {
            mDownloadManager = new DownloadManager(getContentResolver(), getPackageName());
        }
        return mDownloadManager;
    }

    public static TestApplication getInstance() {
        return (TestApplication)BaseApplication.getInstance();
    }
}
