package com.dasinong.app.ui.adapter;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dasinong.app.R;
import com.dasinong.app.database.task.domain.Steps;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.SmsSubscribeItem;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.BaseActivity;
import com.dasinong.app.ui.MyInfoActivity;
import com.dasinong.app.ui.RegisterPhoneActivity;
import com.dasinong.app.ui.RegisterServiceActivity;
import com.dasinong.app.ui.manager.AccountManager;
import com.liam.imageload.LoadUtils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TaskDetailsAdapter extends MyBaseAdapter<Steps> {

	public TaskDetailsAdapter(Context ctx, List<Steps> list, boolean flag) {
		super(ctx, list, flag);
	}

	@Override
	public View getView(int pos, View view, ViewGroup group) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.view_task_steps_item, null);
			holder.stepImage = (ImageView) view.findViewById(R.id.imageview_steps_image);
			holder.nameText = (TextView) view.findViewById(R.id.textview_title);
			holder.contentText = (TextView) view.findViewById(R.id.textview_content);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		Steps item = list.get(pos);

		if (TextUtils.isEmpty(item.picture)) {
			holder.stepImage.setVisibility(View.GONE);
		} else {
			LoadUtils.getInstance().loadImage(holder.stepImage, "http://182.254.129.101:8080/nongshi/" + item.picture + ".jpg");
		}

		holder.nameText.setText(item.stepName);
		holder.contentText.setText(item.description);
		holder.contentText.setText(test(holder.contentText));
		return view;
	}

	private String test(TextView textview) {
		SpannableString s = new SpannableString(textview.getText().toString());

		Pattern p = Pattern.compile("草");

		Matcher m = p.matcher(s);

		while (m.find()) {
			int start = m.start();
			int end = m.end();

			s.setSpan(new ClickableSpan() {

				@Override
				public void onClick(View arg0) {
					Toast.makeText(context, "注册协议", Toast.LENGTH_SHORT).show();
				}

				@Override
				public void updateDrawState(TextPaint ds) {
					// TODO Auto-generated method stub
					super.updateDrawState(ds);
					ds.setUnderlineText(false);
				}

			}, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			s.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			s.setSpan(new BackgroundColorSpan(Color.WHITE), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

			textview.setHighlightColor(Color.TRANSPARENT);

			return s.toString();
		}
		return null;
	}

	private SpannableStringBuilder dealWeiboContent(String temp, TextView textView) {
		Pattern pattern = Pattern.compile("((http://|https://){1}[\\w\\.\\-/:]+)|(#(.+?)#)|(@[\\u4e00-\\u9fa5\\w\\-]+)");
		// temp = weiboContent;
		Matcher matcher = pattern.matcher(temp);
		List<String> list = new LinkedList<String>();
		while (matcher.find()) {
			if (!list.contains(matcher.group())) {
				temp = temp.replace(matcher.group(), "<a href=\"" + matcher.group() + "\">" + matcher.group() + "</a>");
				/*
				 * temp = temp.replace( matcher.group(),
				 * "<font color='#365C7C'><a href='" + matcher.group() + "'>" +
				 * matcher.group() + "</a></font>");
				 */
			}
			list.add(matcher.group());
		}
		textView.setText(Html.fromHtml(temp));
		System.out.println("temp" + temp);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
		CharSequence text = textView.getText();
		if (text instanceof Spannable) {
			int end = text.length();
			Spannable sp = (Spannable) textView.getText();
			URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
			SpannableStringBuilder style = new SpannableStringBuilder(text);
			style.clearSpans();
			for (URLSpan url : urls) {
				style.setSpan(typeClick(url.getURL()), sp.getSpanStart(url), sp.getSpanEnd(url),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			return style;
		}
		return null;
	}

	public ClickableSpan typeClick(final String value) {
		return new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				// TODO Auto-generated method stub
				String uname = "";
				uname = value.substring(1, value.length());
				System.out.println("weiboContent---uanme---" + uname);
				Toast.makeText(context, "独守空闺的分开", 0).show();

			}

			@Override
			public void updateDrawState(TextPaint ds) {
				ds.setColor(Color.argb(255, 54, 92, 124));
				ds.setUnderlineText(true);
			}
		};

	}

	public static class ViewHolder {
		ImageView stepImage;
		TextView nameText;
		TextView contentText;
	}

}
