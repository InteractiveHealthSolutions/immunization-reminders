package org.ird.immunizationreminder.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.data.exception.ReminderDataException;
import org.ird.immunizationreminder.datamodel.entities.Reminder;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms.REMINDER_STATUS;

public interface ReminderService {

	List<Reminder> getAllReminders(int firstResult, int fetchsize,boolean isreadonly,FetchMode remTextFetchmode);
	
	Reminder getReminder(String reminderName,boolean isreadonly,FetchMode remTextFetchmode);
	
	List<Reminder> findRemindersByName(String reminderName,int firstResult, int fetchsize,boolean isreadonly,FetchMode remTextFetchmode);
	
	Serializable addReminder(Reminder reminder);
	
	void updateReminder(Reminder reminder);

	Number LAST_QUERY_TOTAL_ROW__COUNT();
	
	List<ReminderSms> findReminderSmsRecordByCriteria(String childId, String reminderName,
			String vaccineName, Date reminderDuedatesmaller,
			Date reminderDuedategreater, Date reminderSentdatesmaller,
			Date reminderSentdategreater, REMINDER_STATUS reminderStatus,
			boolean putNotWithReminderStatus, int firstResult, int fetchsize,boolean isreadonly,FetchMode collectionFetchMode)
			throws ReminderDataException;

	List<ReminderSms> getAllReminderSmsRecord(int firstResult, int fetchsize,boolean isreadonly,FetchMode collectionFetchMode);

	List<ReminderSms> findReminderSmsRecordByCriteriaIncludeName(String partOfName,
			String reminderName, String vaccineName,
			Date reminderDuedatesmaller, Date reminderDuedategreater,
			Date reminderSentdatesmaller, Date reminderSentdategreater,String cellnumber,
			REMINDER_STATUS reminderStatus, boolean putNotWithReminderStatus,
			String armName, int firstResult, int fetchsize,boolean isreadonly,FetchMode collectionFetchMode)
			throws ReminderDataException;
	
	Serializable addReminderSmsRecord(ReminderSms reminderSms);
	
	void updateReminderSmsRecord(ReminderSms reminderSms);

	List<ReminderSms> findByCriteria(String childId, String reminderName,
			String vaccineName, Date reminderDuedatesmaller,
			Date reminderDuedategreater, REMINDER_STATUS reminderStatus,
			boolean putNotWithReminderStatus, String dayNumber,
			String vaccinationRecordNum,boolean isreadonly,FetchMode collectionFetchMode) throws ReminderDataException;

	ReminderSms getReminderSmsRecord(long id,boolean isreadonly,FetchMode collectionFetchMode);
//	public List<ReminderHistory> getAllReminderHistory();
	
//	public List<Child> getChildrenOnReminders(String reminderName) throws ReminderException;
	
//	public List<ReminderHistory> getRemindersSentToChild(String childIdentifier);
	
//	public List<ReminderHistory> getReminderSent(Reminder reminder);
	
//	public List<ReminderHistory> getReminderSent(Date datesmall,Date dategreater);
	
//	public List<ReminderHistory> getReminderSent(String childIdentifier,String cell) throws ChildDataException;
	
//	public List<ReminderHistory> getReminderSent(String childIdentifier,String cell,Date datesmall,Date dategreater) throws ChildDataException;
	
//	public List<ReminderHistory> getReminderSentByChild(String childIdentifier, Date datesmall,Date dategreater) throws ChildDataException;
//	
//	public List<ReminderHistory> getReminderSentByChild(String childIdentifier,Reminder reminder);
//	
//	public List<ReminderHistory> getReminderSentByCell(String cellNum);
//	
//	public List<ReminderHistory> getReminderSentByCell(String cellNum,Date datesmall,Date dategreater);
	

	
//	public List<ChildReminder> getAllChildReminderRecord();
//	
//	public List<ChildReminder> getChildReminder(String childIdentifier);
//	
//	public List<ChildReminder> getChildReminder(String childIdentifier,String reminderName);
//	
//	public List<ChildReminder> getChildReminderbyReminder(String reminderName);
	
//	public void addChildReminder(ChildReminder reminder);
//	
//	public void deleteChildReminder(ChildReminder reminder);
//	
//	public void updateChildReminder(ChildReminder reminder);
//	
//	public void addReminderHistory(ReminderHistory record);
	
//	public void updateReminderText(Reminder reminder,List<String> remText);

}
