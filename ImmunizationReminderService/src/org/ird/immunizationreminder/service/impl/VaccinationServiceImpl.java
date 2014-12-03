package org.ird.immunizationreminder.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.dao.DAOVaccination;
import org.ird.immunizationreminder.dao.DAOVaccine;
import org.ird.immunizationreminder.data.exception.VaccinationDataException;
import org.ird.immunizationreminder.datamodel.entities.Vaccination;
import org.ird.immunizationreminder.datamodel.entities.Vaccination.VACCINATION_STATUS;
import org.ird.immunizationreminder.datamodel.entities.Vaccine;
import org.ird.immunizationreminder.service.VaccinationService;
import org.ird.immunizationreminder.utils.date.DateUtils;

import com.mysql.jdbc.StringUtils;

public class VaccinationServiceImpl implements VaccinationService{

	DAOVaccine daovacc;
	DAOVaccination daoptvaccination;
	private Number LAST_QUERY_TOTAL_ROW__COUNT;
	public VaccinationServiceImpl(DAOVaccine daovacc,DAOVaccination daoptvaccination) {
		this.daovacc=daovacc;
		this.daoptvaccination=daoptvaccination;
	}
	private void setLASTS_ROWS_RETURNED_COUNT(Number LAST_QUERY_TOTAL_ROW__COUNT) {
		this.LAST_QUERY_TOTAL_ROW__COUNT = LAST_QUERY_TOTAL_ROW__COUNT;
	}
	@Override
	public Number LAST_QUERY_TOTAL_ROW__COUNT() {
		return LAST_QUERY_TOTAL_ROW__COUNT;
	}
	@Override
	public Vaccine getByName(String vaccineName){
		Vaccine v= daovacc.getByName(vaccineName);
		setLASTS_ROWS_RETURNED_COUNT(daovacc.LAST_QUERY_TOTAL_ROW__COUNT());
		return v;
	}
	@Override
	public Vaccine getByNameInForm(String vaccineName) {
		Vaccine v= daovacc.getByNameInForm(vaccineName);
		setLASTS_ROWS_RETURNED_COUNT(daovacc.LAST_QUERY_TOTAL_ROW__COUNT());
		return v;
	}
	@Override
	public Vaccine getByNumberInForm(int vaccineNum) {
		Vaccine v= daovacc.getByNumberInForm(vaccineNum);
		setLASTS_ROWS_RETURNED_COUNT(daovacc.LAST_QUERY_TOTAL_ROW__COUNT());
		return v;
	}
	@Override
	public List<Vaccine> getAll(int firstResult,int fetchSize){
		List<Vaccine> vlst= daovacc.getAll(firstResult,fetchSize);
		setLASTS_ROWS_RETURNED_COUNT(daovacc.LAST_QUERY_TOTAL_ROW__COUNT());
		return vlst;
	}
	@Override
	public List<Vaccine> findVaccine(String vaccineName,int firstResult, int fetchSize){
		List<Vaccine> vlst= daovacc.findVaccine(vaccineName,firstResult,fetchSize);
		setLASTS_ROWS_RETURNED_COUNT(daovacc.LAST_QUERY_TOTAL_ROW__COUNT());
		return vlst;
	}
	@Override
	public Serializable addVaccine(Vaccine vaccine) throws VaccinationDataException {
		if(daovacc.getByName(vaccine.getName().trim())!=null){
			throw new VaccinationDataException(VaccinationDataException.VACCINE_ALREADY_EXISTS);
		}
		if(vaccine.getVaccineNumberInForm()!=0 && daovacc.getByNumberInForm(vaccine.getVaccineNumberInForm())!=null){
			throw new VaccinationDataException(VaccinationDataException.VACCINE_FORM_NUMBER_ALREADY_EXISTS);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(vaccine.getVaccineNameInForm()) && daovacc.getByName(vaccine.getVaccineNameInForm().trim())!=null){
			throw new VaccinationDataException(VaccinationDataException.VACCINE_FORM_NAME_ALREADY_EXISTS);
		}
		return daovacc.save(vaccine);
	}
	@Override
	public void updateVaccine(Vaccine vaccine){
		daovacc.update(vaccine);
	}
	@Override
	public Serializable addVaccinationRecord(Vaccination vacinationRecord) {
		return daoptvaccination.save(vacinationRecord);
	}
	@Override
	public List<Vaccination> findVaccinationRecordByCriteria(
						String childId, String vaccineName, Date dueDatesmaller,
						Date dueDategreater, Date vaccinationDatesmaller,
						Date vaccinationDategreater, VACCINATION_STATUS vaccinationStatus,
						int firstResult, int fetchsize,boolean isreadonly,FetchMode childFetchMode
						,FetchMode vaccineFetchMode) throws VaccinationDataException {
		if(dueDatesmaller!=null){
			dueDatesmaller=DateUtils.truncateDatetoDate(dueDatesmaller);
		}
		if(dueDategreater!=null){
			dueDategreater=DateUtils.roundoffDatetoDate(dueDategreater);
		}
		if(vaccinationDatesmaller!=null){
			vaccinationDatesmaller=DateUtils.truncateDatetoDate(vaccinationDatesmaller);
		}
		if(vaccinationDategreater!=null){
			vaccinationDategreater=DateUtils.roundoffDatetoDate(vaccinationDategreater);
		}
		List<Vaccination> pvl=daoptvaccination.findByCriteria(childId
				, vaccineName, dueDatesmaller, dueDategreater, vaccinationDatesmaller
				, vaccinationDategreater, vaccinationStatus, firstResult, fetchsize
				,isreadonly,childFetchMode,vaccineFetchMode);
		setLASTS_ROWS_RETURNED_COUNT(daoptvaccination.LAST_QUERY_TOTAL_ROW__COUNT());
		return pvl;
	}
	@Override
	public List<Vaccination> findVaccinationRecordByCriteriaIncludeName(
						String partOfName, String vaccineName, Date dueDatesmaller,
						Date dueDategreater, Date vaccinationDatesmaller,
						Date vaccinationDategreater, VACCINATION_STATUS vaccinationStatus,
						String armName, int firstResult, int fetchsize,boolean isreadonly,FetchMode childFetchMode
						,FetchMode vaccineFetchMode)
						throws VaccinationDataException {
		if(dueDatesmaller!=null){
			dueDatesmaller=DateUtils.truncateDatetoDate(dueDatesmaller);
		}
		if(dueDategreater!=null){
			dueDategreater=DateUtils.roundoffDatetoDate(dueDategreater);
		}
		if(vaccinationDatesmaller!=null){
			vaccinationDatesmaller=DateUtils.truncateDatetoDate(vaccinationDatesmaller);
		}
		if(vaccinationDategreater!=null){
			vaccinationDategreater=DateUtils.roundoffDatetoDate(vaccinationDategreater);
		}
		List<Vaccination> pvl=daoptvaccination.findByCriteriaIncludeName(partOfName
				, vaccineName, dueDatesmaller, dueDategreater, vaccinationDatesmaller
				, vaccinationDategreater, vaccinationStatus, armName, firstResult, fetchsize
				,isreadonly,childFetchMode,vaccineFetchMode);
		setLASTS_ROWS_RETURNED_COUNT(daoptvaccination.LAST_QUERY_TOTAL_ROW__COUNT());
		return pvl;
	}
	@Override
	public List<Vaccination> getAllVaccinationRecord(int firstResult,int fetchsize,boolean isreadonly,FetchMode childFetchMode
			,FetchMode vaccineFetchMode) {
		List<Vaccination> pvl=daoptvaccination.getAll(firstResult, fetchsize
				,isreadonly,childFetchMode,vaccineFetchMode);
		setLASTS_ROWS_RETURNED_COUNT(daoptvaccination.LAST_QUERY_TOTAL_ROW__COUNT());
		return pvl;
	}
	@Override
	public Vaccination getVaccinationRecord(long recordId,boolean isreadonly,FetchMode childFetchMode
			,FetchMode vaccineFetchMode) {
		Vaccination pv=daoptvaccination.findById(recordId
				,isreadonly,childFetchMode,vaccineFetchMode);
		setLASTS_ROWS_RETURNED_COUNT(pv==null?0:1);
		return pv;
	}
	@Override
	public void updateVaccinationRecord(Vaccination vacinationRecord) {
		daoptvaccination.update(vacinationRecord);
	}

}
