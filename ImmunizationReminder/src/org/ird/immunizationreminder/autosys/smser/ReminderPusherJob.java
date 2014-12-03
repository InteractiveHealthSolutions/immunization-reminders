package org.ird.immunizationreminder.autosys.smser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.ServiceContext;
import org.ird.immunizationreminder.datamodel.entities.ArmDayReminder;
import org.ird.immunizationreminder.datamodel.entities.Child;
import org.ird.immunizationreminder.datamodel.entities.Child.STATUS;
import org.ird.immunizationreminder.datamodel.entities.Reminder;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms.REMINDER_STATUS;
import org.ird.immunizationreminder.datamodel.entities.Vaccination;
import org.ird.immunizationreminder.datamodel.entities.Vaccination.VACCINATION_STATUS;
import org.ird.immunizationreminder.utils.Utils;
import org.ird.immunizationreminder.utils.reporting.ExceptionUtil;
import org.ird.immunizationreminder.web.utils.IMRGlobals;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.OutboundMessage.PeriodType;
import org.irdresearch.smstarseel.data.OutboundMessage.Priority;
import org.irdresearch.smstarseel.service.utils.DateUtils;
import org.irdresearch.smstarseel.service.utils.DateUtils.TIME_INTERVAL;

import com.mysql.jdbc.StringUtils;

