package org.ird.immunizationreminder.datamodel.entities;

import java.lang.reflect.Field;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table (name = "reminder_sms")
public class ReminderSms implements java.io.Serializable {

	public enum REMINDER_STATUS{
		/** Pending*/
		PENDING("PND"),
		/** Sent*/
		SENT("SNT"),
		/** Failed*/
		FAILED("FLD"),
		/** Missed*/
		MISSED("MIS"),
		/** Cancelled*/
		CANCELLED("CNCL"),
		/** N/A*/
		NA("N/A"),
		
		LOGGED("LOGD");
		
		private String REPRESENTATION;
		public String getREPRESENTATION() {
			return REPRESENTATION;
		}
		private REMINDER_STATUS(String representation) {
			this.REPRESENTATION = representation;
		}
		
		public static REMINDER_STATUS findEnum(String representationString){
			for (REMINDER_STATUS en : REMINDER_STATUS.values()) {
				if(en.REPRESENTATION.equalsIgnoreCase(representationString)){
					return en;
				}
			}
			return null;
		}
	}
	@Id
	@GenericGenerator(name="generator", strategy="increment")
    @GeneratedValue(generator="generator")
	@Column(name = "rsms_record_num")
	private long rsmsRecordNum;
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "reminder_id")
	@Basic(fetch = FetchType.EAGER)
	private Reminder reminder;
	
	@Column(name = "text")
	private String text;
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "child_record_num")
	@Basic(fetch = FetchType.EAGER)
	private Child child;
	
	@Column(name = "cell_number")
	private String cellnumber;
	
	@Column(name = "reference_number")
	private String referenceNumber;
	
	@Column(name = "due_time")
	private Time dueTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "due_date")
	private Date dueDate;
	
	@Column(name = "day_number")
	private int dayNumber;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "sent_date")
	private Date sentDate;
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "vaccine_id")
	@Basic(fetch = FetchType.EAGER)
	private Vaccine vaccine;

	@Column(name = "vaccination_record_num")
	private long vaccinationRecordNum;
	
	@Column(name = "sms_cancel_reason")
	private String smsCancelReason;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "reminder_status")
	private REMINDER_STATUS reminderStatus;
	
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
	
	@Column(name = "is_sms_late")
	private Boolean isSmsLate;
	
	@Column(name = "day_difference")
	private Integer dayDifference;
	
	@Column(name = "hours_difference")
	private Integer hoursDifference;
	
	public ReminderSms() {
	}

	public Reminder getReminder() {
		return this.reminder;
	}

	public void setReminder(Reminder reminder) {
		this.reminder = reminder;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getSentDate() {
		return this.sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public Vaccine getVaccine() {
		return this.vaccine;
	}

	public void setVaccine(Vaccine vaccine) {
		this.vaccine = vaccine;
	}

	public long getVaccinationRecordNum() {
		return this.vaccinationRecordNum;
	}

	public void setVaccinationRecordNum(long vaccinationRecordNum) {
		this.vaccinationRecordNum = vaccinationRecordNum;
	}

	public REMINDER_STATUS getReminderStatus() {
		return this.reminderStatus;
	}

	public void setStatus(REMINDER_STATUS reminderStatus) {
		this.reminderStatus = reminderStatus;
	}

/*	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ReminderSms))
			return false;
		ReminderSms castOther = (ReminderSms) other;

		return (this.getReminderId() == castOther.getReminderId())
				&& ((this.getReminderName() == castOther.getReminderName()) || (this
						.getReminderName() != null
						&& castOther.getReminderName() != null && this
						.getReminderName().equals(castOther.getReminderName())))
				&& (this.getChildRecordNum() == castOther
						.getChildRecordNum())
				&& ((this.getText() == castOther.getText()) || (this.getText() != null
						&& castOther.getText() != null && this.getText()
						.equals(castOther.getText())))
				&& ((this.getDueDate() == castOther.getDueDate()) || (this
						.getDueDate() != null
						&& castOther.getDueDate() != null && this.getDueDate()
						.equals(castOther.getDueDate())))
				&& ((this.getSentDate() == castOther.getSentDate()) || (this
						.getSentDate() != null
						&& castOther.getSentDate() != null && this
						.getSentDate().equals(castOther.getSentDate())))
				&& (this.getVaccineId() == castOther.getVaccineId())
				&& ((this.getVaccineName() == castOther.getVaccineName()) || (this
						.getVaccineName() != null
						&& castOther.getVaccineName() != null && this
						.getVaccineName().equals(castOther.getVaccineName())))
				&& (this.getVaccinationRecordNum() == castOther
						.getVaccinationRecordNum())
				&& ((this.getReminderStatus() == castOther.getReminderStatus()) || (this
						.getReminderStatus() != null
						&& castOther.getReminderStatus() != null && this
						.getReminderStatus().equals(
								castOther.getReminderStatus())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getReminderId();
		result = 37
				* result
				+ (getReminderName() == null ? 0 : this.getReminderName()
						.hashCode());
		result = 37 * result + this.getChildRecordNum();
		result = 37 * result
				+ (getText() == null ? 0 : this.getText().hashCode());
		result = 37 * result
				+ (getDueDate() == null ? 0 : this.getDueDate().hashCode());
		result = 37 * result
				+ (getSentDate() == null ? 0 : this.getSentDate().hashCode());
		result = 37 * result + this.getVaccineId();
		result = 37
				* result
				+ (getVaccineName() == null ? 0 : this.getVaccineName()
						.hashCode());
		result = 37 * result + (int) this.getVaccinationRecordNum();
		result = 37
				* result
				+ (getReminderStatus() == null ? 0 : this.getReminderStatus()
						.hashCode());
		return result;
	}
*/
	public void setRsmsRecordNum(long rsmsRecordNum) {
		this.rsmsRecordNum = rsmsRecordNum;
	}

	public long getRsmsRecordNum() {
		return rsmsRecordNum;
	}

	public void setCellnumber(String cellnumber) {
		this.cellnumber = cellnumber;
	}

	public String getCellnumber() {
		return cellnumber;
	}
	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	/**
	 * @param createdByUserId the createdByUserId to set
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
	 * @param createdByUserName the createdByUserName to set
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
	 * @param createdDate the createdDate to set
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
	
	public void setCreator(User creator){
		createdByUserId=creator.getName();
		createdByUserName=creator.getFullName();
		createdDate=new Date();
	}
	/**
	 * @param lastEditedByUserId the lastEditedByUserId to set
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
	 * @param lastEditedByUserName the lastEditedByUserName to set
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
	public void setLastEditor(User editor){
		lastEditedByUserId=editor.getName();
		lastEditedByUserName=editor.getFullName();
		lastUpdated=new Date();
	}

	/**
	 * @param lastUpdated the lastUpdated to set
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

	public void setDueTime(Time dueTime) {
		this.dueTime = dueTime;
	}

	public Time getDueTime() {
		return dueTime;
	}

	public void setDayNumber(int dayNumber) {
		this.dayNumber = dayNumber;
	}

	public int getDayNumber() {
		return dayNumber;
	}

	public void setSmsCancelReason(String smsCancelReason) {
		this.smsCancelReason = smsCancelReason;
	}

	public String getSmsCancelReason() {
		return smsCancelReason;
	}

	public void setIsSmsLate(Boolean isSmsLate) {
		this.isSmsLate = isSmsLate;
	}

	public Boolean getIsSmsLate() {
		return isSmsLate;
	}

	public void setDayDifference(Integer dateDifference) {
		this.dayDifference = dateDifference;
	}

	public Integer getDayDifference() {
		return dayDifference;
	}

	public void setHoursDifference(Integer hoursDifference) {
		this.hoursDifference = hoursDifference;
	}

	public Integer getHoursDifference() {
		return hoursDifference;
	}

	public void setChild(Child child) {
		this.child = child;
	}

	public Child getChild() {
		return child;
	}
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(ReminderSms.class.getSimpleName());
		s.append("[");
		Field[] f = ReminderSms.class.getDeclaredFields();
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