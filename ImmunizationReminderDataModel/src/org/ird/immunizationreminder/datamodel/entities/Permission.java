/*
 *  Licensed to the OpenXdata Foundation (OXDF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The OXDF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with the License. 
 *  You may obtain a copy of the License at <p>
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  </p>
 *
 *  Unless required by applicable law or agreed to in writing, 
 *  software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 *
 *  Copyright 2010 http://www.openxdata.org.
 */
package org.ird.immunizationreminder.datamodel.entities;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


/**
 * This class encapsulates a permission 
 * which can be used to restrict the level of access in <code>openXData</code>. 
 * 
 * <P>
 * Examples of permissions could be<code> View Form Data, Edit Form Data,
 * Delete Form Data, Create New Studies, Export Data, Export Study </code>.
 * </P>
 * 
 * @author Mark
 * @author daniel
 *
 */

@Entity
@Table(name = "permission")
public class Permission implements Serializable {

	/** The database identifier of the permission. */

	@Id
	@GenericGenerator(name="generator", strategy="increment")
    @GeneratedValue(generator="generator")
	@Column(name = "permission_id")
	private int permissionId;

	/** The name of the permission. */

	@Column(name = "name", unique = true)
	private String name;

	/** The description of the permission. */

	@Column(name = "description")
	private String description;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "permissions", targetEntity = Role.class)
	private Set<Role> roles;

	/**
	 * 
	 * Constructs a new permission object.
	 */

	public Permission() {
	}

	/** Constructs a new permission object with a given name.
	 * 
	 * @param name
	 *            the name of the permission.
	 */

	public Permission(String name) {
		this.name = name;
	}

	public int getPermissionId() {
		return permissionId;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setPermissionId(int permissionId) {
		this.permissionId = permissionId;
	}

	public void setName(String name) {
		this.name = name.toUpperCase();
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isNew() {
		return permissionId == 0;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Role> getRoles() {
		return roles;
	}
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(Permission.class.getSimpleName());
		s.append("[");
		Field[] f = Permission.class.getDeclaredFields();
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