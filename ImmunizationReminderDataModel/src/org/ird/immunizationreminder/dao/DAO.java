package org.ird.immunizationreminder.dao;

import java.io.Serializable;

public interface DAO {
	public Serializable save(Object objectinstance);

	public void delete(Object objectinstance);

	public Object merge(Object objectinstance);

	public void update(Object objectinstance);
}
