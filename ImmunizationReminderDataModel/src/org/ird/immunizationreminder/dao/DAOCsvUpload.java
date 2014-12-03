package org.ird.immunizationreminder.dao;

import java.util.List;

import org.ird.immunizationreminder.datamodel.entities.CsvUpload;

public interface DAOCsvUpload extends DAO{

	List getCsvFile(long recordNumber);

	List getUploadReport(long recordNumber);

	Number countAllRows();

	List<CsvUpload> getAllWithProjection(int firstResult, int fetchsize);

	Number LAST_QUERY_TOTAL_ROW__COUNT();
}
