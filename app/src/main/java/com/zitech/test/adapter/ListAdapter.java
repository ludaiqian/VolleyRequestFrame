package com.zitech.test.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class ListAdapter<T> extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected OnItemClickListener mOnItemClicklistener;
    protected List<T> mList;
    protected Context mContext;
    protected ListView mListView;

    public ListAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mList != null ? mList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Object remove(int position) {
        return mList != null && !mList.isEmpty() ? mList.remove(position) : null;
    }

    @Override
    abstract public View getView(int position, View convertView, ViewGroup parent);

    public void setList(List<T> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public List<T> getList() {
        return mList;
    }

    public void setList(T[] list) {
        ArrayList<T> arrayList = new ArrayList<T>(list.length);
        for (T t : list) {
            arrayList.add(t);
        }
        setList(arrayList);
    }

    public void addAll(Collection<T> list) {
        if (list != null) {
            this.mList.addAll(list);
            notifyDataSetChanged();
        }

    }

    public ListView getListView() {
        return mListView;
    }

    public void setListView(ListView listView) {
        mListView = listView;
    }

    public Object getItemAtPosition(int position) {
        return mList.get(position);
    }

    public Context getContext() {
        return mContext;
    }


    public void clear() {
        if (mList != null) mList.clear();
        notifyDataSetChanged();
    }


    public View inflatView(int resId) {
        return mInflater.inflate(resId, null);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClicklistener = onItemClickListener;
    }

    public static class ViewHolder {
        @SuppressWarnings("unchecked")
        public static <T extends View> T get(View convertView, int resId) {
            SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new SparseArray<View>();
                convertView.setTag(viewHolder);
            }
            View childView = viewHolder.get(resId);
            if (childView == null) {
                childView = convertView.findViewById(resId);
                viewHolder.put(resId, childView);
            }
            return (T) childView;
        }
    }
}
