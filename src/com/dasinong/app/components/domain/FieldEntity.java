package com.dasinong.app.components.domain;

import com.dasinong.app.ui.soil.domain.DataEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liuningning on 15/6/14.
 */
public class FieldEntity extends BaseResponse {

	public DataEntity latestReport;
	// field list
	// name:fieldID
	public Map<String, Long> fieldList;

	public CurrentFieldEntity currentField;

	public HomeDate date;
	public String soilHum;

	public static class HomeDate {
		public String lunar;// 大雪
		public int day;// 周41（要是）
		public String date;// 10
	}

	/**
	 * cropName + stageName + SubStagName; 水稻分叶期8叶
	 */
	public static class CurrentFieldEntity {

		public String startDate;
		// 当前的statgeID
		public int currentStageID;
		// 病害
		public List<PetdiswsEntity> petdisws;
		// 作物名
		public String cropName;
		// not show
		public int yield;
		// 任务清单
		public List<TaskwsEntity> taskws;
		// not show userID
		public int userId;
		// not show location_id
		public int locationId;
		// not show
		public boolean active;
		// 结束时间
		public String endDate;
		// 品种(id select substage)
		public int varietyId;
		// 自然（如果不是空，则是Banner的信息。如果是空，则加载Banner)
		public List<NatdiswsEntity> natdisws;

		// not show
		public int fieldId;
		// 当前田的名称
		public String fieldName;

		public int dayToHarvest; // int只给 离收获boo还有多少天（）(小于0不显示)

		// false
		public List<CurrentFieldEntity.Petdisspecws> petdisspecws;

		public List<SubStageEntity> stagelist;

		public static class PetdiswsEntity {
			/**
			 * "rule":
			 * "[发生规律](1)世代。江苏每年发生9～11代，安徽11代，浙江10～12代，福建中部约15代，广东中、南部15代以上。稻蓟马生活周期短，发生代数多，世代重叠，田间发生世代较难划分。(2)越冬。成虫在茭白、麦类、李氏禾、看麦娘等禾本科植物上越冬。翌年3～4月份，成虫先在杂草上活动繁殖，然后迁移到水稻秧田繁殖为害。(3)时期。主要在水稻生长前期为害。迁移代成虫于5月中旬前后在早稻本田，早播中稻秧田产卵繁殖为害，二代成虫于6月上中旬迁入迟栽早稻本田或单季中稻秧、本田和晚稻秧田产卵为害。江淮地区一般于4月中旬起虫口数量呈直线上升，5、6月份达最高虫口密度。(4)气候。7月中旬以后因受高温(平均气温28℃以上)影响和稻叶不适蓟马取食为害，虫口受到抑制，数量迅速下降。冬季气候温暖，有利于稻蓟马的越冬和提早繁殖。在6月初到7月上旬，凡阴雨日多、气温维持在22～23℃的天数长，稻蓟马就会大发生。(5)栽培。早稻穗期受害重于晚稻穗期，以盛花期侵入的虫数较多，次为初花期或谢花期，灌浆期最少。双晚秧田，尤其是双晚直播田因叶嫩多汁，易受蓟马集中为害。秧苗三叶期以后，本田自返青至分蘖期是稻蓟马的严重为害期。如稻后种植绿肥和油菜，将为稻蓟马提供充足的食源和越冬场所，小麦面积较大的地方，稻蓟马的为害就有加重的可能。"
			 * , "sympton":
			 * "[为害症状]成虫1、2代和若虫以口器磨破稻叶表皮，吸食汁液，被害叶上出现黄白色小斑点或微孔，叶尖枯黄卷缩，严重时可使成片秧苗发黄发红，状如火烧。稻苗严重受害时，影响稻株返青和分蘖生长受阻，稻苗坐兜。花器受害，影响受粉结实，有的造成空壳。"
			 * , "form":
			 * "[形态]（1）成虫。体长1～1.3mm，雌虫略大于雄虫。初羽化时体色为褐色，1～2天后，为深褐色至黑色。头近正方形，触角鞭状7节，第6节至第7节与体同色，其余各节均黄褐色。复眼黑色，两复眼间有3个单眼，呈三角形排列。单眼前鬃长于单眼间鬃，单眼间鬃位于单眼三角形连线外缘。复眼后鬃4根。前胸背板发达，明显长于头部，或约于头长相等，后缘有鬃4根。前翅较后翅大，缨毛细长，有2条纵脉，上脉基鬃4+3根，端鬃3根，下脉鬃11～13根。雄成虫腹部3～7节腹板具腺域，雌成虫第8、9腹节有锯齿状产卵器。(2)卵。肾形，长约0.2mm，宽约0.1mm，初产白色透明，后变淡黄色，半透明，孵化前可透见红色眼点。(3)幼虫。共4龄。初孵时体长0.3～0.5mm，白色透明。触角直伸头前方，触角念珠状，第4节特别膨大。复眼红色，无单眼及翅芽。2龄若虫体长0.6～1.2mm，淡黄绿色，复眼褐色。3龄若虫又称前蛹，体长0.8～1.2mm，淡黄色，触角分向两边，单眼模糊，翅芽始现，复部显著膨大。4龄又称蛹，体长0.8～1.3mm，淡褐色，触角向后翻，在头部与前胸背面可见单眼3个，翅芽伸长达腹部5～7节。"
			 * , "habbit":
			 * "[习性]（1）成虫。成虫白天多隐藏在纵卷的叶尖或心叶内，有的潜伏于叶鞘内，早晨、黄昏或阴天多在叶上活动，爬行迅速，受震动后常展翅飞去，有一定迁飞能力，能随气流扩散。雄成虫寿命短，只有几天；雌成虫寿命长，为害季节中多在20天以上。雌虫羽化后经过1～3天开始产卵，产卵期10～20天，一般在羽化后3～6天产卵最多，在适宜的温湿环境下，1头雌虫一生可产卵100粒左右；雌成虫可以进行孤雌生殖，孤雌生殖的产卵量与两性生殖相似。雌成虫有明显趋嫩绿秧苗产卵的习性，在秧田中，一般在2、3叶期以上的秧苗上产卵。雌虫产卵时把产卵器插入稻叶表皮下，散产于叶片表皮下的脉间组织内，对光可看到针孔大小边缘光滑的半透明卵粒。(2)幼虫。多在晚上7～9时孵出，3～5分钟离开壳体，活泼地在叶片上爬行，数分钟后即能取食，1、2龄若虫是取食为害的主要阶段，多聚集中叶耳、叶舌处，特别是在卷针状的心叶内隐匿取食；3龄若虫行动呆滞，取食变缓，此时多集中在叶尖部分，使秧叶自尖起纵卷变黄。因此，大量叶尖纵卷变黄，预兆着3、4龄若虫激增，成虫将盛发。"
			 * , "pestName": "稻蓟马", "alias": "稻直鬃蓟马；俗称灰虫", "id": 3439
			 */

