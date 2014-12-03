package org.ird.immunizationreminder.dao.hibernatedao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.immunizationreminder.dao.DAOChild;
import org.ird.immunizationreminder.data.exception.ChildDataException;
import org.ird.immunizationreminder.datamodel.entities.Child;
import org.ird.immunizationreminder.datamodel.entities.Child.STATUS;

public class DAOChildImpl extends DAOHibernateImpl implements DAOChild{
	private Session session;
	private Number LAST_QUERY_TOTAL_ROW__COUNT;

	public DAOChildImpl(Session session) {
		super(session);
		this.session=session;
	}
	@Override
	public Child findById(int id) {
		Child p=(Child) session.get(Child.class,id);
		setLAST_QUERY_TOTAL_ROW__COUNT(p==null?0:1);
		return p;
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
	public List<Child> getAll(int firstResult,int fetchsize,boolean isreadonly/*,FetchMode armFetchMode*/){
		setLAST_QUERY_TOTAL_ROW__COUNT(countAllRows());
		return session.createQuery("from Child order by childId")
								.setFirstResult(firstResult)
								.setMaxResults(fetchsize)
								.setReadOnly(isreadonly)
								.list();
	}
	@Override
	public Number countAllRows(){
		return (Number) session.createQuery("select count(*) from Child").uniqueResult();
	}
	@SuppressWarnings("unchecked")
	@Override
	public Child findByChildID(String childId,boolean isreadonly/*,FetchMode armFetchmode*/) throws ChildDataException {
		List<Child> plist=session.createCriteria(Child.class)
					.add(Restrictions.eq("childId", childId))
					.setReadOnly(isreadonly)
					.list();
		setLAST_QUERY_TOTAL_ROW__COUNT(plist.size());
		if(plist.size()>1){
			throw new ChildDataException(ChildDataException.GIVEN_CHILD_ID_ASSIGNED_TO_MULTIPLE_CHILDREN);
		}
		if(plist.size()>0){
			return plist.get(0);
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Child findByCurrentCellNumber(String currentCellNumber,boolean isreadonly/*,FetchMode armFetchmode*/) throws ChildDataException {
		List<Child> plist=session.createCriteria(Child.class)
					.add(Restrictions.like("currentCellNo", currentCellNumber,MatchMode.END))
					.add(Restrictions.eq("status", STATUS.FOLLOW_UP))
					.setReadOnly(isreadonly)
					.list();
		setLAST_QUERY_TOTAL_ROW__COUNT(plist.size());
		if(plist.size()>1){
			throw new ChildDataException(ChildDataException.GIVEN_CELL_NUMBER_ASSIGNED_TO_MULTIPLE_CHILDREN);
		}
		if(plist.size()>0){
			return plist.get(0);
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Child> findByCurrentCellIgnoreInconsistency(String currentCellNumber,boolean isreadonly/*,FetchMode armFetchmode*/) {
		List<Child> plist=session.createCriteria(Child.class)
					.add(Restrictions.like("currentCellNo", currentCellNumber,MatchMode.END))
					.setReadOnly(isreadonly)
					.addOrder(Order.asc("childId"))
					.list();
		setLAST_QUERY_TOTAL_ROW__COUNT(plist.size());
		return plist;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Child> findByEpiOrMrNumber(String epiOrMrNumber) {
		List<Child> plist=session.createQuery("from Child where mrNumber = '"+epiOrMrNumber+"' order by childId")
															.list();
		setLAST_QUERY_TOTAL_ROW__COUNT(plist.size());
		return plist;
	}
	/**search on given criteria, set values null if you want to exclude any criteria : donot give 
	 * invalid values
	 * @param partOfFirstOrLastName
	 * @param clinic
	 * @param Status
	 * @param putNotWithTeatmentStatus
	 * @param armName
	 * @param firstResult 
	 * @param fetchsize 
	 * @return
	 * @throws ChildDataException 
	 */

	@SuppressWarnings("unchecked")
	@Override
	public List<Child> findByCriteria(String partOfFirstOrLastName,String clinic
			,STATUS followUpStatus,boolean putNotWithStatus,String armName, int firstResult, int fetchsize,boolean isreadonly/*,FetchMode armFetchmode*/) throws ChildDataException {
		setLAST_QUERY_TOTAL_ROW__COUNT(countCriteriaRows(partOfFirstOrLastName, clinic, followUpStatus, putNotWithStatus,armName));
		Criteria cri= session.createCriteria(Child.class);
		if(partOfFirstOrLastName!=null){
			cri.add(Restrictions.or(Restrictions.like("firstName", partOfFirstOrLastName,MatchMode.START)
									,Restrictions.like("lastName", partOfFirstOrLastName,MatchMode.START)));
		}
		if(clinic!=null){
			cri.add(Restrictions.like("clinic", clinic,MatchMode.ANYWHERE));
		}
		if(followUpStatus!=null){
			try{
				if(putNotWithStatus){
					cri.add(Restrictions.not(Restrictions.eq("status", followUpStatus)));
				}else{
					cri.add(Restrictions.eq("status", followUpStatus));
				}
			}catch (Exception e) {
				throw new ChildDataException(ChildDataException.INVALID_CRITERIA_VALUE_SPECIFIED);
			}
		}
		
		if(armName!=null){
			cri.createAlias("arm", "a").add(Restrictions.like("a.armName", armName,MatchMode.EXACT));
		}
		
		return 	 cri.setReadOnly(isreadonly)
					.addOrder(Order.asc("childId"))
					.setFirstResult(firstResult)
					.setMaxResults(fetchsize)
					.list();
	}
	@Override
	public Number countCriteriaRows(String partOfFirstOrLastName,String clinic
			,STATUS followUpStatus,boolean putNotWithStatus,String armName) throws ChildDataException {
		Criteria cri= session.createCriteria(Child.class);
		if(partOfFirstOrLastName!=null){
			cri.add(Restrictions.or(Restrictions.like("firstName", partOfFirstOrLastName,MatchMode.START)
									,Restrictions.like("lastName", partOfFirstOrLastName,MatchMode.START)));
		}
		if(clinic!=null){
			cri.add(Restrictions.like("clinic", clinic,MatchMode.ANYWHERE));
		}
		if(followUpStatus!=null){
			try{
				if(putNotWithStatus){
					cri.add(Restrictions.not(Restrictions.eq("status", followUpStatus)));
				}else{
					cri.add(Restrictions.eq("status", followUpStatus));
				}
			}catch (Exception e) {
				throw new ChildDataException(ChildDataException.INVALID_CRITERIA_VALUE_SPECIFIED);
			}
		}
		if(armName!=null){
			cri.createAlias("arm", "a").add(Restrictions.like("a.armName", armName,MatchMode.EXACT));
		}
		cri.setProjection(Projections.rowCount());
		return (Number) cri.uniqueResult();
	}
}
