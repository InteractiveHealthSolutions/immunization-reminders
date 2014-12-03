package org.ird.immunizationreminder.dao.hibernatedao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.ird.immunizationreminder.dao.DAOCsvUpload;
import org.ird.immunizationreminder.datamodel.entities.CsvUpload;

public class DAOCsvUploadImpl extends DAOHibernateImpl implements DAOCsvUpload{

	private Session session;
	private Number LAST_QUERY_TOTAL_ROW__COUNT;

	public DAOCsvUploadImpl(Session session) {
		super(session);
		this.session=session;
	}
	private void setLAST_QUERY_TOTAL_ROW__COUNT(Number LAST_QUERY_TOTAL_ROW__COUNT) {
		this.LAST_QUERY_TOTAL_ROW__COUNT = LAST_QUERY_TOTAL_ROW__COUNT;
	}
	@Override
	public Number LAST_QUERY_TOTAL_ROW__COUNT() {
		return LAST_QUERY_TOTAL_ROW__COUNT;
	}
	
	/**
	 * @param firstResult
	 * @param fetchsize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CsvUpload> getAllWithProjection(int firstResult,int fetchsize){
		setLAST_QUERY_TOTAL_ROW__COUNT(countAllRows());
		return session.createQuery("select recordNumber as recordNumber, description as description,numberOfRows as numberOfRows,numberOfRowsRejected as numberOfRowsRejected,numberOfRowsSaved as numberOfRowsSaved,uploadErrors as uploadErrors,uploadedByUserId as uploadedByUserId,uploadedByUserName as uploadedByUserName,dateUploaded as dateUploaded from CsvUpload order by dateUploaded desc").setFirstResult(firstResult).setMaxResults(fetchsize).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	@Override
	public Number countAllRows(){
		return (Number) session.createQuery("select count(*) from CsvUpload").uniqueResult();
	}
	@Override
	public List getUploadReport(long record_number){
		Query q=session.createQuery("select uploadReport from CsvUpload where recordNumber=?").setLong(0, record_number);
		return q.list();
	}
	@Override
	public List getCsvFile(long record_number){
		Query q=session.createQuery("select csvFile from CsvUpload where recordNumber=?").setLong(0, record_number);
		return q.list();
	}
}
