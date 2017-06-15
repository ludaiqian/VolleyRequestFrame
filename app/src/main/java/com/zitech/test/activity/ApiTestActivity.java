package com.zitech.test.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.HttpError;
import com.zitech.framework.utils.Logger;
import com.zitech.framework.utils.ToastMaster;
import com.zitech.test.R;
import com.zitech.test.api.ApiFactory;
import com.zitech.test.api.ApiResponseListener;
import com.zitech.test.api.response.ChapterDetail;


/**
 * 示例1：ApiFactory方式，使用“HTTP”这个TAG查看LOG
 * 分以下三步
 * 1、对接口协议进行抽象，编写具体的请求和响应对象分别继承Request,Response
 * 2、在ZitechApi里面注册一下接口名称对应的返回类型
 * 3、在ApiFactory里添加这个接口的方法
 */
public class ApiTestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_test);
        int subjectId = 1002;
//        MobclickAgent
//        AnalyticsConfig
        ApiFactory.getChapterDetail(this, subjectId, new ApiResponseListener<ChapterDetail>() {
            @Override
            public void onResponseInActive(ChapterDetail response) {
                //Activity未销毁
                Logger.i("Response", response.toString());
            }

            @Override
            public void onResponseAfterDestoried(ChapterDetail response) {
                //Activity已销毁
            }

            @Override
            public void onError(HttpError error) {
//                if (error.getErrorCode() == HttpError.NETWORK_ERROR) {
//                    //网络错误
//                } else if (error.isServerRespondedError()) {
                //后台定义的ErrorCode
                ToastMaster.popToast(ApiTestActivity.this, error.getCauseMessage());
//                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, ApiTestActivity.class);
        context.startActivity(intent);
    }
}
