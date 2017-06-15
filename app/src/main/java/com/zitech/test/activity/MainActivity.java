package com.zitech.test.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.zitech.test.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mApiTest;
    private Button mDownloadList;
    private Button mDownloadSingle;
    private Button mListViewLoadMore;
    private Button mRecycleViewLoadMore;
    private Button mViewHolderTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mApiTest = (Button) findViewById(R.id.btn_api_test);
        mDownloadList = (Button) findViewById(R.id.btn_download_list_test);
        mDownloadSingle = (Button) findViewById(R.id.btn_download_test);
        mListViewLoadMore = (Button) findViewById(R.id.btn_load_more_listview);
        mRecycleViewLoadMore = (Button) findViewById(R.id.btn_load_more_recycleview);
        mViewHolderTest = (Button) findViewById(R.id.btn_view_holder_test);
        mApiTest.setOnClickListener(this);
        mDownloadList.setOnClickListener(this);
        mDownloadSingle.setOnClickListener(this);
        mListViewLoadMore.setOnClickListener(this);
        mRecycleViewLoadMore.setOnClickListener(this);
        mViewHolderTest.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_api_test:
                ApiTestActivity.launch(this);
                break;
            case R.id.btn_download_list_test:
                DownloadListActivity.launch(this);
                break;
            case R.id.btn_download_test:
                DownloadActivity.launch(this);
                break;
            case R.id.btn_load_more_listview:
                LoadMoreSample1Activity.launch(this);
                break;
            case R.id.btn_load_more_recycleview:
                LoadMoreSample2Activity.launch(this);
                break;
            case R.id.btn_view_holder_test:
                ViewSwitcherTestActivity.launch(this);
                break;
            default:
                break;

        }
    }
}
