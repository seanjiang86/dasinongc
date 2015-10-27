package com.dasinong.app.components.home.view;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.Toast;

import com.dasinong.app.R;
import com.dasinong.app.components.domain.BannerEntity;
import com.dasinong.app.components.domain.BannerEntity.ItemEntity;
import com.dasinong.app.net.NetConfig;
import com.dasinong.app.ui.WebBrowserActivity;
import com.lidroid.xutils.BitmapUtils;

public class BannerView extends ViewPager {

	private Context context;
	private BannerEntity entity;

	public BannerView(Context context) {
		super(context);
		this.context = context;
	}

	public BannerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public void initView(BannerEntity banner) {

		if (banner == null) {
			return;
		}

		if (banner.newdata == null) {
			return;
		}

		entity = banner;

		List<View> viewList = new ArrayList<View>();
		List<ItemEntity> dataList = banner.newdata;
		
		for (final ItemEntity itemEntity : dataList) {
			switch (itemEntity.type) {
			case 1:
				View adBanner = View.inflate(context, R.layout.view_home_banner_ad, null);
				ImageView adImage = (ImageView) adBanner.findViewById(R.id.banner_ad_img);
				BitmapUtils bitmapUtils = new BitmapUtils(context);
				
				if (!TextUtils.isEmpty(itemEntity.picName)) {
					if(itemEntity.picName.startsWith("http://")){
//						LoadUtils.getInstance().loadImage(adImage, itemEntity.picName);
						bitmapUtils.display(adImage, itemEntity.picName);
					} else {
//						LoadUtils.getInstance().loadImage(adImage, "http://" + itemEntity.picName);
						bitmapUtils.display(adImage, "http://" + itemEntity.picName);
					}
				}

				viewList.add(adBanner);
				break;
			case 2:
				View warnBanner = View.inflate(context, R.layout.view_home_banner_warn, null);
				ImageView warnIcon = (ImageView) warnBanner.findViewById(R.id.banner_title_container_icon);
				if (!TextUtils.isEmpty(itemEntity.picName)) {

					String name = itemEntity.picName.substring(0, itemEntity.picName.lastIndexOf("."));
					int resId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
					if (resId != 0) {
						warnIcon.setImageResource(resId);
					}
				}
				TextView warnTitle = (TextView) warnBanner.findViewById(R.id.banner_title_container_title);
				warnTitle.setText(itemEntity.content);

				viewList.add(warnBanner);
				break;
			case 3:
				View sayingBanner = View.inflate(context, R.layout.view_home_banner_saying, null);
				ImageView sayingIcon = (ImageView) sayingBanner.findViewById(R.id.banner_all_container_icon);

				if (!TextUtils.isEmpty(itemEntity.picName)) {
					String name = itemEntity.picName.substring(0, itemEntity.picName.lastIndexOf("."));
					int resId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());

					if (resId != 0) {
						sayingIcon.setImageResource(resId);
					}
				}

				TextView sayingTitle = (TextView) sayingBanner.findViewById(R.id.banner_all_container_title);
				TextView sayingContent = (TextView) sayingBanner.findViewById(R.id.banner_all_container_content_above);

				sayingTitle.setText(itemEntity.title);
				sayingContent.setText(itemEntity.content);

				viewList.add(sayingBanner);
				break;
			}
		}
		initViewPager(viewList);
	}

	private void initViewPager(final List<View> viewList) {
		if (viewList == null || viewList.isEmpty()) {
			return;
		}

		setAdapter(new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return viewList.size();
			}

			@Override
			public Object instantiateItem(ViewGroup container, final int position) {
				container.addView(viewList.get(position));
				viewList.get(position).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!TextUtils.isEmpty(entity.newdata.get(position).url)) {
							Intent intent = new Intent(context, WebBrowserActivity.class);
							if(entity.newdata.get(position).type == 1){
								intent.putExtra(WebBrowserActivity.URL, "http://" + entity.newdata.get(position).url);
							} else if(entity.newdata.get(position).type == 2){
								intent.putExtra(WebBrowserActivity.TITLE, "天气预警");
								intent.putExtra(WebBrowserActivity.URL, NetConfig.BASE_URL + entity.newdata.get(position).url);
							}
							getContext().startActivity(intent);
						}
					}
				});
				return viewList.get(position);
			}

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView(viewList.get(position));
			}

		});
	}
}
