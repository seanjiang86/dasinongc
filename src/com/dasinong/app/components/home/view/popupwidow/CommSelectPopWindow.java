/**
 * 使用方法
 * CommSelectPopWindow popuwindow=new CommSelectPopWindow(mContext)
 * popuwindow.setDatas(new String[]{"测试一","测试2"});
 * popuwindow.setPopWidth(text_payee_identity_type.getMeasuredWidth());
 * popuwindow.setmPopItemSelectListener(new PopItemSelectListener() {
 * public void itemSelected(int position, CharSequence tag) {
 * }
 * });
 * popuwindow.showAsDropDown(text_payee_identity_type);
 */

package com.dasinong.app.components.home.view.popupwidow;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dasinong.app.R;


/**
 * 公共组件
 *
 * @author
 */
public class CommSelectPopWindow {
    private int DEFAULT_MOST_SHOW_ITEM = 5;//能看到的最多选项数
    private Context mContext;
    private PopupWindow mPopWindow;
    private View rootView;
    private ListView lv;
    private List<? extends CharSequence> items;
    private PopAdapter mAdapter;
    private int popWidth = LayoutParams.WRAP_CONTENT;
    ;
    private int popHeight = LayoutParams.WRAP_CONTENT;
    ;
    private boolean singleline = true;

    private PopItemSelectListener mItemSelectListener;

    public CommSelectPopWindow(Context mContext) {
        this.mContext = mContext;
    }

    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if (mPopWindow == null) {
            init();
        }
        mPopWindow.setWidth(popWidth);
        mPopWindow.setHeight(popHeight);
        mPopWindow.update();
        hiddenSoftInput(anchor);

        int[] location = new int[2];
        anchor.getLocationInWindow(location);
        int height = anchor.getMeasuredHeight();
        int height2 = anchor.getRootView().getMeasuredHeight();

