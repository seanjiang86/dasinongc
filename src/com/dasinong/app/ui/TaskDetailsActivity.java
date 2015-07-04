package com.dasinong.app.ui;

import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.database.task.dao.impl.StepsDaoImpl;
import com.dasinong.app.database.task.dao.impl.SubStageDaoImpl;
import com.dasinong.app.database.task.dao.impl.TaskSpecDaoImpl;
import com.dasinong.app.database.task.domain.Steps;
import com.dasinong.app.database.task.domain.SubStage;
import com.dasinong.app.database.task.domain.TaskSpec;
import com.dasinong.app.ui.adapter.TaskDetailsAdapter;
import com.dasinong.app.ui.view.TopbarView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class TaskDetailsActivity extends BaseActivity {

	public static final String TASK_ID = "task_id";
	
	private TopbarView mTopbarView;
	
	private ListView mListView;

	private List<Steps> taskSpecs;
	
	private int taskId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tast_details);
		
		initData();
		initView();
		setUpView();
		
	}

	private void initData() {
		taskId = getIntent().getIntExtra(TASK_ID, 0);
		
		StepsDaoImpl dao1 = new StepsDaoImpl(this);
		taskSpecs = dao1.queryStepsWithTaskSpecId(taskId);
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mListView = (ListView) this.findViewById(R.id.list_task_detail_list);
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
		
		TaskDetailsAdapter mAdapter = new TaskDetailsAdapter(this, taskSpecs, false);
		mListView.setAdapter(mAdapter);
//		mListView
		
	}
	
}
