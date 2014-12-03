package org.ird.immunizationreminder.autosys.smser;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.ServiceContext;
import org.ird.immunizationreminder.datamodel.entities.Child;
import org.ird.immunizationreminder.datamodel.entities.Child.STATUS;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms.REMINDER_STATUS;
import org.ird.immunizationreminder.datamodel.entities.Response;
import org.ird.immunizationreminder.datamodel.entities.Response.RESPONSE_TYPE;
import org.ird.immunizationreminder.datamodel.entities.Vaccination;
import org.ird.immunizationreminder.datamodel.entities.Vaccination.VACCINATION_STATUS;
import org.ird.immunizationreminder.utils.reporting.ExceptionUtil;
import org.ird.immunizationreminder.web.utils.IMRGlobals;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.InboundMessage;
import org.irdresearch.smstarseel.data.InboundMessage.InboundStatus;

public class ResponseReaderJob extends TimerTask{

	private static int MAX_CELL_NUMBER_MATCH_LENGTH = Integer.parseInt(
			Context.getIRSetting("cellnumber.number-length-without-zero","10")
			);
	
	@Override
	public void run() 
	{
		TarseelServices tsc = TarseelContext.getServices();

		try{
			List<InboundMessage> list = tsc.getSmsService().findInbound(null, null, InboundStatus.UNREAD, null, null, null, tsc.getDeviceService().findProjectById(IMRGlobals.SMS_TARSEEL_PROJECT_ID).getProjectId(), false);
			
			System.out.println("Running Job: ResponseReaderJob "+new Date() + ". Fetched "+list.size()+" UNREAD sms");

			for (InboundMessage ib : list) {
				ServiceContext sc = Context.getServices();
	
				try{
					String sender = ib.getOriginator();
					if(sender.length() > MAX_CELL_NUMBER_MATCH_LENGTH){
						sender = sender.substring(sender.length() - MAX_CELL_NUMBER_MATCH_LENGTH);
					}
					
					List<Child> p = sc.getChildService().findByCurrentCellIgnoreInconsistency( sender , false );
					Child chld = null;
					if ( p.size() > 0 ) {
						for (Child c : p) {
							chld = c;
							if(c.getStatus().equals(STATUS.FOLLOW_UP)){
								break;
							}
						}
					}
				
					if(chld != null){
						//boolean b=ReminderSystem.alertResponseRecieved(p,msg);//boolean send confirmation is returned i.e whether patient should b notified of response received confirmation
						Calendar cal = Calendar.getInstance();
						cal.setTime( new Date(System.currentTimeMillis()-1000*60*60*24*365L) );
						
						Calendar cal2 = Calendar.getInstance();
						cal2.setTime( new Date(System.currentTimeMillis()+1000*60*60*24*15L) );
					
						List<Vaccination> pv =sc.getVaccinationService().findVaccinationRecordByCriteria(chld.getChildId(), null, cal.getTime(), cal2.getTime(), null, null, VACCINATION_STATUS.PENDING, 0, Integer.MAX_VALUE-1,false,FetchMode.SELECT,FetchMode.SELECT);

						if(pv.size()==0){
							pv= sc.getVaccinationService().findVaccinationRecordByCriteria(chld.getChildId(), null, cal.getTime(), cal2.getTime(), null, null, null, 0, Integer.MAX_VALUE-1,false,FetchMode.SELECT,FetchMode.SELECT);
						}
					
						Vaccination vacc=pv.size() > 0 ? pv.get(0) : null;
	
						//ideally it will never happen
						if(vacc != null && vacc.getVaccinationDuedate()!=null){
							Calendar vaccinationduedateAtmorning=Calendar.getInstance();
							vaccinationduedateAtmorning.setTime(vacc.getVaccinationDuedate());
							vaccinationduedateAtmorning.set(Calendar.HOUR_OF_DAY, 8);
							vaccinationduedateAtmorning.set(Calendar.MINUTE, 8);
	
							Calendar now=Calendar.getInstance();
							
							if((vacc.getChildResponded() == null || !vacc.getChildResponded()) && now.getTime().compareTo(vaccinationduedateAtmorning.getTime())>0){
								vacc.setChildResponded(true);
								
								sc.getVaccinationService().updateVaccinationRecord(vacc);
								
								List<ReminderSms> rsml=sc.getReminderService()
												.findByCriteria(null,null,null,null,null,REMINDER_STATUS.PENDING
														,false,null,Long.toString(vacc.getVaccinationRecordNum()),false,FetchMode.SELECT);
								
								for (ReminderSms reminderSms : rsml) {
									reminderSms.setStatus(REMINDER_STATUS.CANCELLED);
									reminderSms.setSmsCancelReason((reminderSms.getSmsCancelReason()==null?"":reminderSms.getSmsCancelReason())+"Response recieved.");
									
									sc.getReminderService().updateReminderSmsRecord(reminderSms);
								}
							}
						}
						
						Response response=new Response();
						response.setCellNo(sender);
						response.setChild(chld);
						response.setResponseType(RESPONSE_TYPE.VERIFIED);
						response.setRecieveDate(ib.getRecieveDate());
						response.setVaccinationRecordNum(vacc.getVaccinationRecordNum());
						response.setRecieveTime(new Time( ib.getRecieveDate().getTime()));
						response.setActualSystemDate(new Date());
						response.setResponseText(ib.getText());
						
						sc.getResponseService().addResponseRecord(response);
						sc.commitTransaction();
					}

					tsc.getSmsService().markInboundAsRead(ib.getReferenceNumber());
				}
				catch (Exception e) {
					e.printStackTrace();
					EmailEngine.getInstance().emailErrorReportToAdmin(ib.getReferenceNumber()+ " not saved in IMR database", "A Response Record was not saved in IMR database" + ib.getReferenceNumber() + "\n" + e.getMessage());
				}
				finally{
					sc.closeSession();
				}
			}
			
			//without if it would throw exception transaction not successfully started
			if(list.size() > 0) tsc.commitTransaction();
		}
		catch (Exception e) {
			e.printStackTrace();
			String msg = "Exception running ResponseReaderJob "+e.getMessage();
			
			try{
				EmailEngine.getInstance().emailErrorReportToAdminAsASeparateThread("Exception running ResponseReaderJob "+e.getMessage(), msg+"\n"+ExceptionUtil.getStackTrace(e));
			}
			catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		finally{
			tsc.closeSession();
		}
	}
}
