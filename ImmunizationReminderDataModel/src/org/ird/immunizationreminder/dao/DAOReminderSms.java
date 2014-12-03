package org.ird.immunizationreminder.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.data.exception.ReminderDataException;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms.REMINDER_STATUS;

public interface DAOReminderSms extends DAO{

	/**
	 * @param id
	 * @param isreadonly
	 * @param collectionFetchMode
	 * @return
	 */
	ReminderSms findById(long id,boolean isreadonly,FetchMode collectionFetchMode);

	Number countCriteriaRows(String childId, String reminderName,
			String vaccineName, Date reminderDuedatesmaller,
			Date reminderDuedategreater, Date reminderSentdatesmaller,
			Date reminderSentdategreater, REMINDER_STATUS reminderStatus,
			boolean putNotWithReminderStatus) throws ReminderDataException;

	Number countAllRows();

	Number LAST_QUERY_TOTAL_ROW__COUNT();

	/**
	 * @param childId
	 * @param reminderName
	 * @param vaccineName
	 * @param reminderDuedatesmaller
	 * @param reminderDuedategreater
	 * @param reminderSentdatesmaller
	 * @param reminderSentdategreater
	 * @param reminderStatus
	 * @param putNotWithReminderStatus
	 * @param firstResult
	 * @param fetchsize
	 * @param isreadonly
	 * @param collectionFetchMode
	 * @return
	 * @throws ReminderDataException
	 */
	List<ReminderSms> findByCriteria(String childId, String reminderName,
			String vaccineName, Date reminderDuedatesmaller,
			Date reminderDuedategreater, Date reminderSentdatesmaller,
			Date reminderSentdategreater, REMINDER_STATUS reminderStatus,
			boolean putNotWithReminderStatus, int firstResult, int fetchsize,boolean isreadonly,FetchMode collectionFetchMode)
			throws ReminderDataException;

	/**
	 * @param firstResult
	 * @param fetchsize
	 * @param isreadonly
	 * @param collectionFetchMode
	 * @return
	 */
	List<ReminderSms> getAll(int firstResult, int fetchsize,boolean isreadonly,FetchMode collectionFetchMode);

	/**
	 * @param partOfName
	 * @param reminderName
	 * @param vaccineName
	 * @param reminderDuedatesmaller
	 * @param reminderDuedategreater
	 * @param reminderSentdatesmaller
	 * @param reminderSentdategreater
	 * @param cellnumber
	 * @param reminderStatus
	 * @param putNotWithReminderStatus
	 * @param armName
	 * @param firstResult
	 * @param fetchsize
	 * @param isreadonly
	 * @param collectionFetchMode
	 * @return
	 * @throws ReminderDataException
	 */
	List<ReminderSms> findByCriteriaIncludeName(String partOfName,
			String reminderName, String vaccineName,
			Date reminderDuedatesmaller, Date reminderDuedategreater,
			Date reminderSentdatesmaller, Date reminderSentdategreater,String cellnumber,
			REMINDER_STATUS reminderStatus, boolean putNotWithReminderStatus,
			String armName, int firstResult, int fetchsize,boolean isreadonly,FetchMode collectionFetchMode)
			throws ReminderDataException;

	Number countCriteriaIncludeNameRows(String partOfName, String reminderName,
			String vaccineName, Date reminderDuedatesmaller,
			Date reminderDuedategreater, Date reminderSentdatesmaller,
			Date reminderSentdategreater,String cellnumber, REMINDER_STATUS reminderStatus,
			boolean putNotWithReminderStatus, String armName)
			throws ReminderDataException;

	/**
	 * @param childId
	 * @param reminderName
	 * @param vaccineName
	 * @param reminderDuedatesmaller
	 * @param reminderDuedategreater
	 * @param reminderStatus
	 * @param putNotWithReminderStatus
	 * @param dayNumber
	 * @param vaccinationRecordNum
	 * @param isreadonly
	 * @param collectionFetchMode
	 * @return
	 * @throws ReminderDataException
	 */
	List<ReminderSms> findByCriteria(String childId, String reminderName,
			String vaccineName, Date reminderDuedatesmaller,
			Date reminderDuedategreater, REMINDER_STATUS reminderStatus,
			boolean putNotWithReminderStatus, String dayNumber,
			String vaccinationRecordNum,boolean isreadonly,FetchMode collectionFetchMode) throws ReminderDataException;

}
