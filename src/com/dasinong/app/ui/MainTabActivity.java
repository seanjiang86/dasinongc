package com.dasinong.app.ui;

import com.dasinong.app.R;
import com.dasinong.app.ui.fragment.EncyclopediaFragment;
import com.dasinong.app.ui.fragment.HomeFragment;
import com.dasinong.app.ui.fragment.MeFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

/**
 * @ClassName MainTabActivity
 * @author ysl
 * @Description 
 */
public class MainTabActivity extends BaseActivity{	
	private FragmentTabHost mTabHost;
	
	private LayoutInflater layoutInflater;
		
	private Class fragmentArray[] = {HomeFragment.class,EncyclopediaFragment.class,MeFragment.class};
	
//	private int mImageViewArray[] = {R.drawable.main_tab1_selector,R.drawable.main_tab2_selector,R.drawable.main_tab3_selector,
	
	private String mTextviewArray[] = {"大司农", "农事百科","更多"};

	private int index;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab_layout);
        initData();
        initView();
        
//        startLoadingDialog();
        
//        if(getIntent() != null){
//			index = getIntent().getIntExtra("index", 0);
//			if(index != 0){
//				mTabHost.setCurrentTab(index);
//			}
//		}
    }
	

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if(intent != null){
			index = intent.getIntExtra("index", 0);
			if(index != 0){
				mTabHost.setCurrentTab(index);
			}
		}
	}
	 
	protected void initData() {
		index = getIntent().getIntExtra("index", 0);
	}

	protected void initView(){
		layoutInflater = LayoutInflater.from(this);
				
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);	
		
		int count = fragmentArray.length;	
				
		for(int i = 0; i < count; i++){	
			TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
		}
		
		if(index != 0){
			mTabHost.setCurrentTab(index);
		}
		
	}
				
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
	}
	
	private View getTabItemView(int index){
		View view = layoutInflater.inflate(R.layout.view_main_tab_item, null);
	
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
//		imageView.setImageResource(mImageViewArray[index]);
		
		TextView textView = (TextView) view.findViewById(R.id.textview);		
		textView.setText(mTextviewArray[index]);
	
		return view;
	}
	
	@Override
    public void onResume() {
    	super.onResume();
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    }
    
}
