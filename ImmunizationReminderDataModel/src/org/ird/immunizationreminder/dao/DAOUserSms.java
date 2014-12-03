/*package org.ird.immunizationreminder.dao;

import java.util.Date;
import java.util.List;

import org.ird.immunizationreminder.data.exception.DataException;

public interface DAOUserSms extends DAO{

	UserSms findById(long id);

	Number countCriteriaRows(Date duedatesmaller, Date duedategreater,
			Date sentdatesmaller, Date sentdategreater, String smsStatus,
			boolean putNotWithSmsStatus) throws DataException;

	Number countAllRows();

	Number LAST_QUERY_TOTAL_ROW__COUNT();

	List<UserSms> findByCriteria(Date duedatesmaller, Date duedategreater,
			Date sentdatesmaller, Date sentdategreater, String smsStatus,
			boolean putNotWithSmsStatus, int firstResult, int fetchsize)
			throws DataException;

	List<UserSms> getAll(int firstResult, int fetchsize);

}
*/