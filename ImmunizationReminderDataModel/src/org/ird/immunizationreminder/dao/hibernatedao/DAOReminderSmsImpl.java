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
import org.ird.immunizationreminder.dao.DAOReminderSms;
import org.ird.immunizationreminder.data.exception.ReminderDataException;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms.REMINDER_STATUS;

/**
 * @author maimoonak
 *
 */
public class DAOReminderSmsImpl extends DAOHibernateImpl implements DAOReminderSms{

	private Session session;
	private Number LAST_QUERY_TOTAL_ROW__COUNT;
	
	public DAOReminderSmsImpl(Session session) {
		super(session);
		this.session=session;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.immunizationreminder.dao.DAOReminderSms#findById(long, boolean, org.hibernate.FetchMode)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ReminderSms findById(long id,boolean isreadonly,FetchMode collectionFetchMode) {
		List<ReminderSms> rsms= session.createCriteria(ReminderSms.class)
								.add(Restrictions.eq("rsmsRecordNum", id))
								.setFetchMode("child", collectionFetchMode)
								.setFetchMode("reminder", collectionFetchMode)
								.setFetchMode("vaccine", collectionFetchMode)
								.setReadOnly(isreadonly)
								.list();
		setLAST_QUERY_TOTAL_ROW__COUNT(rsms.size());
		return rsms.size()==0?null:rsms.get(0);
	}
	private void setLAST_QUERY_TOTAL_ROW__COUNT(Number LAST_QUERY_TOTAL_ROW__COUNT) {
		this.LAST_QUERY_TOTAL_ROW__COUNT = LAST_QUERY_TOTAL_ROW__COUNT;
	}
	@Override
	public Number LAST_QUERY_TOTAL_ROW__COUNT() {
		return LAST_QUERY_TOTAL_ROW__COUNT;
	}
	

