package org.ird.immunizationreminder.service;

import java.sql.SQLException;
import java.util.List;
import org.ird.immunizationreminder.datamodel.entities.CsvUpload;

public interface TransactionLogService {
	
	String getCsvFile(long recordNumber) throws SQLException, Exception;

	String getUploadReport(long recordNumber) throws SQLException, Exception;

	List<CsvUpload> getAllWithProjection(int firstResult, int fetchsize);

	Number LAST_QUERY_TOTAL_ROW__COUNT();

	void saveCsvUpload(CsvUpload csvUpload);
}
