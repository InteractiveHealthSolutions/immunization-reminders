package org.ird.immunizationreminder.dao.hibernatedao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ird.immunizationreminder.dao.DAORole;
import org.ird.immunizationreminder.datamodel.entities.Role;

public class DAORoleImpl extends DAOHibernateImpl implements DAORole{
	private Session session;

	public DAORoleImpl(Session session) {
		super(session);
		this.session=session;
	}
	@Override
	public Role findById(int id) {
		return (Role) session.get(Role.class,id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Role findByName(String roleName) {
		List<Role> rlist=session.createQuery("from Role where name=?").setString(0, roleName).list();
		if(rlist.size()>0){
			return rlist.get(0);
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getAll() {
		return session.createQuery("from Role order by name").list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Role> findByCriteria(String roleName) {
		List<Role> results = session.createCriteria(Role.class)
					.add(Restrictions.like("name", roleName,MatchMode.START)).addOrder(Order.asc("name")).list();
		return results;
	}
}
