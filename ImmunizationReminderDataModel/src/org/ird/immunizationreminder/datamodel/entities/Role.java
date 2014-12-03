package org.ird.immunizationreminder.datamodel.entities;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "role")
public class Role implements Serializable {

	@Id
	@GenericGenerator(name="generator", strategy="increment")
    @GeneratedValue(generator="generator")
	@Column(name = "role_id")
	private int roleId = 0;

	/** The name of the role. */

	@Column(name = "name", unique = true)
	private String name;

	@Column(name = "description")
	private String description;

	/** The set of permissions that the role has. */

	@ManyToMany(targetEntity = Permission.class, fetch = FetchType.EAGER, cascade = {
			CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "role_permission", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
	private Set<Permission> permissions;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "roles", targetEntity = User.class)
	private List<User> users;

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

	public Role() {
		permissions = new HashSet<Permission>();
	}

	public Role(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		try {
			this.description = description.trim();
		} catch (Exception e) {
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.toUpperCase();
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public boolean isNew() {
		return roleId == 0;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public void addPermission(Permission permission) {
		if (permission != null && !this.permissions.contains(permission)) {
			this.permissions.add(permission);
		}
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public void addPermissions(List<Permission> permissions) {
		for (Permission x : permissions) {
			if (x != null && !this.permissions.contains(x))
				this.permissions.add(x);
		}
	}

	public void removePermission(Permission permission) {
		if (permission != null) {
			for (Permission x : permissions) {
				if (x.getName().equals(permission.getName())) {
					permissions.remove(x);
					break;
				}
			}
		}
	}

	public boolean hasPermission(String permissionName) {
		if (permissions != null) {
			for (Permission p : permissions) {
				if (p.getName().equals(permissionName))
					return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * Checks if the given <code>Role</code> is the default administrator
	 * <code>Role</code> that ships with the system.
	 * 
	 * @param role
	 *            <code>Role</code> to check.
	 * 
	 * @return <code>True only and only if role.getName().equals("Role_Administrator")
	 */

	public boolean isDefaultAdminRole() {
		return this.getName().equals("Role_Administrator");
	}

	public void setCreatedByUserId(String createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

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

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	/**
	 * @return the lastUpdated
	 */

	public Date getLastUpdated() {
		return lastUpdated;
	}
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(Role.class.getSimpleName());
		s.append("[");
		Field[] f = Role.class.getDeclaredFields();
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