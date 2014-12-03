package org.ird.immunizationreminder.web.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.LoggedInUser;
import org.ird.immunizationreminder.context.ServiceContext;
import org.ird.immunizationreminder.datamodel.entities.ArmDayReminder;
import org.ird.immunizationreminder.datamodel.entities.Child;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms.REMINDER_STATUS;
import org.ird.immunizationreminder.datamodel.entities.Vaccination;
import org.ird.immunizationreminder.datamodel.entities.Vaccination.VACCINATION_STATUS;
import org.ird.immunizationreminder.utils.date.DateUtils;
import org.ird.immunizationreminder.utils.reporting.ExceptionUtil;
import org.ird.immunizationreminder.utils.reporting.LoggerUtil;
import org.ird.immunizationreminder.web.dwr.DWRVaccineService;
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

public class EditVaccinationRecordController extends SimpleFormController{
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			return new ModelAndView(new RedirectView("login.htm"));
		}
//		String addedpId=request.getParameter("semi-added-childid");

//		if(addedpId.compareTo(child.getChildId())!=0){
//			throw new Exception("session's attribute is having child instance intermingled. contact program vendor");
//		}
		ServiceContext sc = Context.getServices();
		try{
			Vaccination childvacc=(Vaccination)command;
			Date vaccinationDate=(Date) request.getSession().getAttribute("vaccinationDate"+childvacc.getVaccinationRecordNum());
			Child child=(Child) request.getSession().getAttribute("child"+childvacc.getVaccinationRecordNum());

			try{
				request.getSession().removeAttribute("vaccinationDate"+childvacc.getVaccinationRecordNum());
				request.getSession().removeAttribute("child"+childvacc.getVaccinationRecordNum());
			}catch (Exception e) {
				// TODO: handle exception
			}
			
			childvacc.setLastEditor(user.getUser());
			childvacc.setVaccine(sc.getVaccinationService()
										.getByName(request.getParameter("vaccineName")));
			if(childvacc.getIsFirstVaccination()){
				childvacc.setVaccinationDuedate(childvacc.getVaccinationDate());
			}
			
			sc.getVaccinationService().updateVaccinationRecord(childvacc);
			
			if(childvacc.getVaccinationStatus().equals(VACCINATION_STATUS.PENDING)){
				List<ReminderSms> rsms=sc.getReminderService().findByCriteria(null, null,
						null, null, null, null, false, null, Long.toString(childvacc.getVaccinationRecordNum())
						,false,FetchMode.SELECT); 	
					//verify rsms are equal to daynums in arm
					for (ReminderSms r : rsms) {
						if(!DateUtils.datesEqual(r.getDueDate(), new Date()) && r.getReminderStatus().equals(REMINDER_STATUS.PENDING)){
							String[] timestr=request.getParameter("time"+r.getDayNumber()).replaceAll(" ", "").split(":");
							int hr=Integer.parseInt(timestr[0]);
							int min=Integer.parseInt(timestr[1]);
							int sec=Integer.parseInt(timestr[2]);

							Calendar cal=Calendar.getInstance();
							cal.setTime(childvacc.getVaccinationDuedate());
							cal.set(Calendar.HOUR_OF_DAY, hr);
							cal.set(Calendar.MINUTE, min);
							cal.set(Calendar.SECOND, sec);
							cal.add(Calendar.DATE, r.getDayNumber());
							if ( r.getDayNumber() >= 0 ) {
								if ( cal.get( Calendar.DAY_OF_WEEK ) == Calendar.SUNDAY ) {
									cal.add( Calendar.DATE , 1 );
								}
							}
							r.setDueDate(cal.getTime());
							r.setLastEditor(user.getUser());
							sc.getReminderService().updateReminderSmsRecord(r);
						}
					}
			}else if(childvacc.getNextVaccinationRecordNum()>0){
				Vaccination nextVacc = sc.getVaccinationService().getVaccinationRecord(childvacc.getNextVaccinationRecordNum(),false,FetchMode.SELECT,FetchMode.SELECT);
				//if vaccinationdate saved in session and child vaccination date are not equal:shows vacc 
				//date is changed
				if(vaccinationDate != null && !DateUtils.datesEqual(vaccinationDate, childvacc.getVaccinationDate())){
					if(nextVacc.getVaccinationStatus().equals(VACCINATION_STATUS.PENDING)){

						SimpleDateFormat df = new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT);
						Date nextAsgnDate = null;
						if(nextVacc.getVaccine().getName().toLowerCase().contains("measles2")){
							nextAsgnDate = df.parse(new DWRVaccineService().calculateMeasles2DateFromMeasles1(child.getBirthdate(), childvacc.getVaccinationDate()));
						}
						else{
							nextAsgnDate = df.parse(new DWRVaccineService().calculateNextVaccinationDateFromPrev(childvacc.getVaccinationDate()
								, request.getParameter("nextvaccinationDateVal"), true));
				
						}
						nextVacc.setVaccinationDuedate(nextAsgnDate);
						sc.getVaccinationService().updateVaccinationRecord(nextVacc);
						
						childvacc.setNextAssignedDate(nextAsgnDate);
						sc.getVaccinationService().updateVaccinationRecord(childvacc);

						try{
							List<ReminderSms> rsmslist=sc.getReminderService().findByCriteria(null, null,
									null, null, null, null, false, null, Long.toString(nextVacc.getVaccinationRecordNum())
									,false,FetchMode.SELECT); 
							
							for (ArmDayReminder armdayrem : child.getArm().getArmday()) {
								ReminderSms remSms=new ReminderSms();
								
								remSms.setDueTime(armdayrem.getDefaultReminderTime());
								Calendar cal1=Calendar.getInstance();
								cal1.setTime(nextVacc.getVaccinationDuedate());
								cal1.set(Calendar.HOUR_OF_DAY, remSms.getDueTime().getHours());
								cal1.set(Calendar.MINUTE, remSms.getDueTime().getMinutes());
								cal1.set(Calendar.SECOND, remSms.getDueTime().getSeconds());
								cal1.add(Calendar.DATE, armdayrem.getId().getDayNumber());
								if ( remSms.getDayNumber() >= 0 ) {
									if ( cal1.get( Calendar.DAY_OF_WEEK ) == Calendar.SUNDAY ) {
										cal1.add( Calendar.DATE , 1 );
									}
								}
								
								remSms.setDueDate(cal1.getTime());
								
								for (ReminderSms reminderSms : rsmslist) {
									if(reminderSms.getDayNumber()==armdayrem.getId().getDayNumber()){

										if(!DateUtils.datesEqual(reminderSms.getDueDate(), new Date()) && reminderSms.getReminderStatus().equals(REMINDER_STATUS.PENDING)){
											reminderSms.setDueDate(cal1.getTime());
											reminderSms.setLastEditor(user.getUser());
											sc.getReminderService().addReminderSmsRecord(reminderSms);
										}
/*										else {
											Calendar testdate=Calendar.getInstance();
											testdate.add(Calendar.DATE, 7);

											if(nextVacc.getVaccinationDuedate().after(testdate.getTime())){
												remSms.setCreator(user.getUser());
												remSms.setStatus(REMINDER_STATUS.PENDING);
												remSms.setDayNumber(armdayrem.getId().getDayNumber());
												remSms.setVaccinationRecordNum(nextVacc.getVaccinationRecordNum());
												sc.getReminderService().addReminderSmsRecord(remSms);
											}
										}*/
									}
								}							
							}
						}catch (Exception e) {
								LoggerUtil.logIt(ExceptionUtil.getStackTrace(e));
						}
					}
				}
			}
			sc.commitTransaction();
		//roll back if either of them is not saved properly or any exception occurs
			return new ModelAndView(new RedirectView("viewVaccinationRecord.htm?action=search&editOrUpdateMessage=VaccinationRecordEditedSuccessfully&recordNum="+childvacc.getVaccinationRecordNum()));
		}catch (Exception e) {
			sc.rollbackTransaction();
			request.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}finally{
			sc.closeSession();
		}
	}
	
	@Override
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT), true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor(false));
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		long vaccId=Long.parseLong(request.getParameter("vaccRecNum"));
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		Vaccination pvacc=new Vaccination();
		Vaccination prevVaccination = null;
		Vaccination nextVaccination = null;

		if(user==null){
			boolean session_expired = true;
			request.setAttribute("session_expired", session_expired);
		}
		else{
			ServiceContext sc = Context.getServices();
			try{
				pvacc=sc.getVaccinationService().getVaccinationRecord(vaccId,false,FetchMode.JOIN,FetchMode.JOIN);
				if(pvacc==null){
					request.setAttribute("errorMessage", "Oops some error occurred. Child Vaccination record requested for was not found.");
					return new Vaccination();
				}/*else if(pvacc.getVaccinationStatus().compareTo(VACCINATION_STATUS.PENDING.toString())!=0){
					request.setAttribute("errorMessage", "Child Vaccination record requested for was not pending. only pending records can be edited");
					return new Vaccination();
				}*/else {
					/*int minDaynum=StringUtils.isEmptyOrWhitespaceOnly(pvacc.getChild().getArm().getReminderDaysSequence())?0:
						Integer.parseInt(pvacc.getChild().getArm().getReminderDaysSequence().split(",")[0]);
					Calendar cal=Calendar.getInstance();
					cal.setTime(pvacc.getVaccinationDuedate());
					cal.add(Calendar.DATE, minDaynum);
					Calendar now=Calendar.getInstance();
					now.add(Calendar.DATE, 3);
					if(now.compareTo(cal)>0){
						request.setAttribute("errorMessage", "Child Vaccination record can not be edited after reminder sms' have been queued up(sms are queued before 2 days left in sending first sms of arm).");
						return new Vaccination();
					}else{*/
						if(pvacc.getPreviousVaccinationRecordNum()!=0){
							prevVaccination=sc.getVaccinationService()
								.getVaccinationRecord(pvacc.getPreviousVaccinationRecordNum(),false,FetchMode.SELECT,FetchMode.SELECT);
						}
						if(pvacc.getNextVaccinationRecordNum()!=0){
							nextVaccination=sc.getVaccinationService()
								.getVaccinationRecord(pvacc.getNextVaccinationRecordNum(),false,FetchMode.SELECT,FetchMode.SELECT);
						}
						request.setAttribute("child", pvacc.getChild());
						request.setAttribute("previous_vaccination", prevVaccination);
						request.setAttribute("next_vaccination", nextVaccination);
						request.getSession().setAttribute("vaccinationDate"+pvacc.getVaccinationRecordNum(), pvacc.getVaccinationDate());
						request.getSession().setAttribute("child"+pvacc.getVaccinationRecordNum(), pvacc.getChild());
						request.setAttribute("arm", sc.getArmService().getByName(pvacc.getChild().getArm().getArmName(), FetchMode.JOIN, true));
						return pvacc;
					//}
				}
			}catch (Exception e) {
				request.setAttribute("errorMessage", "Child Vaccination record threw exception:"+e.getMessage());
			}
			finally{
				sc.closeSession();
			}
		}
		return pvacc;
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		boolean session_expired=false;
		Map<String, Object> model=new HashMap<String, Object>();
		
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			session_expired=true;
			request.setAttribute("session_expired", session_expired);
		}
		else{
			ServiceContext sc = Context.getServices();
			try{
				model.put("vaccine", sc.getVaccinationService().getAll(0,100));
			}
			catch (Exception e) {
				request.setAttribute("errorMessagev", "An error occurred while retrieving Vaccine list. Error message is:"+e.getMessage());
			}
			finally{
				sc.closeSession();
			}
		}
		return model;
	}
}
