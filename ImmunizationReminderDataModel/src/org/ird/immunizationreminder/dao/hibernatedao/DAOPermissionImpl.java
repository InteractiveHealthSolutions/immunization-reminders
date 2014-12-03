package org.ird.immunizationreminder.dao.hibernatedao;

import java.util.List;

import org.hibernate.Session;
import org.ird.immunizationreminder.dao.DAOPermission;
import org.ird.immunizationreminder.datamodel.entities.Permission;

public class DAOPermissionImpl extends DAOHibernateImpl implements DAOPermission{
	private Session session;

	public DAOPermissionImpl(Session session) {
		super(session);
		this.session=session;
	}
	@Override
	public Permission findById(int id) {
		return (Permission) session.get(Permission.class,id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Permission> getAll(boolean isreadonly) {
		return session.createQuery("from Permission order by name").setReadOnly(isreadonly).list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public Permission findByPermissionName(String permissionName,boolean isreadonly) {
		List<Permission> plist=session.createQuery("from Permission where name=?").setString(0, permissionName).setReadOnly(isreadonly).list();
		if(plist.size()>0){
			return plist.get(0);
		}
		return null;
	}
}
