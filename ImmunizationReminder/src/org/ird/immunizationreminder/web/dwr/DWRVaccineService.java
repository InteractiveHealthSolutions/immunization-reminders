package org.ird.immunizationreminder.web.dwr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.management.InvalidAttributeValueException;

import org.directwebremoting.WebContextFactory;
import org.hibernate.FetchMode;
import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.LoggedInUser;
import org.ird.immunizationreminder.context.ServiceContext;
import org.ird.immunizationreminder.data.exception.VaccinationDataException;
import org.ird.immunizationreminder.datamodel.entities.Vaccination;
import org.ird.immunizationreminder.datamodel.entities.Vaccine;
import org.ird.immunizationreminder.datamodel.entities.Vaccine.UNIT_GAP;
import org.ird.immunizationreminder.utils.reporting.ExceptionUtil;
import org.ird.immunizationreminder.utils.reporting.LoggerUtil;
import org.ird.immunizationreminder.web.utils.IMRGlobals;
import org.ird.immunizationreminder.web.utils.IRUtils;
import org.ird.immunizationreminder.web.utils.IRUtils.VACCINATION_GAP;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;

public class DWRVaccineService {
	List<String> errorMessages=new ArrayList<String>();
	String errorMessage;
	public List<String> getErrorMessages() {
		return errorMessages;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public String hasPatientTakenVaccine(String childId,String vaccineName){
		LoggedInUser user=UserSessionUtils.getActiveUser(WebContextFactory.get().getHttpServletRequest());
		if(user==null){////if session has expired then it doesnot matter what ever result returned as user will be redirected to login page
			return "Session expired. Logout from application and login again";
		}
		ServiceContext sc = Context.getServices();
		try {
			List<Vaccination> vl = sc.getVaccinationService().findVaccinationRecordByCriteria(childId, vaccineName, null, null
					, null, null, null, 0, 12,true,FetchMode.SELECT,FetchMode.SELECT);
			return vl.size()==0?"no":Long.toString(vl.get(0).getVaccinationRecordNum());
		} catch (VaccinationDataException e) {
			return e.getMessage();
		} catch (Exception e) {
			LoggerUtil.logIt(ExceptionUtil.getStackTrace(e));
			return e.getMessage();
		}finally{
			sc.closeSession();
		}
	}
	
	public String calculateNextVaccinationDateFromPrev(Date vaccinationDate,String vaccineName,boolean use_default_if_gap_missing) throws InvalidAttributeValueException{
		SimpleDateFormat df = new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT);
		LoggedInUser user=UserSessionUtils.getActiveUser(WebContextFactory.get().getHttpServletRequest());
		if(user==null){////if session has expired then it doesnot matter what ever result returned as user will be redirected to login page
			return "Session expired. Logout from application and login again";
		}
		ServiceContext sc = Context.getServices();

		Vaccine vaccine = sc.getVaccinationService().getByName(vaccineName);
		sc.closeSession();

		return df.format(IRUtils.calcNextVaccDuedate(vaccinationDate, vaccine, VACCINATION_GAP.USE_GAP_FROM_PREVIOUS_VACCINE_FIELD, use_default_if_gap_missing));
	}
	
	public String calculateMeasles2DateFromMeasles1(Date birthdate, Date measles1date) 
	{
		SimpleDateFormat df = new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT);

		LoggedInUser user=UserSessionUtils.getActiveUser(WebContextFactory.get().getHttpServletRequest());
		if(user==null){////if session has expired then it doesnot matter what ever result returned as user will be redirected to login page
			return "Session expired. Logout from application and login again";
		}
		
		Calendar actNextAssignedDate=Calendar.getInstance();
		actNextAssignedDate.setTime(birthdate);
		
		actNextAssignedDate.add(Calendar.MONTH, 15);

		Calendar m1c = Calendar.getInstance();
		m1c.setTime(new Date(measles1date.getTime()));
		m1c.add(Calendar.MONTH, 3);

		if(m1c.after(actNextAssignedDate)){
			actNextAssignedDate = m1c;
		}
		

		/*if(actNextAssignedDate.get(Calendar.DAY_OF_WEEK) <= Calendar.WEDNESDAY){//<4
			actNextAssignedDate.add(Calendar.DATE, Calendar.WEDNESDAY - actNextAssignedDate.get(Calendar.DAY_OF_WEEK));
		}
		else */
		
		if(actNextAssignedDate.get(Calendar.DAY_OF_WEEK) <= Calendar.SATURDAY){//<7
			actNextAssignedDate.add(Calendar.DATE, Calendar.SATURDAY - actNextAssignedDate.get(Calendar.DAY_OF_WEEK));
		}
		
		return df.format(actNextAssignedDate.getTime());
	}
	
	public String addVaccine(String vaccinename,int vaccinenumInform,String vaccinenameInform
			,String description,int gapFromPrev,String prevGapUnit,int gapToNext,String nextGapUnit) {
		LoggedInUser user=UserSessionUtils.getActiveUser(WebContextFactory.get().getHttpServletRequest());
		if(user==null){
			return "Session expired. Logout from application and login again";
		}
		ServiceContext sc = Context.getServices();

		Vaccine vacc=new Vaccine();
		vacc.setName(vaccinename);
		vacc.setVaccineNameInForm(vaccinenameInform);
		vacc.setVaccineNumberInForm(vaccinenumInform);
		vacc.setDescription(description);
		vacc.setGapInWeeksFromPreviousVaccine(gapFromPrev);
		vacc.setUnitPrevGap(UNIT_GAP.valueOf(prevGapUnit));
		vacc.setGapInWeeksToNextVaccine(gapToNext);
		vacc.setUnitNextGap(UNIT_GAP.valueOf(nextGapUnit));
		vacc.setCreator(user.getUser());
		try{
			sc.getVaccinationService().addVaccine(vacc);
			sc.commitTransaction();
		}catch (Exception e) {
			return e.getMessage();
		}
		sc.closeSession();
		return "Vaccine added successfully";
	}
	public String addVaccine2(String vaccinename,int vaccinenumInform,String vaccinenameInform
			,String description,int gapFromPrev,UNIT_GAP prevGapUnit,int gapToNext,UNIT_GAP nextGapUnit) {
		return addVaccine(vaccinename, vaccinenumInform, vaccinenameInform, description, gapFromPrev, prevGapUnit.name(), gapToNext, nextGapUnit.name());
	}
}