			public boolean petDisStatus;
			public String petDisSpecName;
			public int id;
			public int fieldId;
			public int petDisId;
			public String description;
			public String type;

		}

		// 这个不是

		public static class Petdisspecws {
			/**
			 * "rule":
			 * "[发生规律](1)世代。江苏每年发生9～11代，安徽11代，浙江10～12代，福建中部约15代，广东中、南部15代以上。稻蓟马生活周期短，发生代数多，世代重叠，田间发生世代较难划分。(2)越冬。成虫在茭白、麦类、李氏禾、看麦娘等禾本科植物上越冬。翌年3～4月份，成虫先在杂草上活动繁殖，然后迁移到水稻秧田繁殖为害。(3)时期。主要在水稻生长前期为害。迁移代成虫于5月中旬前后在早稻本田，早播中稻秧田产卵繁殖为害，二代成虫于6月上中旬迁入迟栽早稻本田或单季中稻秧、本田和晚稻秧田产卵为害。江淮地区一般于4月中旬起虫口数量呈直线上升，5、6月份达最高虫口密度。(4)气候。7月中旬以后因受高温(平均气温28℃以上)影响和稻叶不适蓟马取食为害，虫口受到抑制，数量迅速下降。冬季气候温暖，有利于稻蓟马的越冬和提早繁殖。在6月初到7月上旬，凡阴雨日多、气温维持在22～23℃的天数长，稻蓟马就会大发生。(5)栽培。早稻穗期受害重于晚稻穗期，以盛花期侵入的虫数较多，次为初花期或谢花期，灌浆期最少。双晚秧田，尤其是双晚直播田因叶嫩多汁，易受蓟马集中为害。秧苗三叶期以后，本田自返青至分蘖期是稻蓟马的严重为害期。如稻后种植绿肥和油菜，将为稻蓟马提供充足的食源和越冬场所，小麦面积较大的地方，稻蓟马的为害就有加重的可能。"
			 * , "sympton":
			 * "[为害症状]成虫1、2代和若虫以口器磨破稻叶表皮，吸食汁液，被害叶上出现黄白色小斑点或微孔，叶尖枯黄卷缩，严重时可使成片秧苗发黄发红，状如火烧。稻苗严重受害时，影响稻株返青和分蘖生长受阻，稻苗坐兜。花器受害，影响受粉结实，有的造成空壳。"
			 * , "form":
			 * "[形态]（1）成虫。体长1～1.3mm，雌虫略大于雄虫。初羽化时体色为褐色，1～2天后，为深褐色至黑色。头近正方形，触角鞭状7节，第6节至第7节与体同色，其余各节均黄褐色。复眼黑色，两复眼间有3个单眼，呈三角形排列。单眼前鬃长于单眼间鬃，单眼间鬃位于单眼三角形连线外缘。复眼后鬃4根。前胸背板发达，明显长于头部，或约于头长相等，后缘有鬃4根。前翅较后翅大，缨毛细长，有2条纵脉，上脉基鬃4+3根，端鬃3根，下脉鬃11～13根。雄成虫腹部3～7节腹板具腺域，雌成虫第8、9腹节有锯齿状产卵器。(2)卵。肾形，长约0.2mm，宽约0.1mm，初产白色透明，后变淡黄色，半透明，孵化前可透见红色眼点。(3)幼虫。共4龄。初孵时体长0.3～0.5mm，白色透明。触角直伸头前方，触角念珠状，第4节特别膨大。复眼红色，无单眼及翅芽。2龄若虫体长0.6～1.2mm，淡黄绿色，复眼褐色。3龄若虫又称前蛹，体长0.8～1.2mm，淡黄色，触角分向两边，单眼模糊，翅芽始现，复部显著膨大。4龄又称蛹，体长0.8～1.3mm，淡褐色，触角向后翻，在头部与前胸背面可见单眼3个，翅芽伸长达腹部5～7节。"
			 * , "habbit":
			 * "[习性]（1）成虫。成虫白天多隐藏在纵卷的叶尖或心叶内，有的潜伏于叶鞘内，早晨、黄昏或阴天多在叶上活动，爬行迅速，受震动后常展翅飞去，有一定迁飞能力，能随气流扩散。雄成虫寿命短，只有几天；雌成虫寿命长，为害季节中多在20天以上。雌虫羽化后经过1～3天开始产卵，产卵期10～20天，一般在羽化后3～6天产卵最多，在适宜的温湿环境下，1头雌虫一生可产卵100粒左右；雌成虫可以进行孤雌生殖，孤雌生殖的产卵量与两性生殖相似。雌成虫有明显趋嫩绿秧苗产卵的习性，在秧田中，一般在2、3叶期以上的秧苗上产卵。雌虫产卵时把产卵器插入稻叶表皮下，散产于叶片表皮下的脉间组织内，对光可看到针孔大小边缘光滑的半透明卵粒。(2)幼虫。多在晚上7～9时孵出，3～5分钟离开壳体，活泼地在叶片上爬行，数分钟后即能取食，1、2龄若虫是取食为害的主要阶段，多聚集中叶耳、叶舌处，特别是在卷针状的心叶内隐匿取食；3龄若虫行动呆滞，取食变缓，此时多集中在叶尖部分，使秧叶自尖起纵卷变黄。因此，大量叶尖纵卷变黄，预兆着3、4龄若虫激增，成虫将盛发。"
			 * , "pestName": "稻蓟马", "alias": "稻直鬃蓟马；俗称灰虫", "id": 3439
			 */

