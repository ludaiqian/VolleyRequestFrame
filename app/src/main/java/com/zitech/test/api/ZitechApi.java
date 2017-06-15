package com.zitech.test.api;

import com.zitech.test.api.response.AccountOnline;
import com.zitech.test.api.response.ChapterDetail;
import com.zitech.test.api.response.NewsList;
import com.zitech.test.api.response.TrainingList;

import org.fans.http.frame.toolbox.ResponseTypeProvider;

import java.lang.reflect.Type;

public class ZitechApi {
    // 7. 获取章节内容
    public static final String CHAPTER_DETAIL = "chapter/detail";
    public static final String ACCOUNT_ONLINE_TEST = "account/online";
    public static final String TRAINING_LIST = "training/list";
    public static final String NEWS_LIST = "ClientNews";

    public static void init() {
        // register(CHAPTER_DETAIL, new TypeToken<ChapterDetail>() {
        // }.getType());
        /**
         * 获取章节内容<br>
         * {@link com.zitech.test.api.request.ChapterDetailRequest}
         **/
        register(CHAPTER_DETAIL, ChapterDetail.class);
        /**
         * 测试账户是否已登录<br>
         * {@link com.zitech.test.api.request.Request}
         **/
        register(ACCOUNT_ONLINE_TEST, AccountOnline.class);
        register(TRAINING_LIST, TrainingList.class);
        register(NEWS_LIST, NewsList.class);
    }

    private static void register(String method, Type type) {
        ResponseTypeProvider.getInstance().registerResponseType(method, type);
    }
}
