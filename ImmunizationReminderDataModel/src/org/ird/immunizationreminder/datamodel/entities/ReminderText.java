package org.ird.immunizationreminder.datamodel.entities;

import java.lang.reflect.Field;
import java.util.Date;

public class ReminderText implements java.io.Serializable {

	private int recordNum;
	private String text;
	private String description;
	private Reminder reminder;
	private String createdByUserId;
	private String createdByUserName;
	private Date createdDate;
	private String lastEditedByUserId;
	private String lastEditedByUserName;
	private Date lastUpdated;

	public ReminderText() {
	}

	public ReminderText(int recordNum) {
		this.recordNum = recordNum;
	}

	public ReminderText(int recordNum, String text, String description, String createdByUserId,
			String createdByUserName, Date createdDate,
			String lastEditedByUserId, String lastEditedByUserName,
			Date lastUpdated) {
		this.recordNum = recordNum;
		this.text = text;
		this.description = description;
		this.createdByUserId = createdByUserId;
		this.createdByUserName = createdByUserName;
		this.createdDate = createdDate;
		this.lastEditedByUserId = lastEditedByUserId;
		this.lastEditedByUserName = lastEditedByUserName;
		this.lastUpdated = lastUpdated;
	}

	public int getRecordNum() {
		return this.recordNum;
	}

	public void setRecordNum(int recordNum) {
		this.recordNum = recordNum;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Reminder getReminder() {
		return this.reminder;
	}

	public void setReminder(Reminder reminder) {
		this.reminder = reminder;
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
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(ReminderText.class.getSimpleName());
		s.append("[");
		Field[] f = ReminderText.class.getDeclaredFields();
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