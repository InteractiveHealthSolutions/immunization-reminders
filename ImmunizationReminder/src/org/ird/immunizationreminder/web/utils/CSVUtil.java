package org.ird.immunizationreminder.web.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.ird.immunizationreminder.datamodel.entities.ReminderSms;
import org.ird.immunizationreminder.datamodel.entities.Response;
import org.ird.immunizationreminder.utils.date.DateUtils;
import org.ird.immunizationreminder.web.APVRRAnalysisGridRow;
import org.ird.immunizationreminder.web.APVRRAnalysisGridRow.VaccinationRecord;
import org.ird.immunizationreminder.web.servlet.UploadDataServlet;

import com.mysql.jdbc.StringUtils;

public class CSVUtil {
	
public static void makeCsv(HttpServletResponse response,List<APVRRAnalysisGridRow> record,String ArmName) throws IOException {
	response.setContentType("application/zip"); 
	response.setHeader("Content-Disposition", "attachment; filename="+ArmName+"_APVRRRecord_"+DateUtils.convertToString(new Date()).substring(0,10)+".zip"); 
	ByteArrayOutputStream fw = new ByteArrayOutputStream();
	fw.write(("\"S. no.\",\"Child ID\",\"Epi Reg/MR Number\",\"Arm\",\"Center\",\"Name\",\"Father Name\"" +
			",\"Address\",\"Phone Num\",\"Alternate Contact num\",\"Current Cell Number\",\"Gender\"" +
			",\"Birthdate\",\" Is birthdate estimated ?\",\" Age (weeks)\",\" Status\"" +
			",\"Program Enrolled\",\"Date Enrolled\",\"Has completed ?\",\"Date completion\"" +
			",\"Termination Date\",\"Termination Reason\",\"Description\"").getBytes());
	fw.write('\n');
	
	int i=1;
	for(APVRRAnalysisGridRow rec:record){
		fw.write(("\""+i+"\"").getBytes());
		fw.write(',');
		fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getChildId())?"":rec.getChild().getChildId())+"\"").getBytes());
		fw.write(',');
		fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getMrNumber())?"":rec.getChild().getMrNumber().trim())+"\"").getBytes());
		fw.write(',');
		fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getArm().getArmName())?"":rec.getChild().getArm().getArmName().trim())+"\"").getBytes());
		fw.write(',');
		fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getClinic())?"":rec.getChild().getClinic().trim())+"\"").getBytes());
		fw.write(',');
		fw.write(("\""+(((StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getFirstName())?"":rec.getChild().getFirstName().trim())+" "+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getMiddleName())?"":rec.getChild().getMiddleName().trim())+""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getLastName())?"":rec.getChild().getLastName().trim())))+"\"").getBytes());
		fw.write(',');
		fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getFatherName())?"":rec.getChild().getFatherName().trim())+"\"").getBytes());
		fw.write(',');
		fw.write(("\""+((
				(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getHouseNum())?"":rec.getChild().getHouseNum())+" "+
				(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getStreetNum())?"":rec.getChild().getStreetNum())+" "+
				(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getSector())?"":rec.getChild().getSector())+" "+
				(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getColony())?"":rec.getChild().getColony())+" "+
				(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getTown())?"":rec.getChild().getTown())+" "+
				(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getUcNum())?"":rec.getChild().getUcNum())+" "+
				(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getLandmark())?"":rec.getChild().getLandmark())
				).replace(",",",,").replace("\"","\"\""))+"\"").getBytes());
		fw.write(',');
		fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getPhoneNo())?"":rec.getChild().getPhoneNo().trim())+"\"").getBytes());
		fw.write(',');
		fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getAlternateCellNo())?"":rec.getChild().getAlternateCellNo().replace(",",",,"))+"\"").getBytes());	
		fw.write(',');
		fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getCurrentCellNo())?"":rec.getChild().getCurrentCellNo().replace(",",",,"))+"\"").getBytes());
		fw.write(',');
		fw.write(("\""+(rec.getChild().getGender())+"\"").getBytes());
		fw.write(',');
		fw.write(("\""+(rec.getChild().getBirthdate()==null?"":DateUtils.convertToString(rec.getChild().getBirthdate()))+"\"").getBytes());
		fw.write(',');
		fw.write(("\""+(rec.getChild().getEstimatedBirthdate())+"\"").getBytes());
		fw.write(',');
		fw.write(("\""+(rec.getChild().getAge())+"\"").getBytes());
		fw.write(',');
		fw.write(("\""+(rec.getChild().getStatus())+"\"").getBytes());
		fw.write(',');
		fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getProgrammeEnrolled())?"":rec.getChild().getProgrammeEnrolled().replace(",",",,").trim())+"\"").getBytes());
		fw.write(',');
		fw.write(("\""+(rec.getChild().getDateEnrolled()==null?"":DateUtils.convertToString(rec.getChild().getDateEnrolled()))+"\"").getBytes());
		fw.write(',');
		fw.write(("\""+(rec.getChild().getHasCompleted())+"\"").getBytes());
		fw.write(',');
		fw.write(("\""+(rec.getChild().getDateOfCompletion()==null?"":rec.getChild().getDateOfCompletion())+"\"").getBytes());
		fw.write(',');
		fw.write(("\""+(rec.getChild().getTerminationDate()==null?"":rec.getChild().getTerminationDate())+"\"").getBytes());
		fw.write(',');
		fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getTerminationReason())?"":rec.getChild().getTerminationReason().replace(",",",,").trim())+"\"").getBytes());
		fw.write(',');
		fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getDescription())?"":rec.getChild().getDescription().replace(",",",,").replace("\"", "'").trim())+"\"").getBytes());
		fw.write('\n');
		i++;
	}
	
	byte[] b=fw.toString().getBytes();
	
	ZipOutputStream zip = new ZipOutputStream(response.getOutputStream());
	zip.putNextEntry(new ZipEntry("Children.csv"));
	zip.write(b);
	zip.closeEntry();

	fw.reset();
	
	fw.write(("\"S. no.\",\"Child ID\",\"Arm\",\"Center\",\"Gender\",\"Status\",\"Vaccination Record\"" +
			",\"Vaccine\",\"Due Date\",\"Vaccination Date\",\"Is First Vaccination ?\"" +
			",\"Vaccination Status\",\"Is Last Vaccination ?\",\"Child Responded ?\"" +
			",\"Previous Vaccination Record\",\"Next Vaccination Record\",\"Description\"" +
			",\"Total Reminders\",\"Reminders Pending\",\"Is Any Reminder Late ?\"" +
			",\"Max Days Late ?\",\"Total Responses Received\"").getBytes());
	fw.write('\n');
	
	i=1;
	for(APVRRAnalysisGridRow rec:record){
		if(rec.getRecord().size()>0){
			/*fw.write(("\""+i+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getChildId())?"":rec.getChild().getChildId())+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getArm().getArmName())?"":rec.getChild().getArm().getArmName().trim())+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getClinic())?"":rec.getChild().getClinic().trim())+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(rec.getChild().getGender())+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(rec.getChild().getStatus())+"\"").getBytes());
			fw.write(',');

			i++;
			
			fw.write('\n');
		}*/
		for(VaccinationRecord recl:rec.getRecord()){
			fw.write(("\""+i+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getChildId())?"":rec.getChild().getChildId())+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getArm().getArmName())?"":rec.getChild().getArm().getArmName().trim())+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getClinic())?"":rec.getChild().getClinic().trim())+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(rec.getChild().getGender())+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(rec.getChild().getStatus())+"\"").getBytes());
			fw.write(',');	
			fw.write(("\""+(recl.getVaccination().getVaccinationRecordNum())+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(recl.getVaccination().getVaccine()==null?"":recl.getVaccination().getVaccine().getName().trim().replace(",",",,"))+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(recl.getVaccination().getVaccinationDuedate()==null?"":recl.getVaccination().getVaccinationDuedate())+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(recl.getVaccination().getVaccinationDate()==null?"":recl.getVaccination().getVaccinationDate())+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(recl.getVaccination().getIsFirstVaccination())+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(recl.getVaccination().getVaccinationStatus())+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(recl.getVaccination().getIsLastVaccination())+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(recl.getVaccination().getChildResponded())+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(recl.getVaccination().getPreviousVaccinationRecordNum())+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(recl.getVaccination().getNextVaccinationRecordNum())+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(recl.getVaccination().getDescription())?"":recl.getVaccination().getDescription().trim().replace(",",",,").replace("\"", "'"))+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(recl.getReminderSms().size())+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(recl.getRemindersPending())+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(recl.getAnyReminderLate())+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(recl.getMaxDaysLate())+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(recl.getResponse().size())+"\"").getBytes());
			fw.write(',');
			
			i++;
			
			fw.write('\n');
		}//end for
		}//end if
	}
	
	b=fw.toString().getBytes();
	
	zip.putNextEntry(new ZipEntry("Vaccination.csv"));
	zip.write(b);
	zip.closeEntry();
	
	fw.reset();
	fw.write(("\"S. no.\",\"Child ID\",\"Arm\",\"Center\",\"Vaccination Record\",\"Vaccine\"" +
			",\"Due Date\",\"Vaccination Date\",\"Vaccination Status\",\"Child Responded ?\"" +
			",\"Reminder Sms Duedate\",\"Reminder Sms Sentdate\",\"Day number\",\"Reminder Status\"" +
			",\"Cancel Reason\",\"Is sms late ?\",\"Days Late\",\"Cell number\",\"Text\"").getBytes());
	fw.write('\n');
	
	i=1;
	for(APVRRAnalysisGridRow rec:record){
		if(rec.getRecord().size()>0){
			/*fw.write(("\""+i+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getChildId())?"":rec.getChild().getChildId())+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getArm().getArmName())?"":rec.getChild().getArm().getArmName().trim())+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getClinic())?"":rec.getChild().getClinic().trim())+"\"").getBytes());
			fw.write(',');
			
			i++;

			fw.write('\n');
		}*/
		for(VaccinationRecord recl:rec.getRecord()){
			if(recl.getReminderSms().size()<=0){
				fw.write(("\""+i+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getChildId())?"":rec.getChild().getChildId())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getArm().getArmName())?"":rec.getChild().getArm().getArmName().trim())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getClinic())?"":rec.getChild().getClinic().trim())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(recl.getVaccination().getVaccinationRecordNum())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(recl.getVaccination().getVaccine()==null?"":recl.getVaccination().getVaccine().getName())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(recl.getVaccination().getVaccinationDuedate()==null?"":recl.getVaccination().getVaccinationDuedate())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(recl.getVaccination().getVaccinationDate()==null?"":recl.getVaccination().getVaccinationDate())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(recl.getVaccination().getVaccinationStatus())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(recl.getVaccination().getChildResponded())+"\"").getBytes());
				fw.write(',');
				
				i++;
				
				fw.write('\n');
			}
				for (ReminderSms rsms : recl.getReminderSms()) {
					fw.write(("\""+i+"\"").getBytes());
					fw.write(',');
					fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getChildId())?"":rec.getChild().getChildId())+"\"").getBytes());
					fw.write(',');
					fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getArm().getArmName())?"":rec.getChild().getArm().getArmName().trim())+"\"").getBytes());
					fw.write(',');
					fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getClinic())?"":rec.getChild().getClinic().trim())+"\"").getBytes());
					fw.write(',');
					fw.write(("\""+(recl.getVaccination().getVaccinationRecordNum())+"\"").getBytes());
					fw.write(',');
					fw.write(("\""+(recl.getVaccination().getVaccine()==null?"":recl.getVaccination().getVaccine().getName().trim().replace(",",",,"))+"\"").getBytes());
					fw.write(',');
					fw.write(("\""+(recl.getVaccination().getVaccinationDuedate()==null?"":recl.getVaccination().getVaccinationDuedate())+"\"").getBytes());
					fw.write(',');
					fw.write(("\""+(recl.getVaccination().getVaccinationDate()==null?"":recl.getVaccination().getVaccinationDate())+"\"").getBytes());
					fw.write(',');
					fw.write(("\""+(recl.getVaccination().getVaccinationStatus())+"\"").getBytes());
					fw.write(',');
					fw.write(("\""+(recl.getVaccination().getChildResponded())+"\"").getBytes());
					fw.write(',');
					fw.write(("\""+(rsms.getDueDate()==null?"":rsms.getDueDate())+"\"").getBytes());
					fw.write(',');
					fw.write(("\""+(rsms.getSentDate()==null?"":rsms.getSentDate())+"\"").getBytes());
					fw.write(',');
					fw.write(("\""+(rsms.getDayNumber())+"\"").getBytes());
					fw.write(',');
					fw.write(("\""+(rsms.getReminderStatus())+"\"").getBytes());
					fw.write(',');
					fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rsms.getSmsCancelReason())?"":rsms.getSmsCancelReason().trim().replace(",",",,").replace("\"", "'"))+"\"").getBytes());
					fw.write(',');
					fw.write(("\""+(rsms.getIsSmsLate())+"\"").getBytes());
					fw.write(',');				
					fw.write(("\""+(rsms.getDayDifference())+"\"").getBytes());
					fw.write(',');
					fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rsms.getCellnumber())?"":rsms.getCellnumber().trim())+"\"").getBytes());
					fw.write(',');				
					fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rsms.getText())?"":rsms.getText().trim().replace(",",",,").replace("\"", "'"))+"\"").getBytes());
					fw.write(',');
					
					i++;
					
					fw.write('\n');
				}
		}//end for
		}//end if
	}
	
	b=fw.toString().getBytes();
	
	zip.putNextEntry(new ZipEntry("ChildVaccReminders.csv"));
	zip.write(b);
	zip.closeEntry();
	
	fw.reset();
	fw.write(("\"S. no.\",\"Child ID\",\"Arm\",\"Center\",\"Vaccination Record\",\"Vaccine\",\"Due Date\"" +
			",\"Vaccination Date\",\"Vaccination Status\",\"Child Responded ?\",\"Total Reminders\"" +
			",\"Reminders Pending\",\"Response Recieve date\",\"System Recieve date\"" +
			",\"Response Recieve time\",\"Cell number\",\"Text\"").getBytes());
	fw.write('\n');
	
	i=1;
	for(APVRRAnalysisGridRow rec:record){
		if(rec.getRecord().size()>0){
			/*fw.write(("\""+i+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getChildId())?"":rec.getChild().getChildId())+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getArm().getArmName())?"":rec.getChild().getArm().getArmName().trim())+"\"").getBytes());
			fw.write(',');
			fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getClinic())?"":rec.getChild().getClinic().trim())+"\"").getBytes());
			fw.write(',');
			
			i++;

			fw.write('\n');
		}*/
		for(VaccinationRecord recl:rec.getRecord()){
			if(recl.getResponse().size()<=0){
				fw.write(("\""+i+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getChildId())?"":rec.getChild().getChildId())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getArm().getArmName())?"":rec.getChild().getArm().getArmName().trim())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getClinic())?"":rec.getChild().getClinic().trim())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(recl.getVaccination().getVaccinationRecordNum())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(recl.getVaccination().getVaccine()==null?"":recl.getVaccination().getVaccine().getName().trim().replace(",", ",,"))+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(recl.getVaccination().getVaccinationDuedate()==null?"":recl.getVaccination().getVaccinationDuedate())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(recl.getVaccination().getVaccinationDate()==null?"":recl.getVaccination().getVaccinationDate())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(recl.getVaccination().getVaccinationStatus())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(recl.getVaccination().getChildResponded())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(recl.getReminderSms().size())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(recl.getRemindersPending())+"\"").getBytes());
				fw.write(',');
				
				i++;

				fw.write('\n');
			}
			for (Response resp : recl.getResponse()) {
				fw.write(("\""+i+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getChildId())?"":rec.getChild().getChildId())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getArm().getArmName())?"":rec.getChild().getArm().getArmName().trim())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getClinic())?"":rec.getChild().getClinic().trim())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(recl.getVaccination().getVaccinationRecordNum())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(recl.getVaccination().getVaccine()==null?"":recl.getVaccination().getVaccine().getName().trim().replace(",", ",,"))+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(recl.getVaccination().getVaccinationDuedate()==null?"":recl.getVaccination().getVaccinationDuedate())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(recl.getVaccination().getVaccinationDate()==null?"":recl.getVaccination().getVaccinationDate())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(recl.getVaccination().getVaccinationStatus())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(recl.getVaccination().getChildResponded())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(recl.getReminderSms().size())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(recl.getRemindersPending())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(resp.getRecieveDate()==null?"":resp.getRecieveDate())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(resp.getActualSystemDate()==null?"":resp.getActualSystemDate())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(resp.getRecieveTime()==null?"":resp.getRecieveTime())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(resp.getCellNo())?"":resp.getCellNo().trim())+"\"").getBytes());
				fw.write(',');				
				fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(resp.getResponseText())?"":resp.getResponseText().trim().replace(",",",,").replace("\"", "'"))+"\"").getBytes());
				fw.write(',');
				
				i++;
				
				fw.write('\n');
			}
		}//end for
		}//end if
	}
	
	b=fw.toString().getBytes();
	
	zip.putNextEntry(new ZipEntry("ChildVaccResponses.csv"));
	zip.write(b);
	zip.closeEntry();
	zip.close();
}

