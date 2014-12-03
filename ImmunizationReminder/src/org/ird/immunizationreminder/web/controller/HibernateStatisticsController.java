package org.ird.immunizationreminder.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.LoggedInUser;
import org.ird.immunizationreminder.utils.reporting.LoggerUtil;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

public class HibernateStatisticsController implements Controller{

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse arg1) throws Exception {
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			request.setAttribute("lmessage", "Your Session has expired . please login again.");
		}
		try{
			return new ModelAndView("hibernateStatswindow","stats",LoggerUtil.getHibernatestats(Context.getStatistics()).toString().replace("\n", "<br>"));	
		}catch (Exception e) {
			request.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView(new RedirectView("exception.htm"));		
		}
	}
}
