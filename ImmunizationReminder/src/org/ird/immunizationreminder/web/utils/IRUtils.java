package org.ird.immunizationreminder.web.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.management.InvalidAttributeValueException;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.ServiceContext;
import org.ird.immunizationreminder.data.exception.ReminderDataException;
import org.ird.immunizationreminder.data.exception.VaccinationDataException;
import org.ird.immunizationreminder.datamodel.entities.Child;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms;
import org.ird.immunizationreminder.datamodel.entities.Response;
import org.ird.immunizationreminder.datamodel.entities.Vaccination;
import org.ird.immunizationreminder.datamodel.entities.Vaccine;
import org.ird.immunizationreminder.datamodel.entities.Vaccine.UNIT_GAP;
import org.ird.immunizationreminder.web.APVRRAnalysisGridRow;

public class IRUtils {
	
	private static int DEFAULT_GAP=1;
	private static UNIT_GAP DEFAULT_GAP_Unit=UNIT_GAP.MONTH;

	public static String ERROR_MESSAGE=null;
	public static String WARNING_MESSAGE=null;
	
	public enum VACCINATION_GAP{
		USE_GAP_FROM_PREVIOUS_VACCINE_FIELD,
		USE_GAP_TO_NEXT_VACCINE_FIELD,
		CALCULATE_DATE_ANYWAY_USE_DEFAULT
	}
	/** 
	 * method assumes that vacciationDate and vaccine are not null. test values for null else 
	 * method will throw nullPointerException
	 * 
	 * @param vaccinationDate
	 * @param vaccine
	 * @return
	 * @throws InvalidAttributeValueException 
	 */
	public static Date calcNextVaccDuedate(Date vaccinationDate,Vaccine vaccine
					,VACCINATION_GAP gapType,boolean use_default_if_field_missing) throws InvalidAttributeValueException{
		ERROR_MESSAGE=null;
		WARNING_MESSAGE=null;
		
		Calendar actNextAssignedDate=Calendar.getInstance();
		final long vaccDatel= vaccinationDate.getTime();
		actNextAssignedDate.setTime(new Date(vaccDatel));
		
		int gaptovvac = 0;
		UNIT_GAP unit = null;
		
		if(gapType.compareTo(VACCINATION_GAP.USE_GAP_FROM_PREVIOUS_VACCINE_FIELD)==0){
			gaptovvac=vaccine.getGapInWeeksFromPreviousVaccine();
			unit=vaccine.getUnitPrevGap();
			if(gaptovvac <= 0){
				if(use_default_if_field_missing){
					WARNING_MESSAGE="\ngiven vaccine is not specified to be given at this point in study. " +
							"however assigned default value '1 Month' as gap to next vaccine";
					gaptovvac=DEFAULT_GAP;
					unit=DEFAULT_GAP_Unit;
				}
				else{
					throw new InvalidAttributeValueException("GAP_FROM_PREVIOUS_VACCINE for given vaccine is not specified.");
				}
			}
		}
		if(gapType.compareTo(VACCINATION_GAP.USE_GAP_TO_NEXT_VACCINE_FIELD)==0){
			gaptovvac=vaccine.getGapInWeeksToNextVaccine();
			unit=vaccine.getUnitNextGap();
			if(gaptovvac <= 0){
				if(use_default_if_field_missing){
					WARNING_MESSAGE="\ngiven vaccine is not specified to be given at this point in study. " +
							"however assigned default value '1 Month' as gap to next vaccine";
					gaptovvac=DEFAULT_GAP;
					unit=DEFAULT_GAP_Unit;
				}
				else{
					throw new InvalidAttributeValueException("GAP_TO_NEXT_VACCINE for given vaccine is not specified.");
				}
			}
		}
		
		if(unit.equals(UNIT_GAP.DAY)){
			actNextAssignedDate.add(Calendar.DATE, gaptovvac);

		}else if(unit.equals(UNIT_GAP.WEEK)){
			actNextAssignedDate.add(Calendar.DATE, gaptovvac*7);

		}else if(unit.equals(UNIT_GAP.MONTH)){
			actNextAssignedDate.add(Calendar.MONTH, gaptovvac);

		}else if(unit.equals(UNIT_GAP.YEAR)){
			actNextAssignedDate.add(Calendar.YEAR, gaptovvac);
		}
			
		if(vaccine.getName().toLowerCase().contains("measles")){
			/*if(actNextAssignedDate.get(Calendar.DAY_OF_WEEK) <= Calendar.WEDNESDAY){//<4
				actNextAssignedDate.add(Calendar.DATE, Calendar.WEDNESDAY - actNextAssignedDate.get(Calendar.DAY_OF_WEEK));
			}
			else */
			
			if(actNextAssignedDate.get(Calendar.DAY_OF_WEEK) <= Calendar.SATURDAY){//<7
				actNextAssignedDate.add(Calendar.DATE, Calendar.SATURDAY - actNextAssignedDate.get(Calendar.DAY_OF_WEEK));
			}
		}
		else{
			if (actNextAssignedDate.get( Calendar.DAY_OF_WEEK ) == Calendar.SUNDAY) {
				actNextAssignedDate.add( Calendar.DATE , 1 );
			}
		}
		return actNextAssignedDate.getTime();
	}
	public static Date getMeasles1Date(Date DOB){
		final long doblong = DOB.getTime();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(doblong));
		cal.add(Calendar.MONTH, 9);
		//donot change check position
		/*if(cal.get(Calendar.DAY_OF_WEEK) <= Calendar.WEDNESDAY){//<4
			cal.add(Calendar.DATE, Calendar.WEDNESDAY - cal.get(Calendar.DAY_OF_WEEK));
		}
		else */
		
