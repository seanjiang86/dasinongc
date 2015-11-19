package com.dasinong.app.ui.fragment;

import com.dasinong.app.R;
import com.dasinong.app.database.disaster.domain.PetDisspec;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.CPProductEntity;
import com.dasinong.app.entity.VarietyEntity;
import com.dasinong.app.ui.PesticideDetailActivity;
import com.dasinong.app.ui.VarietyDetailActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HarmDescriptionFragment extends Fragment {

	private String currentTitle;
	private BaseEntity entity;
	private View view;

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

		switch (currentTitle) {
		case VarietyDetailActivity.YIELD_PERFORMANCE:
			view = View.inflate(getActivity(), R.layout.fragment_baike_text, null);
			TextView tv_yieldPerformance = (TextView) view.findViewById(R.id.tv);

			if (entity != null) {
				VarietyEntity varietyEntity = (VarietyEntity) entity;
				tv_yieldPerformance.setText(varietyEntity.data.yieldPerformance);
			}

			break;
		case VarietyDetailActivity.CHARACTERISTICS:
			view = View.inflate(getActivity(), R.layout.fragment_baike_text, null);
			TextView tv_characteristics = (TextView) view.findViewById(R.id.tv);

			if (entity != null) {
				VarietyEntity varietyEntity = (VarietyEntity) entity;
				tv_characteristics.setText(varietyEntity.data.characteristics);
			}
			break;
		case PesticideDetailActivity.USE_DIRECTION:
			view = View.inflate(getActivity(), R.layout.fragment_baike_table, null);
			LinearLayout root = (LinearLayout) view.findViewById(R.id.ll_root);

			if (entity != null) {
				CPProductEntity cPProductEntity = (CPProductEntity) entity;
				for (int i = 0; i < cPProductEntity.data.useDirections.size(); i++) {
					TableLayout table = (TableLayout) View.inflate(getActivity(), R.layout.item_table, null);
					TextView tv_crop = (TextView) table.findViewById(R.id.tv_crop);
					TextView tv_disease = (TextView) table.findViewById(R.id.tv_disease);
					TextView tv_volumn = (TextView) table.findViewById(R.id.tv_volumn);
					TextView tv_method = (TextView) table.findViewById(R.id.tv_method);

					tv_crop.setText(cPProductEntity.data.useDirections.get(i).useCrop);
					tv_disease.setText(cPProductEntity.data.useDirections.get(i).useDisease);
					tv_volumn.setText(cPProductEntity.data.useDirections.get(i).useVolumn);
					tv_method.setText(cPProductEntity.data.useDirections.get(i).useMethod);

					LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
					params.setMargins(0, 0, 0, 20);
					table.setLayoutParams(params);
					root.addView(table);
				}
			}
			break;
		case PesticideDetailActivity.GUIDELINE:

			view = View.inflate(getActivity(), R.layout.fragment_baike_text, null);
			TextView tv_guideline = (TextView) view.findViewById(R.id.tv);

			if (entity != null) {
				CPProductEntity cPProductEntity = (CPProductEntity) entity;
				tv_guideline.setText(cPProductEntity.data.guideline);
			}

			break;
		case PesticideDetailActivity.TIP:

			view = View.inflate(getActivity(), R.layout.fragment_baike_text, null);
			TextView tv_tip = (TextView) view.findViewById(R.id.tv);

			if (entity != null) {
				CPProductEntity cPProductEntity = (CPProductEntity) entity;
				tv_tip.setText(cPProductEntity.data.tip);
			}

			break;
		}
		return view;
	}
}
