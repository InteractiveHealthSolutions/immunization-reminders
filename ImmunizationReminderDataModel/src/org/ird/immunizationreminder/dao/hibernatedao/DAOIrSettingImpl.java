package org.ird.immunizationreminder.dao.hibernatedao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.ird.immunizationreminder.dao.DAOIrSetting;
import org.ird.immunizationreminder.datamodel.entities.IrSetting;

public class DAOIrSettingImpl extends DAOHibernateImpl implements DAOIrSetting{

	private Session session;

	public DAOIrSettingImpl(Session session) {
		super(session);
		this.session=session;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<IrSetting> getAll() {
		return session.createQuery("from IrSetting").list();
	}
	@Override
	public IrSetting findById(int id) {
			return (IrSetting) session.get(IrSetting.class,id);
	}
	@SuppressWarnings("unchecked")
	@Override
	public IrSetting getSetting(String settingName) {
		List l= session.createCriteria(IrSetting.class)
								.add(Restrictions.like("name", settingName,MatchMode.EXACT)).list();
		if(l.size()>0){
			return (IrSetting) l.get(0);
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<IrSetting> matchByCriteria(String settingName) {
		Criteria cri= session.createCriteria(IrSetting.class)
								.add(Restrictions.like("name", settingName,MatchMode.ANYWHERE));
		return cri.list();
	}
}
