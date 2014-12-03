/*package org.ird.immunizationreminder.service;

import java.util.Date;
import java.util.List;

import ird.xoutTB.dao.DAOUserSMS;
import ird.xoutTB.db.entity.UserSMS;
import ird.xoutTB.db.entity.UserSMS.SMS_STATUS;
import ird.xoutTB.service.UserSMSService;

public class UserSMSServiceImpl implements UserSMSService{

	DAOUserSMS dao;
	
	public UserSMSServiceImpl(DAOUserSMS dao) {
		this.dao=dao;
	}
	@Override
	public List<UserSMS> getAllUserSMSRecord() {
		return dao.getAllUserSMSRecord();
	}

	@Override
	public List<UserSMS> getUserSMSNotWithStatus(SMS_STATUS status) {
		return dao.getUserSMSNotWithStatus(status);
	}

	@Override
	public List<UserSMS> getUserSMSWithDueDateTime(Date datesmall,
			Date dategreater) {
		return dao.getUserSMSWithDueDateTime(datesmall, dategreater);
	}

	@Override
	public List<UserSMS> getUserSMSWithSentDatetime(Date datesmall,
			Date dategreater) {
		return dao.getUserSMSWithSentDatetime(datesmall, dategreater);
	}

	@Override
	public List<UserSMS> getUserSMSWithStatus(SMS_STATUS status) {
		return dao.getUserSMSWithStatus(status);
	}

	@Override
	public void saveUserSMS(UserSMS usersms) {
		dao.saveUserSMS(usersms);
	}

	@Override
	public void updateUserSMS(UserSMS usersms) {
		dao.updateUserSMS(usersms);
	}
	@Override
	public List<UserSMS> getUserSMSWithDueDateTimeAndNotHavingStatus(
			Date datesmall, Date dategreater, SMS_STATUS status) {
		return dao.getUserSMSWithDueDateTimeAndNotHavingStatus(datesmall, dategreater, status);
	}
	@Override
	public List<UserSMS> getUserSMSWithDueDateTimeAndStatus(Date datesmall,
			Date dategreater, SMS_STATUS status) {
		return dao.getUserSMSWithDueDateTimeAndStatus(datesmall, dategreater, status);
	}
	@Override
	public List<UserSMS> getUserSMSWithSentDatetimeAndNotHavingStatus(
			Date datesmall, Date dategreater, SMS_STATUS status) {
		return dao.getUserSMSWithSentDatetimeAndNotHavingStatus(datesmall, dategreater, status);
	}
	@Override
	public List<UserSMS> getUserSMSWithSentDatetimeAndStatus(Date datesmall,
			Date dategreater, SMS_STATUS status) {
		return dao.getUserSMSWithSentDatetimeAndStatus(datesmall, dategreater, status);
	}

}
*/