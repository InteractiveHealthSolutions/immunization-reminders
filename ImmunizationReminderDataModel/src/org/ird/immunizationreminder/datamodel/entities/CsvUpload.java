package org.ird.immunizationreminder.datamodel.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity 
@Table(name = "csv_upload")
public class CsvUpload {
	@Id
	@GenericGenerator(name="generator", strategy="increment")
    @GeneratedValue(generator="generator")
	@Column(name = "record_number")
	private long recordNumber;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "csv_file")
	private String csvFile;
	
	@Column(name = "number_of_rows")
	private long numberOfRows;
	
	@Column(name = "number_of_rows_rejected")
	private long numberOfRowsRejected;
	
	@Column(name = "number_of_rows_saved")
	private long numberOfRowsSaved;
	
	@Column(name = "upload_errors")
	private String uploadErrors;
	
	@Column(name = "upload_report")
	private String uploadReport;
	
	@Column(name = "uploaded_by_user_id")
	private String uploadedByUserId;
	
	@Column(name = "uploaded_by_user_name")
	private String uploadedByUserName;
	
	@Temporal (TemporalType.TIMESTAMP)
	@Column(name = "date_uploaded")
	private Date dateUploaded;

	public void setRecordNumber(long recordNumber) {
		this.recordNumber = recordNumber;
	}
	public long getRecordNumber() {
		return recordNumber;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public void setDateUploaded(Date dateUploaded) {
		this.dateUploaded = dateUploaded;
	}
	public Date getDateUploaded() {
		return dateUploaded;
	}
	public void setCsvFile(String csvFile) {
		this.csvFile = csvFile;
	}
	public String getCsvFile() {
		return csvFile;
	}
	public void setNumberOfRows(long numberOfRows) {
		this.numberOfRows = numberOfRows;
	}
	public long getNumberOfRows() {
		return numberOfRows;
	}

	public void setUploader(User creator){
		uploadedByUserId=creator.getName();
		uploadedByUserName=creator.getFullName();
		dateUploaded=new Date();
	}
	public void setNumberOfRowsRejected(long numberOfRowsRejected) {
		this.numberOfRowsRejected = numberOfRowsRejected;
	}
	public long getNumberOfRowsRejected() {
		return numberOfRowsRejected;
	}
	public void setNumberOfRowsSaved(long numberOfRowsSaved) {
		this.numberOfRowsSaved = numberOfRowsSaved;
	}
	public long getNumberOfRowsSaved() {
		return numberOfRowsSaved;
	}
	public void setUploadReport(String uploadReport) {
		this.uploadReport = uploadReport;
	}
	public String getUploadReport() {
		return uploadReport;
	}
	public void setUploadedByUserId(String uploadedByUserId) {
		this.uploadedByUserId = uploadedByUserId;
	}
	public String getUploadedByUserId() {
		return uploadedByUserId;
	}
	public void setUploadedByUserName(String uploadedByUserName) {
		this.uploadedByUserName = uploadedByUserName;
	}
	public String getUploadedByUserName() {
		return uploadedByUserName;
	}
	public void setUploadErrors(String uploadErrors) {
		this.uploadErrors = uploadErrors;
	}
	public String getUploadErrors() {
		return uploadErrors;
	}
}
