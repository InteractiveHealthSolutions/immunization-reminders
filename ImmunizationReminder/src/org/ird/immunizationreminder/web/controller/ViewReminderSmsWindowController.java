package org.ird.immunizationreminder.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.LoggedInUser;
import org.ird.immunizationreminder.context.ServiceContext;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

public class ViewReminderSmsWindowController implements Controller{

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse arg1) throws Exception {
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			request.setAttribute("lmessage", "Your Session has expired . please login again.");
		}
		String rsid=request.getParameter("rsId");
		ServiceContext sc = Context.getServices();
		try{
			Map<String, Object> model=new HashMap<String, Object>();
			ReminderSms rsms=sc.getReminderService().getReminderSmsRecord(Long.parseLong(rsid),true,FetchMode.JOIN);
			model.put("remsms", rsms);
			return new ModelAndView("viewReminderSmswindow","model",model);	
		}catch (Exception e) {
			request.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView(new RedirectView("exception.htm"));		
		}
		finally{
			sc.closeSession();
		}
	}
}
