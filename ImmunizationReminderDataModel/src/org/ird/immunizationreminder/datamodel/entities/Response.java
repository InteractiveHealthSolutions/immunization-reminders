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
@Table (name = "response")
public class Response implements java.io.Serializable {

	public enum RESPONSE_TYPE{
		DIRTY,
		VERIFIED
	}
	@Id
	@GenericGenerator(name="generator", strategy="increment")
    @GeneratedValue(generator="generator")
	@Column(name = "record_num")
	private long recordNum;
	
	@Column(name = "vaccination_record_num")
	private long vaccinationRecordNum;
	
	@Column(name = "cell_no")
	private String cellNo;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "recieve_date")
	private Date recieveDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "actual_system_date")
	private Date actualSystemDate;
	
	@Column(name = "recieve_time")
	private Time recieveTime;
	
	@Column(name = "response_text")
	private String responseText;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "response_type")
	private RESPONSE_TYPE responseType;
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "child_record_num")
	@Basic(fetch = FetchType.EAGER)
	private Child child;

	public Response() {
	}

	public Response(int recordNum) {
		this.recordNum = recordNum;
	}

	public Response(int recordNum, String cellNo, Date recieveDate,
			Time recieveTime, String responseText, RESPONSE_TYPE responseType) {
		this.recordNum = recordNum;
		this.cellNo = cellNo;
		this.recieveDate = recieveDate;
		this.recieveTime = recieveTime;
		this.responseText = responseText;
		this.responseType = responseType;
	}

	public long getRecordNum() {
		return this.recordNum;
	}

	public void setRecordNum(long recordNum) {
		this.recordNum = recordNum;
	}

	public String getCellNo() {
		return this.cellNo;
	}

	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}

	public Date getRecieveDate() {
		return this.recieveDate;
	}

	public void setRecieveDate(Date recieveDate) {
		this.recieveDate = recieveDate;
	}

	public Time getRecieveTime() {
		return this.recieveTime;
	}

	public void setRecieveTime(Time recieveTime) {
		this.recieveTime = recieveTime;
	}

	public String getResponseText() {
		return this.responseText;
	}

	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}

	public RESPONSE_TYPE getResponseType() {
		return this.responseType;
	}

	public void setResponseType(RESPONSE_TYPE resp) {
		responseType=resp;
	}

	public Child getChild() {
		return this.child;
	}

	public void setChild(Child child) {
		this.child = child;
	}

	public void setVaccinationRecordNum(long vaccinationRecordNum) {
		this.vaccinationRecordNum = vaccinationRecordNum;
	}

	public long getVaccinationRecordNum() {
		return vaccinationRecordNum;
	}

	public void setActualSystemDate(Date actualSystemDate) {
		this.actualSystemDate = actualSystemDate;
	}

	public Date getActualSystemDate() {
		return actualSystemDate;
	}
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(Response.class.getSimpleName());
		s.append("[");
		Field[] f = Response.class.getDeclaredFields();
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
