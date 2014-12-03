package org.ird.immunizationreminder.web.dwr;

import org.directwebremoting.WebContextFactory;
import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.LoggedInUser;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.ird.immunizationreminder.web.validator.IRSettingValidator;

public class DWRIrSettingService {
	public String updateIrSetting(String settingName,String newValue) {
		LoggedInUser user=UserSessionUtils.getActiveUser(WebContextFactory.get().getHttpServletRequest());
		if(user==null){
			return "Your session has expired . Please login again.";
		}
	
		IRSettingValidator valid=new IRSettingValidator();
		if(valid.validateSettingValue(settingName, newValue)){
			boolean isupdated=Context.updateIrSetting(settingName, newValue,user);		
			if(!isupdated){
				return "setting was not updated due to some reason. check log for details.";
			}
			return "setting was updated successfully.";
		}else{
			return valid.ERROR_MESSAGE;
		}
	}
}
