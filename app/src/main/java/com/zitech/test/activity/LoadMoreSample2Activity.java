package com.zitech.test.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shizhefei.mvc.MVCHelper;
import com.zitech.test.R;
import com.zitech.test.adapter.NewsRecycleViewAdapter;
import com.zitech.test.api.datasource.NewsDataSource;
import com.zitech.test.api.response.News;
import com.zitech.test.widget.MVCSwipeRefreshHelper;

import java.util.List;

public class LoadMoreSample2Activity extends AppCompatActivity {

    private MVCHelper<List<News>> mvcHelper;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_move_recycle_view);
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mvcHelper = new MVCSwipeRefreshHelper<List<News>>(swipeRefreshLayout);
        // 设置数据源
        mvcHelper.setDataSource(new NewsDataSource());
        // 设置适配器
        mvcHelper.setAdapter(new NewsRecycleViewAdapter(this));
        // 加载数据
        mvcHelper.refresh();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mvcHelper.destory();

    }
    public static void launch(Context context) {
        Intent intent = new Intent(context, LoadMoreSample2Activity.class);
        context.startActivity(intent);
    }
}
