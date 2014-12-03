package org.ird.immunizationreminder.datamodel.entities;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity 
@Table(name = "arm")
public class Arm implements java.io.Serializable {

	@Id
	@GenericGenerator(name="generator", strategy="increment")
    @GeneratedValue(generator="generator")
	@Column(name = "arm_id")
	private int armId;
	
	@Column(name = "arm_name")
	private String armName;
	
	@Column(name = "reminder_days_sequence")
	private String reminderDaysSequence;
	
	@Column(name = "send_sms")
	private Boolean sendSms;
	
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
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = ArmDayReminder.class)
	//@Fetch (FetchMode.SELECT)
	//@Basic(fetch = FetchType.LAZY)
	@JoinColumn(name = "arm_id")
		
	private Set<ArmDayReminder> armday=new HashSet<ArmDayReminder>();

	public Arm() {
	}

	public Arm(int armId, String armName, String description) {
		this.armId = armId;
		this.armName = armName;
		this.description = description;
	}

	public Arm(int armId, String armName, String description,
			String createdByUserId, String createdByUserName, Date createdDate,
			String lastEditedByUserId, String lastEditedByUserName,
			Date lastUpdated) {
		this.armId = armId;
		this.armName = armName;
		this.description = description;
		this.createdByUserId = createdByUserId;
		this.createdByUserName = createdByUserName;
		this.createdDate = createdDate;
		this.lastEditedByUserId = lastEditedByUserId;
		this.lastEditedByUserName = lastEditedByUserName;
		this.lastUpdated = lastUpdated;
	}

	public int getArmId() {
		return this.armId;
	}

	public void setArmId(int armId) {
		this.armId = armId;
	}

	public String getArmName() {
		return this.armName;
	}

	public void setArmName(String armName) {
		this.armName = armName;
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

	public void setReminderDaysSequence(String reminderDaysSequence) {
		this.reminderDaysSequence = reminderDaysSequence;
	}

	public String getReminderDaysSequence() {
		return reminderDaysSequence;
	}

	public void setSendSms(Boolean sendSms) {
		this.sendSms = sendSms;
	}

	public Boolean getSendSms() {
		return sendSms;
	}

	public void setArmday(Set<ArmDayReminder> armday) {
		this.armday = armday;
	}

	public Set<ArmDayReminder> getArmday() {
		return armday;
	}
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(Arm.class.getSimpleName());
		s.append("[");
		Field[] f = Arm.class.getDeclaredFields();
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
