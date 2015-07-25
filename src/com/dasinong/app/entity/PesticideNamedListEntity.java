package com.dasinong.app.entity;

import java.util.List;

public class PesticideNamedListEntity extends BaseEntity {
	// "activeIngredient": "百菌清",
	// "disease": "炭疽病\n稻瘟病\n炭疽病\n稻瘟病",
	// "volumn": "125-158克/亩\n125-158克/亩\n125-158克/亩\n125-158克/亩",
	// "guideline": "本品应在发病前或发病初期用药，每隔7-10天喷施一次，连续用药2-3次。叶背、叶面均匀喷施。",
	// "registrationId": "PD20084701",
	// "manufacturer": "云南天丰农药有限公司",
	// "tip":
	// "1、产品的安全间隔期水稻为10天，每季作物最多使用3次。2、不能与石硫合剂、波尔多液等碱性农药混用，也不可与杀螟硫磷、克螨特、三环锡等农药混用。3、本品对鱼有毒，施药时须远离池塘湖泊。禁止在河塘等水体中清洗施药器具。用过的容器应妥善处理，不可做他用，也不可随意丢弃。4、使用本品时应穿戴防护服和手套，避免吸入药液施药期间不可吃东西和饮水，施药后应及时洗手和洗脸。5、避免孕妇及哺乳期妇女接触。6、为延缓抗性，与其他作用机理农药交替使用。",
	// "name": "红云、谱箘特",
	// "id": 754,
	// "type": "可湿性粉剂"

	private List<Pesticide> data;

	public List<Pesticide> getData() {
		return data;
	}

	public void setData(List<Pesticide> data) {
		this.data = data;
	}

	public class Pesticide {

		private String activeIngredient;
		private String disease;
		private String volumn;
		private String guideline;
		private String registrationId;
		private String manufacturer;
		private String tip;
		private String name;
		private String id;
		private String type;

		public String getActiveIngredient() {
			return activeIngredient;
		}

		public void setActiveIngredient(String activeIngredient) {
			this.activeIngredient = activeIngredient;
		}

		public String getDisease() {
			return disease;
		}

		public void setDisease(String disease) {
			this.disease = disease;
		}

		public String getVolumn() {
			return volumn;
		}

		public void setVolumn(String volumn) {
			this.volumn = volumn;
		}

		public String getGuideline() {
			return guideline;
		}

		public void setGuideline(String guideline) {
			this.guideline = guideline;
		}

		public String getRegistrationId() {
			return registrationId;
		}

		public void setRegistrationId(String registrationId) {
			this.registrationId = registrationId;
		}

		public String getManufacturer() {
			return manufacturer;
		}

		public void setManufacturer(String manufacturer) {
			this.manufacturer = manufacturer;
		}

		public String getTip() {
			return tip;
		}

		public void setTip(String tip) {
			this.tip = tip;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

	}

}
