package org.ird.immunizationreminder.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.LoggedInUser;
import org.ird.immunizationreminder.context.ServiceContext;
import org.ird.immunizationreminder.datamodel.entities.Vaccine;
import org.ird.immunizationreminder.utils.reporting.ExceptionUtil;
import org.ird.immunizationreminder.utils.reporting.LoggerUtil;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class EditVaccineController extends SimpleFormController{
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		String message="Vaccine edited successfully.";
	
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			return new ModelAndView(new RedirectView("login.htm"));
		}
		
		Vaccine vacc=(Vaccine) command;
		ServiceContext sc = Context.getServices();
		try{
			vacc.setLastEditor(user.getUser());

			sc.getVaccinationService().updateVaccine(vacc);
			sc.commitTransaction();
			
			LoggerUtil.logIt("Vaccine '"+vacc.getName()+"' edited by '"+user.getUser().getName()+":"+user.getUser().getFullName()+"' ");
		}
		catch (Exception e) {
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}
		finally{
			sc.closeSession();
		}
		return new ModelAndView(new RedirectView("viewVaccines.htm?editOrUpdateMessage="+message+"&action=search&vaccineName="+vacc.getName()));
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		Vaccine vacc = new Vaccine();

		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			boolean session_expired = true;
			request.setAttribute("session_expired", session_expired);
		}else{
			String rec=request.getParameter("editRecord");
			ServiceContext sc = Context.getServices();
			try{
			vacc=sc.getVaccinationService().getByName(rec);
			}catch (Exception e) {
				LoggerUtil.logIt(ExceptionUtil.getStackTrace(e));
				request.setAttribute("errorMessage", "An error occurred while retrieving Vaccine. Error message is:"+e.getMessage());
			}finally{
				sc.closeSession();
			}
		}
		return vacc;
	}
}
