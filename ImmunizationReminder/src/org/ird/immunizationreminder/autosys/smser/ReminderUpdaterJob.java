package org.ird.immunizationreminder.autosys.smser;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.ServiceContext;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms.REMINDER_STATUS;
import org.ird.immunizationreminder.web.utils.IMRGlobals;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.OutboundMessage;
import org.irdresearch.smstarseel.data.OutboundMessage.OutboundStatus;
import org.irdresearch.smstarseel.service.utils.DateUtils;
import org.irdresearch.smstarseel.service.utils.DateUtils.TIME_INTERVAL;

public class ReminderUpdaterJob extends TimerTask {

	public void run() 
	{
		ServiceContext sc = Context.getServices();
		
		try{
			List<ReminderSms> remlist = sc.getReminderService().findReminderSmsRecordByCriteria(null, null, null, null, null, null , null, REMINDER_STATUS.LOGGED, false, 0, Integer.MAX_VALUE, false, FetchMode.SELECT);
			
			System.out.println("Running Job: ReminderUpdaterJob "+new Date() + ". Fetched "+remlist.size()+" LOGGED sms");

			for (ReminderSms rem : remlist) 
			{
				TarseelServices tsc = TarseelContext.getServices();
				try{
					OutboundMessage om = tsc.getSmsService().findOutboundMessageByReferenceNumber(rem.getReferenceNumber(), true);
					
					if(!om.getStatus().equals(OutboundStatus.PENDING)
							&& !om.getStatus().equals(OutboundStatus.UNKNOWN))
					{
						if(om.getStatus().equals(OutboundStatus.FAILED)){
							final long currentTime = System.currentTimeMillis();
							final long maxValidTime = om.getDueDate().getTime()+(om.getValidityPeriod()  * 60 * 60 * 1000L);
							final int hoursLeft = (int) ((maxValidTime - currentTime)/(1000 * 60 * 60L));
							final boolean retry = hoursLeft >= 1 && 
									(DateUtils.differenceBetweenIntervals(new Date(), om.getSystemProcessingEndDate(), TIME_INTERVAL.HOUR) >= 4 
											|| hoursLeft-(IMRGlobals.REMINDER_PUSHER_TIMER_INTERVAL_MIN/60) < 1);
							if(hoursLeft <= 0 || !retry){
								rem.setStatus(REMINDER_STATUS.FAILED);
								rem.setSmsCancelReason((rem.getSmsCancelReason()==null?"":rem.getSmsCancelReason())+om.getErrormessage());
							}
							else {
								rem.setStatus(REMINDER_STATUS.PENDING);
								rem.setSmsCancelReason((rem.getSmsCancelReason()==null?"":rem.getSmsCancelReason())+om.getReferenceNumber()+"FAILED.");
							}
						}
						else{
							if(om.getStatus().equals(OutboundStatus.SENT)){
								rem.setStatus(REMINDER_STATUS.SENT);
							}
							else {
								rem.setStatus(REMINDER_STATUS.MISSED);
								rem.setSmsCancelReason((rem.getSmsCancelReason()==null?"":rem.getSmsCancelReason())+om.getFailureCause());
							}
							
							rem.setSentDate(om.getSentdate());
						}
						
						if(om.getSentdate() != null){
							final long duedt = om.getDueDate().getTime();
							final long sntdt = om.getSentdate().getTime();
							
							rem.setDayDifference(DateUtils.differenceBetweenIntervals(new Date(sntdt), new Date(duedt), TIME_INTERVAL.DATE));
							rem.setHoursDifference(DateUtils.differenceBetweenIntervals(new Date(sntdt), new Date(duedt), TIME_INTERVAL.HOUR));
							rem.setIsSmsLate(DateUtils.differenceBetweenIntervals(new Date(sntdt), new Date(duedt), TIME_INTERVAL.DATE) > 0);
						}
						else {
							rem.setIsSmsLate(true);
						}
						
						sc.getReminderService().updateReminderSmsRecord(rem);
					}//end if
					
				}
				catch(Exception e){
					e.printStackTrace();
				}
				finally{
					tsc.closeSession();
				}
			}//end for
			
			sc.commitTransaction();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}
}
