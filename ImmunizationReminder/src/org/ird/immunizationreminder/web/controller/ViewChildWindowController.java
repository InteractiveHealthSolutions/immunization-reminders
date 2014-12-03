package org.ird.immunizationreminder.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.LoggedInUser;
import org.ird.immunizationreminder.context.ServiceContext;
import org.ird.immunizationreminder.datamodel.entities.Child;
import org.ird.immunizationreminder.web.utils.IRUtils;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

public class ViewChildWindowController implements Controller{

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse arg1) throws Exception {
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			request.setAttribute("lmessage", "Your Session has expired . please login again.");
		}
		String pvid=request.getParameter("pId");
		ServiceContext sc = Context.getServices();
		try{
			Map<String, Object> model=new HashMap<String, Object>();
			Child child=sc.getChildService().getChildbyChildId(pvid,true);
			model.put("child", child);
			model.put("vaccs", IRUtils.addRecord(child));
			return new ModelAndView("viewChildwindow","model",model);	
		}catch (Exception e) {
			request.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView(new RedirectView("exception.htm"));		
		}
		finally{
			sc.closeSession();
		}
	}
}
