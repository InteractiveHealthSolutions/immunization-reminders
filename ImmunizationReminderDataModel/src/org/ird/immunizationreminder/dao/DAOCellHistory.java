/*package org.ird.immunizationreminder.dao;

import java.util.Date;
import java.util.List;

import org.ird.immunizationreminder.datamodel.entities.CellHistory;

public interface DAOCellHistory extends DAO{
	
	Number countAllRows();
	
	Number countCriteriaRows(String childId, Date begindate, Date enddate,
			String cellNumber);
	Number countCriteriaIncludeNameRows(String partOfFirstOrLastName,
			Date begindate, Date enddate, String cellNumber);
	Number LAST_QUERY_TOTAL_ROW__COUNT();
	
	CellHistory findById(int id);
	
	List<CellHistory> getAll(int firstResult, int fetchsize);
	
	List<CellHistory> findByCriteriaIncludeName(String partOfFirstOrLastName,
			Date begindate, Date enddate, String cellNumber, int firstResult,
			int fetchsize);

	List<CellHistory> findByCriteria(String childId, Date begindate,
			Date enddate, String cellNumber, int firstResult, int fetchsize);
}
*/