package com.dasinong.app.components.home.view;

import com.dasinong.app.R;
import com.dasinong.app.components.domain.BannerEntity;
import com.dasinong.app.components.domain.BannerEntity.ItemEntity;
import com.dasinong.app.net.NetConfig;
import com.dasinong.app.ui.WebBrowserActivity;
import com.liam.imageload.LoadUtils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class BannerView extends ViewPager {

	private ViewGroup mAllContainer;
	private ViewGroup mImageContainer;
	private ViewGroup mImageTitleContainer;

	private Context context;
	private View view;

	public BannerView(Context context) {
		super(context);
		this.context = context;
		// init(context);
	}

	public BannerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// init(context);
	}

	// private void init(Context context) {
	// view = View.inflate(context, R.layout.view_home_banner, null);
	// initView();
	//
	// // setOnClickListener(this);
	// }
	//
	// private void initView() {
	// mAllContainer = (ViewGroup) view.findViewById(R.id.banner_all_container);
	// mImageContainer = (ViewGroup)
	// view.findViewById(R.id.banner_img_container);
	// mImageTitleContainer = (ViewGroup)
	// view.findViewById(R.id.banner_title_container);
	//
	// initViewStatus();
	// }

	// public void onItemClick(View view) {
	//
	//
	//
	// if (!TextUtils.isEmpty(url)) {
	// Intent intent = new Intent(this.getContext(), WebBrowserActivity.class);
	// intent.putExtra(WebBrowserActivity.TITLE, "天气预警");
	// intent.putExtra(WebBrowserActivity.URL, NetConfig.BASE_URL + url);
	// getContext().startActivity(intent);
	// }
	// }

	public void updateView(final BannerEntity banner) {

		if (banner == null) {
			return;
		}

		if (banner.newdata == null) {
			return;
		}

		setAdapter(new PagerAdapter() {

			@Override
			public int getCount() {
				return banner.newdata.size();
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public Object instantiateItem(ViewGroup container, final int position) {
				view = View.inflate(context, R.layout.view_home_banner, null);
				mAllContainer = (ViewGroup) view.findViewById(R.id.banner_all_container);
				mImageContainer = (ViewGroup) view.findViewById(R.id.banner_img_container);
				mImageTitleContainer = (ViewGroup) view.findViewById(R.id.banner_title_container);

				initViewStatus();

				switch (banner.newdata.get(position).type) {

				case 1:
					mImageContainer.setVisibility(View.VISIBLE);
					dealImageContainer(banner.newdata.get(position));
					container.addView(view);
					break;
				case 2:
					mImageTitleContainer.setVisibility(View.VISIBLE);
					dealTitleImageContainer(banner.newdata.get(position));
					container.addView(view);
					break;
				case 3:
					mAllContainer.setVisibility(View.VISIBLE);
					dealAllView(banner.newdata.get(position));
					container.addView(view);
					break;
				default:
					break;
				}

				view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!TextUtils.isEmpty(banner.newdata.get(position).url)) {
							Intent intent = new Intent(context, WebBrowserActivity.class);
							intent.putExtra(WebBrowserActivity.TITLE, "天气预警");
							intent.putExtra(WebBrowserActivity.URL, NetConfig.BASE_URL + banner.newdata.get(position).url);
							getContext().startActivity(intent);
						}

					}
				});

				return container.getChildAt(position);
			}

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView((View) object);
			}
		});
	}

	private void initViewStatus() {
		mAllContainer.setVisibility(View.GONE);
		mImageContainer.setVisibility(View.GONE);
		mImageTitleContainer.setVisibility(View.GONE);
	}

	private void dealTitleImageContainer(ItemEntity itemEntity) {
		ImageView icon = (ImageView) mImageTitleContainer.findViewById(R.id.banner_title_container_icon);
		// if(!TextUtils.isEmpty(banner.data.picUrl)) {
		// LoadUtils.getInstance().loadImage(icon, banner.data.picUrl);
		// }
		if (!TextUtils.isEmpty(itemEntity.picName)) {

			String name = itemEntity.picName.substring(0, itemEntity.picName.lastIndexOf("."));
			int resId = this.getContext().getResources().getIdentifier(name, "drawable", getContext().getPackageName());
			if (resId != 0) {
				icon.setImageResource(resId);
			}
		}
		TextView title = (TextView) mImageTitleContainer.findViewById(R.id.banner_title_container_title);
		title.setText(itemEntity.content);
		// TextView content = (TextView)
		// findViewById(R.id.banner_all_container_content_above);

	}

	private void dealAllView(ItemEntity itemEntity) {
		ImageView icon = (ImageView) mAllContainer.findViewById(R.id.banner_all_container_icon);
		// if(!TextUtils.isEmpty(banner.data.picUrl)) {
		// LoadUtils.getInstance().loadImage(icon, banner.data.picUrl);
		// }

		if (!TextUtils.isEmpty(itemEntity.picName)) {
			String name = itemEntity.picName.substring(0, itemEntity.picName.lastIndexOf("."));
			int resId = this.getContext().getResources().getIdentifier(name, "drawable", getContext().getPackageName());

			if (resId != 0) {
				icon.setImageResource(resId);
			}
		}

		TextView title = (TextView) mAllContainer.findViewById(R.id.banner_all_container_title);
		title.setText(itemEntity.title);
		TextView above = (TextView) mAllContainer.findViewById(R.id.banner_all_container_content_above);
		above.setText(itemEntity.content);
	}

	private void dealImageContainer(ItemEntity itemEntity) {
		ImageView icon = (ImageView) mImageContainer.findViewById(R.id.banner_img_icon);
		if (!TextUtils.isEmpty(itemEntity.picUrl)) {
			LoadUtils.getInstance().loadImage(icon, itemEntity.picUrl);
		}

	}
}
