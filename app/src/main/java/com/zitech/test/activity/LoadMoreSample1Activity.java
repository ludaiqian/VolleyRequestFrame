package com.zitech.test.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

import com.shizhefei.mvc.MVCHelper;
import com.zitech.test.R;
import com.zitech.test.adapter.NewsAdapter;
import com.zitech.test.api.ZitechApi;
import com.zitech.test.api.datasource.DataSource;
import com.zitech.test.api.datasource.ListFetcher;
import com.zitech.test.api.request.NewsRequest;
import com.zitech.test.api.response.News;
import com.zitech.test.api.response.NewsList;
import com.zitech.test.widget.MVCSwipeRefreshHelper;

import org.fans.http.frame.toolbox.packet.ApiResponse;

import java.util.List;

/**
 * <a herf="https://github.com/LuckyJayce/MVCHelper"/>
 * <p>
 * 配置样式查看以下代码
 * {@link com.zitech.test.widget.MyLoadViewFactory}
 */
public class LoadMoreSample1Activity extends AppCompatActivity {

    private MVCHelper<List<News>> mvcHelper;

    //教育
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载。
        setContentView(R.layout.activity_load_move_list_view);
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mvcHelper = new MVCSwipeRefreshHelper<List<News>>(swipeRefreshLayout);
        NewsRequest request = new NewsRequest();
        request.setApiMethod(ZitechApi.NEWS_LIST);
        ListFetcher<News> fetcher = new ListFetcher<News>() {
            @Override
            public List<News> fetch(ApiResponse<?> response) {
                NewsList list = (NewsList) response;
                return list.getItem();
            }
        };
        // 设置数据源
        mvcHelper.setDataSource(new DataSource<News>(request, fetcher));
        // 设置适配器
        mvcHelper.setAdapter(new NewsAdapter(this));
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
        Intent intent = new Intent(context, LoadMoreSample1Activity.class);
        context.startActivity(intent);
    }
}
