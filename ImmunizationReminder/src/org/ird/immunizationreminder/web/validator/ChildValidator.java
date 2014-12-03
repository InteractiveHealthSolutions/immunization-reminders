package org.ird.immunizationreminder.web.validator;

import org.ird.immunizationreminder.datamodel.entities.Child;
import org.ird.immunizationreminder.utils.date.DateUtils;
import org.ird.immunizationreminder.utils.validation.DataValidation;
import org.ird.immunizationreminder.utils.validation.REG_EX;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.mysql.jdbc.StringUtils;

public class ChildValidator implements Validator{
	
	public boolean supports(Class cls) {
		return Child.class.equals(cls);
	}
	
	public void validate(Object command, Errors error) {
		EntityValidation validation=new EntityValidation();

		Child p=(Child)command;
		
		if(!validation.validateChildId(p)){
			error.rejectValue("childId" , "" , validation.ERROR_MESSAGE);
		}

		if(!validation.validateChildContactNumber(p)){
			error.rejectValue("currentCellNo" , "" , validation.ERROR_MESSAGE);
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(p.getAlternateCellNo())){
			
		}
		else if(!validation.validateChildContactNumber(p.getAlternateCellNo(), false)){
			error.rejectValue("alternateCellNo" , "error.child.invalid-cell-num" 
					, "invalid cell number");
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(p.getFirstName())){
			error.rejectValue("firstName" , "error.child.first-name-empty" 
					, "child first name must be specified");
		}
		else{
			if(!DataValidation.validate(REG_EX.NAME_CHARACTERS, p.getFirstName())){
				error.rejectValue("firstName" , "error.child.first-name-invalid" 
						, "child first name is invalid");
			}
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(p.getMiddleName())){
		}
		else{
			if(!DataValidation.validate(REG_EX.NAME_CHARACTERS, p.getMiddleName())){
				error.rejectValue("middleName" , "error.child.middle-name-invalid" ,
						"child middle name is invalid");
			}
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(p.getLastName())){
		}
		else{
			if(!DataValidation.validate(REG_EX.NAME_CHARACTERS, p.getLastName())){
				error.rejectValue("lastName" , "error.child.last-name-invalid" 
						, "child last name is invalid");
			}
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(p.getFatherName())){
		}
		else{
			if(!DataValidation.validate(REG_EX.NAME_CHARACTERS, p.getFatherName())){
				error.rejectValue("fatherName" , "error.child.father-name-invalid" 
						, "child father name is invalid");
			}
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly( p.getHouseNum())){
		}
		else{
			if(!DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, p.getHouseNum())){
				error.rejectValue("houseNum" , "error.child.house-number-invalid" 
						, "House number is invalid");
			}
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly( p.getStreetNum())){
		}
		else{
			if(!DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, p.getStreetNum())){
				error.rejectValue("streetNum" , "error.child.street-number-invalid" 
						, "Street number is invalid");
			}
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(p.getSector())){
		}
		else{
			if(!DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, p.getSector())){
				error.rejectValue("sector" , "error.child.sector-invalid" , "Sector is invalid");
			}
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(p.getColony())){
		}
		else{
			if(!DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, p.getColony())){
				error.rejectValue("colony" , "error.child.colony-invalid" , "Colony is invalid");
			}
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(p.getTown())){
		}
		else{
			if(!DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, p.getTown())){
				error.rejectValue("town" , "error.child.town-invalid" , "Town is invalid");
			}
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(p.getLandmark())){
		}
		else{
			if(!DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, p.getLandmark())){
				error.rejectValue("landmark" , "error.child.landmark-invalid" , "Landmark is invalid");
			}
		}
		
		if(!validation.validateMrNumber(p)){
				error.rejectValue("mrNumber" , "" , validation.ERROR_MESSAGE);
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(p.getClinic())
				||!DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, p.getClinic())){
				error.rejectValue("clinic" , "error.child.clinic-invalid" , "Center name is invalid");
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(p.getPhoneNo())){
		}
		else{
			if(!DataValidation.validate(REG_EX.PHONE_NUMBER, p.getPhoneNo())){
				error.rejectValue("phoneNo" , "error.child.phone-num-invalid" , "Phone number is invalid");
			}
		}
		
		if(p.getBirthdate()==null){
				error.rejectValue("birthdate" , "error.child-birthdate.empty" 
						, "either birthdate or age must be provided");
		}
		else if(DateUtils.afterTodaysDate(p.getBirthdate())){
				error.rejectValue("birthdate" , "error.child.invalid-field" , "invalid date");
		}
		
		if(p.getDateEnrolled()==null){
			error.rejectValue("dateEnrolled" , "error.child.invalid-field" , "invalid date");
		}
		else if(DateUtils.afterTodaysDate(p.getDateEnrolled())){
				error.rejectValue("dateEnrolled" , "error.child.invalid-field" , "invalid date");
		}
		else
			if(p.getDateOfCompletion()!=null 
					&& p.getDateEnrolled().compareTo(p.getDateOfCompletion())>0){
			error.rejectValue("dateEnrolled" , "" 
					, "date enrolled cannot be after than date of completion");
		}
	}
}
