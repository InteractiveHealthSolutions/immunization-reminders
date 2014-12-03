package org.ird.immunizationreminder.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.ird.immunizationreminder.dao.DAOPermission;
import org.ird.immunizationreminder.dao.DAORole;
import org.ird.immunizationreminder.dao.DAOUser;
import org.ird.immunizationreminder.data.exception.DataException;
import org.ird.immunizationreminder.datamodel.entities.Permission;
import org.ird.immunizationreminder.datamodel.entities.Role;
import org.ird.immunizationreminder.datamodel.entities.User;
import org.ird.immunizationreminder.datamodel.entities.User.UserStatus;
import org.ird.immunizationreminder.service.UserService;
import org.ird.immunizationreminder.service.exception.UserServiceException;
import org.ird.immunizationreminder.service.utils.SecurityUtils;
import org.ird.immunizationreminder.service.validations.UserValidations;

public class UserServiceImpl implements UserService{

	DAOUser u;
	DAORole r;
	DAOPermission perm;
	private Number LAST_QUERY_TOTAL_ROW__COUNT;
	
	public UserServiceImpl(DAOUser user,DAORole role,DAOPermission per){
		u=user;
		r=role;
		perm=per;
	}
	private void setLASTS_ROWS_RETURNED_COUNT(Number LAST_QUERY_TOTAL_ROW__COUNT) {
		this.LAST_QUERY_TOTAL_ROW__COUNT = LAST_QUERY_TOTAL_ROW__COUNT;
	}
	@Override
	public Number LAST_QUERY_TOTAL_ROW__COUNT() {
		return LAST_QUERY_TOTAL_ROW__COUNT;
	}
	@Override
	public void changePassword(String username,String oldpwd, String pw1, String pw2) throws Exception{
		User user=u.findByUsername(username);

		if(user!=null){
			if(pw1.compareTo(pw2)!=0){
				throw new UserServiceException(UserServiceException.PASSWORDS_NOT_MATCH);
			}
			if(!UserValidations.validatePassword(pw1)){
				throw new UserServiceException(UserServiceException.INVALID_PASSWORD_CHARACTERS);
			}
			if(oldpwd.compareTo(SecurityUtils.decrypt(user.getPassword(),user.getName()))!=0){
				throw new UserServiceException(UserServiceException.WRONG_PASSWORD);
			}
			user.setClearTextPassword(pw1);
			updateUser(user);
		}
	}

	@Override
	public Serializable createUser(User user) throws Exception {
		if(user.hasNewPassword()){
			user.setPassword(SecurityUtils.encrypt(user.getClearTextPassword(), user.getName()));
		}else{
			throw new UserServiceException(UserServiceException.USER_PASSWORD_MISSING);
		}
		UserValidations.validateUser(user);
		if(u.findByUsername(user.getName())!=null){
			throw new UserServiceException(UserServiceException.USER_EXISTS);
		}
		return u.save(user);
		
	}

	@Override
	public void deleteRole(Role role)  {
		r.delete(role);
	}

	@Override
	public void deleteUser(User user)  {
		u.delete(user);
	}

	@Override
	public List<Permission> getAllPermissions(boolean isreadonly) {
		List<Permission> plst= perm.getAll(isreadonly);
		setLASTS_ROWS_RETURNED_COUNT(plst.size());
		return plst;
	}

	@Override
	public List<Role> getAllRoles() {
		List<Role> rlst= r.getAll();
		setLASTS_ROWS_RETURNED_COUNT(rlst.size());
		return rlst;
	}

	@Override
	public List<User> getAllUsers(int firstResult, int fetchSize)  {
		List<User> ulst= u.getAll(firstResult,fetchSize);
		setLASTS_ROWS_RETURNED_COUNT(u.LAST_QUERY_TOTAL_ROW__COUNT());
		return ulst;
	}

/*	@Override
	public Permission getPermission(int permissionId) {
		return perm.findById(permissionId);
	}*/

/*	@Override
	public Role getRole(int roleId) {
		return r.findById(roleId);
	}
*/
	@Override
	public User getUser(String userName) {
		User usr= u.findByUsername(userName);
		setLASTS_ROWS_RETURNED_COUNT(u.LAST_QUERY_TOTAL_ROW__COUNT());
		return usr;
	}
	@Override
	public List<User> findUserByCriteria(String email, String partOfFirstOrLastName,
			UserStatus userStatus, boolean putNotWithUserStatus,int firstResult, int fetchSize)
			throws DataException{
		List<User> ulst=  u.findByCriteria(email, partOfFirstOrLastName, userStatus, putNotWithUserStatus,firstResult,fetchSize);
		setLASTS_ROWS_RETURNED_COUNT(u.LAST_QUERY_TOTAL_ROW__COUNT());
		return ulst;
	}
	@Override
	public void updateRole(Role role) {
		r.update(role);
	}

	@Override
	public User updateUser(User user) throws Exception{
		if(user.hasNewPassword()){
			user.setPassword(SecurityUtils.encrypt(user.getClearTextPassword(), user.getName()));
		}
		return (User) u.merge(user);
	}
	@Override
	public Role getRole(String rolename) {
		Role rol= r.findByName(rolename);
		setLASTS_ROWS_RETURNED_COUNT(rol==null?0:1);
		return rol;
	}
	@Override
	public Serializable addRole(Role role) throws UserServiceException  {
		if(r.findByName(role.getName())!=null){
			throw new UserServiceException(UserServiceException.ROLE_EXISTS);
		}
		return r.save(role);
	}
	@Override
	public Permission getPermission(String permission,boolean isreadonly) {
		Permission p= perm.findByPermissionName(permission,true);
		setLASTS_ROWS_RETURNED_COUNT(p==null?0:1);
		return p;
	}
	@Override
	public List<Role> getRolesByName(String name) {
		List<Role> rlst=r.findByCriteria(name);
		setLASTS_ROWS_RETURNED_COUNT(rlst.size());
		return rlst;
	}

}
