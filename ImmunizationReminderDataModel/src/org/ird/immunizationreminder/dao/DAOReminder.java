package org.ird.immunizationreminder.dao;

import java.util.List;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.datamodel.entities.Reminder;

public interface DAOReminder extends DAO{

	Reminder findById(int id);

	/**
	 * @param reminderName
	 * @param isreadonly
	 * @param remTextFetchmode
	 * @return
	 */
	Reminder getReminderByName(String reminderName,boolean isreadonly,FetchMode remTextFetchmode);

	Number countAllRows();

	Number countReminderRows(String reminderName);

	Number LAST_QUERY_TOTAL_ROW__COUNT();

	/**
	 * @param reminderName
	 * @param firstResult
	 * @param fetchsize
	 * @param isreadonly
	 * @param remTextFetchmode
	 * @return
	 */
	List<Reminder> findReminder(String reminderName, int firstResult,
			int fetchsize,boolean isreadonly,FetchMode remTextFetchmode);

	/**
	 * @param firstResult
	 * @param fetchsize
	 * @param isreadonly
	 * @param remTextFetchmode
	 * @return
	 */
	List<Reminder> getAll(int firstResult, int fetchsize,boolean isreadonly,FetchMode remTextFetchmode);

}
