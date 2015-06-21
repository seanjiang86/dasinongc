package com.dasinong.app.ui.soil.adapter.ViewHolder;


import android.util.SparseArray;

import android.view.View;

import android.widget.TextView;



/**
 * Created by liuningning on 15/6/21.
 */
public class ViewHolder {


    private SparseArray<View> views = new SparseArray<View>();
    private View convertView;

    public ViewHolder(View convertView) {
        this.convertView = convertView;
    }

    public <T extends View> T getView(int resId) {
        View v = views.get(resId);
        if (null == v) {
            v = convertView.findViewById(resId);
            views.put(resId, v);
        }
        return (T) v;
    }

    /**
     * 设置文本的内容
     * @param resId id
     * @param textValue textValue
     */
    public void setTextValue(int resId,String textValue) {

        TextView textView = getView(resId);
        textView.setText(textValue);
    }


    /**
     * 设置文本的内容
     * @param resId id
     * @param textRes textRes
     */
    public void setTextValue(int resId,int textRes) {

        TextView textView = getView(resId);
        textView.setText(textRes);
    }


    /**
     * 设置view visibility
     * @param resId id
     *
     */
    public void setVisibility(int resId) {

       getView(resId).setVisibility(View.VISIBLE);
    }

    /**
     * 设置view gone
     * @param resId id
     *
     */
    public void setInVisibility(int resId) {

        getView(resId).setVisibility(View.GONE);
    }

}
