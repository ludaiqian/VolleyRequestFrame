package com.zitech.test.api.datasource;

import com.android.volley.HttpError;
import com.shizhefei.mvc.IAsyncDataSource;
import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;
import com.zitech.test.api.ApiFactory;
import com.zitech.test.api.ApiResponseListener;
import com.zitech.test.api.request.NewsRequest;
import com.zitech.test.api.response.News;
import com.zitech.test.api.response.NewsList;

import org.fans.http.frame.toolbox.FastVolley;

import java.util.List;

/**
 * 单独编写的新闻DataSouce示例
 * Created by lu on 2016/3/12.
 */
public class NewsDataSource implements IAsyncDataSource<List<News>> {
    private PagedProxy proxy = new PagedProxy(10);

    public NewsDataSource() {
        super();
    }

    @Override
    public RequestHandle refresh(ResponseSender<List<News>> sender) throws Exception {
        return loadNews(sender, proxy.reset());
    }

    @Override
    public RequestHandle loadMore(ResponseSender<List<News>> sender) throws Exception {
        return loadNews(sender, proxy.toNextPage());
    }

    @Override
    public boolean hasMore() {
        return proxy.hasNextPage();
    }

    private RequestHandle loadNews(final ResponseSender<List<News>> sender, final int page) throws Exception {

        final NewsRequest request = ApiFactory.getNewsList(page, new ApiResponseListener<NewsList>() {
            @Override
            public void onResponseInActive(NewsList response) {
                if (!proxy.isPageCountSet()) {
                    proxy.setPageCount(response.getTotalPage());
                }
                List<News> items = response.getItem();
                if (items == null || items.size() == 0) {
                    proxy.setReachEnd(true);
                }
                sender.sendData(items);
            }

            @Override
            public void onError(HttpError error) {
                sender.sendError(error);
            }
        });
        return new RequestHandle() {
            @Override
            public void cancle() {
                FastVolley.getInstance().cancle(request.toString());
            }

            @Override
            public boolean isRunning() {
                return false;
            }
        };
    }

}
