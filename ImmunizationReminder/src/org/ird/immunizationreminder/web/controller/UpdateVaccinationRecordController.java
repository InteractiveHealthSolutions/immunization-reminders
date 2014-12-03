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

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.LoggedInUser;
import org.ird.immunizationreminder.context.ServiceContext;
import org.ird.immunizationreminder.datamodel.entities.ArmDayReminder;
import org.ird.immunizationreminder.datamodel.entities.Child;
import org.ird.immunizationreminder.datamodel.entities.Child.STATUS;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms.REMINDER_STATUS;
import org.ird.immunizationreminder.datamodel.entities.Vaccination;
import org.ird.immunizationreminder.datamodel.entities.Vaccination.VACCINATION_STATUS;
import org.ird.immunizationreminder.datamodel.entities.Vaccine;
import org.ird.immunizationreminder.utils.date.DateUtils;
import org.ird.immunizationreminder.web.utils.IMRGlobals;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class UpdateVaccinationRecordController extends SimpleFormController{
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			return new ModelAndView(new RedirectView("login.htm"));
		}
//		String addedpId=request.getParameter("semi-added-childid");

		Child child = (Child)request.getSession().getAttribute("child");
		request.getSession().removeAttribute("child");
		
		//Child child=(Child) request.getAttribute("child");
