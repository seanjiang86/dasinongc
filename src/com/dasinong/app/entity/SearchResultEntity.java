package com.dasinong.app.entity;

import java.util.List;

public class SearchResultEntity extends BaseEntity {

	private SearchData data;
	
	public SearchData getData() {
		return data;
	}

	public void setData(SearchData data) {
		this.data = data;
	}

	public class SearchData {

		private List<SearchItem> disease;
		private List<SearchItem> pest;
		private List<SearchItem> weeds;
		private List<SearchItem> variety;

		 private List<SearchItem> cpproduct;

		public List<SearchItem> getDisease() {
			return disease;
		}

		public void setDisease(List<SearchItem> disease) {
			this.disease = disease;
		}

		public List<SearchItem> getPest() {
			return pest;
		}

		public void setPest(List<SearchItem> pest) {
			this.pest = pest;
		}

		public List<SearchItem> getWeeds() {
			return weeds;
		}

		public void setWeeds(List<SearchItem> weeds) {
			this.weeds = weeds;
		}

		public List<SearchItem> getVariety() {
			return variety;
		}

		public void setVariety(List<SearchItem> variety) {
			this.variety = variety;
		}

		 public List<SearchItem> getCpproduct() {
		 return cpproduct;
		 }
		
		 public void setCpproduct(List<SearchItem> cpproduct) {
		 this.cpproduct = cpproduct;
		 }

	}

	

}
