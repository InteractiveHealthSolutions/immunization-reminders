package org.ird.immunizationreminder.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.LoggedInUser;
import org.ird.immunizationreminder.context.ServiceContext;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

public class GenerateCSVController implements Controller{

	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		LoggedInUser user=UserSessionUtils.getActiveUser(req);
		if(user==null){
			return new ModelAndView(new RedirectView("login.htm"));
		}
		ServiceContext sc = Context.getServices();
		try{
			req.setAttribute("arm", sc.getArmService().getAll(true));
		}catch (Exception e) {
			req.setAttribute("message",e.getMessage());
		}
		finally{
			sc.closeSession();
		}
		return new ModelAndView("generateCSV");
	}
}