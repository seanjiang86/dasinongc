package com.dasinong.app.ui;

import java.util.ArrayList;
import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.database.task.dao.impl.SubStageDaoImpl;
import com.dasinong.app.database.task.dao.impl.TaskSpecDaoImpl;
import com.dasinong.app.database.task.domain.SubStage;
import com.dasinong.app.database.task.domain.TaskSpec;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.view.AnimatedExpandableListView;
import com.dasinong.app.ui.view.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import com.dasinong.app.ui.view.TopbarView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

public class TaskListActivity extends BaseActivity {
	private TopbarView mTopbarView;
	private AnimatedExpandableListView listView;
	private ExampleAdapter adapter;
	List<GroupItem> taskDataList = new ArrayList<GroupItem>();

	private List<TaskSpec> mSelectTask = new ArrayList<TaskSpec>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list);

		initData();
		initView();
		setUpView();

		requestData();

		adapter = new ExampleAdapter(this);
		adapter.setData(taskDataList);

		listView = (AnimatedExpandableListView) findViewById(R.id.listView);
		listView.setAdapter(adapter);

		// if(listView.getChildCount()>0)
		listView.expandGroup(0);

		// In order to show animations, we need to use a custom click handler
		// for our ExpandableListView.
		listView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				// We call collapseGroupWithAnimation(int) and
				// expandGroupWithAnimation(int) to animate group
				// expansion/collapse.
				if (listView.isGroupExpanded(groupPosition)) {
					// listView.collapseGroupWithAnimation(groupPosition);
					listView.collapseGroup(groupPosition);
				} else {
					// listView.expandGroupWithAnimation(groupPosition);
					listView.expandGroup(groupPosition);
				}
				return true;
			}

		});

		listView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

				TaskSpec child = adapter.getChild(groupPosition, childPosition);
				showToast(child.taskSpecName);

				return false;
			}
		});

	}

	private void requestData() {
		RequestService.getInstance().getAllTask(this, "10", BaseEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {

			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {

			}
		});
	}

	private void initData() {
		SubStageDaoImpl dao = new SubStageDaoImpl(this);
		List<String> queryStageCategory = dao.queryStageCategory();
		List<SubStage> queryStageSubCategory = dao.queryStageSubCategory(queryStageCategory.get(0));

		TaskSpecDaoImpl dao1 = new TaskSpecDaoImpl(this);
		for (SubStage subStage : queryStageSubCategory) {
			GroupItem item = new GroupItem();
			item.subStage = subStage;
			List<TaskSpec> queryTaskSpecWithSubStage = dao1.queryTaskSpecWithSubStage(subStage.subStageId);
			item.items = queryTaskSpecWithSubStage;
			taskDataList.add(item);
		}

	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
	}

	private void setUpView() {
		mTopbarView.setCenterText("全部农事");
		mTopbarView.setLeftView(true, true);
		mTopbarView.setRightText("提交");
		mTopbarView.setRightClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateTaskStates();
			}
		});
	}

	protected void updateTaskStates() {

		if (mSelectTask.size() <= 0) {
			return;
		}

		showToast("选择任务：" + mSelectTask.size());

		StringBuilder taskIds = new StringBuilder();
		StringBuilder taskStatuss = new StringBuilder();

		for (int i = 0; i < mSelectTask.size(); i++) {
			TaskSpec task = mSelectTask.get(i);
			if(i == mSelectTask.size()-1){
				taskIds.append(task.taskSpecId);
				taskStatuss.append(true+"");
			}else{
				taskIds.append(task.taskSpecId + ",");
				taskStatuss.append(true + ",");
			}
		}
//		for (TaskSpec task : mSelectTask) {
//			taskIds.append(task.taskSpecId + ",");
//			taskStatuss.append(true + ",");
//		}

		startLoadingDialog();
		RequestService.getInstance().updateTask(this, "10", /*taskIds.toString()*/"4,5,6", taskStatuss.toString(), BaseEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();

			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();

			}
		});
	}

	private static class GroupItem {
		SubStage subStage;
		List<TaskSpec> items = new ArrayList<TaskSpec>();
	}

	private static class ChildHolder {
		TextView title;
		CheckBox state;
		// TextView hint;
	}

	private static class GroupHolder {
		TextView title;
	}

	/**
	 * Adapter for our list of {@link GroupItem}s.
	 */
	private class ExampleAdapter extends AnimatedExpandableListAdapter {
		private LayoutInflater inflater;

		private List<GroupItem> items;

		public ExampleAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void setData(List<GroupItem> items) {
			this.items = items;
		}

		@Override
		public TaskSpec getChild(int groupPosition, int childPosition) {
			return items.get(groupPosition).items.get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			ChildHolder holder;
			final TaskSpec item = getChild(groupPosition, childPosition);
			if (convertView == null) {
				holder = new ChildHolder();
				convertView = inflater.inflate(R.layout.task_list_item, parent, false);
				holder.title = (TextView) convertView.findViewById(R.id.textTitle);
				holder.state = (CheckBox) convertView.findViewById(R.id.cb_state);
				convertView.setTag(holder);
			} else {
				holder = (ChildHolder) convertView.getTag();
			}

			holder.title.setText(item.taskSpecName);
			// holder.hint.setText(item.type);

			holder.state.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						if (!mSelectTask.contains(item)) {
							mSelectTask.add(item);
						}
					} else {
						if (mSelectTask.contains(item)) {
							mSelectTask.remove(item);
						}
					}
				}
			});

			return convertView;
		}

		@Override
		public int getRealChildrenCount(int groupPosition) {
			return items.get(groupPosition).items.size();
		}

		@Override
		public GroupItem getGroup(int groupPosition) {
			return items.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return items.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			GroupHolder holder;
			GroupItem item = getGroup(groupPosition);
			if (convertView == null) {
				holder = new GroupHolder();
				convertView = inflater.inflate(R.layout.task_group_item, parent, false);
				holder.title = (TextView) convertView.findViewById(R.id.textTitle);
				convertView.setTag(holder);
			} else {
				holder = (GroupHolder) convertView.getTag();
			}

			holder.title.setText(item.subStage.stageName);

			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			return true;
		}

	}

}
