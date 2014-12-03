package org.ird.immunizationreminder.datamodel.entities;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "user")
public class User implements Serializable {

	public enum UserStatus {
		ACTIVE,
		DISABLED;
	}

	@Id
	@GenericGenerator(name="generator", strategy="increment")
    @GeneratedValue(generator="generator")
	@Column(name = "user_id")
	private int userId = 0;

	/** The login name of the user. */

	@Column(name = "user_name", unique = true)
	private String name;

	/** The first name of the user. */

	@Column(name = "first_name")
	private String firstName;

	/** The middle name of the user. */

	@Column(name = "middle_name")
	private String middleName;

	/** The last name of the user. */

	@Column(name = "last_name")
	private String lastName;

	/** The user hashed password. */

	@Column(name = "password")
	private String password;

	@Column(name = "phone_no")
	private String phoneNo;

	/** The List of roles that the user has. */

	@ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER ,cascade = { CascadeType.PERSIST,
			CascadeType.MERGE })
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<Role>();

	/** The email address of the user. */

	@Column(name = "email")
	private String email;

	/** Flag to determine if <code>User</code> is disabled or not */

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private UserStatus status;

	@Column(name = "created_by_user_id")
	private String createdByUserId;
	
	@Column(name = "created_by_user_name")
	private String createdByUserName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "last_edited_by_user_id")
	private String lastEditedByUserId;
	
	@Column(name = "last_edited_by_user_name")
	private String lastEditedByUserName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_updated")
	private Date lastUpdated;

	/** The user's clear text password. This is just a means of transfer to the
	 * service layer because it's never persisted.
	 */
	@Transient
	private String clearTextPassword;

	/** Constructor used to create a new user object.
	 */
	public User() {

	}
	/**
	 * Constructor used to create a user with a given login name and password.
	 * @param name
	 *            the login name.
	 * @param clearTextPassword
	 *            the non hashed password.
	 */


	public User(int userId, String name, String password) {
		this.userId = userId;
		this.name = name;
		this.clearTextPassword = password;
	}

	/**
	 * Constructor used to create a user with a given login name.
	 * @param name
	 *            the login name.
	 */

	public User(String name) {
		this.name = name;
	}

	/**
	 * Constructor used to create a user with a given database id and login
	 * name.
	 * @param userId
	 *            the database id.
	 * @param name
	 *            the login name.
	 */

	public User(int userId, String name) {
		this.userId = userId;
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName.toUpperCase();
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName.toUpperCase();
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName.toUpperCase();
	}

	public String getName() {
		return name;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getClearTextPassword() {
		return clearTextPassword;
	}

	public void setClearTextPassword(String clearTextPassword) {
		this.clearTextPassword = clearTextPassword;
	}

	public boolean hasNewPassword() {
		return (clearTextPassword != null && clearTextPassword.trim().length() > 0);
	}

	public String getFullName() {
		String fullName = "";
		if (firstName != null)
			fullName += firstName + " ";
		if (middleName != null)
			fullName += middleName + " ";
		if (lastName != null)
			fullName += lastName;
		return fullName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Retrieves all the <code>User Roles.</code>
	 * @return <code>Set</code> of <code>User Roles.</code>
	 */

	public synchronized Set<Role> getRoles() {
		synchronized (this) {
			return roles;
		}
	}

	/**
	 * Sets the <code>User Roles.</code>
	 * @param roles
	 *            <code>User Roles.</code>
	 */

	public synchronized void setRoles(Set<Role> roles) {
		synchronized (this) {
			this.roles = roles;
		}
	}

	/**
	 * Adds a <code>Role</code> to the set of roles mapped to this
	 * <code>User.</code>
	 * @param role
	 *            <code>Role</code> to map to the <code>User.</code>
	 */

	public synchronized void addRole(Role role) {
		synchronized (this) {
			if (role != null) {
				for (Role x : this.roles) {
					if (x.getName().equalsIgnoreCase(role.getName()))
						return;
				}
				roles.add(role);
			}
		}
	}

	/**
	 * Removes a <code>Role</code> from the set of roles mapped to this
	 * <code>User.</code>
	 * @param role
	 *            <code>Role</code> to remove from the <code>User.</code>
	 */

	public synchronized void removeRole(Role role) {
		synchronized (this) {
			if (role != null) {
				for (Role x : roles) {
					if (x.getName().equals(role.getName())) {
						roles.remove(x);
						break;
					}
				}
			}
		}
	}

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/*
	 * public boolean hasNewPassword(){
	 * 
	 * return (clearTextPassword != null && clearTextPassword.trim().length() >
	 * 0);
	 * 
	 * }
	 */

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Determines if this user has the administrator role
	 * @return
	 */

	public boolean hasAdministrativePrivileges() {
		boolean ret = false;
		if (roles != null) {
			for (Role r : roles) {
				if (r.getName().equalsIgnoreCase("Role_Administrator")
					|| r.getName().equalsIgnoreCase("Administrator")
						|| r.getName().equalsIgnoreCase("Admin")) {
					ret = true;
					break;
				}
			}
		}
		return ret;
	}

	/**
	 * Determines if this user has the specified permission
	 * @param permission
	 *            String permission (e.g. "Perm_Data_Edit")
	 * @return
	 */

	public boolean hasPermission(String permission) {
		boolean ret = false;
		if (permission != null) {
			if (roles != null) {
				for (Role r : roles) {
					if (r.getPermissions().size() != 0) {
						for (Permission p : r.getPermissions()) {
							if (permission.equalsIgnoreCase(p.getName())) {
								ret = true;
								break;
							}
						}
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Ascertains if the current <code>User</code>
	 * is the default administrator that ships with the system.
	 * @see #hasAdministrativePrivileges() to check if a user
	 *      is an administrator (sees if a user has the admin role).
	 * @return <code>true if(user is default administrator)</code>
	 */

	public boolean isDefaultAdministrator() {
		boolean defaultAdmin = false;
		if (this.firstName != null && this.middleName != null) {
			if (this.name.equalsIgnoreCase("admin")
					|| this.name.equalsIgnoreCase("administrator"))
				defaultAdmin = true;
		}
		return defaultAdmin;
	}

	/**
	 * Checks if this <code>User</code> has a given <code>Role</code>.
	 * @param role
	 *            <code>Role</code> to check.
	 * @return <code>True only and only if Role exists in the list of roles assigned to User.</code>
	 */

	public synchronized boolean hasRole(String role) {
		synchronized (this) {
			for (Role xRole : roles) {
				if (xRole.getName().equalsIgnoreCase(role))
					return true;
			}
			return false;
		}
	}
	/**
	 * @param createdByUserId
	 *            the createdByUserId to set
	 */

	public void setCreatedByUserId(String createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	/**
	 * @return the createdByUserId
	 */

	public String getCreatedByUserId() {
		return createdByUserId;
	}

	/**
	 * @param createdByUserName
	 *            the createdByUserName to set
	 */

	public void setCreatedByUserName(String createdByUserName) {
		this.createdByUserName = createdByUserName;
	}

	/**
	 * @return the createdByUserName
	 */

	public String getCreatedByUserName() {
		return createdByUserName;
	}

	/**
	 * @param createdDate
	 *            the createdDate to set
	 */

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the createdDate
	 */

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreator(User creator) {
		createdByUserId = creator.getName();
		createdByUserName = creator.getFullName();
		createdDate = new Date();
	}

	/**
	 * @param lastEditedByUserId
	 *            the lastEditedByUserId to set
	 */

	public void setLastEditedByUserId(String lastEditedByUserId) {
		this.lastEditedByUserId = lastEditedByUserId;
	}
	/**
	 * @return the lastEditedByUserId
	 */

	public String getLastEditedByUserId() {
		return lastEditedByUserId;
	}

	/**
	 * @param lastEditedByUserName
	 *            the lastEditedByUserName to set
	 */

	public void setLastEditedByUserName(String lastEditedByUserName) {
		this.lastEditedByUserName = lastEditedByUserName;
	}

	/**
	 * @return the lastEditedByUserName
	 */

	public String getLastEditedByUserName() {
		return lastEditedByUserName;
	}

	public void setLastEditor(User editor) {
		lastEditedByUserId = editor.getName();
		lastEditedByUserName = editor.getFullName();
		lastUpdated = new Date();
	}
	/**
	 * @param lastUpdated
	 *            the lastUpdated to set
	 */

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	/**
	 * @return the lastUpdated
	 */

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setStatus(UserStatus status){
		this.status = status;
	}

	public UserStatus getStatus(){
		return status;
	}
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(User.class.getSimpleName());
		s.append("[");
		Field[] f = User.class.getDeclaredFields();
		for (Field field : f) {
			try {
				s.append(field.getName()+"="+field.get(this));
				s.append(";");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		int i = s.lastIndexOf(";");
		if(i != -1) s.deleteCharAt(i);
		
		s.append("]");

		return s.toString();	
	}
}