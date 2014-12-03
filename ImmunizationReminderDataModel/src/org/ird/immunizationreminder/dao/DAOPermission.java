package org.ird.immunizationreminder.dao;

import java.util.List;

import org.ird.immunizationreminder.datamodel.entities.Permission;

public interface DAOPermission extends DAO{
	
	Permission findById(int id);
	
	List<Permission> getAll(boolean isreadonly);
	
	Permission findByPermissionName(String permissionName,boolean isreadonly);
}
