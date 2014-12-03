package org.ird.immunizationreminder.dao;

import java.util.List;

import org.ird.immunizationreminder.datamodel.entities.Role;

public interface DAORole extends DAO{

	Role findById(int id);

	Role findByName(String roleName);

	List<Role> getAll();

	List<Role> findByCriteria(String roleName);

}
