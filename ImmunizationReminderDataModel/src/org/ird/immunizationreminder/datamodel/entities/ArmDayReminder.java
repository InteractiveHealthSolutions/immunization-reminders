package org.ird.immunizationreminder.datamodel.entities;

import java.lang.reflect.Field;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "arm_day_reminder")
public class ArmDayReminder implements java.io.Serializable {

	@Id
	private ArmDayReminderId id;
	
	@Column(name = "default_reminder_time")
	private Time defaultReminderTime;
	
	@Column(name = "is_default_time_editable")
	private Boolean isDefaultTimeEditable;
	
	public ArmDayReminder() {
	}

	public ArmDayReminder(ArmDayReminderId id) {
		this.id = id;
	}

	public ArmDayReminderId getId() {
		return this.id;
	}

	public void setId(ArmDayReminderId id) {
		this.id = id;
	}

	public void setDefaultReminderTime(Time defaultReminderTime) {
		this.defaultReminderTime = defaultReminderTime;
	}

	public Time getDefaultReminderTime() {
		return defaultReminderTime;
	}

	public void setIsDefaultTimeEditable(Boolean isDefaultTimeEditable) {
		this.isDefaultTimeEditable = isDefaultTimeEditable;
	}

	public Boolean getIsDefaultTimeEditable() {
		return isDefaultTimeEditable;
	}
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(ArmDayReminder.class.getSimpleName());
		s.append("[");
		Field[] f = ArmDayReminder.class.getDeclaredFields();
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
