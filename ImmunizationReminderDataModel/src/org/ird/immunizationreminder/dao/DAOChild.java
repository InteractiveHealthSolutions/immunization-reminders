package org.ird.immunizationreminder.dao;

import java.util.List;

import org.ird.immunizationreminder.data.exception.ChildDataException;
import org.ird.immunizationreminder.datamodel.entities.Child;
import org.ird.immunizationreminder.datamodel.entities.Child.STATUS;

public interface DAOChild extends DAO{
	
	Number countAllRows();
	
	Number countCriteriaRows(String partOfFirstOrLastName, String clinic,
			STATUS Status, boolean putNotWithTeatmentStatus,String armName)
			throws ChildDataException;
	
	Number LAST_QUERY_TOTAL_ROW__COUNT();
	
	Child findById(int id);
	
	Child findByChildID(String childId,boolean isreadonly/*,FetchMode armFetchmode*/) throws ChildDataException;
	
	Child findByCurrentCellNumber(String currentCellNumber,boolean isreadonly/*,FetchMode armFetchmode*/) throws ChildDataException;

	
	List<Child> getAll(int firstResult, int fetchsize,boolean isreadonly/*,FetchMode armFetchMode*/);
	
	List<Child> findByCriteria(String partOfFirstOrLastName, String clinic,
			STATUS Status, boolean putNotWithTeatmentStatus,String armName,
			int firstResult, int fetchsize,boolean isreadonly/*,FetchMode armFetchmode*/) throws ChildDataException;
	
	List<Child> findByCurrentCellIgnoreInconsistency(String currentCellNumber,boolean isreadonly/*,FetchMode armFetchmode*/);
	
	List<Child> findByEpiOrMrNumber(String epiOrMrNumber);
}