		if(cal.get(Calendar.DAY_OF_WEEK) <= Calendar.SATURDAY){//<7
			cal.add(Calendar.DATE, Calendar.SATURDAY - cal.get(Calendar.DAY_OF_WEEK));
		}
		return cal.getTime();
	}
	/*public static Date getMeasles2Date(Date measles1VaccinationDate){
		ServiceContext sc = Context.getServices();
		final long datelong = measles1VaccinationDate.getTime();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(datelong));
		Vaccine v = sc.getVaccinationService().getByName("measles2");
		int gap = v.getGapInWeeksFromPreviousVaccine();
		if(v.getUnitPrevGap() == UNIT_GAP.DAY){
			cal.add(Calendar.DATE, gap);
		}
		else if(v.getUnitPrevGap() == UNIT_GAP.WEEK){
			cal.add(Calendar.DATE, gap * 7);
		}
		else if(v.getUnitPrevGap() == UNIT_GAP.MONTH){
			cal.add(Calendar.MONTH, gap);
		}
		else if(v.getUnitPrevGap() == UNIT_GAP.YEAR){
			cal.add(Calendar.YEAR, gap);
		}
		//donot change check position
		if(cal.get(Calendar.DAY_OF_WEEK) <= Calendar.WEDNESDAY){//<4
			cal.add(Calendar.DATE, Calendar.WEDNESDAY - cal.get(Calendar.DAY_OF_WEEK));
		}
		else if(cal.get(Calendar.DAY_OF_WEEK) <= Calendar.SATURDAY){//<7
			cal.add(Calendar.DATE, Calendar.SATURDAY - cal.get(Calendar.DAY_OF_WEEK));
		}
		return cal.getTime();
	}*/
	
	/** reminder history and child response data must be in descending order by date (latest date being first one)
	 *	All logic is dealing with readonly param of hibernate hence data got from here is uneditable
	 * if done then changes will be lost.
	 * @throws VaccinationDataException 
	 * @throws ReminderDataException */
	public static APVRRAnalysisGridRow addRecord(Child child,Date vaccinationDuedate1,Date vaccinationDuedate2) throws VaccinationDataException, ReminderDataException {
		APVRRAnalysisGridRow recordGrid=new APVRRAnalysisGridRow();
		recordGrid.setChild(child);
		ServiceContext sc = Context.getServices();
		try{
		List<Vaccination> vaccl=sc.getVaccinationService().findVaccinationRecordByCriteria(child.getChildId(), null, vaccinationDuedate1, vaccinationDuedate2, null, null, null, 0, Integer.MAX_VALUE-1 , true , FetchMode.JOIN , FetchMode.JOIN);
		
		for (Vaccination pv : vaccl) {
			//All reminder sms' on this vacc record
			List<ReminderSms> reml = sc.getReminderService().findByCriteria(null, null, null, null, null, null, false, null, Long.toString(pv.getVaccinationRecordNum()), true , FetchMode.SELECT);

			if(reml==null){
				reml=new ArrayList<ReminderSms>();
			}
/*			int mindaynum=0;
			int maxdaynum=0;

			Vaccination previous=service.getVaccinationService().getVaccinationRecord(pv.getPreviousVaccinationRecordNum());
			Vaccination next=service.getVaccinationService().getVaccinationRecord(pv.getNextVaccinationRecordNum());
	
			if(!StringUtils.isEmptyOrWhitespaceOnly(child.getArm().getReminderDaysSequence())){
				mindaynum=Integer.parseInt(child.getArm().getReminderDaysSequence().substring(0,
						child.getArm().getReminderDaysSequence().indexOf(",")));
				maxdaynum=Integer.parseInt(child.getArm().getReminderDaysSequence().substring(
						child.getArm().getReminderDaysSequence().lastIndexOf(",")+1));
			}	
			Calendar prevc=Calendar.getInstance();
			if(previous==null){//means it is first vaccination
				prevc.setTime(new Date());
			}else if(previous.getVaccinationDuedate()==null){
				prevc.setTime(previous.getVaccinationDate());//take vaccination date instead
				prevc.add(Calendar.DATE, maxdaynum);
			}else{
				prevc.setTime(previous.getVaccinationDuedate());
				prevc.add(Calendar.DATE, maxdaynum);
			}
			Calendar thisonec=Calendar.getInstance();
			thisonec.setTime(pv.getVaccinationDuedate()==null?new Date():pv.getVaccinationDuedate());
			Calendar nextc=Calendar.getInstance();
			if(next==null){//means it is first vaccination
				nextc.setTime(new Date());
			}else if(next.getVaccinationDuedate()==null){
				nextc.setTime(next.getVaccinationDate());//take vaccination date instead
				nextc.add(Calendar.DATE, mindaynum);
			}else{
				nextc.setTime(next.getVaccinationDuedate());
				nextc.add(Calendar.DATE, mindaynum);
			}			
			
			thisonec.add(Calendar.DATE, mindaynum);
			
			long numOfDaysToadd=((thisonec.getTimeInMillis()-prevc.getTimeInMillis())/(1000*60*60*24))/2;
			
			thisonec.add(Calendar.DATE, -Math.abs((int)numOfDaysToadd));
			Date date1=thisonec.getTime();
			
			numOfDaysToadd=((nextc.getTimeInMillis()-thisonec.getTimeInMillis())/(1000*60*60*24))/2;

			thisonec.add(Calendar.DATE, Math.abs((int)numOfDaysToadd));
			Date date2=thisonec.getTime();*/
			
			List<Response> pr = sc.getResponseService().findByVaccinationRecord(pv.getVaccinationRecordNum(), true , FetchMode.SELECT);
			if(pr==null){
				pr=new ArrayList<Response>();
			}
			recordGrid.addRecord(pv, reml, pr);
		}
		}
		finally{
			sc.closeSession();
		}
		
		return recordGrid;
	}
	/** reminder history and child response data must be in descending order by date (latest date being first one)
	 * @throws VaccinationDataException 
	 * @throws ReminderDataException */
	public static APVRRAnalysisGridRow addRecord(Child child) throws VaccinationDataException, ReminderDataException {
		APVRRAnalysisGridRow recordGrid=new APVRRAnalysisGridRow();
		recordGrid.setChild(child);
		
		ServiceContext sc = Context.getServices();
		try{
		List<Vaccination> vaccl=sc.getVaccinationService().findVaccinationRecordByCriteria(child.getChildId(), null, null, null, null, null, null, 0, Integer.MAX_VALUE-1, true , FetchMode.JOIN , FetchMode.SELECT);
		for (Vaccination pv : vaccl) {
			List<ReminderSms> reml = sc.getReminderService().findByCriteria(null, null, null, null, null, null, false, null, Long.toString(pv.getVaccinationRecordNum()), true , FetchMode.SELECT);
			
			if(reml==null){
				reml=new ArrayList<ReminderSms>();
			}
			
			List<Response> pr = sc.getResponseService().findByVaccinationRecord(pv.getVaccinationRecordNum(), true , FetchMode.SELECT);
			if(pr==null){
				pr=new ArrayList<Response>();
			}
			
			recordGrid.addRecord(pv, reml, pr);
		}
		}
		finally{
		sc.closeSession();
		}
		return recordGrid;
	}

}
