package org.ird.immunizationreminder.web;

import java.util.ArrayList;
import java.util.List;

import org.ird.immunizationreminder.datamodel.entities.Child;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms.REMINDER_STATUS;
import org.ird.immunizationreminder.datamodel.entities.Response;
import org.ird.immunizationreminder.datamodel.entities.Vaccination;

public class APVRRAnalysisGridRow {

	private Child child;
	List<VaccinationRecord> record=new ArrayList<VaccinationRecord>();
	
	public Child getChild() {
		return child;
	}
	public void setChild(Child p) {
		child=p;
	}
	public  List<VaccinationRecord> getRecord() {
		return record;
	}
/*	public  void setRecord(List<VaccinationRecord> rec) {
		record=rec;
	}*/
	public  void addRecord(Vaccination pvacc,List<ReminderSms> rh,List<Response> pr) {
		record.add(new VaccinationRecord( pvacc,rh, pr));
	}
	public class VaccinationRecord{
		private List<ReminderSms> reminderSms;
		private List<Response> response;
		private Vaccination vaccination;
		private int remindersPending=0;
		private int unsentReminders = 0;
		private int cancelledReminder = 0;
		private int sentReminder = 0;
		private Boolean anyReminderLate = false;
		private Integer maxDaysLate = null;
		public VaccinationRecord(Vaccination pv,List<ReminderSms> rh,List<Response> pr){
			reminderSms=rh;
			response=pr;
			vaccination=pv;
			for (ReminderSms rem : rh) {
				if(rem.getReminderStatus().equals(REMINDER_STATUS.FAILED) 
						|| rem.getReminderStatus().equals(REMINDER_STATUS.MISSED)){
					unsentReminders++;
				}
				if(rem.getReminderStatus().equals(REMINDER_STATUS.CANCELLED) ){
					cancelledReminder++;
				}
				if(rem.getReminderStatus().equals(REMINDER_STATUS.SENT) ){
					sentReminder++;
				}
				if(rem.getReminderStatus().equals(REMINDER_STATUS.PENDING) ){
					remindersPending++;
				}
				if(rem.getIsSmsLate() != null 
						&& rem.getIsSmsLate().booleanValue()){
					anyReminderLate = true;
					
					if(maxDaysLate == null){
						maxDaysLate = rem.getDayDifference();
					}else{
						maxDaysLate = (rem.getDayDifference() != null
								&& rem.getDayDifference() > maxDaysLate) 
										? rem.getDayDifference() 
										: maxDaysLate;
					}
				}
			}
		}
			public List<ReminderSms> getReminderSms() {
			return reminderSms;
			}
			public List<Response> getResponse() {
				return response;
			}
/*			public void setReminderSms(List<ReminderSms> r) {
				reminderSms=r;
			}
			public void setResponse(List<Response> patResp) {
				response=patResp;
			}
			public void setVaccination(Vaccination pvacc) {
				this.pvacc = pvacc;
			}*/
			public Vaccination getVaccination() {
				return vaccination;
			}
			public void setRemindersPending(int remindersPending) {
				this.remindersPending = remindersPending;
			}
			public int getRemindersPending() {
				return remindersPending;
			}
			public void setAnyReminderLate(Boolean anyReminderLate) {
				this.anyReminderLate = anyReminderLate;
			}
			public Boolean getAnyReminderLate() {
				return anyReminderLate;
			}
			public void setMaxDaysLate(Integer maxDaysLate) {
				this.maxDaysLate = maxDaysLate;
			}
			public Integer getMaxDaysLate() {
				return maxDaysLate;
			}
			public void setUnsentReminders(int unsentReminders) {
				this.unsentReminders = unsentReminders;
			}
			public int getUnsentReminders() {
				return unsentReminders;
			}
			public void setCancelledReminder(int cancelledReminder) {
				this.cancelledReminder = cancelledReminder;
			}
			public int getCancelledReminder() {
				return cancelledReminder;
			}
			public void setSentReminder(int sentReminder) {
				this.sentReminder = sentReminder;
			}
			public int getSentReminder() {
				return sentReminder;
			}
	}
}