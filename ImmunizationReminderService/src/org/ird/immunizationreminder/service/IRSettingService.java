package org.ird.immunizationreminder.service;

import java.util.List;

import org.ird.immunizationreminder.datamodel.entities.IrSetting;

public interface IRSettingService {
	IrSetting getIrSetting(String name);

	void updateIrSetting(IrSetting setting);

	List<IrSetting> getIrSettings();

	List<IrSetting> matchSetting(String settingName);
}
