package org.ird.immuremsys.http;


public class ResponseParam {
	public static final String XML_RESPONSE_ROOT_TAG = "imrs";
	
	public static final String RESPONSE_STATUS = "status";
	public static final String RESPONSE_STATUS_ERROR = "error";
	public static final String RESPONSE_STATUS_SUCCESS = "success";
	public static final String RESPONSE_MESSAGE = "msg";

	public static final ResponseParam LG_USERNAME = new ResponseParam("urnm");
	public static final ResponseParam LG_PASSWORD = new ResponseParam("urpwd");
	public static final ResponseParam LG_PHONETIME = new ResponseParam("dttim");

	public static final ResponseParam UV_CHILD_ID = new ResponseParam("chlid");
	public static final ResponseParam UV_CHILD_NAME = new ResponseParam("chlflnam");

	public static final ResponseParam UV_PREV_VACC_DETAILS = new ResponseParam("prvvccdt");
	public static final ResponseParam UV_CUR_VACC_DUEDATE = new ResponseParam("curvccduedt");
	public static final ResponseParam UV_CUR_VACC_NAME = new ResponseParam("curvccnam");
	public static final ResponseParam UV_VACC_RECIEVED_NAMES = new ResponseParam("vccrcvdnam");
	public static final ResponseParam UV_BIRTHDATE = new ResponseParam("chlbrthdt");
	public static final ResponseParam UV_PREV_VACC_DATE = new ResponseParam("prvvccdate");

	private final String paramName;
	
	private ResponseParam(String paramName) {
		this.paramName = paramName;
	}
	public String getParamName(){
		return this.paramName;
	}
	public String toString() {
		return this.paramName;
	}
}
