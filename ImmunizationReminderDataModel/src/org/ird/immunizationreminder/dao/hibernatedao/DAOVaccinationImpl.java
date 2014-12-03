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
import org.ird.immunizationreminder.dao.DAOVaccination;
import org.ird.immunizationreminder.data.exception.VaccinationDataException;
import org.ird.immunizationreminder.datamodel.entities.Vaccination;
import org.ird.immunizationreminder.datamodel.entities.Vaccination.VACCINATION_STATUS;

public class DAOVaccinationImpl extends DAOHibernateImpl implements DAOVaccination{
	private  Session session;
	private Number LAST_QUERY_TOTAL_ROW__COUNT;

	public DAOVaccinationImpl(Session session) {
		super(session);
		this.session=session;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Vaccination findById(long id,boolean isreadonly,FetchMode childFetchMode
								,FetchMode vaccineFetchMode) {
		List<Vaccination> pv=  session.createCriteria(Vaccination.class)
								.add(Restrictions.eq("vaccinationRecordNum", id))
								.setReadOnly(isreadonly)
								.setFetchMode("child", childFetchMode)
								.setFetchMode("vaccine", vaccineFetchMode)
								.list();
		setLAST_QUERY_TOTAL_ROW__COUNT(pv.size());
		return pv.size()==0?null:pv.get(0);
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
	public List<Vaccination> getAll(int firstResult, int fetchsize,boolean isreadonly,FetchMode childFetchMode
			,FetchMode vaccineFetchMode) {
		setLAST_QUERY_TOTAL_ROW__COUNT(countAllRows());
		return session.createCriteria(Vaccination.class)
							.setReadOnly(isreadonly)
							.setFetchMode("child", childFetchMode)
							.setFetchMode("vaccine", vaccineFetchMode)
							.setFirstResult(firstResult)
							.setMaxResults(fetchsize)
							.addOrder(Order.desc("vaccinationDuedate"))
							.addOrder(Order.desc("vaccinationDate"))
							.list();
	}
	@Override
	public Number countAllRows() {
		return (Number) session.createQuery("select count(*) from Vaccination").uniqueResult();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Vaccination> findByCriteria(String childId,String vaccineName,
			Date dueDatesmaller ,Date dueDategreater,Date vaccinationDatesmaller
			,Date vaccinationDategreater,VACCINATION_STATUS vaccinationStatus, int firstResult, int fetchsize
			,boolean isreadonly,FetchMode childFetchMode
			,FetchMode vaccineFetchMode) throws VaccinationDataException {
		setLAST_QUERY_TOTAL_ROW__COUNT(countCriteriaRows(childId, vaccineName, dueDatesmaller, dueDategreater, vaccinationDatesmaller, vaccinationDategreater, vaccinationStatus));
		
		Criteria cri=session.createCriteria(Vaccination.class);
		if(childId!=null){
			cri.createAlias("child", "p").add(Restrictions.eq("p.childId", childId));
		}
		if(vaccineName!=null){
			cri.createAlias("vaccine", "v").add(Restrictions.eq("v.name",vaccineName));
		}
		if(dueDatesmaller!=null && dueDategreater!=null){
			cri.add(Restrictions.between("vaccinationDuedate", dueDatesmaller, dueDategreater));
		}
		if(vaccinationDatesmaller!=null && vaccinationDategreater !=null){
			cri.add(Restrictions.between("vaccinationDate", vaccinationDatesmaller, vaccinationDategreater));
		}
		if(vaccinationStatus!=null){
			try{
				cri.add(Restrictions.eq("vaccinationStatus", vaccinationStatus));
			}catch (Exception e) {
				throw new VaccinationDataException(VaccinationDataException.INVALID_CRITERIA_VALUE_SPECIFIED);
			}
		}
		return cri.addOrder(Order.desc("vaccinationDuedate"))
							.addOrder(Order.desc("vaccinationDate"))
							.setReadOnly(isreadonly)
							.setFetchMode("child", childFetchMode)
							.setFetchMode("vaccine", vaccineFetchMode)
							.setFirstResult(firstResult)
							.setMaxResults(fetchsize)
							
							.list();
	}
	@Override
	public Number countCriteriaRows(String childId,String vaccineName,
			Date dueDatesmaller ,Date dueDategreater,Date vaccinationDatesmaller
			,Date vaccinationDategreater,VACCINATION_STATUS vaccinationStatus) throws VaccinationDataException {
		Criteria cri=session.createCriteria(Vaccination.class);
		if(childId!=null){
			cri.createAlias("child", "p").add(Restrictions.eq("p.childId", childId));
		}
		if(vaccineName!=null){
			cri.createAlias("vaccine", "v").add(Restrictions.eq("v.name",vaccineName));
		}
		if(dueDatesmaller!=null && dueDategreater!=null){
			cri.add(Restrictions.between("vaccinationDuedate", dueDatesmaller, dueDategreater));
		}
		if(vaccinationDatesmaller!=null && vaccinationDategreater !=null){
			cri.add(Restrictions.between("vaccinationDate", vaccinationDatesmaller, vaccinationDategreater));
		}
		if(vaccinationStatus!=null){
			try{
				cri.add(Restrictions.eq("vaccinationStatus", vaccinationStatus));
			}catch (Exception e) {
				throw new VaccinationDataException(VaccinationDataException.INVALID_CRITERIA_VALUE_SPECIFIED);
			}
		}
		cri.setProjection(Projections.rowCount());
		return (Number) cri.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Vaccination> findByCriteriaIncludeName(String partOfName,String vaccineName,
			Date dueDatesmaller ,Date dueDategreater,Date vaccinationDatesmaller
			,Date vaccinationDategreater,VACCINATION_STATUS vaccinationStatus,String armName
			, int firstResult, int fetchsize,boolean isreadonly,FetchMode childFetchMode
			,FetchMode vaccineFetchMode) throws VaccinationDataException {
		
		setLAST_QUERY_TOTAL_ROW__COUNT(countCriteriaIncludeNameRows(partOfName, vaccineName, dueDatesmaller, dueDategreater, vaccinationDatesmaller, vaccinationDategreater, vaccinationStatus,armName));
		
		Criteria cri=session.createCriteria(Vaccination.class);
		
		cri.createAlias("child", "p");
		if(partOfName!=null){
			cri.add(
					Restrictions.or(Restrictions.like("p.firstName", partOfName,MatchMode.START)
									,Restrictions.like("p.lastName", partOfName,MatchMode.START)));
		}
		if(vaccineName!=null){
			cri.createAlias("vaccine", "v").add(Restrictions.eq("v.name",vaccineName));
		}
		if(dueDatesmaller!=null && dueDategreater!=null){
			cri.add(Restrictions.between("vaccinationDuedate", dueDatesmaller, dueDategreater));
		}
		if(vaccinationDatesmaller!=null && vaccinationDategreater !=null){
			cri.add(Restrictions.between("vaccinationDate", vaccinationDatesmaller, vaccinationDategreater));
		}
		if(vaccinationStatus!=null){
			try{
				cri.add(Restrictions.eq("vaccinationStatus", vaccinationStatus));
			}catch (Exception e) {
				throw new VaccinationDataException(VaccinationDataException.INVALID_CRITERIA_VALUE_SPECIFIED);
			}
		}
		if(armName!=null){
			cri.createAlias("p.arm", "parm").add(Restrictions.like("parm.armName", armName,MatchMode.EXACT));
		}
		return cri.addOrder(Order.desc("vaccinationDuedate"))
							.addOrder(Order.desc("vaccinationDate"))
							.setReadOnly(isreadonly)
							.setFetchMode("child", childFetchMode)
							.setFetchMode("vaccine", vaccineFetchMode)
							.setFirstResult(firstResult)
							.setMaxResults(fetchsize)
							
							.list();
	}
	@Override
	public Number countCriteriaIncludeNameRows(String partOfName,String vaccineName,
			Date dueDatesmaller ,Date dueDategreater,Date vaccinationDatesmaller
			,Date vaccinationDategreater,VACCINATION_STATUS vaccinationStatus,String armName) throws VaccinationDataException {
		Criteria cri=session.createCriteria(Vaccination.class);
		if(partOfName!=null){
			cri.createAlias("child", "p").add(
					Restrictions.or(Restrictions.like("p.firstName", partOfName,MatchMode.START)
									,Restrictions.like("p.lastName", partOfName,MatchMode.START)));
		}
		if(vaccineName!=null){
			cri.createAlias("vaccine", "v").add(Restrictions.eq("v.name",vaccineName));
		}
		if(dueDatesmaller!=null && dueDategreater!=null){
			cri.add(Restrictions.between("vaccinationDuedate", dueDatesmaller, dueDategreater));
		}
		if(vaccinationDatesmaller!=null && vaccinationDategreater !=null){
			cri.add(Restrictions.between("vaccinationDate", vaccinationDatesmaller, vaccinationDategreater));
		}
		if(vaccinationStatus!=null){
			try{
				cri.add(Restrictions.eq("vaccinationStatus", vaccinationStatus));
			}catch (Exception e) {
				throw new VaccinationDataException(VaccinationDataException.INVALID_CRITERIA_VALUE_SPECIFIED);
			}
		}
		if(armName!=null){
			cri.createAlias("child", "p").createAlias("p.arm", "parm").add(Restrictions.like("parm.armName", armName,MatchMode.EXACT));
		}
		cri.setProjection(Projections.rowCount());
		return (Number) cri.uniqueResult();
	}
}
