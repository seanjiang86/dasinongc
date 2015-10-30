package com.dasinong.app.net;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.net.NetConfig.SubUrl;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.utils.FieldUtils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View.OnClickListener;

public class RequestService {

	private volatile static RequestService mInstance;

	private RequestService() {
	}

	public static RequestService getInstance() {
		if (mInstance == null) {
			synchronized (RequestService.class) {
				if (mInstance == null) {
					mInstance = new RequestService();
				}
			}
		}
		return mInstance;
	}

	public void register(Context context, String userName, String password, String cellPhone, String address,
			Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getRegisterParams(userName, password, cellPhone, address);
		new NetRequest(context).get(RequestCode.REGISTER_BY_PASSWORD, params, SubUrl.REGISTER_BY_PASSWORD, callBack, clazz);
	}
	
	public void authcodeLoginReg(Context context, String cellphone, String channel,Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getRegisterLoginParams(cellphone , channel);
		new NetRequest(context).get(RequestCode.LOGIN_REGISTER, params, SubUrl.LOGIN_REGISTER, callBack, clazz);
	}
	
	public void loginByPwd(Context context, String userName, String password , Class<? extends BaseEntity> clazz,
			RequestListener callBack) {
		Map<String, String> params = NetConfig.getLoginParams(userName, password);
		new NetRequest(context).get(RequestCode.LOGIN_BY_PASSWORD, params, SubUrl.LOGIN_BY_PASSWORD, callBack, clazz);
	}

	public void checkUser(Context context, String cellPhone, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getCheckUserParams(cellPhone);
		new NetRequest(context).get(RequestCode.CHECK_USER, params, SubUrl.CHECK_USER, callBack, clazz);
	}

	/**
	 * 在田里是发送的请求
	 */
	public void searchLocation(Context context, String latitude, String longitude, String mprovince, String mcity,
			String mdistrict, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getSearchLocationParams(latitude, longitude, mprovince, mcity, mdistrict);
		new NetRequest(context).get(RequestCode.SEARCH_LOCATION, params, SubUrl.SEARCH_LOCATION, callBack, clazz);
	}

	public void getVarietyList(Context context, String cropName, String locationId, Class<? extends BaseEntity> clazz,
			RequestListener callBack) {
		Map<String, String> params = NetConfig.getGetVarietyListParams(cropName, locationId);
		new NetRequest(context).get(RequestCode.GET_VARIETY_LIST, params, SubUrl.GET_VARIETY_LIST, callBack, clazz);
	}

	public void getLocation(Context context, String province, String city, String county, String district,
			Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getGetLocationParams(province, city, county, district);
		new NetRequest(context).get(RequestCode.GET_LOCATION, params, SubUrl.GET_LOCATION, callBack, clazz);
	}

	public void searchNearUser(Context context, String latitude, String longitude, Class<? extends BaseEntity> clazz,
			RequestListener callBack) {
		Map<String, String> params = NetConfig.getSearchNearUserParams(latitude, longitude);
		new NetRequest(context).get(RequestCode.SEARCH_NEAR_USER, params, SubUrl.SEARCH_NEAR_USER, callBack, clazz);
	}

	public void sendRequestWithToken(Context context, Class<? extends BaseEntity> clazz, int requestCode, String url,
			Object param, RequestListener callBack) {
		Map<String, String> map = FieldUtils.convertToHashMap(param);
		Map<String, String> params = NetConfig.getBaseParams(true, map);
		new NetRequest(context).get(requestCode, params, url, callBack, clazz);
	}

	public void sendRequestWithOutToken(Context context, Class<? extends BaseEntity> clazz, int requestCode, String url,
			Object param, RequestListener callBack) {
		Map<String, String> map = FieldUtils.convertToHashMap(param);
		Map<String, String> params = NetConfig.getBaseParams(false, map);
		new NetRequest(context).get(requestCode, params, url, callBack, clazz);
	}

