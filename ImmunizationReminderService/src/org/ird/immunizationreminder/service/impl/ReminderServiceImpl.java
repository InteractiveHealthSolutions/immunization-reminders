package org.ird.immunizationreminder.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.dao.DAOReminder;
import org.ird.immunizationreminder.dao.DAOReminderSms;
import org.ird.immunizationreminder.data.exception.ReminderDataException;
import org.ird.immunizationreminder.datamodel.entities.Reminder;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms.REMINDER_STATUS;
import org.ird.immunizationreminder.service.ReminderService;
import org.ird.immunizationreminder.utils.date.DateUtils;

public class ReminderServiceImpl implements ReminderService{

	DAOReminder rdao;
	DAOReminderSms rsmsdao;
	private Number LAST_QUERY_TOTAL_ROW__COUNT;

	public ReminderServiceImpl(DAOReminder rem,DAOReminderSms rsmsdao){
		this.rdao=rem;
		this.rsmsdao=rsmsdao;
	}
	private void setLASTS_ROWS_RETURNED_COUNT(Number LAST_QUERY_TOTAL_ROW__COUNT) {
		this.LAST_QUERY_TOTAL_ROW__COUNT = LAST_QUERY_TOTAL_ROW__COUNT;
	}
	@Override
	public Number LAST_QUERY_TOTAL_ROW__COUNT() {
		return LAST_QUERY_TOTAL_ROW__COUNT;
	}
	@Override
	public Serializable addReminder(Reminder reminder) {
		return rdao.save(reminder);
	}

	@Override
	public List<Reminder> getAllReminders(int firstResult, int fetchsize,boolean isreadonly,FetchMode remTextFetchmode) {
		List<Reminder> rem=rdao.getAll(firstResult, fetchsize,isreadonly,remTextFetchmode);
		setLASTS_ROWS_RETURNED_COUNT(rdao.LAST_QUERY_TOTAL_ROW__COUNT());
		return rem;
	}
	@Override
	public Reminder getReminder(String reminderName,boolean isreadonly,FetchMode remTextFetchmode) {
		Reminder rem=rdao.getReminderByName(reminderName,isreadonly,remTextFetchmode);
		setLASTS_ROWS_RETURNED_COUNT(rem==null?0:1);
		return rem;
	}
	@Override
	public List<Reminder> findRemindersByName(String reminderName,int firstResult, int fetchsize,boolean isreadonly,FetchMode remTextFetchmode) {
		List<Reminder> reml= rdao.findReminder(reminderName, firstResult, fetchsize,isreadonly,remTextFetchmode);
		setLASTS_ROWS_RETURNED_COUNT(reml.size());
		return reml;
	}
	@Override
	public void updateReminder(Reminder reminder) {
		rdao.update(reminder);
	}

