package org.ird.immunizationreminder.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.data.exception.VaccinationDataException;
import org.ird.immunizationreminder.datamodel.entities.Vaccination;
import org.ird.immunizationreminder.datamodel.entities.Vaccination.VACCINATION_STATUS;

public interface DAOVaccination extends DAO{
	
	Vaccination findById(long id,boolean isreadonly,FetchMode childFetchMode
			,FetchMode vaccineFetchMode);
	
	Number countAllRows();
	
	Number countCriteriaRows(String childId, String vaccineName,
			Date dueDatesmaller, Date dueDategreater,
			Date vaccinationDatesmaller, Date vaccinationDategreater,
			VACCINATION_STATUS vaccinationStatus) throws VaccinationDataException;
	
	Number LAST_QUERY_TOTAL_ROW__COUNT();
	
	List<Vaccination> findByCriteria(String childId,
			String vaccineName, Date dueDatesmaller, Date dueDategreater,
			Date vaccinationDatesmaller, Date vaccinationDategreater,
			VACCINATION_STATUS vaccinationStatus, int firstResult, int fetchsize
			,boolean isreadonly,FetchMode childFetchMode
			,FetchMode vaccineFetchMode)
			throws VaccinationDataException;
	
	List<Vaccination> getAll(int firstResult, int fetchsize,boolean isreadonly,FetchMode childFetchMode
			,FetchMode vaccineFetchMode);
	
	List<Vaccination> findByCriteriaIncludeName(String partOfName,
			String vaccineName, Date dueDatesmaller, Date dueDategreater,
			Date vaccinationDatesmaller, Date vaccinationDategreater,
			VACCINATION_STATUS vaccinationStatus, String armName, int firstResult,
			int fetchsize,boolean isreadonly,FetchMode childFetchMode
			,FetchMode vaccineFetchMode) throws VaccinationDataException;
	
	Number countCriteriaIncludeNameRows(String partOfName, String vaccineName,
			Date dueDatesmaller, Date dueDategreater,
			Date vaccinationDatesmaller, Date vaccinationDategreater,
			VACCINATION_STATUS vaccinationStatus, String armName)
			throws VaccinationDataException;
}
