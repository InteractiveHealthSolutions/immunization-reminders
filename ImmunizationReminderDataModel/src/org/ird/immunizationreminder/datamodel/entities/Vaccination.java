package org.ird.immunizationreminder.datamodel.entities;

import java.lang.reflect.Field;
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
@Table (name = "vaccination")
public class Vaccination implements java.io.Serializable {

	public enum VACCINATION_STATUS{
		/** Pending */
		PENDING("PND"),
		/** Vaccinated */
		VACCINATED("VACC"),
		/** Not Vaccinated */
		NOT_VACCINATED("NVAC"),
		/** Missed */
		MISSED("MIS"),
		/** Late Vaccinated */
		LATE_VACCINATED("LVAC");
		
		private String REPRESENTATION;
		public String getREPRESENTATION() {
			return REPRESENTATION;
		}

		private VACCINATION_STATUS(String representation) {
			this.REPRESENTATION = representation;
		}
		
		public static VACCINATION_STATUS findEnum(String representationString){
			for (VACCINATION_STATUS en : VACCINATION_STATUS.values()) {
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
	@Column(name = "vaccination_record_num")
	private long	vaccinationRecordNum;
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "child_record_num")
	@Basic(fetch = FetchType.EAGER)
	private Child	child;
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "vaccine_id")
	@Basic(fetch = FetchType.EAGER)
	private Vaccine	vaccine;
	
	@Column(name = "description")
	private String	description;
	
	@Column(name = "dose")
	private String	dose;

	@Temporal(TemporalType.DATE)
	@Column(name = "vaccination_date")
	private Date	vaccinationDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "vaccination_duedate")
	private Date	vaccinationDuedate;
	
	@Column(name = "child_responded")
	private Boolean	childResponded;
	
	@Column(name = "is_last_vaccination")
	private boolean	isLastVaccination;

	@Column(name = "is_first_vaccination")
	private boolean	isFirstVaccination;

	@Column(name = "next_vaccination_record_num")
	private long	nextVaccinationRecordNum;

	@Column(name = "previous_vaccination_record_num")
	private long	previousVaccinationRecordNum;

	@Column(name = "child_brought_by")
	private String	childBroughtBy;

	@Column(name = "child_vaccinated_by")
	private String	childVaccinatedBy;

	@Enumerated(EnumType.STRING)
	@Column(name = "vaccination_status")
	private VACCINATION_STATUS	vaccinationStatus;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "next_assigned_date")
	private Date	nextAssignedDate;
	
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

	public Vaccination() {
	}

	public long getVaccinationRecordNum() {
		return this.vaccinationRecordNum;
	}

	public void setVaccinationRecordNum(long vaccinationRecordNum) {
		this.vaccinationRecordNum = vaccinationRecordNum;
	}

	public Child getChild() {
		return this.child;
	}

	public void setChild(Child child) {
		this.child = child;
	}

	public Vaccine getVaccine() {
		return this.vaccine;
	}

	public void setVaccine(Vaccine vaccine) {
		this.vaccine = vaccine;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDose() {
		return this.dose;
	}

	public void setDose(String dose) {
		this.dose = dose;
	}

	public Date getVaccinationDate() {
		return this.vaccinationDate;
	}

	public void setVaccinationDate(Date vaccinationDate) {
		this.vaccinationDate = vaccinationDate;
	}

	public Date getVaccinationDuedate() {
		return this.vaccinationDuedate;
	}

	public void setVaccinationDuedate(Date vaccinationDuedate) {
		this.vaccinationDuedate = vaccinationDuedate;
	}

	public Boolean getChildResponded() {
		return this.childResponded;
	}

	public void setChildResponded(Boolean childResponded) {
		this.childResponded = childResponded;
	}

	public String getChildBroughtBy() {
		return this.childBroughtBy;
	}

	public void setChildBroughtBy(String childBroughtBy) {
		this.childBroughtBy = childBroughtBy;
	}

	public String getChildVaccinatedBy() {
		return this.childVaccinatedBy;
	}

	public void setChildVaccinatedBy(String childVaccinatedBy) {
		this.childVaccinatedBy = childVaccinatedBy;
	}

	public VACCINATION_STATUS getVaccinationStatus() {
		return this.vaccinationStatus;
	}

	public void setVaccinationStatus(VACCINATION_STATUS vaccinationStatus) {
		this.vaccinationStatus = vaccinationStatus;
	}
	public Date getNextAssignedDate() {
		return this.nextAssignedDate;
	}

	public void setNextAssignedDate(Date nextAssignedDate) {
		this.nextAssignedDate = nextAssignedDate;
	}

	public void setLastEditor(User editor){
		setLastEditedByUserId(editor.getName());
		setLastEditedByUserName(editor.getFullName());
		setLastUpdated(new Date());
	}
	public void setCreator(User creator){
		setCreatedByUserId(creator.getName());
		setCreatedByUserName(creator.getFullName());
		setCreatedDate(new Date());
	}

	public void setCreatedByUserName(String createdByUserName) {
		this.createdByUserName = createdByUserName;
	}

	public String getCreatedByUserName() {
		return createdByUserName;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedByUserId(String createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	public String getCreatedByUserId() {
		return createdByUserId;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastEditedByUserName(String lastEditedByUserName) {
		this.lastEditedByUserName = lastEditedByUserName;
	}

	public String getLastEditedByUserName() {
		return lastEditedByUserName;
	}

	public void setLastEditedByUserId(String lastEditedByUserId) {
		this.lastEditedByUserId = lastEditedByUserId;
	}

	public String getLastEditedByUserId() {
		return lastEditedByUserId;
	}

	public void setIsLastVaccination(boolean islastvaccination) {
		this.isLastVaccination = islastvaccination;
	}

	public boolean getIsLastVaccination() {
		return isLastVaccination;
	}

	public void setIsFirstVaccination(boolean isFirstVaccination) {
		this.isFirstVaccination = isFirstVaccination;
	}

	public boolean getIsFirstVaccination() {
		return isFirstVaccination;
	}

	public void setPreviousVaccinationRecordNum(long previousVaccinationRecordNum) {
		this.previousVaccinationRecordNum = previousVaccinationRecordNum;
	}

	public long getPreviousVaccinationRecordNum() {
		return previousVaccinationRecordNum;
	}

	public void setNextVaccinationRecordNum(long nextVaccinationRecordNum) {
		this.nextVaccinationRecordNum = nextVaccinationRecordNum;
	}

	public long getNextVaccinationRecordNum() {
		return nextVaccinationRecordNum;
	}
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(Vaccination.class.getSimpleName());
		s.append("[");
		Field[] f = Vaccination.class.getDeclaredFields();
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