	public void getAllTask(Context context, String fieldId, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getAllTaskParams(fieldId);
		new NetRequest(context).get(RequestCode.GET_ALL_TASK, params, SubUrl.GET_All_TASK, callBack, clazz);
	}

//	public void getMyInfo(Context context, Class<? extends BaseEntity> clazz, RequestListener callBack) {
//		Map<String, String> params = NetConfig.getDefaultParams();
//		new NetRequest(context).get(RequestCode.GET_MY_INFO, params, SubUrl.GET_MY_INFO, callBack, clazz);
//	}
	public void getMyInfo(Context context, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getDefaultParams();
		new NetRequest(context).requestPost(RequestCode.GET_MY_INFO, params, SubUrl.GET_MY_INFO, callBack, clazz);
	}

	public void uploadInfo(Context context, String userName, String cellphone, String password, String address, String telephone,
			Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getUploadInfoParams(userName, cellphone, password, address, telephone);
		new NetRequest(context).get(RequestCode.UPLOAD_MY_INFO, params, SubUrl.UPLOAD_MY_INFO, callBack, clazz);
	}

	public void updateTask(Context context, String fieldId, String taskIds, String taskStatuss,
			Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getUploadTaskParams(fieldId, taskIds, taskStatuss);
		new NetRequest(context).get(RequestCode.UPLOAD_MY_TASK, params, SubUrl.UPLOAD_MY_TASK, callBack, clazz);
	}

	public void setPhoneAuthState(Context context, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getDefaultParams();
		new NetRequest(context).get(RequestCode.UPLOAD_PHONE_AUTH_STATE, params, SubUrl.UPLOAD_PHONE_AUTH_STATE, callBack, clazz);
	}

	public void smsSubscribe(Context context, String id,String targetName, String cellphone, String province, String city, String country,
			String district, String area, String cropId, boolean isAgriWeather, boolean isNatAlter, boolean isRiceHelper,
			Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getSmsSubParams(id,targetName, cellphone, province, city, country, district, area,
				cropId, isAgriWeather, isNatAlter, isRiceHelper);
		if(TextUtils.isEmpty(id)){
			new NetRequest(context).get(RequestCode.SMS_SUBSCRIBE, params, SubUrl.SMS_SUBSCRIBE, callBack, clazz);
		}else{
			new NetRequest(context).get(RequestCode.MODIFI_SMS_SUBSCRIBE, params, SubUrl.MODIFI_SMS_SUBSCRIBE, callBack, clazz);
		}
	}

	public void getSubScribeLists(Context context, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getDefaultParams();
		new NetRequest(context).get(RequestCode.GET_SUBSCRIBE_LIST, params, SubUrl.GET_SUBSCRIBE_LIST, callBack, clazz);
	}
	
	//TODO  123
	
	public void resetPwd(Context context, String oPassword, String nPassword, Class<? extends BaseEntity> clazz,
			RequestListener callBack) {
		Map<String, String> params = NetConfig.getResetPwdParams(oPassword, nPassword);
		if(TextUtils.isEmpty(oPassword)){
			new NetRequest(context).get(RequestCode.RESET_PWSSWORD, params, SubUrl.RESET_PWSSWORD, callBack, clazz);
		}else{
			new NetRequest(context).get(RequestCode.UPDATE_PWSSWORD, params, SubUrl.UPDATE_PWSSWORD, callBack, clazz);
		}
	}

	public void deleteSmsSub(Context context, String id, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getDeleteSmsSubParams(id);
		new NetRequest(context).get(RequestCode.DELETE_SMS_SUBSCRIBE, params, SubUrl.DELETE_SMS_SUBSCRIBE, callBack, clazz);
	}
	public void loadSubScribeDetail(Context context, String id, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getDeleteSmsSubParams(id);
		new NetRequest(context).get(RequestCode.SMS_SUBSCRIBE_DETAIL, params, SubUrl.SMS_SUBSCRIBE_DETAIL, callBack, clazz);
	}

