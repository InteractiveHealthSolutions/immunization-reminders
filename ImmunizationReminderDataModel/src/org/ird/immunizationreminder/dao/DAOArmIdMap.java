package org.ird.immunizationreminder.dao;

import java.util.List;

import org.ird.immunizationreminder.datamodel.entities.ArmIdMap;

public interface DAOArmIdMap extends DAO{
	
	ArmIdMap findByChildIdToMap(int id) ;
	
	List<ArmIdMap> getAll();
	
	List<ArmIdMap> getByIdsOccupied(boolean idsSwitchValue);
}
