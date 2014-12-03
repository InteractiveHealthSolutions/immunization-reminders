/*package org.ird.immunizationreminder.service;

import java.util.List;

import ird.xoutTB.dao.DAODirectQuery;
import ird.xoutTB.db.entity.UserQuery;
import ird.xoutTB.service.QueryService;

public class QueryServiceImpl implements QueryService{
DAODirectQuery dq;

public QueryServiceImpl(DAODirectQuery dao){
	dq=dao;
}
	@Override
	public List executeDirectQuery(String query) {
		return dq.executeQuery(query);
	}
	@Override
	public void deleteQuery(UserQuery query) {
		dq.deleteQuery(query);
		
	}
	@Override
	public UserQuery getQuery(String exactqueryName) {
		return dq.getQuery(exactqueryName);
	}
	@Override
	public void saveQuery(UserQuery query) {
		dq.saveQuery(query);
	}
	@Override
	public List<UserQuery> searchQuery(String queryName) {
		return dq.searchQuery(queryName);
	}
	@Override
	public List<UserQuery> searchQueryByUsername(String userName) {
		return dq.searchQueryByUsername(userName);
	}
	@Override
	public List<UserQuery> searchQuery(String queryName, String username) {
		return dq.searchQuery(queryName,username);
	}
	@Override
	public List<UserQuery> searchQuery() {
		return dq.searchQuery();
	}
}
*/