			public boolean petDisStatus;
			public String petDisSpecName;
			public int id;
			public int fieldId;
			public int petDisId;
			public String sympton;
			public String type;

		}

		public class TaskwsEntity {

			public boolean taskStatus;// 状态
			public int fieldId;
			public int taskSpecId;
			public int subStageId;
			public int taskId;//
			public String taskSpecName;// desc
			public String stageName;
			public String subStageName;

		}

		public class NatdiswsEntity {
			/**
			 * natDisId : 10 natDisSpecId : 10 natDisStatus : false fieldId : 10
			 * natDisSpecName : 台风
			 */
			public int natDisId;
			public int natDisSpecId;
			public boolean natDisStatus;
			public int fieldId;
			public String natDisSpecName;
			public String description;
			public boolean alerttype;

		}

		public static class SubStageEntity implements Comparable<SubStageEntity> {
			public String stageName;
			public int subStageId;
			public String subStageName;

			@Override
			public int compareTo(SubStageEntity another) {
				if (subStageId < another.subStageId)
					return -1;
				if (subStageId > another.subStageId)
					return 1;
				return 0;
			}
		}
	}

	public static final String TASK_TYPE_ALL = "all";
	public static final String TASK_TYPE_NONE = "none";

	// all | currentStage | none
	public static class Param {
		public String fieldId;
		public String task;
		public String lat;
		public String lon;
	}
}
