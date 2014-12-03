package org.ird.immunizationreminder.dao;

import java.util.List;

import org.ird.immunizationreminder.data.exception.DataException;
import org.ird.immunizationreminder.datamodel.entities.User;
import org.ird.immunizationreminder.datamodel.entities.User.UserStatus;

public interface DAOUser extends DAO{

	User findById(int id);

	User findByUsername(String username);

	Number countCriteriaRows(String email, String partOfFirstOrLastName,
			UserStatus userStatus, boolean putNotWithUserStatus)
			throws DataException;

	Number countAllRows();

	Number LAST_QUERY_TOTAL_ROW__COUNT();

	List<User> findByCriteria(String email, String partOfFirstOrLastName,
			UserStatus userStatus, boolean putNotWithUserStatus, int firstResult,
			int fetchsize) throws DataException;

	List<User> getAll(int firstResult, int fetchsize);

}
