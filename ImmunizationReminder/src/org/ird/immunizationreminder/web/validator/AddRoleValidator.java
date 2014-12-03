package org.ird.immunizationreminder.web.validator;

import org.ird.immunizationreminder.datamodel.entities.Role;
import org.ird.immunizationreminder.utils.validation.DataValidation;
import org.ird.immunizationreminder.utils.validation.REG_EX;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.mysql.jdbc.StringUtils;

public class AddRoleValidator implements Validator{
	
	public boolean supports(Class cls) {
		return Role.class.equals(cls);
	}

	public void validate(Object command, Errors error) {
		
		Role role=(Role)command;
		
		if( StringUtils.isEmptyOrWhitespaceOnly(role.getName()) ){
			error.rejectValue("name" , "error.role.empty-name" , "Role name must be provided");
		}
		else if(!DataValidation.validate(REG_EX.WORD, role.getName())){
			error.rejectValue("name" , "error.role.invalid-name" , "Role name is invalid");
		}
 	}
}
