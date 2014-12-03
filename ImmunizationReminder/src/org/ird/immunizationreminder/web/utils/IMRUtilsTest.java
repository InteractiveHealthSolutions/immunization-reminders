package org.ird.immunizationreminder.web.utils;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import javax.management.InvalidAttributeValueException;

import org.ird.immunizationreminder.datamodel.entities.Vaccine;
import org.ird.immunizationreminder.datamodel.entities.Vaccine.UNIT_GAP;
import org.ird.immunizationreminder.web.utils.IRUtils.VACCINATION_GAP;
import org.junit.Test;

public class IMRUtilsTest {
	
	@Test public void calcNextVaccDuedateFromPreviousTest(){
		Vaccine vacc=new Vaccine();
		vacc.setGapInWeeksFromPreviousVaccine(1);
		vacc.setGapInWeeksToNextVaccine(1);
		vacc.setUnitNextGap(UNIT_GAP.MONTH);
		vacc.setUnitPrevGap(UNIT_GAP.MONTH);

		for (int i = 0; i < 15; i++) {
			Calendar vaccinationDate=Calendar.getInstance();
			vaccinationDate.set(Calendar.DATE, 12);
			vaccinationDate.set(Calendar.MONTH, i);
			vaccinationDate.set(Calendar.YEAR, 2011);
			vaccinationDate.set(Calendar.HOUR_OF_DAY, 12);
			vaccinationDate.set(Calendar.MINUTE, 34);
			vaccinationDate.set(Calendar.SECOND, 44);
			vaccinationDate.set(Calendar.MILLISECOND, 444);

			Calendar expecteddate=Calendar.getInstance();
			expecteddate.set(Calendar.DATE, 12);
			expecteddate.set(Calendar.MONTH, i+1);
			expecteddate.set(Calendar.YEAR, 2011);
			expecteddate.set(Calendar.HOUR_OF_DAY, 12);
			expecteddate.set(Calendar.MINUTE, 34);
			expecteddate.set(Calendar.SECOND, 44);
			expecteddate.set(Calendar.MILLISECOND, 444);
			if(expecteddate.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
				expecteddate.add(Calendar.DATE, 1);
			}
			
			try {
				System.out.println("i:"+i
						+"\n"
						+expecteddate.getTime()+
						"\n"
						+IRUtils.calcNextVaccDuedate
						(vaccinationDate.getTime(), vacc, VACCINATION_GAP.USE_GAP_FROM_PREVIOUS_VACCINE_FIELD, false)
						+":::::::::::::::::::::::::::::::::::::\n");
				assertEquals("not equal i:"+i,expecteddate.getTime(), IRUtils.calcNextVaccDuedate
						(vaccinationDate.getTime(), vacc, VACCINATION_GAP.USE_GAP_FROM_PREVIOUS_VACCINE_FIELD, false));
			} catch (InvalidAttributeValueException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
