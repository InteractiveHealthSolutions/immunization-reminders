package org.ird.immunizationreminder.service.impl;

import java.util.List;

import org.ird.immunizationreminder.dao.DAOIrSetting;
import org.ird.immunizationreminder.datamodel.entities.IrSetting;
import org.ird.immunizationreminder.service.IRSettingService;

public class IRSettingServiceImpl implements IRSettingService{
	private DAOIrSetting dao;
	public IRSettingServiceImpl(DAOIrSetting dao) {
		this.dao=dao;
	}
	@Override
	public IrSetting getIrSetting(String name) {
		return dao.getSetting(name);
	}

	@Override
	public List<IrSetting> getIrSettings() {
		return dao.getAll();
	}
	@Override
	public List<IrSetting> matchSetting(String settingName){
		return dao.matchByCriteria(settingName);
	}
	@Override
	public void updateIrSetting(IrSetting setting) {
		dao.update(setting);
	}
}
