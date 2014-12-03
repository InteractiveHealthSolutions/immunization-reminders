package org.ird.immunizationreminder.web.mobile;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.LoggedInUser;
import org.ird.immunizationreminder.context.ServiceContext;
import org.ird.immunizationreminder.data.exception.ChildDataException;
import org.ird.immunizationreminder.datamodel.entities.ArmDayReminder;
import org.ird.immunizationreminder.datamodel.entities.Child;
import org.ird.immunizationreminder.datamodel.entities.Child.STATUS;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms.REMINDER_STATUS;
import org.ird.immunizationreminder.datamodel.entities.Vaccination;
import org.ird.immunizationreminder.datamodel.entities.Vaccination.VACCINATION_STATUS;
import org.ird.immunizationreminder.service.exception.UserServiceException;
import org.ird.immunizationreminder.utils.date.DateUtils;

public class SubmitHandler {
	private static Hashtable<String, LoggedInUser> users = new Hashtable<String, LoggedInUser>();
	
	public static void handleSubmit(HttpServletRequest request, HttpServletResponse resp) throws TransformerException, IOException{
		String query = request.getParameter(RequestParam.SUBMIT_FORM_TYPE.getParamName());
		
		if(query.equalsIgnoreCase(FormType.LOGIN)){
			IMRSMobileServlet.sendResponse(resp, handleLogin(request).docToString());
		}
		else if(query.equalsIgnoreCase(FormType.UPDATE_VACCINATION))
		{
			IMRSMobileServlet.sendResponse(resp, handleUpdateVaccination(request).docToString());
		}
	}
	
