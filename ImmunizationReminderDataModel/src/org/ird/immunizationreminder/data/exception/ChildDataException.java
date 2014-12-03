package org.ird.immunizationreminder.data.exception;

public class ChildDataException extends Exception{
	
	public static final String CHILD_DOESNOT_EXIST="Given child does not exist";
	public static final String CHILD_ID_MISSING_OR_NULL="No child id is specified";
	public static final String CHILD_CURRENT_CELL_MISSING="No currnet cell number for child is specified";
	public static final String CHILD_CELL_NUMBER_INVALID_DIGIT_RANGE="Current cell number not matches exact digit range";
	public static final String INVALID_CELL_NUMBER="Current cell number contains invalid characters";
	public static final String CHILD_NAME_EMPTY="Child must have a first name";
	public static final String CHILD_EXISTS="Given Child already exists";
	public static final String CELL_NUM_OCCUPIED="Given cell number already occupied by another child";
	public static final String CHILD_NOT_FOUND="No child found";
	public static final String GIVEN_CHILD_ID_ASSIGNED_TO_MULTIPLE_CHILDREN="Data inconsistent. Given child id has been found on more than one child records.";
	public static final String GIVEN_CELL_NUMBER_ASSIGNED_TO_MULTIPLE_CHILDREN="Data inconsistent. Given child cell number has been found on more than one child records.";
	public static final String INVALID_CRITERIA_VALUE_SPECIFIED="Invalid value specified for search criteria";
	public static final String EPI_NUMBER_EXISTS="Epi registration number exists.";

	//public static final String 

	public String ERROR_CODE;

		private String errorMessage;
		public ChildDataException(String errorcode){
			this.errorMessage=errorcode.toString();
			this.ERROR_CODE=errorcode.toString();
		}
		public ChildDataException(String errorcode,String message){
			this.errorMessage=message;
			this.ERROR_CODE=errorcode.toString();
		}
		public String getMessage(){
			return errorMessage+(super.getMessage()==null?"":("\n"+super.getMessage()));
		}
		
		
		
	}
