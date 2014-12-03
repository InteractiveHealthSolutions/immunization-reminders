package org.ird.immunizationreminder.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.data.exception.VaccinationDataException;
import org.ird.immunizationreminder.datamodel.entities.Vaccination;
import org.ird.immunizationreminder.datamodel.entities.Vaccination.VACCINATION_STATUS;
import org.ird.immunizationreminder.datamodel.entities.Vaccine;

public interface VaccinationService {
	Vaccine getByName(String vaccineName);
	
	Vaccine getByNameInForm(String vaccineName);

	Vaccine getByNumberInForm(int vaccineNum);

	List<Vaccine> getAll(int firstResult, int fetchSize);

	List<Vaccine> findVaccine(String vaccineName,int firstResult, int fetchSize);
	
	Serializable addVaccine(Vaccine vaccine) throws VaccinationDataException;

	void updateVaccine(Vaccine vaccine);

	Number LAST_QUERY_TOTAL_ROW__COUNT();
	
	Vaccination getVaccinationRecord(long recordId,boolean isreadonly,FetchMode childFetchMode
			,FetchMode vaccineFetchMode);
	
	List<Vaccination> findVaccinationRecordByCriteria(String childId,
			String vaccineName, Date dueDatesmaller, Date dueDategreater,
			Date vaccinationDatesmaller, Date vaccinationDategreater,
			VACCINATION_STATUS vaccinationStatus, int firstResult, int fetchsize,boolean isreadonly,FetchMode childFetchMode
			,FetchMode vaccineFetchMode)
			throws VaccinationDataException;
	
	List<Vaccination> getAllVaccinationRecord(int firstResult, int fetchsize,boolean isreadonly,FetchMode childFetchMode
			,FetchMode vaccineFetchMode);
	
	List<Vaccination> findVaccinationRecordByCriteriaIncludeName(String partOfName,
			String vaccineName, Date dueDatesmaller, Date dueDategreater,
			Date vaccinationDatesmaller, Date vaccinationDategreater,
			VACCINATION_STATUS vaccinationStatus, String armName, int firstResult,
			int fetchsize,boolean isreadonly,FetchMode childFetchMode
			,FetchMode vaccineFetchMode) throws VaccinationDataException;
	
	Serializable addVaccinationRecord(Vaccination vacinationRecord);
	
	void updateVaccinationRecord(Vaccination vacinationRecord);

}