public class ReminderPusherJob extends TimerTask
{
	@Override
	public void run() 
	{
		ServiceContext sc = Context.getServices();
		try{
			Calendar cal1 = Calendar.getInstance();// date before 10 years
			cal1.add(Calendar.YEAR, -10);
			cal1.set(Calendar.HOUR_OF_DAY, 0);
			cal1.set(Calendar.MINUTE, 0);
			cal1.set(Calendar.SECOND, 0);
			
			Calendar cal2 = Calendar.getInstance();
			cal2.set(Calendar.HOUR_OF_DAY, 23);// date of today
			cal2.set(Calendar.MINUTE, 59);
			cal2.set(Calendar.SECOND, 59);
			
			List<ReminderSms> remlist = sc.getReminderService().findReminderSmsRecordByCriteria(null, null, null, cal1.getTime(), cal2.getTime(),null, null, REMINDER_STATUS.PENDING, false, 0, Integer.MAX_VALUE, false, FetchMode.SELECT);
			
			System.out.println("Running Job: ReminderPusherJob "+new Date() + ". Fetched "+remlist.size()+" PENDING sms");
			
			for (ReminderSms rem : remlist) 
			{
				final long remDuedatemillis = rem.getDueDate().getTime();

				try{
					Vaccination pv = sc.getVaccinationService().getVaccinationRecord(rem.getVaccinationRecordNum(),false,FetchMode.SELECT,FetchMode.JOIN);
					Child child = rem.getChild();
					
					// if not under followup, CANCEL reminder
					if(child.getStatus().equals(STATUS.FOLLOW_UP))
					{
						// if vaccination status gets changed (not PENDING), CANCEL reminder.
						if(pv.getVaccinationStatus().equals(VACCINATION_STATUS.PENDING))
						{
							String text = pickupRandomReminderText(rem,	child, pv);
							rem.setText(text);

							if(DateUtils.differenceBetweenIntervals(new Date(), new Date(remDuedatemillis), TIME_INTERVAL.DATE) > 0
									&& (rem.getDayNumber() <= 0
											|| DateUtils.differenceBetweenIntervals(new Date(), pv.getVaccinationDuedate(),TIME_INTERVAL.DATE) > 10))
							{
								rem.setStatus(REMINDER_STATUS.MISSED);
								rem.setIsSmsLate(true);
								rem.setDayDifference(DateUtils.getDaysPassed(new Date(), new Date(remDuedatemillis)));
								rem.setHoursDifference(DateUtils.getDifferenceOfHours(new Date(), new Date(remDuedatemillis)));
								rem.setSmsCancelReason((rem.getSmsCancelReason()==null?"":rem.getSmsCancelReason())+"MISSED DAY.");
							}
							else {
								String recipient = child.getCurrentCellNo();
								
								PeriodType vp = PeriodType.DAY;
								int validityPerioddays = 1;
								
								if(rem.getDayNumber() > 0){
									//for reminders after duedate it should be 6 days else for less than due it should be only 1
									validityPerioddays = IMRGlobals.MAX_REMINDER_1_7_VALIDITY_DAYS;
								}

								if(recipient != null && recipient.trim().length() >= 3)
								{
									TarseelServices tsc = TarseelContext.getServices();
									String referenceNumber = "";
									try{
										referenceNumber  = tsc.getSmsService().createNewOutboundSms(recipient, text, rem.getDueDate(), Priority.HIGH, validityPerioddays, vp, IMRGlobals.SMS_TARSEEL_PROJECT_ID, null);
										tsc.commitTransaction();
									}
									finally{
										tsc.closeSession();
									}
									rem.setReferenceNumber(referenceNumber);
									rem.setStatus(REMINDER_STATUS.LOGGED);
								}
								else{
									rem.setCellnumber(recipient);
									rem.setStatus(REMINDER_STATUS.MISSED);
									rem.setSmsCancelReason((rem.getSmsCancelReason()==null?"":rem.getSmsCancelReason()) +"Invalid Cell Num.");
								}
							}
						}
						else{
							rem.setStatus(REMINDER_STATUS.CANCELLED);
							rem.setSmsCancelReason(
									(rem.getSmsCancelReason()==null?"":rem.getSmsCancelReason())
									+".VaccinationStatus "+pv.getVaccinationStatus()
									+":"+new Date()+".");
						}
					}
					else{
						rem.setStatus(REMINDER_STATUS.CANCELLED);
						rem.setSmsCancelReason(
								(rem.getSmsCancelReason()==null?"":rem.getSmsCancelReason())
								+".TreatmentStatus "+child.getStatus()
								+":"+new Date()+".");
					}

					sc.getReminderService().updateReminderSmsRecord(rem);
					sc.commitTransaction();
				}
				catch (Exception e) {
					e.printStackTrace();
					String msg = "Exception pushing reminder "+e.getMessage();
					
					try{
						EmailEngine.getInstance().emailErrorReportToAdminAsASeparateThread("Exception pushing reminder "+e.getMessage(), msg+":rsmsnum:"+rem.getRsmsRecordNum());
					}
					catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}//end for
		}
		catch (Exception e) {
			e.printStackTrace();
			String msg = "Exception running ReminderPusherJob "+e.getMessage();
			
			try{
				EmailEngine.getInstance().emailErrorReportToAdminAsASeparateThread("Exception running ReminderPusherJob "+e.getMessage(), msg+"\n"+ExceptionUtil.getStackTrace(e));
			}
			catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		finally{
			sc.closeSession();
		}
	}
	
	//change if vaccine thing is added and arm day reminders are provided on web interface
	private String pickupRandomReminderText(ReminderSms reminder,Child child,Vaccination pv){
		ServiceContext sc = Context.getServices();
		List<String> rt=new ArrayList<String>();
		String text = "";

		try{
			ArmDayReminder armdayr = null;
			for ( ArmDayReminder ardr : child.getArm().getArmday() ) {
				if(reminder.getDayNumber() == ardr.getId().getDayNumber()){
					armdayr = ardr;
					break;
				}
			}
			List<Reminder> rels = sc.getReminderService().getAllReminders( 0 , 20 , true , FetchMode.JOIN );
			Reminder rem = new Reminder();
			for ( Reminder reminder2 : rels ) {
				if(reminder2.getReminderId() == armdayr.getId().getReminderId()){
					rem = reminder2;
					break;
				}
			}
			reminder.setReminder(rem);
			
			
			for (String reminderText : reminder.getReminder().getReminderText()) {
				rt.add(reminderText);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		List<String> names=new ArrayList<String>();
		
		int num;
		try{
			num=Utils.getRandomNumber(rt.size());
		}
		catch (Exception e) {
			num=0;
		}
		
		try{
			text=rt.get((num>0&&num<=rt.size())?num-1:0);
		}
		catch (Exception e) {
			text=sc.getIRSettingService().getIrSetting("reminder.default-reminder-text").getValue();
		}
		Matcher matcher = Pattern.compile("\\[\\[\\w+\\.\\w+]\\]").matcher(text);
		
		String textToSend=""+text;
		try{
			while (matcher.find()) {
				names.add(text.substring(matcher.start(),matcher.end()));
			}		
			for (String nm : names) {
				if(nm.matches("\\[\\[Child\\.\\w+\\]\\]")){
					Class<Child> pcls;
					try {
						pcls = (Class<Child>) Class.forName("org.ird.immunizationreminder.datamodel.entities.Child");
						try {
							String fieldname=nm.replace("[[Child.","");
							fieldname=fieldname.replace("]]", "");
							Field[] aaa = pcls.getDeclaredFields();
							for (Field field : aaa) {
								if(field.getName().equalsIgnoreCase(fieldname)){
									field.setAccessible(true);
									textToSend=textToSend.replace(nm, (String)field.get(child));
								}
							}
						} catch (Exception e) {
							textToSend=textToSend.replace(nm, "");
						} 
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					}
				}
				else if(nm.matches("\\[\\[ReminderSms\\.\\w+\\]\\]")){
					Class<ReminderSms> pcls;
					try {
						pcls = (Class<ReminderSms>) Class.forName("org.ird.immunizationreminder.datamodel.entities.ReminderSms");
						try {
							String fieldname=nm.replace("[[ReminderSms.","");
							fieldname=fieldname.replace("]]", "-----");
							Field[] aaa = pcls.getDeclaredFields();
							for (Field field : aaa) {
								if(field.getName().equalsIgnoreCase(fieldname)){
									field.setAccessible(true);
									textToSend=textToSend.replace(nm, (String)field.get(reminder));
								}
							}
						} catch (Exception e) {
							textToSend=textToSend.replace(nm, "-----");
						} 
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					}
			  }
			  else if(nm.matches("\\[\\[Vaccination.Day]\\]")){
				  Calendar c=Calendar.getInstance();
				  c.setTime(pv.getVaccinationDuedate());
				  textToSend=textToSend.replace("[[Vaccination.Day]]", c.getDisplayName(Calendar.DAY_OF_WEEK,2,Locale.US));
			  }
			  else if(nm.matches("\\[\\[Vaccination.Date]\\]")){
				  Calendar c=Calendar.getInstance();
				  c.setTime(pv.getVaccinationDuedate());
				  textToSend=textToSend.replace("[[Vaccination.Date]]", c.getTime().toString());
			  }
		  }
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
		if(StringUtils.isEmptyOrWhitespaceOnly( textToSend )){
			textToSend=(String) Context.getIRSetting("reminder.default-reminder-text","Apnay bachay ko hifazati teeka lagwanay k liyay btaey gaey wqt pay markaz e sehat say rujoo karen.");
		}
		return textToSend;
	}
}
