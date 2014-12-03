package org.ird.immunizationreminder.service.validations;

import org.ird.immunizationreminder.datamodel.entities.User;
import org.ird.immunizationreminder.service.exception.UserServiceException;
import org.ird.immunizationreminder.utils.validation.DataValidation;
import org.ird.immunizationreminder.utils.validation.REG_EX;


public class UserValidations {

	public static void validateUser(User user) throws UserServiceException{
		
			if(user.getName()==null || user.getName().compareTo("")==0){
				throw new UserServiceException(UserServiceException.USERNAME_EMPTY);
			}
			if(user.getFirstName()==null || user.getFirstName().compareTo("")==0){
				throw new UserServiceException(UserServiceException.USER_FIRST_NAME_MISSING);
			}
			if(user.getPassword()==null || user.getPassword().compareTo("")==0){
				throw new UserServiceException(UserServiceException.USER_PASSWORD_MISSING);
			}
	}
	public static boolean validatePassword(String pwd){
		if(pwd==null || pwd.trim().compareTo("")==0 || pwd.length()<6){
			return false;
		}
		return DataValidation.validate(REG_EX.PASSWORD,pwd);
	}
	
}
