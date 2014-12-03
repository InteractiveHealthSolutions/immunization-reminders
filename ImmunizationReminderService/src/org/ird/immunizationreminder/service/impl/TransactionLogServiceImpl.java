package org.ird.immunizationreminder.service.impl;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.List;
import org.ird.immunizationreminder.dao.DAOCsvUpload;
import org.ird.immunizationreminder.datamodel.entities.CsvUpload;
import org.ird.immunizationreminder.service.TransactionLogService;
import org.ird.immunizationreminder.utils.Utils;

public class TransactionLogServiceImpl implements TransactionLogService{
	
	DAOCsvUpload daocsv;
	private Number LAST_QUERY_TOTAL_ROW__COUNT;
	
	public TransactionLogServiceImpl(DAOCsvUpload daocsv){
		this.daocsv=daocsv;
	}
	
	private void setLASTS_ROWS_RETURNED_COUNT(Number LAST_QUERY_TOTAL_ROW__COUNT) {
		this.LAST_QUERY_TOTAL_ROW__COUNT = LAST_QUERY_TOTAL_ROW__COUNT;
	}
	
	@Override
	public Number LAST_QUERY_TOTAL_ROW__COUNT() {
		return LAST_QUERY_TOTAL_ROW__COUNT;
	}

	@Override
	public List<CsvUpload> getAllWithProjection(int firstResult, int fetchsize) {
		List<CsvUpload> csvl = daocsv.getAllWithProjection(firstResult, fetchsize);
		setLASTS_ROWS_RETURNED_COUNT(daocsv.LAST_QUERY_TOTAL_ROW__COUNT());
		return csvl;
	}

	@Override
	public String getCsvFile(long recordNumber) throws SQLException, Exception {
		List csfile = daocsv.getCsvFile(recordNumber);
		if(csfile.size()==0){
			return null;
		}
		return (String) csfile.get(0);
		//return Utils.convertStreamToStringBuilder(((Clob)csfile.get(0)).getAsciiStream());
	}

	@Override
	public String getUploadReport(long recordNumber) throws SQLException, Exception {
		List report = daocsv.getUploadReport(recordNumber);
		if(report.size()==0){
			return null;
		}
		return (String) report.get(0);
		//return Utils.convertStreamToStringBuilder(((Clob)report.get(0)).getAsciiStream());
	}
	@Override
	public void saveCsvUpload(CsvUpload csvUpload){
		daocsv.save(csvUpload);
	}
}
