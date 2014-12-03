package org.ird.immunizationreminder.dao;

import java.util.List;

import org.ird.immunizationreminder.datamodel.entities.Vaccine;

public interface DAOVaccine extends DAO{

	Vaccine findById(int id);

	Vaccine getByName(String vaccineName);

	Number countAllRows();

	Number countVaccineRows(String vaccineName);

	Number LAST_QUERY_TOTAL_ROW__COUNT();

	List<Vaccine> findVaccine(String vaccineName, int firstResult, int fetchsize);

	List<Vaccine> getAll(int firstResult, int fetchSize);

	Vaccine getByNameInForm(String vaccineName);

	Vaccine getByNumberInForm(int vaccineNum);

}
