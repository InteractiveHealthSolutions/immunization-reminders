package org.ird.immunizationreminder.data.exception;

public class VaccinationDataException extends Exception{
	
	public static final String INVALID_CRITERIA_VALUE_SPECIFIED="Invalid value specified for search criteria";
	public static final String VACCINE_ALREADY_EXISTS="A vaccine already exists with given name in database";
	public static final String VACCINE_FORM_NUMBER_ALREADY_EXISTS="A vaccine already exists with given form number in database";
	public static final String VACCINE_FORM_NAME_ALREADY_EXISTS="A vaccine already exists with given form name in database";

	//public static final String 

	public String ERROR_CODE;

		private String errorMessage;
		public VaccinationDataException(String errorcode){
			this.errorMessage=errorcode.toString();
			this.ERROR_CODE=errorcode.toString();
		}
		public VaccinationDataException(String errorcode,String message){
			this.errorMessage=message;
			this.ERROR_CODE=errorcode.toString();
		}
		public String getMessage(){
			return errorMessage+(super.getMessage()==null?"":("\n"+super.getMessage()));
		}
		
		
		
	}
