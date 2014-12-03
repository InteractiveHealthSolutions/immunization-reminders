package org.ird.immunizationreminder.web.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.LoggedInUser;
import org.ird.immunizationreminder.context.ServiceContext;
import org.ird.immunizationreminder.datamodel.entities.Reminder;
import org.ird.immunizationreminder.utils.reporting.LoggerUtil;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class EditReminderController extends SimpleFormController{

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		String message = "Reminder data edited successfully.";

		LoggedInUser user = UserSessionUtils.getActiveUser( request );
		if (user == null) {
			return new ModelAndView( new RedirectView( "login.htm" ) );
		}

		String[] rt = request.getParameterValues( "remText" );
		Set<String> remt = new HashSet<String>();
		for ( String s : rt ) {
			remt.add( s );
		}
		
		Reminder rem = (Reminder) command;
		ServiceContext sc = Context.getServices();
		try {
			rem.setReminderText( remt );
			rem.setLastEditor( user.getUser() );
			
			sc.getReminderService().updateReminder( rem );

			sc.commitTransaction();
			
			LoggerUtil.logIt( "Reminder '" + rem.getName() + "' edited by '"
					+ user.getUser().getName() + ":"
					+ user.getUser().getFullName() + "' " );

		}
		catch ( Exception e ) {
			request.getSession().setAttribute( "exceptionTrace" , e );
			return new ModelAndView( new RedirectView( "exception.htm" ) );
		}
		finally {
			sc.closeSession();
		}
		return new ModelAndView(new RedirectView("viewReminders.htm?action=search&editOrUpdateMessage="+message+"&reminderName="+rem.getName()));
	}
	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		Reminder rem=new Reminder();
		
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			boolean session_expired = true;
			request.setAttribute("session_expired", session_expired);
		}
		else{
			String rec=request.getParameter("editRecord");
			ServiceContext sc = Context.getServices();
			try{
				rem=sc.getReminderService().getReminder(rec,false,FetchMode.JOIN);
			}
			catch (Exception e) {
				request.setAttribute("errorMessage", e.getMessage());
			}
			finally{
				sc.closeSession();
			}
		}
		return rem;
	}
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		Reminder rem = (Reminder) command;
		Map<String , Object> model = new HashMap<String , Object>();
		model.put( "reminderText" , rem.getReminderText() );
		return model;
		
	}
}
