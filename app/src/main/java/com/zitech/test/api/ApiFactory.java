package com.zitech.test.api;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.app.Fragment;

import com.android.volley.HttpError;
import com.android.volley.Request.Method;
import com.android.volley.ResponseListener;
import com.zitech.framework.utils.VersionUtils;
import com.zitech.framework.widget.LoadingDialog;
import com.zitech.test.Constants;
import com.zitech.test.api.request.ChapterDetailRequest;
import com.zitech.test.api.request.NewsRequest;
import com.zitech.test.api.request.PagedRequest;
import com.zitech.test.api.request.Request;
import com.zitech.test.api.response.AccountOnline;
import com.zitech.test.api.response.ChapterDetail;
import com.zitech.test.api.response.NewsList;
import com.zitech.test.api.response.PagedResponse;
import com.zitech.test.api.response.TrainingList;

import org.fans.http.frame.toolbox.FastVolley;
import org.fans.http.frame.HttpParams;
import org.fans.http.frame.Parser;
import org.fans.http.frame.PojoRequest;
import org.fans.http.frame.RequestBuilder;
import org.fans.http.frame.toolbox.ResponseTypeProvider;
import org.fans.http.frame.toolbox.packet.ApiRequest;
import org.fans.http.frame.toolbox.packet.ApiResponse;
import org.fans.http.frame.toolbox.JsonSerializer;
import org.fans.http.frame.toolbox.ParamsConvertor;

import java.lang.reflect.Type;

/**
 * API协议工厂范例
 *
 * @author ludaiqian
 */
public class ApiFactory {


    /**
     * 7. 获取章节内容
     *
     * @param target             Activity或者Fragment
     * @param subjectId
     * @param respsponseListener
     */
    public static final void getChapterDetail(Object target, int subjectId, ApiResponseListener<ChapterDetail> respsponseListener) {
        ChapterDetailRequest request = new ChapterDetailRequest();
        request.setApiMethod(ZitechApi.CHAPTER_DETAIL);
        request.setSubjectId(subjectId);
        execute(target, request, respsponseListener);
    }

    /**
     * 2. 测试账户是否已登录
     *
     * @param target             Activity或者Fragment
     * @param respsponseListener
     */
    public static final void isAccountOnline(Object target, ApiResponseListener<AccountOnline> respsponseListener) {
        Request request = new Request();
        request.setApiMethod(ZitechApi.ACCOUNT_ONLINE_TEST);
        execute(target, request, respsponseListener);
    }


    /**
     * 3	获取训练清单内容(不使用对象的的方式)
     *
     * @param target             Activity或者Fragment
     * @param respsponseListener
     */
    public static final void getTrainingList(Object target, int method, String subjectId, ApiResponseListener<TrainingList> respsponseListener) {
        HttpParams params = new HttpParams();
        params.put("subjectId", subjectId);
        params.putHeader("cookie", "test");
        execute(target, Constants.URL + ZitechApi.TRAINING_LIST, Method.POST, params, null, new ZitechParser(ZitechApi.TRAINING_LIST), respsponseListener);
    }

    /**
     * 3	获取训练清单内容
     *
     * @param responseListener
     */
    public static final NewsRequest getNewsList(int page, ApiResponseListener<NewsList> responseListener) {
        NewsRequest request = new NewsRequest();
        request.setApiMethod(ZitechApi.NEWS_LIST);
        request.setPage(page);
        execute(null, "http://api.iclient.ifeng.com/" + request.getApiMethod(), Method.GET, convert(request), request.toString(), request, responseListener);
        return request;
    }

    /**
     * 3	统一的分页请求
     */
    public static final void getPagedResponse(PagedRequest request, ApiResponseListener<? extends PagedResponse> responseListener) {
        execute(null, "http://api.iclient.ifeng.com/" + request.getApiMethod(), Method.GET, convert(request), request.toString(), request, responseListener);
    }


    public static <T extends ApiResponse> void execute(Object target, Request request, final ApiResponseListener<T> responseListener) {
        execute(target, Constants.URL + request.getApiMethod(), Method.POST, convert(request), request.toString(), request, responseListener);
    }


    public static <T extends ApiResponse> void execute(Object target, String url, int method, HttpParams params, String uniqueKey, Parser parser, final ApiResponseListener<T> responseListener) {
        Activity hostActivity = null;
        Fragment hostSupportFragment = null;
        android.app.Fragment hostFragment = null;
        if (target instanceof Activity) {
            hostActivity = (Activity) target;
        } else if (target instanceof Fragment) {
            hostSupportFragment = (Fragment) target;
            hostActivity= hostSupportFragment.getActivity();
        } else if (VersionUtils.isHoneycomb()) {
            if (target instanceof android.app.Fragment) {
                hostFragment = (android.app.Fragment) target;
                hostActivity = hostFragment.getActivity();
            }
        }
        final Activity aTarget = hostActivity;
        final Fragment fTargetOfSupport = hostSupportFragment;
        final Object fTarget = hostFragment;
        PojoRequest httpRequest = new RequestBuilder()
                .url(url)//Url
                .contentType("application/x-www-form-urlencoded")//ContenType
                .parser(parser)//解析器
                .uniqueKey(uniqueKey)//唯一标识,重复的key将不会被提交到请求队列
                .httpMethod(method)// GET/POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                .params(params)//Http请求参数
                .callback(new ResponseListener<T>() {
                    @Override
                    public void onResponse(T response) {
                        boolean activityDestoried = false;
                        if (aTarget != null) {
                            activityDestoried = aTarget.isFinishing();
                        }
                        if (response.isSuccess()) {
                            boolean destoried = activityDestoried || (fTargetOfSupport != null && !fTargetOfSupport.isAdded()) || (fTarget != null && !((android.app.Fragment) fTarget).isAdded());
                            if (destoried) {
                                responseListener.onResponseAfterDestoried(response);
                            } else {
                                responseListener.onResponseInActive(response);

                            }
                        } else {
                            responseListener.onError(new HttpError(response.getResultCode(), response.getMessage()));
                        }
                    }

                    @Override
                    public void onError(HttpError error) {
                        responseListener.onError(error);
                    }
                })
                .create();
        execute(hostActivity, httpRequest);
    }

    public static void execute(Context context, PojoRequest request) {
        if (context != null) {
            Dialog dialog = createLoadingDialog(context, request, true);
            dialog.show();
            request.progressDialog = dialog;
        }
        FastVolley.getInstance().execute(request);
    }


    protected static Dialog createLoadingDialog(Context context, final org.fans.http.frame.Request<?> request, final boolean canAbort) {
        Dialog dialog = new LoadingDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(canAbort);
        if (canAbort) {
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                    request.cancel();
                }
            });
        }
        return dialog;
    }

    private static HttpParams convert(ApiRequest request) {
        //转换为params参数
        ParamsConvertor convertor = new ParamsConvertor();
        //转换为json参数
        //JsonConvertor convertor=new JsonConvertor();
        return convertor.convert(request);

    }

    static class ZitechParser<T> implements Parser<T> {
        private String method;

        public ZitechParser(String method) {
            this.method = method;
        }

        @Override
        public T parse(String result) {
            Type type = ResponseTypeProvider.getInstance().getApiResponseType(method);
            return (T) JsonSerializer.DEFAULT.deserialize(result, type);
        }
    }

}
