package com.dasinong.app.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dasinong.app.R;
import com.dasinong.app.database.disaster.domain.NatDisspec;
import com.dasinong.app.database.disaster.domain.PetDisspec;
import com.dasinong.app.database.disaster.service.DisasterManager;
import com.dasinong.app.ui.HarmDetialsActivity;
import com.dasinong.app.ui.adapter.MyBaseAdapter;

/**
 * 
 * @author Ming 此类为病虫草害列表页每个Indicator对应的Fragment
 */

public class HarmFragment extends Fragment {
	
	private List list = new ArrayList();
	//private List<NatDisspec> natList = new ArrayList<NatDisspec> ();
	private int position;
	
	public static HarmFragment newInstance(int position) {
		
		HarmFragment myFragment = new HarmFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("position", position);
		myFragment.setArguments(bundle);
		
		return myFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		position = getArguments() != null ? getArguments().getInt("position") : 1;
		
		initdata(position);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = View.inflate(getActivity(), R.layout.fragment_harm, null);
		ListView lv_harm = (ListView) view.findViewById(R.id.lv_harm);
		
		lv_harm.setAdapter(new HarmAdapter<NatDisspec>(getActivity(), list, false));
		
		lv_harm.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 Intent intent = new Intent(getActivity(),HarmDetialsActivity.class);
				 startActivity(intent);
			}
		});

		return view;
	}
	
	//加载数据
	private void initdata(int position) {
		String type = null ;
		
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
		case 3:
			type = "自然灾害";
			break;
		}
		if("自然灾害".equals(type)){
			list = manager.getNatDisease();
		}else{
			list = manager.getDisease(type);
		}
	}
	
	class HarmAdapter <T> extends MyBaseAdapter<T>{

		public HarmAdapter(Context ctx, List<T> list, boolean flag) {
			super(ctx, list, flag);
		}
		
		@Override
		public View getView(int pos, View view, ViewGroup group) {
			ViewHolder holder ;
			if(view == null){
				holder = new ViewHolder();
				view = View.inflate(context, R.layout.harm_list_item, null);
				holder.iv_harm_pic = (ImageView) view.findViewById(R.id.iv_harm_pic);
				holder.tv_harm_name = (TextView) view.findViewById(R.id.tv_harm_name);
				holder.tv_harm_des = (TextView) view.findViewById(R.id.tv_harm_des);
				view.setTag(holder);
			}else{
				holder = (ViewHolder) view.getTag();
			}
			
			if(position == 3){
				NatDisspec nat = (NatDisspec) list.get(pos);
				holder.iv_harm_pic.setVisibility(View.GONE);
				holder.tv_harm_name.setText(nat.natDisSpecName);
				holder.tv_harm_des.setText(nat.solution);
			}else{
				PetDisspec pet = (PetDisspec) list.get(pos);
				holder.iv_harm_pic.setImageResource(R.drawable.test_pic);
				holder.tv_harm_name.setText(pet.petDisSpecName);
				holder.tv_harm_des.setText(pet.description);
			}
			
			return view;
		}
	}
	
	public static class ViewHolder {
		ImageView iv_harm_pic;
		TextView tv_harm_name;
		TextView tv_harm_des;
	}

}
