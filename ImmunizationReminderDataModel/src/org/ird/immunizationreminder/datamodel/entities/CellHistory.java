/*package org.ird.immunizationreminder.datamodel.entities;

import java.lang.reflect.Field;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table (name = "cell_history")
public class CellHistory implements java.io.Serializable {

	@Id
	@GenericGenerator(name="generator", strategy="increment")
    @GeneratedValue(generator="generator")
	@Column(name = "serial_num")
	private long serialNum;
	
	@Column(name = "cell_no")
	private String cellNo;
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "child_record_num")
	@Basic(fetch = FetchType.EAGER)
	private Child child;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_added")
	private Date dateAdded;
	
	@Column(name = "added_by_user_id")
	private String addedByUserId;
	
	@Column(name = "added_by_user_name")
	private String addedByUserName;

	public CellHistory() {
	}

	public CellHistory(long serialNum) {
		this.serialNum = serialNum;
	}

	public CellHistory(long serialNum, String cellNo, 
			Date dateAdded, String addedByUserId, String addedByUserName) {
		this.serialNum = serialNum;
		this.cellNo = cellNo;
		this.dateAdded = dateAdded;
		this.addedByUserId = addedByUserId;
		this.addedByUserName = addedByUserName;
	}

	public long getSerialNum() {
		return this.serialNum;
	}

	public void setSerialNum(long serialNum) {
		this.serialNum = serialNum;
	}

	public String getCellNo() {
		return this.cellNo;
	}

	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}

	public Child getChild() {
		return this.child;
	}

	public void setChild(Child child) {
		this.child = child;
	}

	public Date getDateAdded() {
		return this.dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public String getAddedByUserId() {
		return this.addedByUserId;
	}

	public void setAddedByUserId(String addedByUserId) {
		this.addedByUserId = addedByUserId;
	}

	public String getAddedByUserName() {
		return this.addedByUserName;
	}

	public void setAddedByUserName(String addedByUserName) {
		this.addedByUserName = addedByUserName;
	}
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(CellHistory.class.getSimpleName());
		s.append("[");
		Field[] f = CellHistory.class.getDeclaredFields();
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
*/