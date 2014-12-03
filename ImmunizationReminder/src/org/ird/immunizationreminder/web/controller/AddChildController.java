package org.ird.immunizationreminder.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.LoggedInUser;
import org.ird.immunizationreminder.context.ServiceContext;
import org.ird.immunizationreminder.datamodel.entities.Child;
import org.ird.immunizationreminder.web.utils.IMRGlobals;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class AddChildController extends SimpleFormController{
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			return new ModelAndView(new RedirectView("login.htm"));
		}
		Child child=(Child)command;

		ServiceContext sc = Context.getServices();
		try{
			child.setCreator(user.getUser());
			child.setArm(sc.getArmService().findByChildIdToMap(Integer.parseInt(child.getChildId())).getArm());
			request.getSession().setAttribute("semi-added-child", child);
			request.getSession().setAttribute("semiaddedchildArm", sc.getArmService()
					.getByName(child.getArm().getArmName(), FetchMode.JOIN, true));

			UserSessionUtils.removeCookie("selectedArmAddChild", request,response);
		} 
		catch(NullPointerException e){
			try {
				throw new Exception(
						"\nArm for child '"
								+ child.getChildId()
								+ "' was not set. Error is :"
								+ e.getMessage()
								+ ". Check if Arm is defined for given child id in study.");
			} catch (Exception e1) {
				request.getSession().setAttribute("exceptionTrace", e1);
				return new ModelAndView(new RedirectView("exception.htm"));
			}
		}
		finally{
			sc.closeSession();
		}
		return new ModelAndView(new RedirectView(getSuccessView()));
	}
	
	@Override
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT), true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor(false));

	}
}
