package org.ird.immunizationreminder.datamodel.entities;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table (name = "reminder")
public class Reminder implements java.io.Serializable {

	@Id
	@GenericGenerator(name="generator", strategy="increment")
    @GeneratedValue(generator="generator")
	@Column(name = "reminder_id")
	private int reminderId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
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
	
	@ElementCollection
	@CollectionTable(name = "REMINDER_TEXTS", joinColumns = @JoinColumn(name = "REMINDER_RECORD_ID"))
	@Column(name = "REMINDER_TEXT")
	private Set<String> reminderText = new HashSet<String>();

	public Reminder() {
	}

	public Reminder(int reminderId) {
		this.reminderId = reminderId;
	}

	public Reminder(int reminderId, String name, String description,
			String createdByUserId, String createdByUserName, Date createdDate,
			String lastEditedByUserId, String lastEditedByUserName,
			Date lastUpdated) {
		this.reminderId = reminderId;
		this.name = name;
		this.description = description;
		this.createdByUserId = createdByUserId;
		this.createdByUserName = createdByUserName;
		this.createdDate = createdDate;
		this.lastEditedByUserId = lastEditedByUserId;
		this.lastEditedByUserName = lastEditedByUserName;
		this.lastUpdated = lastUpdated;
	}

	public int getReminderId() {
		return this.reminderId;
	}

	public void setReminderId(int reminderId) {
		this.reminderId = reminderId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatedByUserId() {
		return this.createdByUserId;
	}

	public void setCreatedByUserId(String createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	public String getCreatedByUserName() {
		return this.createdByUserName;
	}

	public void setCreatedByUserName(String createdByUserName) {
		this.createdByUserName = createdByUserName;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public Date getLastUpdated() {
		return this.lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public void setReminderText(Set<String> reminderText) {
		this.reminderText = reminderText;
	}

	public Set<String> getReminderText() {
		return reminderText;
	}
	public void setLastEditor(User editor){
		lastEditedByUserId=editor.getName();
		lastEditedByUserName=editor.getFullName();
		lastUpdated=new Date();
	}
	public void setCreator(User creator){
		createdByUserId=creator.getName();
		createdByUserName=creator.getFullName();
		createdDate=new Date();
	}
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(Reminder.class.getSimpleName());
		s.append("[");
		Field[] f = Reminder.class.getDeclaredFields();
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