public static StringBuilder getCsvUploadReport(List<String> uploadMessages,List<String> saved,List<String> unsaved){
	
	StringBuilder report=new StringBuilder();
	
report.append(
		"<span style=\"font-size: 20px;font-weight: bolder;background-color: #dddddd;text-align: left;\">"+
		"Upload Process Messages:"+
		"</span>"+
		"<table class=\"datalist\" width=\"100%\" border=\"1px\">" +
			"<tr>"+
				"<th>" +
				"Errors found in uploading process" +
				"</th>" +
			"</tr>"
);
	if(uploadMessages!=null && uploadMessages.size()>0){
			for (String string : uploadMessages) {
				
report.append(
			"<tr>" +
				
				"<td>" +
				string+
				"</td>" +
			"</tr>" 
);
			}
	}else{
		report.append(
				"<tr>" +
					
					"<td>" +
					"Unsaved entries due to error in data values can be found below in 'Unsaved Entries' table but no Error or Warning found in uploading process itself or Csv architecture."+
					"</td>" +
				"</tr>" 
	);
	}
report.append("</table>");
	
report.append(
		"<span style=\"font-size: 20px;font-weight: bolder;background-color: #dddddd;text-align: left;\">" +
		"Saved Entries:"+
		"</span>"+
		"<table class=\"datalist\" width=\"100%\" border=\"1px\">" +
			"<tr>"+
				"<th>" +
				"Errors / Warnings" +
				"</th>" +	
				"<th>" +
				"Child id" +
				"</th>"+
				"<th>" +
				"Child name" +
				"</th>"+
				"<th>" +
				"Enrollment date" +
				"</th>"+
				"<th>" +
				"Line initials in file" +
				"</th>"+
			"</tr>"
);
	if(saved!=null && saved.size()>0){
			for (String string : saved) {
				String[] strr=string.split(UploadDataServlet.CSV_REPORT_FIELD_SEPARATOR,-1);
				String[] lineSplitted=strr[4].split("\t");
report.append(
			"<tr>" +
				"<td>" +///Errrors
				strr[0].replace("\n", "<br>~~") +
				"</td>" +
	
				"<td>" +//child id
				strr[1] +
				"</td>" +
				"<td>" +//child name
				strr[2] +
				"</td>" +
				"<td>" +//date Enrolled
				strr[3]+
				"</td>" +
				"<td>" +///line
				(lineSplitted.length>0?lineSplitted[0]:"Nil")+"--" +(lineSplitted.length>1?lineSplitted[1]:"Nil") +"--"+
				(lineSplitted.length>2?lineSplitted[2]:"Nil")+"--"+(lineSplitted.length>3?lineSplitted[3]:"Nil") +"--"+
				(lineSplitted.length>4?lineSplitted[4]:"Nil")+"--"+(lineSplitted.length>5?lineSplitted[5]:"Nil")  +"......(contd.).." +
				"</td>" +
			"</tr>"
);
			}
	}else{
		report.append(
				"<tr>" +
					"<td colspan=\"5\">" +
					"No rows saved. See errors / warnings in data values below in 'Unsaved Entries' table against each row."+
					"</td>" +
				"</tr>" 
	);
	}
report.append("</table>");
	
report.append(
		"<span style=\"font-size: 20px;font-weight: bolder;background-color: #dddddd;text-align: left;\">" +
		"Un Saved Entries:"+
		"</span>"+
		"<table class=\"datalist\" width=\"100%\" border=\"1px\">" +
		"<tr>"+
		"<th>" +
		"Errors / Warnings" +
		"</th>" +	
		"<th>" +
		"Child id" +
		"</th>"+
		"<th>" +
		"Child name" +
		"</th>"+
		"<th>" +
		"Enrollment date" +
		"</th>"+
		"<th>" +
		"Line initials in file" +
		"</th>"+
	"</tr>"
);
	if(unsaved!=null && unsaved.size()>0){
			for (String string : unsaved) {
				String[] strr=string.split(UploadDataServlet.CSV_REPORT_FIELD_SEPARATOR,-1);
				String[] lineSplitted=strr[4].split("\t");
report.append(
			"<tr>" +
				"<td>" +///Errrors
				strr[0].replace("\n", "<br>~~") +
				"</td>" +
	
				"<td>" +//child id
				strr[1] +
				"</td>" +
				"<td>" +//child name
				strr[2] +
				"</td>" +
				"<td>" +//date Enrolled
				strr[3]+
				"</td>" +
				"<td>" +///line
				(lineSplitted.length>0?lineSplitted[0]:"Nil")+"--" +(lineSplitted.length>1?lineSplitted[1]:"Nil") +"--"+
				(lineSplitted.length>2?lineSplitted[2]:"Nil")+"--"+(lineSplitted.length>3?lineSplitted[3]:"Nil") +"--"+
				(lineSplitted.length>4?lineSplitted[4]:"Nil")+"--"+(lineSplitted.length>5?lineSplitted[5]:"Nil")  +"......(contd.).." +
				"</td>" +
			"</tr>"
);
			}
	}else{
		report.append(
				"<tr>" +
					
					"<td colspan=\"5\">" +
					"No Unsaved entries found."+
					"</td>" +
				"</tr>" 
	);
	}
report.append("</table>");

	return report;
}
}
