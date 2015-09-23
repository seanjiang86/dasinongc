package com.dasinong.app.components.domain;

import java.util.List;

/**
 * Created by liuningning on 15/6/28.
 */
public class BannerEntity extends BaseResponse {

	public ItemEntity data;

	public List<ItemEntity> newdata;

	public static class ItemEntity implements Comparable<ItemEntity> {
		/**
		 * content : 高考成绩不理想真无所谓，你奋斗过，努力过，拼搏过，就不必太在乎结果。考的分低，
		 * 不说明你蠢，但为此离家出走、自残自杀，就是蠢了。高考，只是一段分数、一个大学而已，
		 * 命运哪能让它决定？傻逼的人生觉得分低无法解释，彪悍的人生是再考一次！ id : 1 title : 一个大学而已 type : 2 url
		 * : #
		 */
		public String content;
		public int id;
		public String title;
		public int type;
		public String url;
		public String picUrl;
		public String picName;
		
		// TODO MING 暂时解决陶氏包显示banner问题

		@Override
		public int compareTo(ItemEntity another) {
			if (id < another.id) {
				return 1;
			} else if (id > another.id) {
				return -1;
			}
			return 0;
		}
	}

	public static class Param {
		public String lat;
		public String lon;
		public String monitorLocationId;
	}
}
