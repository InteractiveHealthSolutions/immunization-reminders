/*package org.ird.immunizationreminder.dao.hibernatedao;

// Generated May 2, 2011 12:48:03 PM by Hibernate Tools 3.2.0.b9

import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.immunizationreminder.dao.DAOUserSms;
import org.ird.immunizationreminder.data.exception.DataException;
import org.ird.immunizationreminder.datamodel.entities.Vaccination;
import org.ird.immunizationreminder.datamodel.entities.UserSms;
import org.ird.immunizationreminder.datamodel.entities.User.STATUS;

*//**
 * Home object for domain model class UserSms.
 * @see org.ird.immunizationreminder.datamodel.entities.UserSms
 * @author Hibernate Tools
 *//*
public class DAOUserSmsImpl extends DAOHibernateImpl implements DAOUserSms{
	private SessionFactory sessionFactory;
	private Number LAST_QUERY_TOTAL_ROW__COUNT;

	public DAOUserSmsImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
		this.sessionFactory=sessionFactory;
	}
	@Override
	public UserSms findById(long id) {
		UserSms usms= (UserSms) sessionFactory.getCurrentSession().get(UserSms.class,id);
		setLAST_QUERY_TOTAL_ROW__COUNT(usms==null?0:1);
		return usms;
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
	public List<UserSms> getAll(int firstResult, int fetchsize) {
		setLAST_QUERY_TOTAL_ROW__COUNT(countAllRows());
		return sessionFactory.getCurrentSession().createQuery("from UserSms order by dueDate desc").setFirstResult(firstResult).setMaxResults(fetchsize).list();
	}
	@Override
	public Number countAllRows() {
		return (Number) sessionFactory.getCurrentSession().createQuery("select count(*) from UserSms").uniqueResult();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<UserSms> findByCriteria(Date duedatesmaller,Date duedategreater,Date sentdatesmaller,
			Date sentdategreater,String smsStatus,boolean putNotWithSmsStatus, int firstResult, int fetchsize) throws DataException {
		setLAST_QUERY_TOTAL_ROW__COUNT(countCriteriaRows(duedatesmaller, duedategreater, sentdatesmaller, sentdategreater, smsStatus, putNotWithSmsStatus));
		Criteria cri=sessionFactory.getCurrentSession().createCriteria(Vaccination.class);

		if(duedatesmaller!=null && duedategreater!=null){
			cri.add(Restrictions.between("dueDate", duedatesmaller, duedategreater));
		}
		if(sentdatesmaller!=null && sentdategreater !=null){
			cri.add(Restrictions.between("sentdate", sentdatesmaller, sentdategreater));
		}
		if(smsStatus!=null){
			try{
				STATUS status=STATUS.valueOf(smsStatus);
				if(putNotWithSmsStatus){
					cri.add(Restrictions.not(Restrictions.like("status", status.toString(),MatchMode.EXACT)));
				}else{
					cri.add(Restrictions.like("status", status.toString(),MatchMode.EXACT));
				}
			}catch (Exception e) {
				throw new DataException(DataException.INVALID_CRITERIA_VALUE_SPECIFIED);
			}
		}
		return cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
	}
	@Override
	public Number countCriteriaRows(Date duedatesmaller,Date duedategreater,Date sentdatesmaller,
			Date sentdategreater,String smsStatus,boolean putNotWithSmsStatus) throws DataException {
		Criteria cri=sessionFactory.getCurrentSession().createCriteria(Vaccination.class);

		if(duedatesmaller!=null && duedategreater!=null){
			cri.add(Restrictions.between("dueDate", duedatesmaller, duedategreater));
		}
		if(sentdatesmaller!=null && sentdategreater !=null){
			cri.add(Restrictions.between("sentdate", sentdatesmaller, sentdategreater));
		}
		if(smsStatus!=null){
			try{
				STATUS status=STATUS.valueOf(smsStatus);
				if(putNotWithSmsStatus){
					cri.add(Restrictions.not(Restrictions.like("status", status.toString(),MatchMode.EXACT)));
				}else{
					cri.add(Restrictions.like("status", status.toString(),MatchMode.EXACT));
				}
			}catch (Exception e) {
				throw new DataException(DataException.INVALID_CRITERIA_VALUE_SPECIFIED);
			}
		}
		cri.setProjection(Projections.rowCount());
		return (Number) cri.uniqueResult();
	}
}
*/