	public void uploadHeadImage(Context context, String filePath, RequestListener callBack) {
		new NetRequest(context).upload(0, NetConfig.BASE_URL + "uploadAvater", filePath, BaseEntity.class, callBack);
	}
	
	public void searchWord(Context context, String key,String type, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getSearchWordParams(key,type);
		new NetRequest(context).get(RequestCode.SEARCH_WORD, params, SubUrl.SEARCH_WORD, callBack, clazz);
	}
	public void requestSecurityCode(Context context, String cellphone, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getRequestSecurityCodeParams(cellphone);
		new NetRequest(context).get(RequestCode.REQUEST_SECURITY_CODE, params, SubUrl.REQUEST_SECURITY_CODE, callBack, clazz);
	}
	public void loginWithSecCode(Context context, String cellphone,String seccode, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getLoginWithSecCodeParams(cellphone, seccode);
		new NetRequest(context).get(RequestCode.LOGIN_WITH_SECCODE, params, SubUrl.LOGIN_WITH_SECCODE, callBack, clazz);
	}
	public void getSteps(Context context, String fieldId,String taskSpecId, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getStepsParams(fieldId, taskSpecId);
		new NetRequest(context).get(RequestCode.GET_STEPS, params, SubUrl.GET_STEPS, callBack, clazz);
	}
	public void isPassSet(Context context,String cellphone , Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getIsPassSetParams(cellphone);
		new NetRequest(context).get(RequestCode.IS_PWSS_SET, params, SubUrl.IS_PWSS_SET, callBack, clazz);
	}
	
	public void createField(Context context, String seedingortransplant,String area,String startDate,String locationId,String varietyId,String currentStageId,String yield,Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getCreateFieldParams("true",seedingortransplant,area,startDate,locationId,varietyId,currentStageId,yield);
		new NetRequest(context).get(RequestCode.CREATE_FIELD, params, SubUrl.CREATE_FIELD, callBack, clazz);
	}
	public void uploadPetDisPic(Context context, List<String> paths ,String cropName, String disasterType,String disasterName,String affectedArea,String eruptionTime, String disasterDist,String fieldOperations,String fieldId, RequestListener callBack) {
		new NetRequest(context).uploadImages(0,NetConfig.BASE_URL + "insertDisasterReport", paths, cropName, disasterType, disasterName, affectedArea, eruptionTime,  disasterDist, fieldOperations,fieldId,BaseEntity.class, callBack);
	}
	public void getPetDisSpecDetial(Context context, int petDisSpecId ,Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getGetPetDisSpecDetial(petDisSpecId);
		new NetRequest(context).get(RequestCode.GET_PET_DIS_SPEC_DETIAL, params, SubUrl.GET_PET_DIS_SPEC_DETIAL, callBack, clazz);
	}
	public void getPetSolu(Context context, int petSoluId ,Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getGetPetSolu(petSoluId);
		new NetRequest(context).get(RequestCode.GET_PET_SOLU, params, SubUrl.GET_PET_SOLU, callBack, clazz);
	}
	
