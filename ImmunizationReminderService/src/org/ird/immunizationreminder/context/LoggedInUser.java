package org.ird.immunizationreminder.context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ird.immunizationreminder.datamodel.entities.Role;
import org.ird.immunizationreminder.datamodel.entities.User;

public class LoggedInUser{
	private User user;
	private Date date;

	LoggedInUser(User user){
		this.user=user;
		this.date=new Date();
	}
	public User getUser(){
		refreshDateTime();
		return user;
	}
	public Date getDateTime(){
		return date;
	}
	public void refreshDateTime(){
		this.date=new Date();
	}
	public List<String> getRoles(){
		List<String> roles=new ArrayList<String>();
		for (Role role : user.getRoles()) {
			roles.add(role.getName());
		}
		return roles;
	}
	public boolean hasRole(String role){
		return user.hasRole(role);
	}
	public boolean hasPermission(String permission){
		refreshDateTime();
		return user.hasPermission(permission);
	}
}