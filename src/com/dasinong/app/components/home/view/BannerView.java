package com.dasinong.app.components.home.view;

import android.content.Context;
import android.opengl.Visibility;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dasinong.app.R;
import com.dasinong.app.components.domain.BannerEntity;
import com.dasinong.app.net.NetConfig;
import com.liam.imageload.LoadUtils;

/**
 * Created by liuningning on 15/6/5.
 */
public class BannerView extends LinearLayout implements View.OnClickListener {

    private ViewGroup mAllContainer;
    private ViewGroup mImageContainer;
    private ViewGroup mImageTitleContainer;

    private String url;

    public BannerView(Context context) {
        super(context);
        init(context);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_home_banner, this, true);
        initView();
        setOnClickListener(this);
    }

    private void initView() {
        mAllContainer = (ViewGroup) findViewById(R.id.banner_all_container);
        mImageContainer = (ViewGroup) findViewById(R.id.banner_img_container);
        mImageTitleContainer = (ViewGroup) findViewById(R.id.banner_title_container);

        visibilityAllView();
    }

    private void visibilityAllView() {
        mAllContainer.setVisibility(View.GONE);
        mImageContainer.setVisibility(View.GONE);
        mImageTitleContainer.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        //TODO banner

        if (!TextUtils.isEmpty(url)) {
            Toast.makeText(getContext(), "open url" + url, Toast.LENGTH_SHORT).show();
        }

    }

    public void updateView(BannerEntity banner) {

        if (banner == null) {
            return;
        }

        url = banner.data.url;
        visibilityAllView();
        switch (banner.data.type) {

            case 0:
                mImageContainer.setVisibility(VISIBLE);
                dealImageContainer(banner);
                break;
            case 2:
                mAllContainer.setVisibility(VISIBLE);
                dealAllView(banner);
                break;
            case 1:
                mImageTitleContainer.setVisibility(VISIBLE);
                dealTitleImageContainer(banner);
                break;
            default:
                break;
        }


    }

    private void dealTitleImageContainer(BannerEntity banner) {
        ImageView icon = (ImageView) findViewById(R.id.banner_title_container_icon);
        //  LoadUtils.getInstance().loadImage(icon, NetConfig.IMAGE_URL+user.getPictureId());

        TextView title = (TextView) findViewById(R.id.banner_title_container_title);
        title.setText(banner.data.title);
        // TextView content = (TextView) findViewById(R.id.banner_all_container_content_above);

    }

    private void dealAllView(BannerEntity banner) {

        ImageView icon = (ImageView) findViewById(R.id.banner_all_container_icon);
        //  LoadUtils.getInstance().loadImage(icon, NetConfig.IMAGE_URL+user.getPictureId());

        TextView title = (TextView) findViewById(R.id.banner_all_container_title);
        title.setText(banner.data.title);
        TextView above = (TextView) findViewById(R.id.banner_all_container_content_above);
        above.setText(banner.data.content);
        TextView behind = (TextView) findViewById(R.id.banner_all_container_content_behind);


    }

    private void dealImageContainer(BannerEntity banner) {
        ImageView icon = (ImageView) findViewById(R.id.banner_img_icon);
        //  LoadUtils.getInstance().loadImage(icon, NetConfig.IMAGE_URL+user.getPictureId());


    }
}
