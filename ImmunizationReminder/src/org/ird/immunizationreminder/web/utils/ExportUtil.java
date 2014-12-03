package org.ird.immunizationreminder.web.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.ird.immunizationreminder.datamodel.entities.ReminderSms;
import org.ird.immunizationreminder.web.APVRRAnalysisGridRow;
import org.ird.immunizationreminder.web.APVRRAnalysisGridRow.VaccinationRecord;

import com.mysql.jdbc.StringUtils;

public class ExportUtil {
	public static void makeExporterCsv(HttpServletResponse response,List<APVRRAnalysisGridRow> record) throws IOException {
		response.setContentType("application/zip"); 
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		response.setHeader("Content-Disposition", "attachment; filename=IMR_"+sdf.format(new Date())+".zip"); 
		
		ByteArrayOutputStream fw = new ByteArrayOutputStream();
		fw.write(("\"S. no.\",\"Child ID\",\"ARM\"" + ",\"VACC_REC_SIZE\""+//TODO del it
					",\"Vaccine\",\"(Penta1) DueDate\",\"(Penta1) VaccinationDate\"" +
						
						",\"Vaccine\",\"(Penta2) DueDate\",\"(Penta2) VaccinationDate\"" +
						",\"Responses\",\"Any Reminder late?\",\"Unsent Reminders\",\"Total Reminders\",\"Reminders Pending\"" +
						",\"(Penta2)Rem-3\",\"days late\",\"(Penta2)Rem-1\",\"days late\",\"(Penta2)Rem0\",\"days late\"" +
						",(Penta2)Rem1,\"days late\",(Penta2)Rem7,\"days late\"" +
						
						",\"Vaccine\",\"(Penta3) DueDate\",\"(Penta3) VaccinationDate\"" +
						",\"Responses\",\"Any Reminder late?\",\"Unsent Reminders\",\"Total Reminders\",\"Reminders Pending\"" +
						",\"(Penta3)Rem-3\",\"days late\",\"(Penta3)Rem-1\",\"days late\",\"(Penta3)Rem0\",\"days late\"" +
						",(Penta3)Rem1,\"days late\",(Penta3)Rem7,\"days late\"" +
						
						",\"Vaccine\",\"(Measles1) DueDate\",\"(Measles1) VaccinationDate\"" +
						",\"Responses\",\"Any Reminder late?\",\"Unsent Reminders\",\"Total Reminders\",\"Reminders Pending\"" +
						",\"(Measles1)Rem-3\",\"days late\",\"(Measles1)Rem-1\",\"days late\",\"(Measles1)Rem0\",\"days late\"" +
						",(Measles1)Rem1,\"days late\",(Measles1)Rem7,\"days late\"" +
						
						",\"Vaccine\",\"(Measles2) DueDate\",\"(Measles2) VaccinationDate\"" +
						",\"Responses\",\"Any Reminder late?\",\"Unsent Reminders\",\"Total Reminders\",\"Reminders Pending\"" +
						",\"(Measles2)Rem-3\",\"days late\",\"(Measles2)Rem-1\",\"days late\",\"(Measles2)Rem0\",\"days late\"" +
						",(Measles2)Rem1,\"days late\",(Measles2)Rem7,\"days late\"" +
						"").getBytes());
		fw.write('\n');
		
		int i=1;
		for(APVRRAnalysisGridRow rec:record){//one row is one child
				fw.write(("\""+i+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getChildId())?"":rec.getChild().getChildId())+"\"").getBytes());
				fw.write(',');
				fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(rec.getChild().getArm().getArmName())?"":rec.getChild().getArm().getArmName().trim())+"\"").getBytes());
				fw.write(',');
				List<VaccinationRecord> vaccRecord = rec.getRecord();//assumption: atleast there will be one record
				//loop 5 times over VaccinationRecordList to fill rows for P1 P2 P3 M1 M2
				//assumption vaccinations are less than or equal to 3 records
