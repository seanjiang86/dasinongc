package com.dasinong.app.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dasinong.app.R;
import com.dasinong.app.database.disaster.domain.NatDisspec;
import com.dasinong.app.database.disaster.domain.PetDisspec;
import com.dasinong.app.database.disaster.service.DisasterManager;
import com.dasinong.app.ui.HarmDetialsActivity;
import com.dasinong.app.ui.adapter.HarmAdapter;

/**
 * 
 * @author Ming 此类为病虫草害列表页每个Indicator对应的Fragment
 */

public class HarmFragment extends Fragment {

	private List list = new ArrayList();
	// private List<NatDisspec> natList = new ArrayList<NatDisspec> ();
	private int fragmentPosition;

	public final static String TYPE_PET = "petdisspec";

	public static HarmFragment newInstance(int position) {

		HarmFragment myFragment = new HarmFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("position", position);
		myFragment.setArguments(bundle);

		return myFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		fragmentPosition = getArguments() != null ? getArguments().getInt("position") : 0;

		initdata(fragmentPosition);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = View.inflate(getActivity(), R.layout.fragment_harm, null);
		ListView lv_harm = (ListView) view.findViewById(R.id.lv_harm);

		lv_harm.setAdapter(new HarmAdapter<NatDisspec>(getActivity(), list, fragmentPosition, false));

		lv_harm.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getActivity(), HarmDetialsActivity.class);

				PetDisspec pet = (PetDisspec) list.get(position);
				Bundle bundle = new Bundle();
				bundle.putString("type", TYPE_PET);
				bundle.putSerializable("pet", pet);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		return view;
	}

	// 加载数据
	private void initdata(int position) {
		String type = null;

		DisasterManager manager = DisasterManager.getInstance(getActivity());
		switch (position) {
		case 0:
			type = "病害";
			break;
		case 1:
			type = "虫害";
			break;
		case 2:
			type = "草害";
			break;
		}
		list = manager.getDisease(type);
	}
}
