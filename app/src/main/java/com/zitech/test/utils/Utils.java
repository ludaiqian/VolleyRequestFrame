package com.zitech.test.utils;

import android.database.Cursor;

import com.mozillaonline.providers.DownloadManager;
import com.mozillaonline.providers.downloads.Constants;
import com.mozillaonline.providers.downloads.DownloadInfo;
import com.zitech.framework.BaseApplication;
import com.zitech.test.TestApplication;

import java.util.HashMap;

/**
 * Created by lu on 2016/3/28.
 */
public class Utils {

    /**
     * 获取下载任务<br>
     */
    public static HashMap<String, DownloadInfo> getDownloadingApks() {
        HashMap<String, DownloadInfo> apks = new HashMap<>();
//        query.r
        DownloadManager manager = TestApplication.getInstance().getDownloadManager();
        DownloadManager.Query query = new DownloadManager.Query();
        //下载成功
        int status = DownloadManager.STATUS_SUCCESSFUL;
        //暂停
        status |= DownloadManager.STATUS_PAUSED;
        //失败
        status |= DownloadManager.STATUS_FAILED;
        //等待
        status |= DownloadManager.STATUS_PENDING;
        //正在下载
        status |= DownloadManager.STATUS_RUNNING;
        //状态过滤
        query.setFilterByStatus(status);
        //类型过滤
        query.setFilterByMimeType(Constants.MIMETYPE_APK);
        //id过滤
        //query.setFilterById(id...)
        //url过滤
//        query.setFilterByUri(url);
        Cursor cursor = manager.query(query);
//        List<DownloadInfo> mDownloads = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            DownloadInfo.Reader reader = new DownloadInfo.Reader(BaseApplication.getInstance().getContentResolver(), cursor);
            DownloadInfo info = reader.newDownloadInfo(BaseApplication.getInstance());
//            mDownloads.add(info);
            apks.put(info.mUid, info);
        }

        return apks;
    }

    public static DownloadInfo query(long id) {
        DownloadManager manager = TestApplication.getInstance().getDownloadManager();
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        Cursor cursor = manager.query(query);
        if (cursor.moveToFirst()) {
            DownloadInfo.Reader reader = new DownloadInfo.Reader(BaseApplication.getInstance().getContentResolver(), cursor);
            DownloadInfo info = reader.newDownloadInfo(BaseApplication.getInstance());
//            mDownloads.add(info);
            return info;
        }
        return null;
    }

    public static DownloadInfo query(String uid) {
        DownloadManager manager = TestApplication.getInstance().getDownloadManager();
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterByUid(uid);
        Cursor cursor = manager.query(query);
        if (cursor.moveToFirst()) {
            DownloadInfo.Reader reader = new DownloadInfo.Reader(BaseApplication.getInstance().getContentResolver(), cursor);
            DownloadInfo info = reader.newDownloadInfo(BaseApplication.getInstance());
//            mDownloads.add(info);
            return info;
        }
        return null;
    }
}
