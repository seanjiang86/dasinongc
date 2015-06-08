package com.dasinong.app.ui;

import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.database.task.dao.impl.SubStageDaoImpl;
import com.dasinong.app.database.task.dao.impl.TaskSpecDaoImpl;
import com.dasinong.app.database.task.domain.SubStage;
import com.dasinong.app.ui.view.TopbarView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class TaskDetailsActivity extends BaseActivity {

	private TopbarView mTopbarView;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tast_details);
		
		initData();
		initView();
		setUpView();
		
	}

	private void initData() {
		
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
	}

	private void setUpView() {
		mTopbarView.setCenterText("农事指导");
		mTopbarView.setLeftView(true, true);
		mTopbarView.setRightText("查看全部");
		mTopbarView.setRightClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TaskDetailsActivity.this,TaskListActivity.class);
				startActivity(intent);
			}
		});
	}
	
}
