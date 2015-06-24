package com.dasinong.app.ui.soil.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dasinong.app.ui.soil.adapter.ViewHolder.ViewHolder;

import java.util.List;

/**
 * Created by liuningning on 15/6/21.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected List<T> mData;

    public CommonAdapter(List<T> data) {
        this.mData = data;

    }


    @Override
    public int getCount() {
        if (mData == null || mData.isEmpty()) {
            return 0;
        }
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        if (mData == null || mData.isEmpty()) {
            return null;
        }
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), getResourceId(), null);
            viewHolder = new ViewHolder(convertView);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        T result = mData.get(position);
        updateView(result, viewHolder);
        return viewHolder.getRootView();
    }

    protected abstract int getResourceId();


    protected abstract void updateView(T result, ViewHolder viewHolder);
}
