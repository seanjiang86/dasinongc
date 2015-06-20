package com.dasinong.app.entity;

/**
 * @author: yusonglin
 * @类 说 明:
 * @version 1.0
 * @创建时间：2015-6-20 下午12:03:57
 * 
 */
public class SmsSubDetailEntity extends BaseEntity {

	private SmsSubscribeItem data;

	public SmsSubscribeItem getData() {
		return data;
	}

	public void setData(SmsSubscribeItem data) {
		this.data = data;
	}

}
