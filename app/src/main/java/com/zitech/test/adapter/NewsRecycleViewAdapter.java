/*
Copyright 2015 shizhefei（LuckyJayce）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.zitech.test.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shizhefei.mvc.IDataAdapter;
import com.zitech.framework.transform.RoundedCornersTransformation;
import com.zitech.framework.widget.RemoteImageView;
import com.zitech.test.R;
import com.zitech.test.api.response.News;

import java.util.ArrayList;
import java.util.List;

public class NewsRecycleViewAdapter extends RecyclerView.Adapter<NewsRecycleViewAdapter.NewsViewHolder> implements IDataAdapter<List<News>> {
    private List<News> newsList = new ArrayList<News>();
    private LayoutInflater inflater;
    private Context mContext;

    public NewsRecycleViewAdapter(Context context) {
        super();
        inflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsViewHolder(inflater.inflate(R.layout.item_news, parent, false));
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        News news = newsList.get(position);
        TextView title = (TextView) holder.title;

        TextView content = holder.content;
        RemoteImageView iv = holder.thumb;
        title.setText(news.getSource());
        content.setText(news.getTitle());
        iv.setBitmapTransformation(new RoundedCornersTransformation(mContext, 20, true));
        iv.setImageUri(R.drawable.translucent_black_round_corner, news.getThumbnail());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    @Override
    public void notifyDataChanged(List<News> data, boolean isRefresh) {
        if (isRefresh) {
            newsList.clear();
        }
        newsList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public List<News> getData() {
        return newsList;
    }

    @Override
    public boolean isEmpty() {
        return newsList.isEmpty();
    }

    class NewsViewHolder extends ViewHolder {

        TextView title;
        TextView content;
        RemoteImageView thumb;

        public NewsViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.content);
            thumb = (RemoteImageView) view.findViewById(R.id.thumb);
        }
    }

}