	private static XmlResponse handleLogin(HttpServletRequest request) 
	{
		String username = request.getParameter(RequestParam.LG_USERNAME.getParamName());
		String password = request.getParameter(RequestParam.LG_PASSWORD.getParamName());
		String datetime = request.getParameter(RequestParam.LG_PHONETIME.getParamName());

		try {
			Date date = new SimpleDateFormat(IMRMobileGlobals.MOBILE_DATE_FORMAT).parse(datetime);
			
			if(!DateUtils.datesEqual(date, new Date())){
				return XmlResponse.getErrorResponse("Invalid date on your mobile. Please update date and time and try again.");
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
			return XmlResponse.getErrorResponse("Error handling datetime:" + e1.getMessage());
		}
		try {
			users.put(username,Context.getAuthenticatedUser(username, password));
		} catch (UserServiceException e) {
			return XmlResponse.getErrorResponse("Authentication error. " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return XmlResponse.getErrorResponse("Error while authenticating user. Try again. If problem persists contact program administrator");
		}
		
		return XmlResponse.getSuccessResponse();
	}

	private static XmlResponse handleUpdateVaccination(HttpServletRequest request)
	{
		ServiceContext sc = Context.getServices();
		
		String childid = request.getParameter(RequestParam.UV_CHILDID.getParamName());
		String currentvaccDate = request.getParameter(RequestParam.UV_CURR_VACCINATION_DATE.getParamName());
		String currVaccName = request.getParameter(RequestParam.UV_CURR_VACCINE_RECIEVED.getParamName());
		String ialastvacc = request.getParameter(RequestParam.UV_IS_LAST_VACCINATION.getParamName());
		String vaccStatus = request.getParameter(RequestParam.UV_VACCINATION_STATUS.getParamName());
		String nextvacc = request.getParameter(RequestParam.UV_NEXT_VACCINE.getParamName());
		String nextasssigneddate = request.getParameter(RequestParam.UV_NEXT_VACCINATION_DATE.getParamName());
		String reasonnotvacc = request.getParameter(RequestParam.UV_REASON_NOT_VACCINATED.getParamName());
		String addNote = request.getParameter(RequestParam.UV_ADDITIONAL_NOTE.getParamName());
		String userID = request.getParameter(RequestParam.REQ_USER.getParamName());

		try {
			Child child = null;
			
			try {
				child = sc.getChildService().getChildbyChildId(childid, false);
			} catch (ChildDataException e) {
				return XmlResponse.getErrorResponse(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				return XmlResponse.getErrorResponse("An Unknown error occurred while finding child. Details are :"+e.getMessage());
			}
			
			if(child == null){
				return XmlResponse.getErrorResponse("Child not found. Plz recheck ID and try again");
			}
			
			Vaccination pndvacc = null;
			List<Vaccination> pndvacclst;
			try{
				pndvacclst = sc.getVaccinationService().findVaccinationRecordByCriteria(
					child.getChildId(), currVaccName, null, null, null, null, VACCINATION_STATUS.PENDING, 0, 10
					, false , FetchMode.JOIN , FetchMode.JOIN);
			}
			catch (Exception e) {
				e.printStackTrace();
				return XmlResponse.getErrorResponse("Some error occurred while finding vaccination. Try again. If problem persists contact system administrator");
			}
			if(pndvacclst.size()==0){
				return XmlResponse.getErrorResponse("No vaccination was not found to be pending with given name for given child id. Either child has completed vaccination course or some data inconsistency have ocuurred.");
			}
			else if(pndvacclst.size()>1){
				return XmlResponse.getErrorResponse("Multiple vaccinations were found to be pending for given child_id with given name. Data inconsistency has occurred. Contact your Database Administrator.");
			}
	
			Calendar cal=Calendar.getInstance();
			cal.add(Calendar.DATE, 4);
			if(!DateUtils.truncateDatetoDate(pndvacclst.get(0).getVaccinationDuedate()).before(cal.getTime())){//if date of vaccination is far from today
				return XmlResponse.getErrorResponse("Note: "
										+ "\nChild`s Vaccination Duedate is on "
										+ new SimpleDateFormat("MMM dd, yyyy").format(pndvacclst.get(0).getVaccinationDuedate())
										+ "\nand you are can update vaccination information only three days before vaccination due date or onwards." 
										+ "\nHowever to change/edit this vaccination record navigate to edit vaccination in Vaccinations menu in web Application");
			}
			else{
				pndvacc = pndvacclst.get(0);
			}
			
			try{
				pndvacc.setLastEditor(sc.getUserService().getUser(userID));
				pndvacc.setLastEditedByUserName(pndvacc.getLastEditedByUserName()+":MOBILE");
			}catch (Exception e) {
				return XmlResponse.getErrorResponse("Error while finding user for request.");
			}
			
			if(reasonnotvacc != null ){
				pndvacc.setDescription((pndvacc.getDescription() == null ? "" : pndvacc.getDescription().trim())+reasonnotvacc);
			}
			
			if(addNote != null ){
				pndvacc.setDescription((pndvacc.getDescription() == null ? "" : pndvacc.getDescription().trim())+addNote.trim());
			}
			
			if(ialastvacc != null && ialastvacc.trim().equalsIgnoreCase("yes")){
				pndvacc.setIsLastVaccination(true);
			}
			else{
				pndvacc.setIsLastVaccination(false);
			}
			
			try{
				pndvacc.setVaccinationStatus(VACCINATION_STATUS.valueOf(VACCINATION_STATUS.class, vaccStatus.toUpperCase()));
			}catch (Exception e) {
				return XmlResponse.getErrorResponse("Error while manipulating vaccination status. Contact you program vendor.");
			}
			try {
				pndvacc.setVaccinationDate(new SimpleDateFormat(IMRMobileGlobals.MOBILE_DATE_FORMAT).parse(currentvaccDate));
			} catch (ParseException e1) {
				e1.printStackTrace();
				return XmlResponse.getErrorResponse("Error handling datetime:" + e1.getMessage());
			}
			try{
				sc.getVaccinationService().updateVaccinationRecord(pndvacc);
			}
			catch (Exception e) {
				e.printStackTrace();
				return XmlResponse.getErrorResponse("Some error occurred while updating vaccination. Try again.");
			}
			
			if(pndvacc.getIsLastVaccination()
					||nextasssigneddate == null){
				pndvacc.setNextAssignedDate(null);
			}
			else{
				try {
					pndvacc.setNextAssignedDate(new SimpleDateFormat(IMRMobileGlobals.MOBILE_DATE_FORMAT).parse(nextasssigneddate));
				} catch (ParseException e1) {
					e1.printStackTrace();
					return XmlResponse.getErrorResponse("Error handling datetime:" + e1.getMessage());
				}
			}
			if(!pndvacc.getIsLastVaccination() && nextasssigneddate != null && nextvacc != null){
				
				Vaccination nextVaccRecord=new Vaccination();
				try{
					nextVaccRecord.setCreator(sc.getUserService().getUser(userID));
					nextVaccRecord.setCreatedByUserName(nextVaccRecord.getCreatedByUserName()+":MOBILE");
				}catch (Exception e) {
					return XmlResponse.getErrorResponse("Error while finding user for request.");
				}
				try{
					nextVaccRecord.setVaccine(sc.getVaccinationService().getByName(nextvacc));
				}
				catch (Exception e) {
					e.printStackTrace();
					return XmlResponse.getErrorResponse("Some error occurred while finding vaccine with given name. Try again. If problem persists contact program vendor");
				}
				nextVaccRecord.setVaccinationDuedate(pndvacc.getNextAssignedDate());
				nextVaccRecord.setVaccinationStatus(VACCINATION_STATUS.PENDING);
				nextVaccRecord.setIsFirstVaccination(false);
				nextVaccRecord.setChild(child);
				nextVaccRecord.setPreviousVaccinationRecordNum(pndvacc.getVaccinationRecordNum());
				
				Serializable nextid;
				try{
					nextid = sc.getVaccinationService().addVaccinationRecord(nextVaccRecord);
				}
				catch (Exception e) {
					e.printStackTrace();
					return XmlResponse.getErrorResponse("Some error occurred while adding next vaccination. Data may have become inconsistent. Contact your Program administrator.");
				}
				if(!pndvacc.getVaccine().getName().equalsIgnoreCase(nextvacc)){
					for (ArmDayReminder armday : child.getArm().getArmday()) {
						ReminderSms remsms=new ReminderSms();
						try{
							remsms.setCreator(sc.getUserService().getUser(userID));
							remsms.setCreatedByUserName(remsms.getCreatedByUserName()+":MOBILE");
						}catch (Exception e) {
							return XmlResponse.getErrorResponse("Error while finding user for request.");
						}
						remsms.setDayNumber(armday.getId().getDayNumber());
						/*String[] timestr=request.getParameter("time"+armday.getId().getDayNumber()).replaceAll(" ", "").split(":");
						int hr=Integer.parseInt(timestr[0]);
						int min=Integer.parseInt(timestr[1]);
						int sec=Integer.parseInt(timestr[2]);
						Time time=new Time(hr, min, sec);*/
						remsms.setDueTime(armday.getDefaultReminderTime());
						
						Calendar cal1=Calendar.getInstance();
						cal1.setTime(nextVaccRecord.getVaccinationDuedate());
						cal1.set(Calendar.HOUR_OF_DAY, remsms.getDueTime().getHours());
						cal1.set(Calendar.MINUTE, remsms.getDueTime().getMinutes());
						cal1.set(Calendar.SECOND, remsms.getDueTime().getSeconds());
						cal1.add(Calendar.DATE, armday.getId().getDayNumber());
						
						remsms.setDueDate(cal1.getTime());
						remsms.setChild(child);
						remsms.setStatus(REMINDER_STATUS.PENDING);
						remsms.setVaccinationRecordNum(Long.parseLong(nextid.toString()));
						try{
							sc.getReminderService().addReminderSmsRecord(remsms);
						}
						catch (Exception e) {
							e.printStackTrace();
							return XmlResponse.getErrorResponse("Some error occurred while adding reminders to next vaccination. Data may have become inconsistent. Contact your Program administrator.");
						}
					}
				}
				pndvacc.setNextVaccinationRecordNum(Long.parseLong(nextid.toString()));
				try{
				sc.getVaccinationService().updateVaccinationRecord(pndvacc);
				}
				catch (Exception e) {
					e.printStackTrace();
					return XmlResponse.getErrorResponse("Some error occurred while updating vaccination`s sucessor number. Data may have become inconsistent. Contact your Program administrator.");
				}
			}else if(pndvacc.getIsLastVaccination()){//if it is lastvaccination then set child has completed vaccinations
				child.setHasCompleted(true);
				child.setStatus(STATUS.COMPLETED);
				child.setDateOfCompletion(new Date());
				try {
					child.setLastEditor(sc.getUserService().getUser(userID));
					child.setLastEditedByUserName(child.getLastEditedByUserName()+":MOBILE");
				} catch (UserServiceException e) {
					e.printStackTrace();
				}
				sc.getChildService().updateChild(child);
			}
	
			//roll back if either of them is not saved properly or any exception occurs
			sc.commitTransaction();
			return XmlResponse.getSuccessResponse();
		}
		finally{
			sc.closeSession();
		}

	}
}
