package org.ird.immunizationreminder.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.immunizationreminder.context.LoggedInUser;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class ChangepwdController implements Controller{

	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {

		LoggedInUser user=UserSessionUtils.getActiveUser(req);
		if(user==null){
			req.setAttribute("message", "Your session has expired . Please login again.");
		}
//		ServiceContext service=user.getServiceContext();
//		
//		String action=req.getParameter("action");
//		if(action!=null&&action.compareTo("change")==0){
//			String opwd=req.getParameter("opwd");
//			String npwd=req.getParameter("npwd");
//			String cpwd=req.getParameter("cpwd");
//			try{
//				service.openSession();
//				service.getUserService().changePassword(user.getUser().getName(), opwd, npwd, cpwd);
//			service.commitTransaction();
//			req.setAttribute("message", "Password changed successfully.");
//			}catch (UserDataException e) {
//				req.setAttribute("message", e.getMessage());
//			}catch (Exception e) {
//				req.getSession().setAttribute("exceptionTrace", e);
//				return new ModelAndView(new RedirectView("exception.htm"));
//			}finally{
//					service.closeSession();
//			}
//		}
		return new ModelAndView("changepwd");
	}
}
