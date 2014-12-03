package org.ird.immunizationreminder.web.mobile;


public class RequestParam {

		public static final RequestParam APP_VER = new RequestParam("appver");
		
		public static final RequestParam REQ_TYPE = new RequestParam("rqtyp");
		public static final RequestParam REQ_TYPE_FETCH = new RequestParam("rqtftch");
		public static final RequestParam REQ_TYPE_SUBMIT = new RequestParam("rqtsbmt");
		public static final RequestParam REQ_USER = new RequestParam("rqtusr");

		public static final RequestParam FETCH_FORM_TYPE = new RequestParam("ftfrmtp");
		public static final RequestParam SUBMIT_FORM_TYPE = new RequestParam("sbfrmtp");

		public static final RequestParam LG_USERNAME = new RequestParam("urnm");
		public static final RequestParam LG_PASSWORD = new RequestParam("urpwd");
		public static final RequestParam LG_PHONETIME = new RequestParam("phndttm");

		public static final RequestParam UV_CHILDID = new RequestParam("chlid");
		public static final RequestParam UV_CURR_VACCINE_RECIEVED = new RequestParam("vccrcvdnam");
		public static final RequestParam UV_VACCINATION_STATUS = new RequestParam("vccsts");
		public static final RequestParam UV_CURR_VACCINATION_DATE = new RequestParam("curvccdt");
		public static final RequestParam UV_IS_LAST_VACCINATION = new RequestParam("islstvcc");
		public static final RequestParam UV_NEXT_VACCINE = new RequestParam("nxtvccnam");
		public static final RequestParam UV_NEXT_VACCINATION_DATE = new RequestParam("nxtvccdudt");
		public static final RequestParam UV_REASON_NOT_VACCINATED = new RequestParam("rsnntvcc");
		public static final RequestParam UV_ADDITIONAL_NOTE = new RequestParam("addnote");

		public static final RequestParam QUERY_ID_TYPE = new RequestParam("qidtyp");
		public static final RequestParam QUERY_VACC_PENDING = new RequestParam("qvacpnd");

		public static final RequestParam CHILD_ID = new RequestParam("chlid");
		public static final RequestParam MR_NUMBER = new RequestParam("chlmr");



		private final String paramName;
		
		private RequestParam(String paramName) {
			this.paramName = paramName;
		}
		public String getParamName(){
			return this.paramName;
		}
		public String toString() {
			return this.paramName;
		}
}