//		if(addedpId.compareTo(child.getChildId())!=0){
//			throw new Exception("session's attribute is having child instance intermingled. contact program vendor");
//		}
		ServiceContext sc = Context.getServices();
		try{
				Vaccination childvacc=(Vaccination)command;
				childvacc.setIsFirstVaccination(false);
				childvacc.setLastEditor(user.getUser());
				sc.getVaccinationService().updateVaccinationRecord(childvacc);
				
				if(childvacc.getIsLastVaccination()){
					childvacc.setNextAssignedDate(null);
				}
				
				String shouldBeSysCalc = request.getParameter("systemCalculated");
				if(!childvacc.getIsLastVaccination()
						&& ((!childvacc.getVaccine().getName().toLowerCase().contains("penta3")
								&& !childvacc.getVaccine().getName().toLowerCase().contains("measles2"))
								|| shouldBeSysCalc == null)){
					String nextVaccName=request.getParameter("nextVaccineName");
					String nextVaccDescription=request.getParameter("nextVaccDescription");
					
					Vaccination nextVaccRecord=new Vaccination();
					nextVaccRecord.setCreator(user.getUser());
					nextVaccRecord.setDescription(nextVaccDescription);
					nextVaccRecord.setVaccine(sc.getVaccinationService().getByName(nextVaccName));
					nextVaccRecord.setVaccinationDuedate(childvacc.getNextAssignedDate());
					nextVaccRecord.setVaccinationStatus(VACCINATION_STATUS.PENDING);
					nextVaccRecord.setIsFirstVaccination(false);
					nextVaccRecord.setChild(child);
					nextVaccRecord.setPreviousVaccinationRecordNum(childvacc.getVaccinationRecordNum());
					
					Serializable nextid = sc.getVaccinationService().addVaccinationRecord(nextVaccRecord);

					for (ArmDayReminder armday : child.getArm().getArmday()) {
						ReminderSms remsms=new ReminderSms();
						remsms.setCreator(user.getUser());
						remsms.setDayNumber(armday.getId().getDayNumber());
						String[] timestr=request.getParameter("time"+armday.getId().getDayNumber()).replaceAll(" ", "").split(":");
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
						remsms.setVaccinationRecordNum(Long.parseLong(nextid.toString()));
						sc.getReminderService().addReminderSmsRecord(remsms);
					}
					childvacc.setNextVaccinationRecordNum(Long.parseLong(nextid.toString()));
					sc.getVaccinationService().updateVaccinationRecord(childvacc);
				}else if(childvacc.getIsLastVaccination()){//if it is lastvaccination then set child has completed vaccinations
					child.setHasCompleted(true);
					child.setStatus(STATUS.COMPLETED);
					child.setDateOfCompletion(new Date());
					sc.getChildService().updateChild(child);
				}

				//roll back if either of them is not saved properly or any exception occurs
				sc.commitTransaction();
				return new ModelAndView(new RedirectView("viewVaccinationRecord.htm?action=search&editOrUpdateMessage=VaccinationRecordUpdatedSuccessfully&recordNum="+childvacc.getVaccinationRecordNum()));

		}catch (Exception e) {
			e.printStackTrace();
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
		String record_id=request.getParameter("record_id");
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		Vaccination pvacc=new Vaccination();
		Vaccination prevVaccination = new Vaccination();
		
		if(user==null){
			boolean session_expired = true;
			request.setAttribute("session_expired", session_expired);
		}
		else{
			ServiceContext sc = Context.getServices();
			try{
				pvacc=sc.getVaccinationService()
							.getVaccinationRecord(Long.parseLong(record_id) ,false, FetchMode.JOIN , FetchMode.JOIN);
				if(pvacc == null){
					request.setAttribute("errorMessage", "No vaccination was not found with given record number. Try again or if problem persists contact database administrator");
					request.setAttribute("shouldenableVaccination", false);
				}else {
					Calendar cal=Calendar.getInstance();
					cal.add(Calendar.DATE, 4);
					if(!DateUtils.truncateDatetoDate(pvacc.getVaccinationDuedate()).before(cal.getTime())){//if date of vaccination is far from today
						request.setAttribute("errorMessage",
												"Note: "
												+ "<br>Child`s Vaccination Duedate is on "
												+ new SimpleDateFormat("MMM dd, yyyy")
														.format(pvacc.getVaccinationDuedate())
														
												+ "<br>and you are authorized to update vaccination information only three days before vaccination due date and onwards." 
												+ "<br>However to change/edit this vaccination record navigate to edit vaccination in Vaccinations menu");
						request.setAttribute("shouldenableVaccination", false);
					}
					request.setAttribute("arm", sc.getArmService().getByName(pvacc.getChild().getArm().getArmName(), FetchMode.JOIN, true));
					prevVaccination=sc.getVaccinationService()
								.getVaccinationRecord(pvacc.getPreviousVaccinationRecordNum(),false,FetchMode.SELECT,FetchMode.SELECT);
				}
			}catch (Exception e) {
				request.setAttribute("errorMessage", "Oops .. Some error occurred. Exception message is:"+e.getMessage());
				request.setAttribute("shouldenableVaccination", false);
			}finally{
				sc.closeSession();
			}
		}
		request.setAttribute("child", pvacc.getChild());
		request.getSession().setAttribute("child", pvacc.getChild());
		request.setAttribute("previous_vaccination", prevVaccination);
		return pvacc;
	}
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		Vaccination currVac = (Vaccination) command;
		boolean session_expired=false;
		Map<String, Object> model=new HashMap<String, Object>();
		
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			session_expired=true;
			request.setAttribute("session_expired", session_expired);
		}else{
			ServiceContext sc = Context.getServices();
			try{
				List<Vaccine> vacl = sc.getVaccinationService().getAll(0,100);
				List<Vaccine> vaclToShow = new ArrayList<Vaccine>(); 
				for (Vaccine vaccine : vacl) {
					////if vaccination is of penta or bcg then add all pentas and bcg 
					////else if measles 1 the add only measles2
					////else if measles2 add nothing it should be system calculated
					if((currVac.getVaccine().getName().toLowerCase().contains("penta")
							|| currVac.getVaccine().getName().toLowerCase().contains("bcg"))
							&& (vaccine.getName().toLowerCase().contains("penta")
									||vaccine.getName().toLowerCase().contains("bcg"))){
						vaclToShow.add(vaccine);
					}
					else if(currVac.getVaccine().getName().toLowerCase().contains("measles1")
							&&(vaccine.getName().toLowerCase().contains("measles1")
									||vaccine.getName().toLowerCase().contains("measles2"))){
						vaclToShow.add(vaccine);
					}
					else if(currVac.getVaccine().getName().toLowerCase().contains("measles2")
							&& vaccine.getName().toLowerCase().contains("measles2")){
						vaclToShow.add(vaccine);
					}
				}
				model.put("vaccine", vaclToShow);
			}catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("errorMessagev", "An error occurred while retrieving Vaccine list. Error message is:"+e.getMessage());
			}
			finally{
				sc.closeSession();
			}
		}
		return model;
	}
}
