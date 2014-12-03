package org.ird.immuremsys.http;

public class IMRSRequestPayload {

	private String payload;
	
	public IMRSRequestPayload() {
		this.payload = "";
	}

	public void addParam(RequestParam paramName, String valueOfParam){
		if(payload.startsWith("&")){
			payload = payload.substring(1);
		}
		payload += "&" + paramName.getParamName()+"="+valueOfParam;
	}
	
	public String getRequestPayload(){
		return this.payload;
	}
	
	public String toString() {
		return this.payload;
	}
}
