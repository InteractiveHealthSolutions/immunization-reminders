package org.ird.immunizationreminder.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.datamodel.entities.Response;
import org.ird.immunizationreminder.datamodel.entities.Response.RESPONSE_TYPE;

public interface DAOResponse extends DAO{
	
	Response findById(int id);
	
	Number countCriteriaRows(String childId, Date begindate, Date enddate,
			String cellNumber, RESPONSE_TYPE responseType);
	
	Number countCriteriaIncludeNameRows(String partOfFirstOrLastName,
			Date begindate, Date enddate, String cellNumber,String armName, RESPONSE_TYPE responseType);
	
	Number LAST_QUERY_TOTAL_ROW__COUNT();
	
	List<Response> findByCriteriaIncludeName(
			String partOfFirstOrLastName, Date begindate, Date enddate,
			String cellNumber, String armName, RESPONSE_TYPE responseType, int firstResult, int fetchsize
			,boolean isreadonly,FetchMode childFetchMode);
	
	List<Response> findByCriteria(String childId, Date begindate,
			Date enddate, String cellNumber, RESPONSE_TYPE responseType, int firstResult, int fetchsize
			,boolean isreadonly,FetchMode childFetchMode);
	
	List<Response> getAll(int firstResult, int fetchsize,boolean isreadonly,FetchMode childFetchMode);
	
	List<Response> findByVaccinationRecord(long vacciantionRecordNum,boolean isreadonly,FetchMode childFetchMode);
}
