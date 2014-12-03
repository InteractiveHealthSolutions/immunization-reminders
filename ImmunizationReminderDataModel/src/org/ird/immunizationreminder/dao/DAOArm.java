package org.ird.immunizationreminder.dao;

import java.util.List;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.datamodel.entities.Arm;

public interface DAOArm extends DAO{
	/**
	 * @param id
	 * @return
	 */
	Arm findById(int id) ;
	
	/**
	 * @param isReadOnly
	 * @return
	 */
	List<Arm> getAll(boolean isReadOnly);
	
	/**
	 * @param armname
	 * @param armDayfetchmode
	 * @param isReadOnly
	 * @return
	 */
	List<Arm> matchByCriteria(String armname, FetchMode armDayfetchmode,boolean isReadOnly);
	
	/**
	 * @param armName
	 * @param armDayfetchmode
	 * @param isReadOnly
	 * @return
	 */
	Arm getByName(String armName, FetchMode armDayfetchmode,boolean isReadOnly);
}
