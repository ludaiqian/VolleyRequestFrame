package com.zitech.test.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zitech.test.R;
import com.zitech.test.adapter.AppListAdapter;
import com.zitech.test.api.entity.ApkUpdateEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 对于下载更新频率，可以修改
 * {@link com.mozillaonline.providers.downloads.Constants#MIN_PROGRESS_STEP}
 * {@link com.mozillaonline.providers.downloads.Constants#MIN_PROGRESS_TIME}
 */
public class DownloadListActivity extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_list);
        mListView = (ListView) findViewById(R.id.list);
        try {
            //
            InputStream in = getResources().getAssets().open("data.json");
            List<ApkUpdateEntity> entityList = new Gson().fromJson(new InputStreamReader(in), new TypeToken<List<ApkUpdateEntity>>() {
            }.getType());
            System.out.println(entityList.size());
            AppListAdapter adapter = new AppListAdapter(this);
            adapter.setList(entityList);
            mListView.setAdapter(adapter);
            adapter.setListView(mListView);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, DownloadListActivity.class);
        context.startActivity(intent);
    }
}
