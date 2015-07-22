package com.dasinong.app.ui.adapter;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dasinong.app.R;
import com.dasinong.app.database.encyclopedias.domain.Petdisspecbrowse;
import com.dasinong.app.entity.DiseaseEntity;

public class VarietiesFirstListAdapter extends MyBaseAdapter<String> {
	
	private static final String[] strs = {"粮食作物","经济作物","蔬菜","水果","菌类","坚果","花卉","草料","药材"};
	private static final int[] resIds = {R.drawable.icon_liangshi,R.drawable.icon_jingjizuowu,R.drawable.icon_shucai,
			R.drawable.icon_shuiguo,R.drawable.icon_junlei,R.drawable.icon_jianguo,
			R.drawable.icon_huahui,R.drawable.icon_caoliao,R.drawable.icon_yaocai};
	
	public VarietiesFirstListAdapter(Context ctx, List<String> list, boolean flag) {
		super(ctx, Arrays.asList(strs), flag);
	}
	
	@Override
	public View getView(int pos, View view, ViewGroup group) {
		ViewHolder holder;
		if(view == null){
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.view_sms_sub_item, null);
			holder.deleteImage = (ImageView) view.findViewById(R.id.imageview_delete_image);
			holder.nameText = (TextView) view.findViewById(R.id.textview_item_name);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final String item = list.get(pos);
		holder.nameText.setText(item);
		return view;
	}

	public static class ViewHolder {
		ImageView  deleteImage;
		TextView  nameText;
	}
	
}
