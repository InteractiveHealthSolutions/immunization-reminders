package org.ird.immunizationreminder.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.LoggedInUser;
import org.ird.immunizationreminder.context.ServiceContext;
import org.ird.immunizationreminder.datamodel.entities.Child;
import org.ird.immunizationreminder.datamodel.entities.Child.STATUS;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms.REMINDER_STATUS;
import org.ird.immunizationreminder.datamodel.entities.Vaccination;
import org.ird.immunizationreminder.datamodel.entities.Vaccination.VACCINATION_STATUS;
import org.ird.immunizationreminder.web.utils.IMRGlobals;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class EditChildController extends SimpleFormController{
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			return new ModelAndView(new RedirectView("login.htm"));
		}
		
		String editmessage="Child Edited Successfully";

		Child child=(Child)command;
//		int cellLn=Integer.parseInt(Context.getIRSetting("cellnumber.number-length-without-zero", "10"));
//		String editedCell=child.getCurrentCellNo().substring(child.getCurrentCellNo().length()-cellLn);
//		String prevCell=getPreviousChild(request).getCurrentCellNo()
//								.substring(getPreviousChild(request).getCurrentCellNo().length()-cellLn);
//		if (editedCell.compareTo(prevCell)!=0) {
//			
//		}
		STATUS uneditedChildStatus=(STATUS) request.getSession().getAttribute("uneditedChildStatus");
		ServiceContext sc = Context.getServices();
		try{
			if((uneditedChildStatus.equals(STATUS.TERMINATED))
					|| (uneditedChildStatus.equals(STATUS.COMPLETED))){
				if(!(child.getStatus().equals(uneditedChildStatus) )){
					editmessage="Children being terminated or having completed their vaccination cannot be changed their status.";
					return new ModelAndView(new RedirectView("viewChildren.htm?action=search&editOrUpdateMessage="+editmessage+"&childid="+child.getChildId()));
				}
			}
			
			if(child.getStatus().equals(STATUS.TERMINATED.toString()) ){
				if(!(uneditedChildStatus.equals(STATUS.TERMINATED))){
					List<Vaccination> vaccs = sc.getVaccinationService().findVaccinationRecordByCriteria(child.getChildId(), null, null, null, null, null, VACCINATION_STATUS.PENDING, 0, 10,false,FetchMode.SELECT,FetchMode.SELECT);
					
					for (Vaccination vaccination : vaccs) {
						vaccination.setVaccinationStatus(VACCINATION_STATUS.MISSED);
						vaccination.setDescription((vaccination.getDescription()==null?"":vaccination.getDescription())+"Patient Terminated.");
						vaccination.setLastEditor(user.getUser());
						
						sc.getVaccinationService().updateVaccinationRecord(vaccination);
						
						List<ReminderSms> reml = sc.getReminderService().findByCriteria(null, null, null, null, null, null, false, null, Long.toString(vaccination.getVaccinationRecordNum()),false,FetchMode.SELECT);
						for (ReminderSms reminderSms : reml) {
							reminderSms.setStatus(REMINDER_STATUS.CANCELLED);
							reminderSms.setSmsCancelReason((reminderSms.getSmsCancelReason()==null?"":reminderSms.getSmsCancelReason())+"Patient Terminated.");
							reminderSms.setLastEditor(user.getUser());
							
							sc.getReminderService().updateReminderSmsRecord(reminderSms);
						}
					}
				}
			}
			child.setLastEditor(user.getUser());
			
			sc.getChildService().mergeUpdateChild(child);
			sc.commitTransaction();
			
			editmessage="Child Edited Successfully";
		}
		catch (Exception e) {
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}
		finally{
			sc.closeSession();
		}
		return new ModelAndView(new RedirectView("viewChildren.htm?action=search&&editOrUpdateMessage="+editmessage+"&childid="+child.getChildId()));
	}
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		String child_id=request.getParameter("child_id");
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		
		if(user==null){
			boolean session_expired = true;
			request.setAttribute("session_expired", session_expired);
		}else{
			ServiceContext sc = Context.getServices();
			Child p = null;
			try{
			p = sc.getChildService().getChildbyChildId(child_id,false);
				if(p==null){
					request.setAttribute("errorMessage", "Oops .. Some error occurred. Child was not found.");
					return new Child();
				}
			}
			catch (Exception e) {
				request.setAttribute("errorMessage", "Oops .. Some exception was thrown. Error message is:"+e.getMessage());
			}
			finally{
				sc.closeSession();
			}
			request.getSession().setAttribute("uneditedChildStatus", p.getStatus());
			return p;
		}
		return new Child();
	}
	@Override
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT), true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
	}
}
