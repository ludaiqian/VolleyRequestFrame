package com.zitech.test.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.zitech.test.R;
import com.zitech.test.widget.ContentViewSwitcher;

public class ViewSwitcherTestActivity extends AppCompatActivity {
    private Handler hander = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            return false;
        }
    });
    private ContentViewSwitcher mHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_holder_test);
        mHolder = (ContentViewSwitcher) findViewById(R.id.viewHolder);
        mHolder.showLoading();
        hander.postDelayed(new Runnable() {
            @Override
            public void run() {
                //假如网络请求成功了。
                mHolder.showContent();
            }
        }, 500);
        //错误显示
//        mHolder.showRetry();
        //无数据
//        mHolder.showEmpty();
    }
    public static void launch(Context context) {
        Intent intent = new Intent(context, ViewSwitcherTestActivity.class);
        context.startActivity(intent);
    }
}
