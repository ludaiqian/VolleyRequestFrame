package com.zitech.test.activity;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mozillaonline.providers.DownloadManager;
import com.mozillaonline.providers.downloads.Constants;
import com.mozillaonline.providers.downloads.DownloadInfo;
import com.mozillaonline.providers.downloads.Downloads;
import com.zitech.test.R;
import com.zitech.test.TestApplication;
import com.zitech.test.utils.Utils;

public class DownloadActivity extends AppCompatActivity implements View.OnClickListener {
    private DownloadManagerContentObserver mObserver;
    private Long id;
    private ProgressBar progressBar;
    private Button start;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        start = (Button) findViewById(R.id.start);
        title = (TextView) findViewById(R.id.title);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        start.setOnClickListener(this);
        //List<DownloadInfo> tasks = Utils.getDownloadingApks();
        //System.out.println(tasks);
//        manager.restartDownload();
        mObserver = new DownloadManagerContentObserver();
        getContentResolver().registerContentObserver(Downloads.CONTENT_URI, true, mObserver);
    }


    /**
     * 对下载进度进行监听
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
//            HashMap<String, DownloadInfo> infos = Session.getInstance().getDownloadManager().getDownloadingAlarms();
//            update(infos)
// ;
            if (id != null) {
                DownloadInfo info = Utils.query(id);
                if (info != null) {
                    long totalBytes = info.mTotalBytes;
                    long currentBytes = info.mCurrentBytes;
                    title.setText("total:" + totalBytes + ",current:" + currentBytes);
                    progressBar.setProgress((int) (currentBytes * 100 / totalBytes));
                }
            }

        }

    }

    @Override
    public void onClick(View v) {
//        DownloadManager manager=new DownloadManager(this);
        DownloadManager manager = TestApplication.getInstance().getDownloadManager();

        if (id == null) {
            Uri url = Uri.parse("http://down.mumayi.com/41052/mbaidu");
            String path = "/";
            DownloadManager.Request request = new DownloadManager.Request(url);
            //设置dir路径
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, path);
            //描述
            request.setDescription("desp");
            //mime类型
            request.setMimeType(Constants.MIMETYPE_APK);
//            request.setUid()
            //request.setVisibleInDownloadsUi(true);
            //通知栏显示
            request.setShowRunningNotification(true);
            //Uid可以用来区分是否唯一Apk
//            request.setUid()
            // 标题
            request.setTitle("title");
//            request.set
            id = manager.enqueue(request);
            start.setText("暂停");
        } else {
            DownloadInfo info = Utils.query(id);
            if (!Downloads.isStatusCompleted(info.mStatus)) {
                if (info.mStatus == Downloads.STATUS_PAUSED_BY_APP) {
                    manager.resumeDownload(id);
                    start.setText("暂停");
                } else {
                    manager.pauseDownload(id);
                    start.setText("开始");
                }
            }
        }
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, DownloadActivity.class);
        context.startActivity(intent);
    }
}
