package org.ird.immunizationreminder.web.validator;

import org.ird.immunizationreminder.datamodel.entities.User;
import org.ird.immunizationreminder.utils.validation.DataValidation;
import org.ird.immunizationreminder.utils.validation.REG_EX;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.mysql.jdbc.StringUtils;

public class AddUserValidator implements Validator{

	
	public boolean supports(Class cls) {
		return User.class.equals(cls);
	}

	public void validate(Object command, Errors error) {

		User user=(User)command;
		
		if(StringUtils.isEmptyOrWhitespaceOnly(user.getName())
				||user.getName().contains(" ")){
			error.rejectValue("name", "error.user.user-name-missing"
					, "username cannot be empty or contain spaces");
		}
		else{
			if(user.getName().equalsIgnoreCase("admin")
					||user.getName().equalsIgnoreCase("administrator")){
				error.rejectValue("name", "error.user.user-name-not-allowed-admin"
						, "username must not be admin or administrator");
			}
			if(user.getName().length()<4){
				error.rejectValue("name" , "error.user.user-name-invalid-length"
						, "username must be minimum 4 characters");
			}
			if(!DataValidation.validate(REG_EX.WORD, user.getName())){
				error.rejectValue("name" , "error.user.user-name-invalid"
						, "username can only contain _,digits and alphas");
			}
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(user.getFirstName())){
			error.rejectValue("firstName" , "error.user.first-name-empty" 
					, "user first name must be specified");
		}
		else{
			if(!DataValidation.validate(REG_EX.NAME_CHARACTERS, user.getFirstName())){
				error.rejectValue("firstName" , "error.user.first-name-invalid" 
						, "user first name is invalid");
			}
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(user.getMiddleName())){
		}
		else{
			if(!DataValidation.validate(REG_EX.NAME_CHARACTERS, user.getMiddleName())){
				error.rejectValue("middleName" , "error.user.middle-name-invalid" 
						, "user middle name is invalid");
			}
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(user.getLastName())){
		}
		else{
			if(!DataValidation.validate(REG_EX.NAME_CHARACTERS, user.getLastName())){
				error.rejectValue("lastName" , "error.user.last-name-invalid" 
						, "user last name is invalid");
			}
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(user.getEmail())){
			error.rejectValue("email" , "error.user.email-empty" , "email required");
		}
		else{
			if(!DataValidation.validate(REG_EX.EMAIL, user.getEmail())){
				error.rejectValue("email" , "error.user.email-invalid" , "invalid email");
			}
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(user.getPhoneNo())){
		}
		else{
			if(!DataValidation.validate(REG_EX.PHONE_NUMBER, user.getPhoneNo())){
				error.rejectValue("phoneNo" , "error.user.phone-invalid" 
						, "phone number contains invalid characters [only allowed; ',' 'space' '-']");
			}
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(user.getClearTextPassword())
				||user.getClearTextPassword().length()<6){
			error.rejectValue("clearTextPassword" , "error.user.user-password.invalid-length" 
					, "invalid password");
		}
		else{
			if(!DataValidation.validate(REG_EX.PASSWORD, user.getClearTextPassword())){
				error.rejectValue("clearTextPassword" , "error.user.user-password.invalid" 
						, "invalid password. can only contain[a-zA-Z0-9~@#%^&*()-_=+[]{}|;,.<>/?");
			}
		}
	}
}
