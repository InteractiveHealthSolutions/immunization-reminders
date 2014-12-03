package org.ird.immunizationreminder.datamodel.entities;

import java.lang.reflect.Field;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table (name = "vaccine")
public class Vaccine implements java.io.Serializable {

	public enum UNIT_GAP{
		DAY,
		WEEK,
		MONTH,
		YEAR;
	}
	@Id
	@GenericGenerator(name="generator", strategy="increment")
    @GeneratedValue(generator="generator")
	@Column(name = "vaccine_id")
	private int vaccineId;
	
	@Column(name = "vaccine_number_in_form")
	private Integer vaccineNumberInForm;
	
	@Column(name = "vaccine_name_in_form")
	private String vaccineNameInForm;
	
	@Column(name = "name")
	private String name;

	@Column(name = "gap_in_weeks_from_previous_vaccine")
	private Integer gapInWeeksFromPreviousVaccine;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "unit_previous_gap")
	private UNIT_GAP unitPrevGap;
	
	@Column(name = "gap_in_weeks_to_next_vaccine")
	private Integer gapInWeeksToNextVaccine;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "unit_next_gap")
	private UNIT_GAP unitNextGap;
	
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

	public Vaccine() {
	}

	public Vaccine(int vaccineId) {
		this.vaccineId = vaccineId;
	}

	public Vaccine(int vaccineId, String name, String description,
			String createdByUserId, String createdByUserName, Date createdDate,
			String lastEditedByUserId, String lastEditedByUserName,
			Date lastUpdated) {
		this.vaccineId = vaccineId;
		this.name = name;
		this.description = description;
		this.createdByUserId = createdByUserId;
		this.createdByUserName = createdByUserName;
		this.createdDate = createdDate;
		this.lastEditedByUserId = lastEditedByUserId;
		this.lastEditedByUserName = lastEditedByUserName;
		this.lastUpdated = lastUpdated;
	}

	public int getVaccineId() {
		return this.vaccineId;
	}

	public void setVaccineId(int vaccineId) {
		this.vaccineId = vaccineId;
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

	public void setVaccineNumberInForm(Integer vaccineNumberInForm) {
		this.vaccineNumberInForm = vaccineNumberInForm;
	}

	public Integer getVaccineNumberInForm() {
		return vaccineNumberInForm;
	}

	public void setVaccineNameInForm(String vaccineNameInForm) {
		this.vaccineNameInForm = vaccineNameInForm;
	}

	public String getVaccineNameInForm() {
		return vaccineNameInForm;
	}

	public void setGapInWeeksFromPreviousVaccine(
			Integer gapInWeeksFromPreviousVaccine) {
		this.gapInWeeksFromPreviousVaccine = gapInWeeksFromPreviousVaccine;
	}

	public Integer getGapInWeeksFromPreviousVaccine() {
		return gapInWeeksFromPreviousVaccine;
	}

	public void setGapInWeeksToNextVaccine(Integer gapInWeeksToNextVaccine) {
		this.gapInWeeksToNextVaccine = gapInWeeksToNextVaccine;
	}

	public Integer getGapInWeeksToNextVaccine() {
		return gapInWeeksToNextVaccine;
	}
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(Vaccine.class.getSimpleName());
		s.append("[");
		Field[] f = Vaccine.class.getDeclaredFields();
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

	public UNIT_GAP getUnitPrevGap() {
		return unitPrevGap;
	}

	public void setUnitPrevGap(UNIT_GAP unitPrevGap) {
		this.unitPrevGap = unitPrevGap;
	}

	public UNIT_GAP getUnitNextGap() {
		return unitNextGap;
	}

	public void setUnitNextGap(UNIT_GAP unitNextGap) {
		this.unitNextGap = unitNextGap;
	}
}