	// TODO MING 查找此方法是否在使用了，如果没有可以删除
	public void browsePetDisByType(Context context, String type ,Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.browsePetDisByTypeParams(type);
		new NetRequest(context).get(RequestCode.PETDIS_BYTYPE, params, SubUrl.PETDIS_BYTYPE, callBack, clazz);
	}
	public void browseCPProductByModel(Context context, String type ,Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.browseCPProductByModelParams(type);
		new NetRequest(context).get(RequestCode.CPPRODUCT_BYMODEL, params, SubUrl.CPPRODUCT_BYMODEL, callBack, clazz);
	}
	public void getCPProdcutsByIngredient(Context context, String type ,Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getCPProdcutsByIngredientParams(type);
		new NetRequest(context).get(RequestCode.CPPRODUCT_BYMODEL_NAMED, params, SubUrl.CPPRODUCT_BYMODEL_NAMED, callBack, clazz);
	}
	public void getVarietysByName(Context context, String type ,Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getVarietysByNameParams(type);
		new NetRequest(context).get(RequestCode.CPPRODUCT_VARIETYS_NAMED, params, SubUrl.CPPRODUCT_VARIETYS_NAMED, callBack, clazz);
	}
	public void changeStage(Context context, String fieldId , String currentStageId ,Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getChangeStageParams(fieldId, currentStageId);
		new NetRequest(context).get(RequestCode.CHANGE_STAGE, params, SubUrl.CHANGE_STAGE, callBack, clazz);
	}
	public void qqAuthRegLog(Context context, String qqtoken , String avater , String username, String channel, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getQQAuthRegLogParams(qqtoken, avater , username, channel);
		new NetRequest(context).get(RequestCode.QQ_AUTH_REG_LOG, params, SubUrl.QQ_AUTH_REG_LOG, callBack, clazz);
	}
	public void getWXAccessToken(Context context, String appid, String secret,String code,Class<? extends BaseEntity> clazz, RequestListener callBack) {
		try {
			new NetRequest(context).getWXAccessToken(0, "https://api.weixin.qq.com/sns/oauth2/access_token?", appid, secret, code, clazz, callBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void getWXUserInfo(Context context, String access_token, String openid,Class<? extends BaseEntity> clazz, RequestListener callBack) {
		try {
			new NetRequest(context).getWXUserInfo(0, "https://api.weixin.qq.com/sns/userinfo?", access_token, openid, clazz, callBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void weixinAuthRegLog(Context context, String weixintoken , String avater , String username, String channel,Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getWXAuthRegLogParams(weixintoken, avater , username , channel);
		new NetRequest(context).get(RequestCode.WX_AUTH_REG_LOG, params, SubUrl.WX_AUTH_REG_LOG, callBack, clazz);
	} 
	public void getStages(Context context, String varietyId ,Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getGetStagesParams(varietyId);
		new NetRequest(context).get(RequestCode.GET_STAGES, params, SubUrl.GET_STAGES, callBack, clazz);
	}
	public void weatherIssue(Context context, String monitorLocationId , String issue,Class<? extends BaseEntity> clazz, RequestListener callBack){
		Map<String, String> params = NetConfig.getWeatherIssueParams(monitorLocationId , issue);
		new NetRequest(context).get(RequestCode.WEATHER_ISSUE, params, SubUrl.WEATHER_ISSUE, callBack, clazz);
	}
	public void setRef(Context context, String refcode ,Class<? extends BaseEntity> clazz, RequestListener callBack){
		Map<String, String> params = NetConfig.getSetRefParams(refcode);
		new NetRequest(context).get(RequestCode.SETREF, params, SubUrl.SET_REF, callBack, clazz);
	}
	public void refApp(Context context, String cellPhones ,Class<? extends BaseEntity> clazz, RequestListener callBack){
		Map<String, String> params = NetConfig.getRefAppParams(cellPhones);
		new NetRequest(context).get(RequestCode.REFAPP, params, SubUrl.REFAPP, callBack, clazz);
	}
	public void logout(Context context,Class<? extends BaseEntity> clazz, RequestListener callBack){
		Map<String, String> params = NetConfig.getDefaultParams();
		new NetRequest(context).get(RequestCode.LOGOUT, params, SubUrl.LOGOUT, callBack, clazz);
	}
	public void browsePetDisSpecsByCropIdAndType(Context context, String cropId , String type , Class<? extends BaseEntity> clazz, RequestListener callBack){
		Map<String, String> params = NetConfig.getBrowsePetDisSpecsByCropIdAndTypeParams(cropId,type);
		new NetRequest(context).get(RequestCode.BROWSE_PETDISSPECS_BY_CROPID_AND_TYPE, params, SubUrl.BROWSE_PETDISSPECS_BY_CROPID_AND_TYPE, callBack, clazz);
	}
}
