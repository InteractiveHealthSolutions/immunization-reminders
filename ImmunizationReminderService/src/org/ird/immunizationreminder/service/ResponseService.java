package org.ird.immunizationreminder.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.datamodel.entities.Response;
import org.ird.immunizationreminder.datamodel.entities.Response.RESPONSE_TYPE;

public interface ResponseService {
	Number LAST_QUERY_TOTAL_ROW__COUNT();

	List<Response> getAllResponseRecord(int firstResult,int fetchSize,boolean isreadonly,FetchMode childFetchMode);
	
	Serializable addResponseRecord(Response response);

	List<Response> getByCriteriaIncludeName(
			String partOfFirstOrLastName, Date begindate, Date enddate,
			String cellNumber,String armName, RESPONSE_TYPE responseType, int firstResult, int fetchsize,boolean isreadonly,FetchMode childFetchMode);

	List<Response> getByCriteriaIncludeChildId(String childId,
			Date begindate, Date enddate, String cellNumber, RESPONSE_TYPE responseTyp, int firstResult,
			int fetchsize,boolean isreadonly,FetchMode childFetchMode);
	
	List<Response> findByVaccinationRecord(long vacciantionRecordNum,boolean isreadonly,FetchMode childFetchMode);

	void updateResponseRecord(Response response);
}
