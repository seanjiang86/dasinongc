package com.dasinong.app.entity;

import java.util.List;

public class VarietyNamedListEntity extends BaseEntity {

	private List<Variety> data;

	public List<Variety> getData() {
		return data;
	}

	public void setData(List<Variety> data) {
		this.data = data;
	}

	// "varietyName": "临麦",
	// "registrationId": "甘审麦2010002",
	// "suitableArea": "适宜我省临夏种植",
	// "yieldPerformance":
	// "2006—2007年参加全省东片水地区域试验,平均亩产335.07公斤,较对照陇春23号减产5.5%。2007年参加临夏州生产试验,平均亩产383.95公斤,较当地品种增产11.36%",
	// "characteristics":
	// "属春性小麦,生育期98~110天。幼苗直立,旗叶上举,株型紧凑,株高95~105厘米。穗纺锤形,顶芒、白颖壳,籽粒卵圆、红粒、角质,千粒重37~43克,容重781~800克/升。2009年经省农科院植保所分小种人工接种鉴定,苗期和成株期对混合菌、HY8、水4、水7、条中32、条中33表现免疫。2008年品质测定,含粗蛋白13.45%,湿面筋29.7%,沉降值32.0毫升,面团形成时间2.5分钟,稳定时间1.7分钟,软化度169F.U,评价值37。",
	// "id": 241,
	// "owner": "临夏回族自治州农业科学研究所"

	public class Variety {

		private String varietyName;
		private String registrationId;
		private String suitableArea;
		private String yieldPerformance;
		private String characteristics;
		private String id;
		private String owner;

		public String getVarietyName() {
			return varietyName;
		}

		public void setVarietyName(String varietyName) {
			this.varietyName = varietyName;
		}

		public String getRegistrationId() {
			return registrationId;
		}

		public void setRegistrationId(String registrationId) {
			this.registrationId = registrationId;
		}

		public String getSuitableArea() {
			return suitableArea;
		}

		public void setSuitableArea(String suitableArea) {
			this.suitableArea = suitableArea;
		}

		public String getYieldPerformance() {
			return yieldPerformance;
		}

		public void setYieldPerformance(String yieldPerformance) {
			this.yieldPerformance = yieldPerformance;
		}

		public String getCharacteristics() {
			return characteristics;
		}

		public void setCharacteristics(String characteristics) {
			this.characteristics = characteristics;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getOwner() {
			return owner;
		}

		public void setOwner(String owner) {
			this.owner = owner;
		}

	}

}
