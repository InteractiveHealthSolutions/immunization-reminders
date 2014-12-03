package org.ird.immunizationreminder.dao.hibernatedao;

import java.io.Serializable;

import org.hibernate.Session;
import org.ird.immunizationreminder.dao.DAO;

public class DAOHibernateImpl implements DAO{
	private Session session;
	public DAOHibernateImpl(Session session) {
		this.session=session;
	}
	@Override
	public Serializable save(Object objectinstance) {
		return session.save(objectinstance);
	}
	@Override
	public void delete(Object objectinstance) {
		session.delete(objectinstance);
	}
	@Override
	public Object merge(Object objectinstance) {
		return session.merge(objectinstance);
	}
	@Override
	public void update(Object objectinstance) {
		session.update(objectinstance);
	}
}
