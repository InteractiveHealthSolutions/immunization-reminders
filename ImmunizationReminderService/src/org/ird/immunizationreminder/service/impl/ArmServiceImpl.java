package org.ird.immunizationreminder.service.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.dao.DAOArm;
import org.ird.immunizationreminder.dao.DAOArmDayReminder;
import org.ird.immunizationreminder.dao.DAOArmIdMap;
import org.ird.immunizationreminder.datamodel.entities.Arm;
import org.ird.immunizationreminder.datamodel.entities.ArmDayReminder;
import org.ird.immunizationreminder.datamodel.entities.ArmDayReminderId;
import org.ird.immunizationreminder.datamodel.entities.ArmIdMap;
import org.ird.immunizationreminder.service.ArmService;

public class ArmServiceImpl implements ArmService{
private Number LASTS_ROWS_RETURNED_COUNT;
	private DAOArm daoarm;
	private DAOArmDayReminder daoardayrem;
	private DAOArmIdMap daoarmidmap;
	public ArmServiceImpl(DAOArm daoarm,DAOArmDayReminder daoardayrem,DAOArmIdMap daoarmidmap) {
		this.daoarm=daoarm;
		this.daoardayrem=daoardayrem;
		this.daoarmidmap=daoarmidmap;
	}
	@Override
	public List<Arm> getAll(boolean isReadOnly) {
		List<Arm> arml= daoarm.getAll(isReadOnly);
		setLASTS_ROWS_RETURNED_COUNT(arml.size());
		return arml;
	}
	private void setLASTS_ROWS_RETURNED_COUNT(Number lASTS_ROWS_RETURNED_COUNT) {
		LASTS_ROWS_RETURNED_COUNT = lASTS_ROWS_RETURNED_COUNT;
	}
	@Override
	public Number LASTS_ROWS_RETURNED_COUNT() {
		return LASTS_ROWS_RETURNED_COUNT;
	}
	@Override
	public Arm getByName(String armName ,FetchMode fetchmode,boolean isReadOnly) {
		Arm arm=daoarm.getByName(armName,fetchmode,isReadOnly);
		setLASTS_ROWS_RETURNED_COUNT(arm==null?0:1);
		return arm;
	}
	@Override
	public List<Arm> matchByCriteria(String armname, FetchMode fetchmode,boolean isreadonly) {
		List<Arm> arml=daoarm.matchByCriteria(armname, fetchmode,isreadonly);
		setLASTS_ROWS_RETURNED_COUNT(arml.size());
		return arml;
	}
	@Override
	public Serializable addArm(Arm arm){
		return daoarm.save(arm);
	}
	@Override
	public void updateArm(Arm arm){
		daoarm.update(arm);
	}
	@Override
	public ArmDayReminder findArmDayReminderById(ArmDayReminderId armdayReminderID) {
		ArmDayReminder adr=daoardayrem.findById(armdayReminderID);
		setLASTS_ROWS_RETURNED_COUNT(adr==null?0:1);
		return adr;
	}
	@Override
	public List<ArmDayReminder> findArmDayReminderByIdsCriteria(String armId,
			String vaccineId, String reminderId, String dayNum) {
		List<ArmDayReminder> adrl=daoardayrem.findByIdsCriteria(armId, vaccineId, reminderId, dayNum);
		setLASTS_ROWS_RETURNED_COUNT(adrl.size());
		return adrl;
	}
	@Override
	public List<ArmDayReminder> findArmDayReminderByNamesCriteria(String armName, String vaccineName, String reminderName) {
		List<ArmDayReminder> adrl=daoardayrem.findByNamesCriteria(armName, vaccineName, reminderName);
		setLASTS_ROWS_RETURNED_COUNT(adrl.size());
		return adrl;
	}
	@Override
	public List<ArmDayReminder> getAllArmDayReminders() {
		List<ArmDayReminder> adrl=daoardayrem.getAll();
		setLASTS_ROWS_RETURNED_COUNT(adrl.size());
		return adrl;
	}
	@Override
	public ArmIdMap findByChildIdToMap(int id) {
		ArmIdMap map=daoarmidmap.findByChildIdToMap(id);
		setLASTS_ROWS_RETURNED_COUNT(map==null?0:1);
		return map;
	}
	@Override
	public List<ArmIdMap> getAllChildIdToMap() {
		List<ArmIdMap> armidmap=daoarmidmap.getAll();
		setLASTS_ROWS_RETURNED_COUNT(armidmap.size());
		return armidmap;
	}
/** 
 * @param idsSwitchValue boolean
 * @return ArmIdMap
 * */
	@Override
	public List<ArmIdMap> getByChildIdsOccupied(boolean idsSwitchValue) {
		List<ArmIdMap> armidmap=daoarmidmap.getByIdsOccupied(idsSwitchValue);
		setLASTS_ROWS_RETURNED_COUNT(armidmap.size());
		return armidmap;
	}
	@Override
	public void addArmIdMap(ArmIdMap armidmap) {
		daoarmidmap.save(armidmap);
	}
	@Override
	public void updateArmIdMap(ArmIdMap armidmap) {
		daoarmidmap.update(armidmap);
	}

}
