package org.ird.immunizationreminder.datamodel.entities;

import java.lang.reflect.Field;
import java.util.Date;

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
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.ird.immunizationreminder.utils.date.DateUtils;
import org.ird.immunizationreminder.utils.date.DateUtils.TIME_INTERVAL;

@Entity
@Table (name = "child")
public class Child implements java.io.Serializable {

	public enum GENDER {
		MALE ,
		FEMALE;
	}

	public enum STATUS {
		/** Follow Up (Under Follow ups)*/
		FOLLOW_UP("FUP"), 
		/** Terminated*/
		TERMINATED("TRM"),
		/** Completed*/
		COMPLETED("CMP"),
		/** Unresponsive*/
		UNRESPONSIVE("UNRES");
		
		private String REPRESENTATION;
		public String getREPRESENTATION() {
			return REPRESENTATION;
		}
		private STATUS(String representation) {
			this.REPRESENTATION = representation;
		}
		
		public static STATUS findEnum(String representationString){
			for (STATUS en : STATUS.values()) {
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
	@Column(name = "child_record_num")
	private int		childRecordNum;
	
	@Column(name = "child_id")
	private String	childId;
	
	@Column(name = "first_name")
	private String	firstName;
	
	@Column(name = "middle_name")
	private String	middleName;
	
	@Column(name = "last_name")
	private String	lastName;
	
	@Column(name = "father_name")
	private String	fatherName;
	
	@Column(name = "current_cell_no")
	private String	currentCellNo;
	
	@Column(name = "alternate_cell_no")
	private String	alternateCellNo;//TODO change it to *ple
	
	@Enumerated(EnumType.STRING)
	@Column(name = "gender")
	private GENDER	gender;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "birthdate")
	private Date	birthdate;
	
	@Transient
	private Integer	age;
	
	@Column(name = "estimated_birthdate")
	private Boolean	estimatedBirthdate;
	
	@Column(name = "phone_no")
	private String	phoneNo;
	
	@Column(name = "house_num")
	private String	houseNum;
	
	@Column(name = "street_num")
	private String	streetNum;
	
	@Column(name = "sector")
	private String	sector;
	
	@Column(name = "town")
	private String	town;
	
	@Column(name = "colony")
	private String	colony;
	
	@Column(name = "uc_num")
	private String	ucNum;
	
	@Column(name = "landmark")
	private String	landmark;
	
	@Column(name = "city")
	private String	city;
	
	@Column(name = "province")
	private String	province;
	
	@Column(name = "country")
	private String	country;
	
	@Column(name = "postal_code")
	private String	postalCode;
	
	@Column(name = "description")
	private String	description;
	
	@Column(name = "programme_enrolled")
	private String	programmeEnrolled;
	
	@Column(name = "MR_number")
	private String	mrNumber;
	
	@Column(name = "clinic")
	private String	clinic;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_enrolled")
	private Date	dateEnrolled;
	
	@Column(name = "has_completed")
	private Boolean	hasCompleted;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_of_completion")
	private Date	dateOfCompletion;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "termination_date")
	private Date	terminationDate;
	
	@Column(name = "termination_reason")
	private String	terminationReason;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private STATUS	status;
	
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
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	//@Fetch (FetchMode.SELECT)
	@JoinColumn(name = "arm_id")
	private Arm		arm;
	
	@Formula (value = "(select a.arm_name from arm a where a.arm_id = arm_id)")
	private String displayArmName;

	public String getDisplayArmName(){
		return displayArmName;
	}
	public Child() {
	}

	public int getChildRecordNum() {
		return this.childRecordNum;
	}
	public void setChildRecordNum(int childRecordNum) {
		this.childRecordNum = childRecordNum;
	}

	public String getChildId() {
		return this.childId;
	}

	public void setChildId(String childId) {
		this.childId = childId;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFatherName() {
		return this.fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getCurrentCellNo() {
		return this.currentCellNo;
	}

	public void setCurrentCellNo(String currentCellNo) {
		this.currentCellNo = currentCellNo;
	}

	public String getAlternateCellNo() {
		return this.alternateCellNo;
	}

	public void setAlternateCellNo(String alternateCellNo) {
		this.alternateCellNo = alternateCellNo;
	}

	public GENDER getGender() {
		return this.gender;
	}

	public void setGender(GENDER gender) {
		this.gender = gender;
	}
	public Date getBirthdate() {
		return this.birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	public void setAge(Integer ageInWeeks) {
		age=ageInWeeks;
	}
	public Integer getAge() {
		if(getBirthdate()!=null){
			return age= DateUtils.differenceBetweenIntervals(new Date(), getBirthdate() , TIME_INTERVAL.WEEK);
		}
		return age;
	}

	public Boolean getEstimatedBirthdate() {
		return this.estimatedBirthdate;
	}

	public void setEstimatedBirthdate(Boolean estimatedBirthdate) {
		this.estimatedBirthdate = estimatedBirthdate;
	}
	public String getPhoneNo() {
		return this.phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getHouseNum() {
		return this.houseNum;
	}

	public void setHouseNum(String houseNum) {
		this.houseNum = houseNum;
	}

	public String getStreetNum() {
		return this.streetNum;
	}

	public void setStreetNum(String streetNum) {
		this.streetNum = streetNum;
	}

	public String getSector() {
		return this.sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getTown() {
		return this.town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getColony() {
		return this.colony;
	}

	public void setColony(String colony) {
		this.colony = colony;
	}

	public String getLandmark() {
		return this.landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostalCode() {
		return this.postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProgrammeEnrolled() {
		return this.programmeEnrolled;
	}

	public void setProgrammeEnrolled(String programmeEnrolled) {
		this.programmeEnrolled = programmeEnrolled;
	}

	public String getMrNumber() {
		return this.mrNumber;
	}

	public void setMrNumber(String mrNumber) {
		this.mrNumber = mrNumber;
	}

	public String getClinic() {
		return this.clinic;
	}

	public void setClinic(String clinic) {
		this.clinic = clinic;
	}

	public Date getDateEnrolled() {
		return this.dateEnrolled;
	}

	public void setDateEnrolled(Date dateEnrolled) {
		this.dateEnrolled = dateEnrolled;
	}

	public Date getDateOfCompletion() {
		return this.dateOfCompletion;
	}

	public void setDateOfCompletion(Date dateOfCompletion) {
		this.dateOfCompletion = dateOfCompletion;
	}

	public STATUS getStatus() {
		return this.status;
	}

	public void setStatus(STATUS Status) {
		this.status = Status;
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

	public Arm getArm() {
		return this.arm;
	}

	public void setArm(Arm arm) {
		this.arm = arm;
	}

	public void setHasCompleted(Boolean completed) {
		this.hasCompleted = completed;
	}

	public Boolean getHasCompleted() {
		return hasCompleted;
	}

	public void setTerminationDate(Date terminationDate) {
		this.terminationDate = terminationDate;
	}

	public Date getTerminationDate() {
		return terminationDate;
	}

	public void setTerminationReason(String terminationReason) {
		this.terminationReason = terminationReason;
	}

	public String getTerminationReason() {
		return terminationReason;
	}

	public void setUcNum(String ucNum) {
		this.ucNum = ucNum;
	}

	public String getUcNum() {
		return ucNum;
	}
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(Child.class.getSimpleName());
		s.append("[");
		Field[] f = Child.class.getDeclaredFields();
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
