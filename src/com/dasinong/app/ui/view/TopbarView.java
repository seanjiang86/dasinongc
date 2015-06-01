package com.dasinong.app.ui.view;

import com.dasinong.app.R;
import com.dasinong.app.ui.BaseActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @ClassName TopbarView
 * @author linmu
 * @Decription 
 * @2015-5-27 下午10:07:45
 */
public class TopbarView extends RelativeLayout {

	private LinearLayout mLeftLayout;
	private ImageView mLeftBackImage;
	
	private TextView mCenterText;
	private TextView mRightText;
	
	private Context mContext;
	
	public TopbarView(Context context) {
		super(context);
		initView(context);
	}

	public TopbarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public TopbarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	private void initView(Context context) {
		this.mContext = context;
		
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.topbar,this);
		
		mLeftLayout = (LinearLayout) this.findViewById(R.id.layout_topbar_left);
		mLeftBackImage = (ImageView) this.findViewById(R.id.imageview_topbar_left_backImage);
		mCenterText = (TextView) this.findViewById(R.id.textview_topbar_center_text);
		mRightText = (TextView) this.findViewById(R.id.textview_topbar_right_text);
		
		mLeftLayout.setVisibility(View.GONE);
		mRightText.setVisibility(View.GONE);
	}

	public void setLeftView(boolean isBack,boolean isFinish){
		mLeftLayout.setVisibility(View.VISIBLE);
		if(isBack){
			mLeftBackImage.setVisibility(View.VISIBLE);
		}else{
			mLeftBackImage.setVisibility(View.GONE);
		}
		if(isFinish){
			mLeftLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					((BaseActivity)mContext).finish();
				}
			});
		}
	}
	
	public void setCenterText(String text){
		mCenterText.setVisibility(View.VISIBLE);
		mCenterText.setText(text);
	}
	
	public void setCenterText(int res){
		mCenterText.setVisibility(View.VISIBLE);
		mCenterText.setText(res);
	}
	
	public void setRightVisible(boolean visible){
		if(visible){
			mRightText.setVisibility(View.VISIBLE);
		}else{
			mRightText.setVisibility(View.GONE);
		}
	}
	
	public void setLeftClickListener(OnClickListener onClickListener) {
		mLeftLayout.setVisibility(View.VISIBLE);
		if(onClickListener != null){
			mLeftLayout.setOnClickListener(onClickListener);
		}
	}
	
	public void setRightClickListener(OnClickListener onClickListener){
		if(onClickListener != null){
			mRightText.setOnClickListener(onClickListener);
		}
	}
	
}
