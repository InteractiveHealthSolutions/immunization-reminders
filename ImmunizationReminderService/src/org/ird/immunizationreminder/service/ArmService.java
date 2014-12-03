package org.ird.immunizationreminder.service;

import java.io.Serializable;
import java.util.List;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.datamodel.entities.Arm;
import org.ird.immunizationreminder.datamodel.entities.ArmDayReminder;
import org.ird.immunizationreminder.datamodel.entities.ArmDayReminderId;
import org.ird.immunizationreminder.datamodel.entities.ArmIdMap;

public interface ArmService {
	Number LASTS_ROWS_RETURNED_COUNT();

	ArmIdMap findByChildIdToMap(int id);

	List<ArmIdMap> getAllChildIdToMap();

	List<ArmIdMap> getByChildIdsOccupied(boolean idsSwitchValue);
	
	List<Arm> getAll(boolean isReadOnly);

	Arm getByName(String armName, FetchMode fetchmode, boolean isReadOnly);
	
	void updateArm(Arm arm);

	Serializable addArm(Arm arm);

	/**
	 * donot seem to be very useful
	 * @param armname
	 * @param fetchmode
	 * @param isReadOnly
	 * @return
	 */
	List<Arm> matchByCriteria(String armname, FetchMode fetchmode,
			boolean isReadOnly);

	ArmDayReminder findArmDayReminderById(ArmDayReminderId armdayReminderID);

	List<ArmDayReminder> getAllArmDayReminders();

	List<ArmDayReminder> findArmDayReminderByNamesCriteria(String armName,
			String vaccineName, String reminderName);

	List<ArmDayReminder> findArmDayReminderByIdsCriteria(String armId,
			String vaccineId, String reminderId, String dayNum);

	void addArmIdMap(ArmIdMap armidmap);

	void updateArmIdMap(ArmIdMap armidmap);

}