        int down = height2 - location[1] - height;
        int up = location[1];
        //2014/07/01  弹出逻辑修改，上下哪个空间大向哪里弹出
        //
        if (up > down + 50) {
            //向上弹出
            resetListHeight(up);
            mPopWindow.setWidth(popWidth + 15);
           // rootView.setBackgroundResource(R.drawable.pop_arraow_up);
            rootView.setPadding(5, 10, 5, 10);
            mPopWindow.showAsDropDown(anchor, xoff - 8, -(height + tmpHeight + 15));
        } else {
            //向下弹出
            resetListHeight(down);
           // rootView.setBackgroundResource(R.drawable.select_pop_bg);
            rootView.setPadding(1, 10, 1, 5);
            mPopWindow.showAsDropDown(anchor, xoff, yoff);
        }


    }

    public void showAsDropDown(View anchor) {
        showAsDropDown(anchor, 0, 0);
    }


    private void hiddenSoftInput(View view) {
        InputMethodManager manger = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        manger.hideSoftInputFromWindow(view.getWindowToken(), manger.HIDE_NOT_ALWAYS);
    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        mPopWindow.setWidth(popWidth);
        mPopWindow.setHeight(popHeight);
        mPopWindow.update();
        hiddenSoftInput(parent);
        mPopWindow.showAtLocation(parent, gravity, x, y);
    }

    private void init() {
        rootView = LayoutInflater.from(mContext).inflate(
                R.layout.pop_comm_select, null);
        lv = (ListView) rootView.findViewById(R.id.lv_comm_pop_select);
        mPopWindow = new PopupWindow(rootView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mPopWindow.setFocusable(true);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOutsideTouchable(true);
    }

    public void setDatas(CharSequence[] chars) {
        List<CharSequence> list = new ArrayList<CharSequence>();
        if (chars == null || chars.length == 0) {
        } else {
            for (int n = 0; n < chars.length; n++) {
                list.add(chars[n]);
            }
        }
        setDatas(list);
    }

    public void setDatas(List<? extends CharSequence> mList) {
        if (mPopWindow == null) {
            init();
        }
        items = mList;
        //更具数据多少 设置listview的高度
        if (mAdapter == null) {
            mAdapter = new PopAdapter();
            lv.setAdapter(mAdapter);
            lv.setOnItemClickListener(mItemClickListener);
        }
        mAdapter.notifyDataSetChanged();
        // resetListHeight();
    }

    private int tmpHeight;

    /**
     * 重新计算listview高度
     * 计算逻辑，可利用高度大于 要求显示的n条高度显示条
     * 小于要求显示的n条，高度为可利用高度
     */
    private void resetListHeight(int maxHeight) {
        int count = mAdapter.getCount();
        ViewGroup.LayoutParams params = lv.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if (count < DEFAULT_MOST_SHOW_ITEM) {
            params.height = params.WRAP_CONTENT;
            tmpHeight = (count) * getItemHeight() + 20;
            if (tmpHeight > maxHeight) {
                tmpHeight = maxHeight;
            }
        } else {
            int h = DEFAULT_MOST_SHOW_ITEM * getItemHeight() + lv.getDividerHeight() * DEFAULT_MOST_SHOW_ITEM;
            if (h < maxHeight) {
                params.height = h;
                tmpHeight = params.height + 10;
            } else {
                //空间不够显示
                int c = maxHeight / getItemHeight();
                //留出30dp距离
                h = c * getItemHeight() + c * lv.getDividerHeight();
                if (h > maxHeight) {
                    h = (c - 1) * getItemHeight() + (c - 1) * lv.getDividerHeight();
                }
                params.height = h;
                tmpHeight = params.height + 10;
            }
        }
        lv.setLayoutParams(params);
    }

    protected int getItemHeight() {
        return mContext.getResources().getDimensionPixelOffset(R.dimen.select_pop_item_height);
    }

    public void setItemShow(int num) {
        DEFAULT_MOST_SHOW_ITEM = num;
    }

    public void setDatas(Integer ids) {
        setDatas(mContext.getResources().getStringArray(ids));
    }

    /**
     * 设置popwind的宽 值同 new PopupWindow(View contentView, int width, int height);
     *
     * @param popWidth
     */
    public void setPopWidth(int popWidth) {
        this.popWidth = popWidth;
    }

    public int getPopWidth() {
        return popWidth;
    }

    /**
     * 设置popwind的高 值同 new PopupWindow(View contentView, int width, int height);
     *
     * @param popHeight
     */
    public void setPopHeight(int popHeight) {
        this.popHeight = popHeight;
    }

    public void setBackground(int resid) {
        lv.setBackgroundResource(resid);
    }

    public void setListItemSingleLine(boolean singleline) {
        this.singleline = singleline;
    }

    /**
     * 下拉选择中的item
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View createItemView(int position, View convertView, ViewGroup parent) {
        convertView = createViewAndData(convertView, items.get(position), position);

        if (position % 2 != 0) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.table_listview_bg_1));
        } else {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.table_listview_bg_2));
        }
        return convertView;
    }

    /**
     * @param convertView
     * @param item
     */
    protected View createViewAndData(View convertView, CharSequence item, int pos) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_pop_select, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tv_item);

        tv.setText(item);
        tv.setSingleLine(singleline);
        return convertView;
    }

    public void setmPopItemSelectListener(
            PopItemSelectListener mItemSelectListener) {
        this.mItemSelectListener = mItemSelectListener;
    }

    private OnItemClickListener mItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            mPopWindow.dismiss();
            if (mItemSelectListener != null) {
                mItemSelectListener.itemSelected(CommSelectPopWindow.this,
                        position, items.get(position));
            }
        }
    };

    class PopAdapter extends BaseAdapter {
        public int getCount() {
            return items == null ? 0 : items.size();
        }

        public Object getItem(int position) {
            return items.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            return createItemView(position, convertView, parent);
        }
    }

    public interface PopItemSelectListener {
        void itemSelected(CommSelectPopWindow window, int position,
                          CharSequence tag);
    }

    public void disMiss() {
        if (null != mPopWindow) {
            if (mPopWindow.isShowing()) {
                mPopWindow.dismiss();
            }
        }
    }

}
