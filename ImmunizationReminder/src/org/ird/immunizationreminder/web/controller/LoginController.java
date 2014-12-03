package org.ird.immunizationreminder.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.service.exception.UserServiceException;
import org.ird.immunizationreminder.utils.reporting.LoggerUtil;
import org.ird.immunizationreminder.web.Credentials;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class LoginController extends SimpleFormController {
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception {
		Credentials cr=(Credentials)command;
			try{
				if(Context.isUserLoggedIn(cr.getUsername())){
					if(!UserSessionUtils.isUserSessionActive(request)){
						request.getSession().setAttribute("logmessage", UserServiceException.USER_ALREADY_LOGGED_IN);
						return new ModelAndView(new RedirectView("login.htm"));
					}
				}else{
					Context.authenticateUser(cr.getUsername(), cr.getPassword());
					LoggerUtil.logIt("User Logged in '"+cr.getUsername()+":"+Context.getUser(cr.getUsername()).getUser().getFullName()+"' ");
					LoggerUtil.logIt(LoggerUtil.getJVMInfo());
				}
			} catch (UserServiceException e) {
					request.getSession().setAttribute("logmessage", e.getMessage());
					return new ModelAndView(new RedirectView("login.htm"));
			}catch (Exception e) {
				request.getSession().setAttribute("exceptionTrace",e);
				return new ModelAndView(new RedirectView("exception.htm"));
			}
			
		request.getSession().setAttribute("username", cr.getUsername());
		Cookie cok=new Cookie("username", cr.getUsername());
		cok.setMaxAge(60*60*8);
		response.addCookie(cok);
		request.getSession().setAttribute("fullname", Context.getcurrentlyLoggedInUsers().get(cr.getUsername()).getUser().getFullName());

		return new ModelAndView(new RedirectView("mainpage.htm"));
	}
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map< String, Object> model=new HashMap<String, Object>();
		model.put("logmessage", request.getSession().getAttribute("logmessage"));
		if(request.getParameter("logmessage")!=null){
			model.put("logmessage", request.getParameter("logmessage"));
		}
		try{
			request.getSession().removeAttribute("logmessage");
		}catch (Exception e) {	}
		
		return model;
	}
}
