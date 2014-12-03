package org.ird.immunizationreminder.dao;

import java.util.List;

import org.ird.immunizationreminder.datamodel.entities.IrSetting;

public interface DAOIrSetting extends DAO{
	
	IrSetting findById(int id);
	
	List<IrSetting> matchByCriteria(String settingName);
	
	List<IrSetting> getAll();
	
	IrSetting getSetting(String settingName);
}
