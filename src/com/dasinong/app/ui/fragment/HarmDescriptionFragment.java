package com.dasinong.app.ui.fragment;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.VarietyEntity;
import com.dasinong.app.ui.VarietyDetialActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HarmDescriptionFragment extends Fragment {

	private String currentTitle;
	private BaseEntity entity;

	public static Fragment newInstance(String title, BaseEntity entity) {
		HarmDescriptionFragment fragment = new HarmDescriptionFragment();
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		bundle.putSerializable("entity", entity);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		currentTitle = getArguments() != null ? getArguments().getString("title") : "";
		entity = (BaseEntity) getArguments().getSerializable("entity");
		super.onCreate(savedInstanceState);
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = null;

		switch (currentTitle) {
		case VarietyDetialActivity.YIELD_PERFORMANCE:
			view = view.inflate(getActivity(), R.layout.fragment_baike_text, null);
			TextView tv_yieldPerformance = (TextView) view.findViewById(R.id.tv);

			if (entity != null) {
				VarietyEntity varietyEntity = (VarietyEntity) entity;
				tv_yieldPerformance.setText(varietyEntity.data.yieldPerformance);
			}

			break;
		case VarietyDetialActivity.CHARACTERISTICS:
			view = view.inflate(getActivity(), R.layout.fragment_baike_text, null);
			TextView tv_characteristics = (TextView) view.findViewById(R.id.tv);

			if (entity != null) {
				VarietyEntity varietyEntity = (VarietyEntity) entity;
				tv_characteristics.setText(varietyEntity.data.characteristics);
			}
			break;
		}
		return view;
	}
}
