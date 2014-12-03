package org.ird.immunizationreminder.dao;

import java.util.List;

import org.ird.immunizationreminder.datamodel.entities.ArmDayReminder;
import org.ird.immunizationreminder.datamodel.entities.ArmDayReminderId;

public interface DAOArmDayReminder extends DAO{
	
	ArmDayReminder findById(ArmDayReminderId armdayReminderID);
	
	List<ArmDayReminder> getAll();
	
	List<ArmDayReminder> findByNamesCriteria(String armName,String vaccineName,String reminderName);
	
	List<ArmDayReminder> findByIdsCriteria(String armId, String vaccineId, String reminderId, String dayNum);
}