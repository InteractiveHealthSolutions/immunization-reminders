package org.ird.immunizationreminder.web.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.LoggedInUser;
import org.ird.immunizationreminder.context.ServiceContext;
import org.ird.immunizationreminder.datamodel.entities.ArmDayReminder;
import org.ird.immunizationreminder.datamodel.entities.Child;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms.REMINDER_STATUS;
import org.ird.immunizationreminder.datamodel.entities.Vaccination;
import org.ird.immunizationreminder.datamodel.entities.Vaccination.VACCINATION_STATUS;
import org.ird.immunizationreminder.datamodel.entities.Vaccine;
import org.ird.immunizationreminder.utils.reporting.ExceptionUtil;
import org.ird.immunizationreminder.utils.reporting.LoggerUtil;
import org.ird.immunizationreminder.web.utils.IMRGlobals;
import org.ird.immunizationreminder.web.utils.IRUtils;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class AddChildFlowVaccinationController extends SimpleFormController{
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			return new ModelAndView(new RedirectView("login.htm"));
		}
		
//		String addedpId=request.getParameter("semi-added-childid");
		Child child=(Child) request.getSession().getAttribute("semi-added-child");
//		if(addedpId.compareTo(child.getChildId())!=0){
//			throw new Exception("session's attribute is having child instance intermingled. contact program vendor");
//		}
		ServiceContext sc = Context.getServices();
		try{
			////Adding child entitled for enrollment
			sc.getChildService().addChild(child);
			
			//Child savedChild=sc.getChildService().getChildbyChildId(child.getChildId(),false);
			
			////Adding vaccination child have received at time of enrollment
			Vaccination childvacc=(Vaccination)command;
			childvacc.setVaccinationDuedate(childvacc.getVaccinationDate());
			childvacc.setCreator(user.getUser());
			childvacc.setIsFirstVaccination(true);
			childvacc.setChild(child);
			childvacc.setVaccine(sc.getVaccinationService().getByName(request.getParameter("vaccineName")));
			childvacc.setPreviousVaccinationRecordNum(0);
			
			Serializable currVaccId = sc.getVaccinationService().addVaccinationRecord(childvacc);
			
			////Adding vaccination assigned for next followup visit
			Vaccination nextVaccRecord=new Vaccination();
			nextVaccRecord.setCreator(user.getUser());
			nextVaccRecord.setDescription(request.getParameter("nextVaccDescription"));
			nextVaccRecord.setVaccine(sc.getVaccinationService().getByName(request.getParameter("nextVaccineName")));
			nextVaccRecord.setVaccinationDuedate(childvacc.getNextAssignedDate());
			nextVaccRecord.setVaccinationStatus(VACCINATION_STATUS.PENDING);
			nextVaccRecord.setIsFirstVaccination(false);
			nextVaccRecord.setChild(child);
			nextVaccRecord.setPreviousVaccinationRecordNum(Long.parseLong(currVaccId.toString()));
			
			Serializable nextVaccId = sc.getVaccinationService().addVaccinationRecord(nextVaccRecord);
			
			////Adding Reminders for next vaccination
			for (ArmDayReminder armday : child.getArm().getArmday()) {
				ReminderSms remsms=new ReminderSms();
				remsms.setCreator(user.getUser());
				remsms.setDayNumber(armday.getId().getDayNumber());

				String[] timestr=request.getParameter("time"+ armday.getId().getDayNumber()).replaceAll(" ", "").split(":");
				
				int hr=Integer.parseInt(timestr[0]);
				int min=Integer.parseInt(timestr[1]);
				int sec=Integer.parseInt(timestr[2]);
				
				Calendar cal=Calendar.getInstance();
				cal.setTime(nextVaccRecord.getVaccinationDuedate());
				cal.set(Calendar.HOUR_OF_DAY, hr);
				cal.set(Calendar.MINUTE, min);
				cal.set(Calendar.SECOND, sec);
				cal.add(Calendar.DATE, armday.getId().getDayNumber());
				if ( remsms.getDayNumber() >= 0 ) {
					if ( cal.get( Calendar.DAY_OF_WEEK ) == Calendar.SUNDAY ) {
						cal.add( Calendar.DATE , 1 );
					}
				}
				
				remsms.setDueDate(cal.getTime());
				
				remsms.setChild(child);
				remsms.setStatus(REMINDER_STATUS.PENDING);
				remsms.setVaccinationRecordNum(Long.parseLong(nextVaccId.toString()));
				
				sc.getReminderService().addReminderSmsRecord(remsms);
				
				childvacc.setNextVaccinationRecordNum(Long.parseLong(nextVaccId.toString()));
				sc.getVaccinationService().updateVaccinationRecord(childvacc);
			}
			////Adding measles1 vaccination by DOB
			Vaccination measlesVaccination = new Vaccination();
			measlesVaccination .setChild(child);
			measlesVaccination.setCreator(user.getUser());
			measlesVaccination.setIsFirstVaccination(false);
			measlesVaccination.setIsLastVaccination(false);
			measlesVaccination.setPreviousVaccinationRecordNum(0);
			measlesVaccination.setVaccine(sc.getVaccinationService().getByName("measles1"));

			if (child.getBirthdate() != null ) {
				measlesVaccination.setVaccinationDuedate(IRUtils.getMeasles1Date(child.getBirthdate()));
			}
			measlesVaccination.setVaccinationStatus(VACCINATION_STATUS.PENDING);
			Serializable measlesVaccId = sc.getVaccinationService().addVaccinationRecord(measlesVaccination);
			////Adding measles1 vaccination reminders
			for (ArmDayReminder armdayrem : child.getArm().getArmday()) {
				ReminderSms remSms = new ReminderSms();
				remSms.setCreator(user.getUser());
				String[] timestr = request.getParameter("time" + armdayrem.getId().getDayNumber()).replaceAll(" ", "").split(":");

				int hr = Integer.parseInt(timestr[0]);
				int min = Integer.parseInt(timestr[1]);
				int sec = Integer.parseInt(timestr[2]);

				Calendar cal = Calendar.getInstance();
				cal.setTime(measlesVaccination.getVaccinationDuedate());
				cal.set(Calendar.HOUR_OF_DAY, hr);
				cal.set(Calendar.MINUTE, min);
				cal.set(Calendar.SECOND, sec);
				cal.add(Calendar.DATE, armdayrem.getId().getDayNumber());
				if ( remSms.getDayNumber() >= 0 ) {
					if ( cal.get( Calendar.DAY_OF_WEEK ) == Calendar.SUNDAY ) {
						cal.add( Calendar.DATE , 1 );
					}
				}
				remSms.setDueDate(cal.getTime());
				remSms.setChild(child);
				remSms.setStatus(REMINDER_STATUS.PENDING);
				remSms.setVaccinationRecordNum(Long.parseLong(measlesVaccId.toString()));

				sc.getReminderService().addReminderSmsRecord(remSms);

				measlesVaccination.setNextVaccinationRecordNum(Long.parseLong(measlesVaccId.toString()));

				sc.getVaccinationService().updateVaccinationRecord(measlesVaccination);
			}
			
			//roll back if either of them is not saved properly or any exception occurs
			sc.commitTransaction();
		}
		catch (Exception e) {
			e.printStackTrace();
			sc.rollbackTransaction();
			request.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}
		finally{
			try{
				request.getSession().removeAttribute("semi-added-child");
			}catch (Exception e) {
			}
			try{
				request.getSession().removeAttribute("semiaddedchildArm");
			}catch (Exception e) {
			}
			sc.closeSession();
		}
		
		final String MESSAGE_CHILD_ADDED_SUCCESSFULLY="Child added successfully.";
		final String TEXT_URL="Add another child";
		
		String NEWLY_ADDED_CHILD_URL;
		final String NEWLY_ADDED_CHILD_MESSAGE="View Your newly added Child here. ";
		final String ALL_CHILDREN_URL="viewChildren.htm";
		final String ALL_CHILDREN_MESSAGE="View all Children. ";
		
		NEWLY_ADDED_CHILD_URL="window.open('viewChildren.htm?action=search&childid="+child.getChildId()+"','_self')";
		request.setAttribute("NEWLY_ADDED_ENTITY_URL", NEWLY_ADDED_CHILD_URL);
		request.setAttribute("NEWLY_ADDED_ENTITY_MESSAGE", NEWLY_ADDED_CHILD_MESSAGE);
		request.setAttribute("ALL_ENTITYS_URL", ALL_CHILDREN_URL);
		request.setAttribute("ALL_ENTITYS_MESSAGE", ALL_CHILDREN_MESSAGE);
		request.setAttribute("message", MESSAGE_CHILD_ADDED_SUCCESSFULLY);

		request.setAttribute("url", "addChild.htm");
		request.setAttribute("urlText", TEXT_URL);
		
		return new ModelAndView("successView");
	}
	
	@Override
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT), true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor(false));
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
				List<Vaccine> vacl = sc.getVaccinationService().getAll(0,100);
				List<Vaccine> vaclToShow = new ArrayList<Vaccine>(); 
				for (Vaccine vaccine : vacl) {
					////if vaccination is of penta or bcg then add all pentas and bcg 
					////else if measles 1 the add only measles2
					////else if measles2 add nothing it should be system calculated
					if(vaccine.getName().toLowerCase().contains("penta1")
							|| vaccine.getName().toLowerCase().contains("penta2")
									||vaccine.getName().toLowerCase().contains("bcg")){
						vaclToShow.add(vaccine);
					}
				}
				model.put("vaccine", vaclToShow);
			}
			catch (Exception e) {
				LoggerUtil.logIt(ExceptionUtil.getStackTrace(e));
				request.setAttribute("errorMessage", "An error occurred while retrieving Vaccine list. You will not be able to continue. Try Later. Error message is:"+e.getMessage());
			}
			finally{
				sc.closeSession();
			}
		}
		request.setAttribute("semi-added-childid", request.getAttribute("semi-added-childid"));
		request.setAttribute("child",request.getSession().getAttribute("semi-added-child"));
		request.setAttribute("arml",request.getSession().getAttribute("semiaddedchildArm"));

		return model;
	}
}
