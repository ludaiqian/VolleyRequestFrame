package com.zitech.test.adapter;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mozillaonline.providers.DownloadManager;
import com.mozillaonline.providers.downloads.Constants;
import com.mozillaonline.providers.downloads.DownloadInfo;
import com.mozillaonline.providers.downloads.Downloads;
import com.zitech.framework.utils.ToastMaster;
import com.zitech.test.R;
import com.zitech.test.TestApplication;
import com.zitech.test.api.entity.ApkUpdateEntity;
import com.zitech.test.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lu on 2016/3/31.
 */
public class AppListAdapter extends ListAdapter<ApkUpdateEntity> {
    private List<ApkEntityWrappter> apkLists;
    private DownloadManager manager;
    private DownloadManagerContentObserver mObserver;
    private HashMap<String, DownloadInfo> downloadApks;

    /**
     * 对apk下载情况进行监听
     */
    private class DownloadManagerContentObserver extends ContentObserver {

        public DownloadManagerContentObserver() {
            super(new Handler());
        }

        /**
         * Receives notification when the data in the observed content provider
         * changes.
         */
        public void onChange(final boolean selfChange) {
            downloadApks = Utils.getDownloadingApks();
            for (ApkEntityWrappter apk : apkLists) {
                DownloadInfo info = downloadApks.get(apk.apkEntity.getMd5());
                if (info != null && (info.mStatus == Downloads.STATUS_RUNNING || apk.state != info.mStatus || info.mId != apk.id)) {
                    apk.setId(info.mId);
                    apk.setState(info.mStatus);
                    apk.setProgress((int) (info.mCurrentBytes * 100 / info.mTotalBytes));
                    updateItem(apk);
                }
//                    }
            }

        }

    }

    public AppListAdapter(Context context) {
        super(context);
        apkLists = new ArrayList<>();
        manager = TestApplication.getInstance().getDownloadManager();
        downloadApks = Utils.getDownloadingApks();
        mObserver = new DownloadManagerContentObserver();
        context.getContentResolver().registerContentObserver(Downloads.CONTENT_URI, true, mObserver);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflatView(R.layout.item_app_list);
        }
        final ApkEntityWrappter wappter = apkLists.get(position);
        render(convertView, wappter);
        return convertView;

    }

    private void render(View convertView, final ApkEntityWrappter wappter) {
        ApkUpdateEntity entity = wappter.apkEntity;
        TextView name = ViewHolder.get(convertView, R.id.name);
        ProgressBar progress = ViewHolder.get(convertView, R.id.progress);
        Button start = ViewHolder.get(convertView, R.id.start);
        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (wappter.isSuccessed()) {
                    ToastMaster.popToast(getContext(), "只是测试一下,没写安装代码");
                } else if (wappter.isLoading()) {
                    manager.pauseDownload(wappter.getId());
                } else {
                    if (wappter.id != ApkEntityWrappter.NONE) {
                        manager.resumeDownload(wappter.getId());
                    } else {
                        Uri url = Uri.parse(wappter.apkEntity.getFileUrl());
                        String path = "/";
                        DownloadManager.Request request = new DownloadManager.Request(url);
                        //设置dir路径
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, path);
                        //描述
//                        request.setDescription("xxx");
                        //mime类型
                        request.setMimeType(Constants.MIMETYPE_APK);
                        //通知栏显示
                        request.setShowRunningNotification(false);
                        // 标题
                        request.setTitle(wappter.apkEntity.getName());
                        request.setUid(wappter.apkEntity.getMd5());
                        long id = manager.enqueue(request);
                    }
                }
            }
        });
        name.setText(entity.getName());
        progress.setProgress(wappter.getProgress());
        if (wappter.isSuccessed()) {
            start.setText("安装");
        } else if (wappter.isLoading()) {
            start.setText("暂停");
        } else {
            start.setText("开始");
        }
    }

    /**
     * 单项更新不使用 notifyDataSetChanged();提升流畅度
     * 用于更新我们想要更新的item
     * <p/>
     **/
    public void updateItem(ApkEntityWrappter apk) {
        try {
            int itemIndex = apkLists.indexOf(apk);
            // // 得到第1个可显示控件的位置,记住是第1个可显示控件噢。而不是第1个控件
            int visiblePosition = mListView.getFirstVisiblePosition();
            // // 得到你需要更新item的View 如果HeaderView存在需要+n(n为HeaderView 的数量)
            View view = mListView.getChildAt(itemIndex - visiblePosition);

            render(view, apk);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setList(List<ApkUpdateEntity> list) {
        super.setList(list);
        for (ApkUpdateEntity entity : list) {
            ApkEntityWrappter wapper = new ApkEntityWrappter(entity);
            DownloadInfo info = downloadApks.get(wapper.apkEntity.getMd5());
            if (info != null) {
                wapper.setId(info.mId);
                wapper.setState(info.mStatus);
                wapper.setProgress((int) (info.mCurrentBytes * 100 / info.mTotalBytes));
            }
            apkLists.add(wapper);
        }

    }

    public static class ApkEntityWrappter {
        static int NONE = -1;
        private long id = NONE;
        private int progress;
        private int state;
        private ApkUpdateEntity apkEntity;

        public ApkEntityWrappter(ApkUpdateEntity apkEntity) {
            this.apkEntity = apkEntity;
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public boolean isSuccessed() {
            return Downloads.isStatusSuccess(state);
        }

        public boolean isLoading() {
            return state == Downloads.STATUS_RUNNING;
        }

        public long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }
}
