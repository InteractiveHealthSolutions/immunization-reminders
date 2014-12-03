package org.ird.immunizationreminder.web.dwr;

import org.directwebremoting.WebContextFactory;
import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.LoggedInUser;
import org.ird.immunizationreminder.context.ServiceContext;
import org.ird.immunizationreminder.service.exception.UserServiceException;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;

public class DWRUserService {

	public String changePassword(String oldPwd,String newPwd,String confirmPwd) {
		LoggedInUser user=UserSessionUtils.getActiveUser(WebContextFactory.get().getHttpServletRequest());
		if(user==null){
			return "Your session has expired . Please login again.";
		}
		if(newPwd.length()<6){
			return "password must contain a minimum length of 6 characters";
		}
		ServiceContext sc = Context.getServices();
		try {
			sc.getUserService().changePassword(user.getUser().getName()
							, oldPwd, newPwd, confirmPwd);
			sc.commitTransaction();
		} catch (UserServiceException e) {
			return e.getMessage();
		} catch (Exception e) {
			return "An error occurred while processing request. Error message is :"+e.getMessage();
		}finally{
			sc.closeSession();
		}
		return "password changed successfully";
	}
}
