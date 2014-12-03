package org.ird.immunizationreminder.dao.hibernatedao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.immunizationreminder.dao.DAOReminder;
import org.ird.immunizationreminder.datamodel.entities.Reminder;

public class DAOReminderImpl extends DAOHibernateImpl implements DAOReminder{
	private Session session;
	private Number LAST_QUERY_TOTAL_ROW__COUNT;

	public DAOReminderImpl(Session session) {
		super(session);
		this.session=session;
	}
	@Override
	public Reminder findById(int id) {
		Reminder rem= (Reminder) session.get(Reminder.class,id);
		setLAST_QUERY_TOTAL_ROW__COUNT(rem==null?0:1);
		return rem;
	}
	private void setLAST_QUERY_TOTAL_ROW__COUNT(Number LAST_QUERY_TOTAL_ROW__COUNT) {
		this.LAST_QUERY_TOTAL_ROW__COUNT = LAST_QUERY_TOTAL_ROW__COUNT;
	}
	@Override
	public Number LAST_QUERY_TOTAL_ROW__COUNT() {
		return LAST_QUERY_TOTAL_ROW__COUNT;
	}
	@Override
	public Number countAllRows() {
		return (Number) session.createQuery("select count(*) from Reminder").uniqueResult();
	}
	
	/* (non-Javadoc)
	 * @see org.ird.immunizationreminder.dao.DAOReminder#getAll(int, int, boolean, org.hibernate.FetchMode)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Reminder> getAll(int firstResult, int fetchsize,boolean isreadonly,FetchMode remTextFetchmode) {
		setLAST_QUERY_TOTAL_ROW__COUNT(countAllRows());
		return session.createCriteria(Reminder.class)
					.addOrder(Order.asc("name"))
					.setReadOnly(isreadonly)
					.setFetchMode("reminderText", remTextFetchmode)
					.setFirstResult(firstResult)
					.setMaxResults(fetchsize)
					.list();
	}
	
	/* (non-Javadoc)
	 * @see org.ird.immunizationreminder.dao.DAOReminder#getReminderByName(java.lang.String, boolean, org.hibernate.FetchMode)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Reminder getReminderByName(String reminderName,boolean isreadonly,FetchMode remTextFetchmode) {
		List<Reminder> r=session.createCriteria(Reminder.class)
							.add(Restrictions.like("name", reminderName,MatchMode.EXACT))
							.setReadOnly(isreadonly)
							.setFetchMode("reminderText", remTextFetchmode)
							.list();
		setLAST_QUERY_TOTAL_ROW__COUNT(r.size());
		if(r.size()>0){
			return r.get(0);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.immunizationreminder.dao.DAOReminder#findReminder(java.lang.String, int, int, boolean, org.hibernate.FetchMode)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Reminder> findReminder(String reminderName, int firstResult, int fetchsize,boolean isreadonly,FetchMode remTextFetchmode) {
		setLAST_QUERY_TOTAL_ROW__COUNT(countReminderRows(reminderName));
		Criteria cri=session.createCriteria(Reminder.class)
						.add(Restrictions.like("name", reminderName,MatchMode.ANYWHERE))
						.setReadOnly(isreadonly)
						.setFetchMode("reminderText", remTextFetchmode);
								
		return cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
	}
	
	@Override
	public Number countReminderRows(String reminderName) {
		Criteria cri=session.createCriteria(Reminder.class)
					.add(Restrictions.like("name", reminderName,MatchMode.ANYWHERE));
		cri.setProjection(Projections.rowCount());
		return (Number) cri.uniqueResult();
	}
}
