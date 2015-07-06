package com.king.photo.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dasinong.app.R;
import com.king.photo.util.Bimp;
import com.king.photo.util.BitmapCache;
import com.king.photo.util.BitmapCache.ImageCallback;
import com.king.photo.util.ImageItem;
import com.king.photo.util.Res;

/**
 * 这个是显示一个文件夹里面的所有图片时用的适配器
 * 
 * @author king
 * @QQ:595163260
 * @version 2014年10月18日 下午11:49:35
 */
public class AlbumGridViewAdapter extends BaseAdapter {
	final String TAG = getClass().getSimpleName();
	private Context mContext;
	private ArrayList<ImageItem> dataList;
	private ArrayList<ImageItem> selectedDataList;
	private DisplayMetrics dm;
	BitmapCache cache;
	private HashMap<Integer, Boolean> mSelectMap = new HashMap<Integer, Boolean>();

	public AlbumGridViewAdapter(Context c, ArrayList<ImageItem> dataList, ArrayList<ImageItem> selectedDataList) {
		mContext = c;
		cache = new BitmapCache();
		this.dataList = dataList;
		this.selectedDataList = selectedDataList;
		dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
	}

	public int getCount() {
		return dataList.size();
	}

	public Object getItem(int position) {
		return dataList.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	ImageCallback callback = new ImageCallback() {
		@Override
		public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) {
			if (imageView != null && bitmap != null) {
				String url = (String) params[0];
				if (url != null && url.equals((String) imageView.getTag())) {
					((ImageView) imageView).setImageBitmap(bitmap);
				} else {
					Log.e(TAG, "callback, bmp not match");
				}
			} else {
				Log.e(TAG, "callback, bmp null");
			}
		}
	};

	/**
	 * 存放列表项控件句柄
	 */
	private class ViewHolder {
		public ImageView imageView;
		public CheckBox childCheckbox;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(Res.getLayoutID("plugin_camera_select_imageview"), parent, false);
			viewHolder.imageView = (ImageView) convertView.findViewById(Res.getWidgetID("image_view"));
			viewHolder.childCheckbox = (CheckBox) convertView.findViewById(R.id.child_checkbox);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String path;

		if (dataList != null && dataList.size() > position)
			path = dataList.get(position).imagePath;
		else
			path = "camera_default";
		if (path.contains("camera_default")) {
			viewHolder.imageView.setImageResource(Res.getDrawableID("plugin_camera_no_pictures"));
		} else {
			final ImageItem item = dataList.get(position);
			viewHolder.imageView.setTag(item.imagePath);
			cache.displayBmp(viewHolder.imageView, item.thumbnailPath, item.imagePath, callback);
		}

		if (Bimp.tempSelectBitmap.contains(dataList.get(position))) {
			System.out.println(position);
			viewHolder.childCheckbox.setChecked(true);
		}
		System.out.println(viewHolder.childCheckbox.isChecked());

		viewHolder.childCheckbox.setOnCheckedChangeListener(new MyCheckedChangeListener(position, viewHolder));

		for (Iterator<Map.Entry<Integer, Boolean>> it = mSelectMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Integer, Boolean> entry = it.next();
			if (entry.getValue()) {
				Bimp.tempSelectBitmap.add(dataList.get(entry.getKey()));
			}
		}

		return convertView;
	}

	private class MyCheckedChangeListener implements OnCheckedChangeListener {
		private int position;
		private ViewHolder viewHolder;

		public MyCheckedChangeListener(int position, ViewHolder viewHolder) {
			this.position = position;
			this.viewHolder = viewHolder;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (dataList != null && mOnItemClickListener != null && position < dataList.size()) {
				mOnItemClickListener.onItemClick(viewHolder.childCheckbox, position, viewHolder.childCheckbox.isChecked());
			}
			// 如果是未选中的CheckBox,则添加动画
			if (!mSelectMap.containsKey(position) || !mSelectMap.get(position)) {
				addAnimation(viewHolder.childCheckbox);
			}
			mSelectMap.put(position, isChecked);
		}
	}

	public int dipToPx(int dip) {
		return (int) (dip * dm.density + 0.5f);
	}

	/**
	 * 给CheckBox加点击动画，利用开源库nineoldandroids设置动画
	 * @param view
	 */
	private void addAnimation(View view) {
		float[] vaules = new float[] { 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f };
		AnimatorSet set = new AnimatorSet();
		set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules), ObjectAnimator.ofFloat(view, "scaleY", vaules));
		set.setDuration(150);
		set.start();
	}

	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		public void onItemClick(CheckBox view, int position, boolean isChecked);
	}

}
