package org.ird.immunizationreminder.dao.hibernatedao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.ird.immunizationreminder.dao.DAOArmDayReminder;
import org.ird.immunizationreminder.datamodel.entities.ArmDayReminder;
import org.ird.immunizationreminder.datamodel.entities.ArmDayReminderId;


public class DAOArmDayReminderImpl extends DAOHibernateImpl implements DAOArmDayReminder {

	private Session session;

	public DAOArmDayReminderImpl(Session session) {
		super(session);
		this.session=session;
	}
	@Override
	public ArmDayReminder findById(ArmDayReminderId armdayReminderID) {
			return (ArmDayReminder) session.get("ArmDayReminder",armdayReminderID);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ArmDayReminder> getAll(){
		return session.createQuery("from ArmDayReminder").list();
	}
	
	/**
	 * find records by specified criteria, keep arguments null you want to exclude from criteria,
	 * invalid values will automatically excluded from search criteria.
	 * @param armId
	 * @param vaccineId
	 * @param reminderId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ArmDayReminder> findByIdsCriteria(String armId,String vaccineId,String reminderId,String dayNum) {
		try{Integer.parseInt(armId);
		}catch (Exception e) {
			armId=null;//TODO throw exception instead of returning whole results to ensure proper on web interface
		}
		try{Integer.parseInt(vaccineId);
		}catch (Exception e) {
			vaccineId=null;
		}
		try{Integer.parseInt(reminderId);
		}catch (Exception e) {
			reminderId=null;
		}
		try{Integer.parseInt(dayNum);
		}catch (Exception e) {
			dayNum=null;
		}
		Criteria cri=session.createCriteria(ArmDayReminder.class);
		if(armId!=null){
			cri.createAlias("arm", "a").add(Restrictions.eq("a.armId",Integer.parseInt(armId)));
		}
		if(vaccineId!=null){
			cri.createAlias("vaccine", "v").add(Restrictions.eq("v.vaccineId",Integer.parseInt(vaccineId)));
		}
		if(reminderId!=null){
			cri.createAlias("reminder", "r").add(Restrictions.eq("r.reminderId",Integer.parseInt(reminderId)));
		}
		if(dayNum!=null){
			cri.add(Restrictions.eq("id.dayNumber",Integer.parseInt(dayNum)));
		}
		return cri.list();
	}
	/**
	 * find records by specified criteria, keep arguments null you want to exclude from criteria,
	 * invalid values will automatically excluded from search criteria.
	 * @param armId
	 * @param vaccineId
	 * @param reminderId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ArmDayReminder> findByNamesCriteria(String armName,String vaccineName,String reminderName) {
		Criteria cri=session.createCriteria(ArmDayReminder.class);
		if(armName!=null){
			cri.createAlias("arm", "a").add(Restrictions.eq("a.armName",Integer.parseInt(armName)));
		}
		if(vaccineName!=null){
			cri.createAlias("vaccine", "v").add(Restrictions.eq("v.vaccineName",Integer.parseInt(vaccineName)));
		}
		if(reminderName!=null){
			cri.createAlias("reminder", "r").add(Restrictions.eq("r.name",Integer.parseInt(reminderName)));
		}
		return cri.list();
	}
}
