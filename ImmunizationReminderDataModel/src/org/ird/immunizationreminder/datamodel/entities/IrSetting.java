package org.ird.immunizationreminder.datamodel.entities;

import java.lang.reflect.Field;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ir_setting")
public class IrSetting implements java.io.Serializable {

	@Id
	@GenericGenerator(name="generator", strategy="increment")
    @GeneratedValue(generator="generator")
	@Column(name = "setting_id")
	private int		settingId;

	@Column(name = "name", unique = true)
	private String	name;

	@Column(name = "value")
	private String	value;

	@Column(name = "description")
	private String	description;

	@Column(name = "last_edited_by_user_id")
	private String lastEditedByUserId;
	
	@Column(name = "last_edited_by_user_name")
	private String lastEditedByUserName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_updated")
	private Date lastUpdated;

	public IrSetting()
	{
	}

	public IrSetting(int settingId, String name, String value) {
		this.settingId = settingId;
		this.name = name;
		this.value = value;
	}

	public IrSetting(int settingId, String name, String value,
			String description, Date lastUpdated, String lastEditedByUserId,
			String lastEditedByUserName) {
		this.settingId = settingId;
		this.name = name;
		this.value = value;
		this.description = description;
		this.lastUpdated = lastUpdated;
		this.lastEditedByUserId = lastEditedByUserId;
		this.lastEditedByUserName = lastEditedByUserName;
	}

	public int getSettingId() {
		return this.settingId;
	}

	public void setSettingId(int settingId) {
		this.settingId = settingId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getLastUpdated() {
		return this.lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getLastEditedByUserId() {
		return this.lastEditedByUserId;
	}

	public void setLastEditedByUserId(String lastEditedByUserId) {
		this.lastEditedByUserId = lastEditedByUserId;
	}

	public String getLastEditedByUserName() {
		return this.lastEditedByUserName;
	}

	public void setLastEditedByUserName(String lastEditedByUserName) {
		this.lastEditedByUserName = lastEditedByUserName;
	}
	public void setLastEditor(User editor){
		lastEditedByUserId=editor.getName();
		lastEditedByUserName=editor.getFullName();
		lastUpdated=new Date();
	}
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(IrSetting.class.getSimpleName());
		s.append("[");
		Field[] f = IrSetting.class.getDeclaredFields();
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