	/* (non-Javadoc)
	 * @see org.ird.immunizationreminder.dao.DAOReminderSms#getAll(int, int, boolean, org.hibernate.FetchMode)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ReminderSms> getAll(int firstResult, int fetchsize,boolean isreadonly,FetchMode collectionFetchmode) {
		setLAST_QUERY_TOTAL_ROW__COUNT(countAllRows());
		return session.createCriteria(ReminderSms.class)
								.addOrder(Order.desc("dueDate"))
								.setReadOnly(isreadonly)
								.setFetchMode("child", collectionFetchmode)
								.setFetchMode("reminder", collectionFetchmode)
								.setFetchMode("vaccine", collectionFetchmode)
								.setFirstResult(firstResult)
								.setMaxResults(fetchsize)
								.list();
	}
	@Override
	public Number countAllRows() {
		return (Number) session.createQuery("select count(*) from ReminderSms").uniqueResult();
	}
	
	/* (non-Javadoc)
	 * @see org.ird.immunizationreminder.dao.DAOReminderSms#findByCriteria(java.lang.String, java.lang.String, java.lang.String, java.util.Date, java.util.Date, java.util.Date, java.util.Date, java.lang.String, boolean, int, int, boolean, org.hibernate.FetchMode)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ReminderSms> findByCriteria(String childId,String reminderName,String vaccineName
			,Date reminderDuedatesmaller,Date reminderDuedategreater,Date reminderSentdatesmaller
			,Date reminderSentdategreater,REMINDER_STATUS reminderStatus,boolean putNotWithReminderStatus
			, int firstResult, int fetchsize,boolean isreadonly,FetchMode collectionFetchmode) throws ReminderDataException {
		
		setLAST_QUERY_TOTAL_ROW__COUNT(countCriteriaRows(childId, reminderName, vaccineName, reminderDuedatesmaller, reminderDuedategreater, reminderSentdatesmaller, reminderSentdategreater, reminderStatus, putNotWithReminderStatus));
		Criteria cri=session.createCriteria(ReminderSms.class);
		if(childId!=null){
			cri.createAlias("child", "p").add(Restrictions.eq("p.childId", childId));
		}
		if(reminderName!=null){
			cri.createAlias("reminder", "r").add(Restrictions.eq("r.name", reminderName));
		}
		if(vaccineName!=null){
			cri.createAlias("vaccine", "v").add(Restrictions.eq("v.name",vaccineName));
		}
		if(reminderDuedatesmaller!=null && reminderDuedategreater!=null){
			cri.add(Restrictions.between("dueDate", reminderDuedatesmaller, reminderDuedategreater));
		}
		if(reminderSentdatesmaller!=null && reminderSentdategreater !=null){
			cri.add(Restrictions.between("sentDate", reminderSentdatesmaller, reminderSentdategreater));
		}
		if(reminderStatus!=null){
			try{
				if(putNotWithReminderStatus){
					cri.add(Restrictions.not(Restrictions.eq("reminderStatus", reminderStatus)));
				}else{
					cri.add(Restrictions.eq("reminderStatus", reminderStatus));
				}
			}catch (Exception e) {
				throw new ReminderDataException(ReminderDataException.INVALID_CRITERIA_VALUE_SPECIFIED);
			}
		}
		return cri.addOrder(Order.desc("dueDate"))
					.setFetchMode("child", collectionFetchmode)
					.setFetchMode("reminder", collectionFetchmode)
					.setFetchMode("vaccine", collectionFetchmode)
					.setFirstResult(firstResult)
					.setMaxResults(fetchsize)
					.setReadOnly(isreadonly)
					.list();
	}
	
	@Override
	public Number countCriteriaRows(String childId,String reminderName,String vaccineName
			,Date reminderDuedatesmaller,Date reminderDuedategreater,Date reminderSentdatesmaller
			,Date reminderSentdategreater,REMINDER_STATUS reminderStatus,boolean putNotWithReminderStatus) throws ReminderDataException {
		
		Criteria cri=session.createCriteria(ReminderSms.class);
		if(childId!=null){
			cri.createAlias("child", "p").add(Restrictions.eq("p.childId", childId));
		}
		if(reminderName!=null){
			cri.createAlias("reminder", "r").add(Restrictions.eq("r.name", reminderName));
		}
		if(vaccineName!=null){
			cri.createAlias("vaccine", "v").add(Restrictions.eq("v.name",vaccineName));
		}
		if(reminderDuedatesmaller!=null && reminderDuedategreater!=null){
			cri.add(Restrictions.between("dueDate", reminderDuedatesmaller, reminderDuedategreater));
		}
		if(reminderSentdatesmaller!=null && reminderSentdategreater !=null){
			cri.add(Restrictions.between("sentDate", reminderSentdatesmaller, reminderSentdategreater));
		}
		if(reminderStatus!=null){
			try{
				if(putNotWithReminderStatus){
					cri.add(Restrictions.not(Restrictions.eq("reminderStatus", reminderStatus)));
				}else{
					cri.add(Restrictions.eq("reminderStatus", reminderStatus));
				}
			}catch (Exception e) {
				throw new ReminderDataException(ReminderDataException.INVALID_CRITERIA_VALUE_SPECIFIED);
			}
		}
		cri.setProjection(Projections.rowCount());
		return (Number) cri.uniqueResult();
	}
	
	/* (non-Javadoc)
	 * @see org.ird.immunizationreminder.dao.DAOReminderSms#findByCriteriaIncludeName(java.lang.String, java.lang.String, java.lang.String, java.util.Date, java.util.Date, java.util.Date, java.util.Date, java.lang.String, java.lang.String, boolean, java.lang.String, int, int, boolean, org.hibernate.FetchMode)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ReminderSms> findByCriteriaIncludeName(String partOfName,String reminderName,String vaccineName
			,Date reminderDuedatesmaller,Date reminderDuedategreater,Date reminderSentdatesmaller
			,Date reminderSentdategreater,String cellnumber,REMINDER_STATUS reminderStatus,boolean putNotWithReminderStatus
			,String armName, int firstResult, int fetchsize,boolean isreadonly,FetchMode collectionFetchmode) throws ReminderDataException {
		setLAST_QUERY_TOTAL_ROW__COUNT(countCriteriaIncludeNameRows(partOfName, reminderName, vaccineName, reminderDuedatesmaller, reminderDuedategreater, reminderSentdatesmaller, reminderSentdategreater, cellnumber ,reminderStatus, putNotWithReminderStatus,armName));
		Criteria cri=session.createCriteria(ReminderSms.class);
		
		cri.createAlias("child", "p");
		if(partOfName!=null){
			cri.add(Restrictions.or(Restrictions.like("p.firstName", partOfName,MatchMode.START)
									,Restrictions.like("p.lastName", partOfName,MatchMode.START)));
		}
		if(reminderName!=null){
			cri.createAlias("reminder", "r").add(Restrictions.eq("r.name", reminderName));
		}
		if(vaccineName!=null){
			cri.createAlias("vaccine", "v").add(Restrictions.eq("v.name",vaccineName));
		}
		if(reminderDuedatesmaller!=null && reminderDuedategreater!=null){
			cri.add(Restrictions.between("dueDate", reminderDuedatesmaller, reminderDuedategreater));
		}
		if(reminderSentdatesmaller!=null && reminderSentdategreater !=null){
			cri.add(Restrictions.between("sentDate", reminderSentdatesmaller, reminderSentdategreater));
		}
		if(cellnumber!=null){
			cri.add(Restrictions.like("cellnumber", cellnumber,MatchMode.END));
		}
		if(reminderStatus!=null){
			try{
				if(putNotWithReminderStatus){
					cri.add(Restrictions.not(Restrictions.eq("reminderStatus", reminderStatus)));
				}else{
					cri.add(Restrictions.eq("reminderStatus", reminderStatus));
				}
			}catch (Exception e) {
				throw new ReminderDataException(ReminderDataException.INVALID_CRITERIA_VALUE_SPECIFIED);
			}
		}
		if(armName!=null){
			cri.createAlias("p.arm", "parm").add(Restrictions.like("parm.armName", armName,MatchMode.EXACT));
		}
		return cri.addOrder(Order.desc("dueDate"))
					.setFetchMode("child", collectionFetchmode)
					.setFetchMode("reminder", collectionFetchmode)
					.setFetchMode("vaccine", collectionFetchmode)
					.setFirstResult(firstResult)
					.setMaxResults(fetchsize)
					.setReadOnly(isreadonly)
					.list();
	}
	
	@Override
	public Number countCriteriaIncludeNameRows(String partOfName,String reminderName,String vaccineName
			,Date reminderDuedatesmaller,Date reminderDuedategreater,Date reminderSentdatesmaller
			,Date reminderSentdategreater,String cellnumber,REMINDER_STATUS reminderStatus,boolean putNotWithReminderStatus,String armName) throws ReminderDataException {
		
		Criteria cri=session.createCriteria(ReminderSms.class);
		if(partOfName!=null){
		cri.createAlias("child", "p").add(
					Restrictions.or(Restrictions.like("p.firstName", partOfName,MatchMode.START)
									,Restrictions.like("p.lastName", partOfName,MatchMode.START)));
		}
		if(reminderName!=null){
			cri.createAlias("reminder", "r").add(Restrictions.eq("r.name", reminderName));
		}
		if(vaccineName!=null){
			cri.createAlias("vaccine", "v").add(Restrictions.eq("v.name",vaccineName));
		}
		if(reminderDuedatesmaller!=null && reminderDuedategreater!=null){
			cri.add(Restrictions.between("dueDate", reminderDuedatesmaller, reminderDuedategreater));
		}
		if(reminderSentdatesmaller!=null && reminderSentdategreater !=null){
			cri.add(Restrictions.between("sentDate", reminderSentdatesmaller, reminderSentdategreater));
		}
		if(cellnumber!=null){
			cri.add(Restrictions.like("cellnumber", cellnumber,MatchMode.END));
		}
		if(reminderStatus!=null){
			try{
				if(putNotWithReminderStatus){
					cri.add(Restrictions.not(Restrictions.eq("reminderStatus", reminderStatus)));
				}else{
					cri.add(Restrictions.eq("reminderStatus", reminderStatus));
				}
			}catch (Exception e) {
				throw new ReminderDataException(ReminderDataException.INVALID_CRITERIA_VALUE_SPECIFIED);
			}
		}
		if(armName!=null){
			cri.createAlias("child", "p").createAlias("p.arm", "parm").add(Restrictions.like("parm.armName", armName,MatchMode.EXACT));
		}
		cri.setProjection(Projections.rowCount());
		return (Number) cri.uniqueResult();
	}

	/* (non-Javadoc)
	 * @see org.ird.immunizationreminder.dao.DAOReminderSms#findByCriteria(java.lang.String, java.lang.String, java.lang.String, java.util.Date, java.util.Date, java.lang.String, boolean, java.lang.String, java.lang.String, boolean, org.hibernate.FetchMode)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ReminderSms> findByCriteria(String childId,String reminderName,String vaccineName
			,Date reminderDuedatesmaller,Date reminderDuedategreater,REMINDER_STATUS reminderStatus
			,boolean putNotWithReminderStatus,String dayNumber,String vaccinationRecordNum
			,boolean isreadonly,FetchMode collectionFetchmode) throws ReminderDataException {
		Criteria cri=session.createCriteria(ReminderSms.class);
		if(childId!=null){
			cri.createAlias("child", "p").add(Restrictions.eq("p.childId", childId));
		}
		if(reminderName!=null){
			cri.createAlias("reminder", "r").add(Restrictions.eq("r.name", reminderName));
		}
		if(vaccineName!=null){
			cri.createAlias("vaccine", "v").add(Restrictions.eq("v.name",vaccineName));
		}
		if(reminderDuedatesmaller!=null && reminderDuedategreater!=null){
			cri.add(Restrictions.between("dueDate", reminderDuedatesmaller, reminderDuedategreater));
		}
		if(reminderStatus!=null){
			try{
				if(putNotWithReminderStatus){
					cri.add(Restrictions.not(Restrictions.eq("reminderStatus", reminderStatus)));
				}else{
					cri.add(Restrictions.eq("reminderStatus", reminderStatus));
				}
			}catch (Exception e) {
				throw new ReminderDataException(ReminderDataException.INVALID_CRITERIA_VALUE_SPECIFIED);
			}
		}
		if(dayNumber!=null){
			try{
				int day=Integer.parseInt(dayNumber);
				cri.add(Restrictions.eq("dayNumber", day));
			}catch (Exception e) {
				throw new ReminderDataException(ReminderDataException.INVALID_CRITERIA_VALUE_SPECIFIED);
			}
		}
		if(vaccinationRecordNum!=null){
			try{
				long vacc=Long.parseLong(vaccinationRecordNum);
				cri.add(Restrictions.eq("vaccinationRecordNum", vacc));
			}catch (Exception e) {
				throw new ReminderDataException(ReminderDataException.INVALID_CRITERIA_VALUE_SPECIFIED);
			}
		}
		List<ReminderSms> lst = cri.addOrder(Order.desc("dueDate"))
					.setFetchMode("child", collectionFetchmode)
					.setFetchMode("reminder", collectionFetchmode)
					.setFetchMode("vaccine", collectionFetchmode)
					.setReadOnly(isreadonly).list();
		
		setLAST_QUERY_TOTAL_ROW__COUNT(lst.size());
		return lst;
	}
}
