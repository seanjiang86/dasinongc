package com.dasinong.app.entity;

import java.util.List;

import com.dasinong.app.database.encyclopedias.domain.Cpproductbrowse;

public class PesticideListEntity extends BaseEntity {

	private List<Cpproductbrowse> data;

	public List<Cpproductbrowse> getData() {
		return data;
	}

	public void setData(List<Cpproductbrowse> data) {
		this.data = data;
	}
	
	
	
}
