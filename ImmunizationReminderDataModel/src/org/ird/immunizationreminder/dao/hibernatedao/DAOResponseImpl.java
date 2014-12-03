package org.ird.immunizationreminder.dao.hibernatedao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.immunizationreminder.dao.DAOResponse;
import org.ird.immunizationreminder.datamodel.entities.Response;
import org.ird.immunizationreminder.datamodel.entities.Response.RESPONSE_TYPE;

public class DAOResponseImpl extends DAOHibernateImpl implements DAOResponse{
	private Session session;
	private Number LAST_QUERY_TOTAL_ROW__COUNT;

	public DAOResponseImpl(Session session) {
		super(session);
		this.session=session;
	}
	@Override
	public Response findById(int id) {
		Response pr= (Response) session.get(Response.class,id);
		setLAST_QUERY_TOTAL_ROW__COUNT(pr==null?0:1);
		return pr;
	}
	private void setLAST_QUERY_TOTAL_ROW__COUNT(Number LAST_QUERY_TOTAL_ROW__COUNT) {
		this.LAST_QUERY_TOTAL_ROW__COUNT = LAST_QUERY_TOTAL_ROW__COUNT;
	}
	@Override
	public Number LAST_QUERY_TOTAL_ROW__COUNT() {
		return LAST_QUERY_TOTAL_ROW__COUNT;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Response> getAll(int firstResult, int fetchsize,boolean isreadonly,FetchMode childFetchMode){
		List<Response> prlst = session.createCriteria(Response.class)
					.addOrder(Order.desc("recieveDate"))
					.setFetchMode("child", childFetchMode)
					.setReadOnly(isreadonly)
					.setFirstResult(firstResult)
					.setMaxResults(fetchsize)
					.list();
		setLAST_QUERY_TOTAL_ROW__COUNT(countAllRows());
		return prlst;
	}
	public Number countAllRows() {
		return (Number) session.createQuery("select count(*) from Response").uniqueResult();
	}
	@SuppressWarnings("unchecked")
	@Override

	public List<Response> findByVaccinationRecord(long vacciantionRecordNum,boolean isreadonly,FetchMode childFetchMode) {
		List<Response> prlst = session.createCriteria(Response.class)
				.add(Restrictions.eq("vaccinationRecordNum", vacciantionRecordNum))
				.addOrder(Order.desc("recieveDate"))
				.setFetchMode("child", childFetchMode)
				.setReadOnly(isreadonly)
				.list();
		
		setLAST_QUERY_TOTAL_ROW__COUNT(prlst.size());
		return prlst;	
	}
	/** search on given criteria, set values null if you want to exclude any criteria : donot give 
	 * invalid values
	 * 
	 * @param child_id
	 * @param begindate
	 * @param enddate
	 * @param cellNumber
	 * @param firstResult 
	 * @param fetchsize 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	//if child id is given arm name doesnt matter

	public List<Response> findByCriteria(String child_id,Date begindate,Date enddate,String cellNumber
			, RESPONSE_TYPE responseType, int firstResult, int fetchsize,boolean isreadonly,FetchMode childFetchMode) {
		setLAST_QUERY_TOTAL_ROW__COUNT(countCriteriaRows(child_id, begindate, enddate, cellNumber, responseType));
		Criteria cri= session.createCriteria(Response.class);
		if(begindate!=null && enddate!=null){
			cri.add(Restrictions.between("recieveDate", begindate, enddate));
		}
		if(cellNumber!=null){
			cri.add(Restrictions.like("cellNo", cellNumber,MatchMode.END));
		}
		if(child_id!=null){
			cri.createAlias("child", "p").add(Restrictions.eq("p.childId", child_id));
		}
		if(responseType != null){
			cri.add(Restrictions.eq("responseType", responseType));
		}
		return cri.addOrder(Order.desc("recieveDate"))
				.setFetchMode("child", childFetchMode)
				.setReadOnly(isreadonly)
				.setFirstResult(firstResult)
				.setMaxResults(fetchsize)
				.list();
	}
	@Override
	public Number countCriteriaRows(String child_id,Date begindate,Date enddate,String cellNumber, RESPONSE_TYPE responseType) {
		Criteria cri= session.createCriteria(Response.class);
		if(begindate!=null && enddate!=null){
			cri.add(Restrictions.between("recieveDate", begindate, enddate));
		}
		if(cellNumber!=null){
			cri.add(Restrictions.like("cellNo", cellNumber,MatchMode.END));
		}
		if(child_id!=null){
			cri.createAlias("child", "p").add(Restrictions.eq("p.childId", child_id));
		}
		if(responseType != null){
			cri.add(Restrictions.eq("responseType", responseType));
		}
		cri.setProjection(Projections.rowCount());
		return (Number) cri.uniqueResult();
	}
	/**search on given criteria, set values null if you want to exclude any criteria : donot give 
	 * invalid values
	 * @param partOfFirstOrLastName
	 * @param begindate
	 * @param enddate
	 * @param cellNumber
	 * @param firstResult 
	 * @param fetchsize 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Response> findByCriteriaIncludeName(String partOfFirstOrLastName,Date begindate
							,Date enddate,String cellNumber,String armName, RESPONSE_TYPE responseType,int firstResult, int fetchsize
							,boolean isreadonly,FetchMode childFetchMode) {
		setLAST_QUERY_TOTAL_ROW__COUNT(countCriteriaIncludeNameRows(partOfFirstOrLastName, begindate, enddate, cellNumber,armName, responseType));
		Criteria cri= session.createCriteria(Response.class);
		if(begindate!=null && enddate!=null){
			cri.add(Restrictions.between("recieveDate", begindate, enddate));
		}
		if(cellNumber!=null){
			cri.add(Restrictions.like("cellNo", cellNumber,MatchMode.END));
		}
		if(partOfFirstOrLastName!=null){
			cri.createAlias("child", "p").add(
					Restrictions.or(Restrictions.like("p.firstName", partOfFirstOrLastName,MatchMode.START)
									,Restrictions.like("p.lastName", partOfFirstOrLastName,MatchMode.START)));
		}
		if(armName!=null){
			cri.createAlias("child", "p").createAlias("p.arm", "parm").add(Restrictions.like("parm.armName", armName,MatchMode.EXACT));
		}
		if(responseType != null){
			cri.add(Restrictions.eq("responseType", responseType));
		}
		return cri.addOrder(Order.desc("recieveDate"))				
					.setFetchMode("child", childFetchMode)
					.setReadOnly(isreadonly)
					.setFirstResult(firstResult)
					.setMaxResults(fetchsize)
					.list();
	}
	@Override
	public Number countCriteriaIncludeNameRows(String partOfFirstOrLastName,Date begindate
				,Date enddate,String cellNumber,String armName,RESPONSE_TYPE responseType) {
		Criteria cri= session.createCriteria(Response.class);
		if(begindate!=null && enddate!=null){
			cri.add(Restrictions.between("recieveDate", begindate, enddate));
		}
		if(cellNumber!=null){
			cri.add(Restrictions.like("cellNo", cellNumber,MatchMode.END));
		}
		if(partOfFirstOrLastName!=null){
			cri.createAlias("child", "p").add(
					Restrictions.or(Restrictions.like("p.firstName", partOfFirstOrLastName,MatchMode.START)
									,Restrictions.like("p.lastName", partOfFirstOrLastName,MatchMode.START)));
		}
		if(armName!=null){
			cri.createAlias("child", "p").createAlias("p.arm", "parm").add(Restrictions.like("parm.armName", armName,MatchMode.EXACT));
		}
		if(responseType != null){
			cri.add(Restrictions.eq("responseType", responseType));
		}
		cri.setProjection(Projections.rowCount());
		return (Number) cri.uniqueResult();
	}
}