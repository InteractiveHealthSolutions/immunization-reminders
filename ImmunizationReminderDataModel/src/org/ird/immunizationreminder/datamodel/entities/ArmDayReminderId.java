package org.ird.immunizationreminder.datamodel.entities;

import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ArmDayReminderId implements java.io.Serializable {

	@Column (name = "arm_id")
	private int armId;
	
	@Column (name = "reminder_id")
	private int reminderId;
	
	@Column (name = "vaccine_id")
	private int vaccineId;
	
	@Column (name = "day_number")
	private int dayNumber;

	public ArmDayReminderId() {
	}

	public ArmDayReminderId(int armId, int reminderId, int vaccineId,int dayNumber) {
		this.armId = armId;
		this.reminderId = reminderId;
		this.vaccineId = vaccineId;
		this.dayNumber=dayNumber;
	}

	public int getArmId() {
		return this.armId;
	}

	public void setArmId(int armId) {
		this.armId = armId;
	}

	public int getReminderId() {
		return this.reminderId;
	}

	public void setReminderId(int reminderId) {
		this.reminderId = reminderId;
	}

	public int getVaccineId() {
		return this.vaccineId;
	}

	public void setVaccineId(int vaccineId) {
		this.vaccineId = vaccineId;
	}

	public void setDayNumber(int dayNumber) {
		this.dayNumber = dayNumber;
	}

	public int getDayNumber() {
		return dayNumber;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ArmDayReminderId))
			return false;
		ArmDayReminderId castOther = (ArmDayReminderId) other;

		return (this.getArmId() == castOther.getArmId())
				&& (this.getReminderId() == castOther.getReminderId())
				&& (this.getVaccineId() == castOther.getVaccineId())
				&& (this.getDayNumber() == castOther.getDayNumber());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getArmId();
		result = 37 * result + this.getReminderId();
		result = 37 * result + this.getVaccineId();
		result = 37 * result + this.getDayNumber();
		return result;
	}
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(ArmDayReminderId.class.getSimpleName());
		s.append("[");
		Field[] f = ArmDayReminderId.class.getDeclaredFields();
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