	@Override
	public List<ReminderSms> findReminderSmsRecordByCriteria(String childId,
						String reminderName, String vaccineName,
						Date reminderDuedatesmaller, Date reminderDuedategreater,
						Date reminderSentdatesmaller, Date reminderSentdategreater,
						REMINDER_STATUS reminderStatus, boolean putNotWithReminderStatus,
						int firstResult, int fetchsize,boolean isreadonly,FetchMode collectionFetchMode) throws ReminderDataException {
		if(reminderDuedatesmaller!=null){
			reminderDuedatesmaller=DateUtils.truncateDatetoDate(reminderDuedatesmaller);
		}
		if(reminderDuedategreater!=null){
			reminderDuedategreater=DateUtils.roundoffDatetoDate(reminderDuedategreater);
		}
		if(reminderSentdatesmaller!=null){
			reminderSentdatesmaller=DateUtils.truncateDatetoDate(reminderSentdatesmaller);
		}
		if(reminderSentdategreater!=null){
			reminderSentdategreater=DateUtils.roundoffDatetoDate(reminderSentdategreater);
		}
		List<ReminderSms> rsms=rsmsdao.findByCriteria(childId, reminderName, vaccineName
				, reminderDuedatesmaller, reminderDuedategreater, reminderSentdatesmaller
				, reminderSentdategreater, reminderStatus, putNotWithReminderStatus, firstResult
				, fetchsize,isreadonly,collectionFetchMode);
		setLASTS_ROWS_RETURNED_COUNT(rsmsdao.countCriteriaRows(childId, reminderName, vaccineName
				, reminderDuedatesmaller, reminderDuedategreater, reminderSentdatesmaller
				, reminderSentdategreater, reminderStatus, putNotWithReminderStatus));
		return rsms;
	}
	@Override
	public List<ReminderSms> findReminderSmsRecordByCriteriaIncludeName(
						String partOfName, String reminderName, String vaccineName,
						Date reminderDuedatesmaller, Date reminderDuedategreater,
						Date reminderSentdatesmaller, Date reminderSentdategreater,
						String cellnumber, REMINDER_STATUS reminderStatus,
						boolean putNotWithReminderStatus, String armName, int firstResult,
						int fetchsize,boolean isreadonly,FetchMode collectionFetchMode) throws ReminderDataException {
		if(reminderDuedatesmaller!=null){
			reminderDuedatesmaller=DateUtils.truncateDatetoDate(reminderDuedatesmaller);
		}
		if(reminderDuedategreater!=null){
			reminderDuedategreater=DateUtils.roundoffDatetoDate(reminderDuedategreater);
		}
		if(reminderSentdatesmaller!=null){
			reminderSentdatesmaller=DateUtils.truncateDatetoDate(reminderSentdatesmaller);
		}
		if(reminderSentdategreater!=null){
			reminderSentdategreater=DateUtils.roundoffDatetoDate(reminderSentdategreater);
		}
		if(cellnumber!=null){
			int cellln=Integer.parseInt(Context.getIRSetting("cellnumber.number-length-without-zero", "10"));
			cellnumber=cellln>cellnumber.length()?cellnumber:cellnumber.substring(cellnumber.length()-cellln);
		}
		List<ReminderSms> rsmsl=rsmsdao.findByCriteriaIncludeName(partOfName, reminderName
				, vaccineName, reminderDuedatesmaller, reminderDuedategreater, reminderSentdatesmaller
				, reminderSentdategreater, cellnumber, reminderStatus, putNotWithReminderStatus
				, armName, firstResult, fetchsize,isreadonly,collectionFetchMode);
		setLASTS_ROWS_RETURNED_COUNT(rsmsdao.countCriteriaIncludeNameRows(partOfName, reminderName
				, vaccineName, reminderDuedatesmaller, reminderDuedategreater, reminderSentdatesmaller
				, reminderSentdategreater, cellnumber, reminderStatus, putNotWithReminderStatus, armName));
		return rsmsl;		
	}
	@Override
	public List<ReminderSms> getAllReminderSmsRecord(int firstResult,int fetchsize,boolean isreadonly,FetchMode collectionFetchMode) {
		List<ReminderSms> rsmsl=rsmsdao.getAll(firstResult, fetchsize,isreadonly,collectionFetchMode);
		setLASTS_ROWS_RETURNED_COUNT(rsmsdao.countAllRows());
		return  rsmsl;
	}
	@Override
	public void updateReminderSmsRecord(ReminderSms reminderSms) {
		rsmsdao.update(reminderSms);
	}
	@Override
	public Serializable addReminderSmsRecord(ReminderSms reminderSms) {
		return rsmsdao.save(reminderSms);
	}
	@Override
	public List<ReminderSms> findByCriteria(String childId, String reminderName, String vaccineName
			, Date reminderDuedatesmaller, Date reminderDuedategreater, REMINDER_STATUS reminderStatus
			, boolean putNotWithReminderStatus,String dayNumber,String vaccinationRecordNum
			,boolean isreadonly,FetchMode collectionFetchMode) throws ReminderDataException{
		List<ReminderSms> rsmsl= rsmsdao.findByCriteria(childId, reminderName, vaccineName, 
				reminderDuedatesmaller, reminderDuedategreater, reminderStatus, putNotWithReminderStatus
				,dayNumber,vaccinationRecordNum,isreadonly,collectionFetchMode);
		setLASTS_ROWS_RETURNED_COUNT(rsmsl.size());
		return  rsmsl;
	}
//	@Override
//	public List<Child> getChildrenOnReminders(String reminderName) throws ReminderException {
//		List<ChildReminder> l=r.getChildReminderbyReminder(reminderName);
//		List<Child> p=new ArrayList<Child>();
//		for (ChildReminder pr : l) {
//			p.add(pr.getChild());
//		}
//		return p;
//	}
	@Override
	public ReminderSms getReminderSmsRecord(long id,boolean isreadonly,FetchMode collectionFetchMode) {
		ReminderSms r=rsmsdao.findById(id,isreadonly,collectionFetchMode);
		setLASTS_ROWS_RETURNED_COUNT(r==null?0:1);
		return r;
	}


//	@Override
//	public List<ReminderHistory> getReminderSent(Reminder reminder) {
//		List<ReminderHistory> remhis=rh.getReminders(reminder.getReminderId());
//		return remhis;
//	}
//
//	@Override
//	public List<ReminderHistory> getReminderSent(Date datesmall,Date dategreater) {
//		List<ReminderHistory> remhis=rh.getReminders(datesmall,dategreater);
//		return remhis;
//	}
//
//	@Override
//	public List<ReminderHistory> getReminderSentByChild(String childIdentifier, Date datesmall,Date dategreater) throws ChildDataException {
//		List<Child> p=h.getChildById(childIdentifier);
//		if(p.size()==0){
//			throw new ChildDataException(ChildDataException.CHILD_DOESNOT_EXIST,ChildDataException.CHILD_DOESNOT_EXIST);
//		}
//		Child pat=p.get(0);
//		List<ReminderHistory> remhis=rh.getReminders(pat.getpId(), datesmall,dategreater);
//		return remhis;
//
//	}
//
//	@Override
//	public List<ReminderHistory> getReminderSentByChild(String child,
//			Reminder reminder) {
//		// TODO implement the method
//		return null;
//	}
//
//	@Override
//	public List<ReminderHistory> getReminderSentByCell(String cellNum) {
//		String cell=cellNum;
//		try{
//		int clen=Integer.parseInt(Context.getIRSetting("cellnumber.number-length-without-zero", "10"));
//		cell=cellNum.substring(cellNum.length()-clen);
//		}catch (Exception e) {
//		}
//		List<ReminderHistory> remhis=rh.getRemindersByCellNum(cell);
//		return remhis;
//
//	}
//
//	@Override
//	public List<ReminderHistory> getReminderSentByCell(String cellNum, Date datesmall,Date dategreater) {
//		String cell=cellNum;
//		try{
//		int clen=Integer.parseInt(Context.getIRSetting("cellnumber.number-length-without-zero", "10"));
//		cell=cellNum.substring(cellNum.length()-clen);
//		}catch (Exception e) {
//		}
//		List<ReminderHistory> remhis=rh.getRemindersByCellNum(cell, datesmall,dategreater);
//		return remhis;
//	}
//
//	@Override
//	public List<ReminderHistory> getRemindersSentToChild(String child) {
//		List<ReminderHistory> remhis=rh.getReminders(child);
//		return remhis;
//	}


//	@Override
//	public void addChildReminder(ChildReminder reminder) {
//		r.addChildReminder(reminder);
//	}
//	@Override
//	public void deleteChildReminder(ChildReminder reminder) {
//		r.deleteChildReminder(reminder);
//	}
//	@Override
//	public List<ChildReminder> getAllChildReminderRecord() {
//		return r.getAllChildReminderRecord();
//	}
//	@Override
//	public List<ChildReminder> getChildReminder(String childIdentifier) {
//		return r.getChildReminder(childIdentifier);
//	}
//	@Override
//	public List<ChildReminder> getChildReminder(String childIdentifier,
//			String reminderName) {
//		return r.getChildReminder(childIdentifier, reminderName);
//	}
//	@Override
//	public List<ChildReminder> getChildReminderbyReminder(
//			String reminderName) {
//		return r.getChildReminderbyReminder(reminderName);
//	}
//	@Override
//	public void updateChildReminder(ChildReminder reminder) {
//		r.updateChildReminder(reminder);
//	}


}
