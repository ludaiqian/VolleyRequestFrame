package com.zitech.test.api.datasource;

import com.android.volley.HttpError;
import com.shizhefei.mvc.IAsyncDataSource;
import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;
import com.zitech.test.api.ApiFactory;
import com.zitech.test.api.ApiResponseListener;
import com.zitech.test.api.request.PagedRequest;
import com.zitech.test.api.response.PagedResponse;

import org.fans.http.frame.toolbox.FastVolley;

import java.util.List;

/**
 * 统一封装的DataSource示例，所有分页请求皆可使用此DataSource
 * Created by lu on 2016/3/12.
 */
public class DataSource<T> implements IAsyncDataSource<List<T>> {
    private PagedRequestProxy mProxy;
    private ListFetcher<T> mListFetcher;

    public DataSource(PagedRequest request, ListFetcher<T> listFetcher) {
        super();
        this.mProxy = new PagedRequestProxy(request);
        this.mListFetcher = listFetcher;
    }

    @Override
    public RequestHandle refresh(ResponseSender<List<T>> sender) throws Exception {
        mProxy.reset();
        return loadNews(sender);
    }

    @Override
    public RequestHandle loadMore(ResponseSender<List<T>> sender) throws Exception {
        mProxy.toNextPage();
        return loadNews(sender);
    }

    @Override
    public boolean hasMore() {
        return mProxy.hasNextPage();
    }

    private RequestHandle loadNews(final ResponseSender<List<T>> sender) throws Exception {

        ApiFactory.getPagedResponse(mProxy.getRequest(), new ApiResponseListener<PagedResponse>() {
            @Override
            public void onResponseInActive(PagedResponse response) {
                if (!mProxy.isPageCountSet()) {
                    mProxy.setPageCount(response.getPageCount());
                }
                List<T> items = mListFetcher.fetch(response);
                if (items == null || items.size() == 0) {
                    mProxy.setReachEnd(true);
                }
                sender.sendData(items);
            }

            @Override
            public void onError(HttpError error) {

            }
        });


        return new RequestHandle() {
            @Override
            public void cancle() {
                FastVolley.getInstance().cancle(mProxy.toString());
            }

            @Override
            public boolean isRunning() {
                return false;
            }
        };
    }

}
