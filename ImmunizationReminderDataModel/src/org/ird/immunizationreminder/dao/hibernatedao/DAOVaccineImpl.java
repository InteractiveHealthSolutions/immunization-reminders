package org.ird.immunizationreminder.dao.hibernatedao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.ird.immunizationreminder.dao.DAOVaccine;
import org.ird.immunizationreminder.datamodel.entities.Vaccine;

public class DAOVaccineImpl extends DAOHibernateImpl implements DAOVaccine{
	private Session session;
	private Number LAST_QUERY_TOTAL_ROW__COUNT;

	public DAOVaccineImpl(Session session) {
		super(session);
		this.session=session;
	}
	@Override
	public Vaccine findById(int id) {
		Vaccine vacc= (Vaccine) session.get(Vaccine.class,id);
		setLAST_QUERY_TOTAL_ROW__COUNT(vacc==null?0:1);
		return vacc;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vaccine getByName(String vaccineName) {
		List<Vaccine> v=session.createQuery("from Vaccine where name ='"+vaccineName+"' order by name").list();
		setLAST_QUERY_TOTAL_ROW__COUNT(v.size());
		if(v.size()>0){
			return v.get(0);
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Vaccine getByNameInForm(String vaccineName) {
		List<Vaccine> v=session.createQuery("from Vaccine where vaccineNameInForm ='"+vaccineName+"' order by name").list();
		setLAST_QUERY_TOTAL_ROW__COUNT(v.size());
		if(v.size()>0){
			return v.get(0);
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Vaccine getByNumberInForm(int vaccineNum) {
		List<Vaccine> v=session.createQuery("from Vaccine where vaccineNumberInForm =? order by name").setInteger(0, vaccineNum).list();
		setLAST_QUERY_TOTAL_ROW__COUNT(v.size());
		if(v.size()>0){
			return v.get(0);
		}
		return null;
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
	public List<Vaccine> getAll(int firstResult, int fetchsize) {
		setLAST_QUERY_TOTAL_ROW__COUNT(countAllRows());
		return session.createQuery("from Vaccine order by name").setFirstResult(firstResult).setMaxResults(fetchsize).list();
	}
	@Override
	public Number countAllRows() {
		return (Number) session.createQuery("select count(*) from Vaccine").uniqueResult();
	}
	/** vaccineName should not be null
	 * 
	 * @see org.ird.immunizationreminder.dao.DAOVaccine#findVaccine(java.lang.String, int, int)
	 **/
	@SuppressWarnings("unchecked")
	@Override
	public List<Vaccine> findVaccine(String vaccineName, int firstResult, int fetchsize) {
		setLAST_QUERY_TOTAL_ROW__COUNT(countVaccineRows(vaccineName));
		Query q=session.createQuery("from Vaccine where name like '"+vaccineName+"%' order by name");
		return q.setFirstResult(firstResult).setMaxResults(fetchsize).list();
	}
	@Override
	public Number countVaccineRows(String vaccineName) {
		Query q=session.createQuery("select count(*) from Vaccine where name like '"+vaccineName+"%'");
		return (Number) q.uniqueResult();
	}
}