//TODO del it
				fw.write(("\""+vaccRecord.size()+"\"").getBytes());
				fw.write(',');
				for (int vloop = 0; vloop < 5; vloop++) {
					int vInd = (vaccRecord.size()-1) - vloop/*index of last - */; //take last one first
					if(vInd < vaccRecord.size() && vInd >= 0){//if this vaccination record doesnot exists i.e only two vaccs have been given
						String vaccineName = (vaccRecord.get(vInd).getVaccination().getVaccine() == null ? "N/A"
								: vaccRecord.get(vInd).getVaccination().getVaccine().getName().trim().replace(",", ",,"));
						fw.write(("\""+vaccineName+"\"").getBytes());
						fw.write(',');
						fw.write(("\""+(vaccRecord.get(vInd).getVaccination().getVaccinationDuedate() == null ? "" 
								: (sdf.format(vaccRecord.get(vInd).getVaccination().getVaccinationDuedate())))+"\"").getBytes());
						fw.write(',');
						fw.write(("\""+(vaccRecord.get(vInd).getVaccination().getVaccinationDate() == null ? "" 
								: (sdf.format(vaccRecord.get(vInd).getVaccination().getVaccinationDate())))+"\"").getBytes());
						fw.write(',');
						
						if(!vaccineName.toLowerCase().contains("penta1")){//IF vaccine is PENTA 1 Reminders arenot applicable
						fw.write(("\""+(vaccRecord.get(vInd).getResponse().size())+"\"").getBytes());
						fw.write(',');
						fw.write(("\""+(vaccRecord.get(vInd).getAnyReminderLate())+"\"").getBytes());
						fw.write(',');
	
						fw.write(("\""+(vaccRecord.get(vInd).getUnsentReminders())+"\"").getBytes());
						fw.write(',');
						fw.write(("\""+(vaccRecord.get(vInd).getReminderSms().size())+"\"").getBytes());
						fw.write(',');
						fw.write(("\""+(vaccRecord.get(vInd).getRemindersPending())+"\"").getBytes());
						fw.write(',');
						//loop through all reminders to get info about each reminder
						//all reminders from day -3 to 7 and write n/A in case of sms_arm
						List<ReminderSms> reminders = vaccRecord.get(vInd).getReminderSms();
						for (int rloop = 0 ; rloop < 5 ; rloop++) {
							int rInd = (reminders.size() - 1) - rloop;
							if(rInd < reminders.size() && rInd >= 0){
								fw.write(("\""+(reminders.get(rInd).getReminderStatus())+"\"").getBytes());
								fw.write(',');
								fw.write(("\""+(reminders.get(rInd).getDayDifference())+"\"").getBytes());
								fw.write(',');
							}
							else{//fill reminder cells with empty string representation
								fw.write(("\"\"").getBytes());
								fw.write(',');
								fw.write(("\"\"").getBytes());
								fw.write(',');
							}
						}
						}//end if
					}
					else{//if this vacc is not given fill cell with empty (***--***) data
						fw.write(("\"\"").getBytes());
						fw.write(',');
						fw.write(("\"\"").getBytes());
						fw.write(',');
						fw.write(("\"\"").getBytes());
						fw.write(',');
						
						fw.write(("\"\"").getBytes());
						fw.write(',');
						fw.write(("\"\"").getBytes());
						fw.write(',');
	
						fw.write(("\"\"").getBytes());
						fw.write(',');
						fw.write(("\"\"").getBytes());
						fw.write(',');
						fw.write(("\"\"").getBytes());
						fw.write(',');
						
						fw.write(("\"\"").getBytes());
						fw.write(',');
						fw.write(("\"\"").getBytes());
						fw.write(',');
					}
			}
			i++;
			fw.write('\n');
		}
		
		byte[] b=fw.toString().getBytes();
		ZipOutputStream zip = new ZipOutputStream(response.getOutputStream());
		zip.putNextEntry(new ZipEntry("IMR_Child_Vacc_reminder_response.csv"));
		zip.write(b);
		zip.closeEntry();
		
		zip.close();
	}
}
