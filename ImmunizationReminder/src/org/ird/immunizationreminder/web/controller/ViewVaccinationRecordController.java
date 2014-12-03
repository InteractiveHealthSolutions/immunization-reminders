package org.ird.immunizationreminder.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.LoggedInUser;
import org.ird.immunizationreminder.context.ServiceContext;
import org.ird.immunizationreminder.datamodel.entities.Vaccination;
import org.ird.immunizationreminder.datamodel.entities.Vaccination.VACCINATION_STATUS;
import org.ird.immunizationreminder.utils.date.DateUtils;
import org.ird.immunizationreminder.utils.validation.DataValidation;
import org.ird.immunizationreminder.utils.validation.REG_EX;
import org.ird.immunizationreminder.web.utils.IMRGlobals;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;

public class ViewVaccinationRecordController implements Controller{

	public ModelAndView handleRequest(HttpServletRequest req,HttpServletResponse resp) throws Exception {
    	int currentPage=0;
    	int totalPages=0;
    	int currentRows=25;//Fetch size
    	int totalRows=0;
		LoggedInUser user=UserSessionUtils.getActiveUser(req);
		if(user==null){
			return new ModelAndView(new RedirectView("login.htm"));
		}
		req.setAttribute("editOrUpdateMessage", req.getParameter("editOrUpdateMessage"));
		ServiceContext sc = Context.getServices();
		try{
			Map<String, Object> model=new HashMap<String, Object>();
			List<Vaccination> pvlst=new ArrayList<Vaccination>();
			
			String lastSearchChildId=req.getParameter("lastSearchChildId");
	
			String lastSearchRecordNum=req.getParameter("lastSearchRecordNum");
	
			String lastSearchDuedate1=req.getParameter("lastSearchDuedate1");
	
			String lastSearchDuedate2=req.getParameter("lastSearchDuedate2");
			
			String lastSearchVaccdate1=req.getParameter("lastSearchVaccdate1");
	
			String lastSearchVaccdate2=req.getParameter("lastSearchVaccdate2");
	
			String lastSearchArmName=req.getParameter("lastSearchArmName");
			
			String lastSearchVaccineName=req.getParameter("lastSearchVaccineName");
	
			String lastSearchVaccstatus=req.getParameter("lastSearchVaccstatus");
	
	//		String lastSearchVaccstatusNotchked=req.getParameter("lastSearchVaccstatusNotchked");
			
			if(req.getParameter("action")!=null&&req.getParameter("action").compareTo("display")==0){//request is to navigate pages
				
				String childId_Name=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchChildId))?null:
					lastSearchChildId;
				String recordNumber=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchRecordNum))?null:
					lastSearchRecordNum;
				Date duedate1=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchDuedate1))?null:
					DateUtils.convertToDate(lastSearchDuedate1);
				Date duedate2=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchDuedate2))?null:
					DateUtils.convertToDate(lastSearchDuedate2);
				Date vaccdate1=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchVaccdate1))?null:
					DateUtils.convertToDate(lastSearchVaccdate1);
				Date vaccdate2=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchVaccdate2))?null:
					DateUtils.convertToDate(lastSearchVaccdate2);
				String armName=StringUtils.isEmptyOrWhitespaceOnly(lastSearchArmName)?null:
					lastSearchArmName;
				String vaccineName=StringUtils.isEmptyOrWhitespaceOnly(lastSearchVaccineName)?null:
					lastSearchVaccineName;			
				VACCINATION_STATUS vaccStatus=StringUtils.isEmptyOrWhitespaceOnly(lastSearchVaccstatus)?null:
					VACCINATION_STATUS.valueOf(lastSearchVaccstatus);			
	//			boolean isNotChecked=StringUtils.isEmptyOrWhitespaceOnly(lastSearchVaccstatusNotchked)?false:true;
				totalPages=Integer.parseInt(req.getParameter("totalPages"));
				currentPage=Integer.parseInt(req.getParameter("pagedir"));
				if(currentPage>=0 && currentPage<=totalPages){
					int startRecord=currentPage*currentRows;
					if(recordNumber!=null){
						try{
							long id=Long.parseLong(recordNumber);
							Vaccination pvr=sc.getVaccinationService().getVaccinationRecord(id,true,FetchMode.JOIN,FetchMode.JOIN);
							if(pvr!=null)pvlst.add(pvr);
						}catch (Exception e) {
							// TODO: handle exception
						}
					}else if(childId_Name!=null&&DataValidation.validate(REG_EX.NUMERIC, lastSearchChildId
							,Integer.parseInt(Context.getIRSetting("child.child-id.min-length", "4"))
							,Integer.parseInt(Context.getIRSetting("child.child-id.max-length", "10")))){
						pvlst=sc.getVaccinationService()
									.findVaccinationRecordByCriteria(childId_Name, vaccineName
											, duedate1, duedate2, vaccdate1, vaccdate2, vaccStatus
											, startRecord, currentRows,true,FetchMode.JOIN,FetchMode.JOIN);
					}else{
						pvlst=sc.getVaccinationService()
								.findVaccinationRecordByCriteriaIncludeName(childId_Name
										, vaccineName, duedate1, duedate2, vaccdate1, vaccdate2
										, vaccStatus, armName,startRecord, currentRows,true,FetchMode.JOIN,FetchMode.JOIN);
					}
				}
					req.setAttribute("searchlog", req.getParameter("searchlog"));
					
				}else if(req.getParameter("action")==null){//page is accessed first time
					Calendar c1 = Calendar.getInstance();
					c1.setTime(new Date(System.currentTimeMillis() - 1000*60*60*24*7));
					Calendar c2 = Calendar.getInstance();
					c2.setTime(new Date(System.currentTimeMillis() + 1000*60*60*24*3));

					pvlst=sc.getVaccinationService()
							.findVaccinationRecordByCriteriaIncludeName(null
									, null, c1.getTime(), c2.getTime(), null, null
									, null, null,0, currentRows,true,FetchMode.JOIN,FetchMode.JOIN);

					lastSearchChildId="";
					lastSearchRecordNum="";
					lastSearchDuedate1=new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT).format(c1.getTime());
					lastSearchDuedate2=new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT).format(c2.getTime());
					lastSearchVaccdate1="";
					lastSearchVaccdate2="";
					lastSearchArmName="";
					lastSearchVaccineName="";
					lastSearchVaccstatus="";
					//lastSearchVaccstatusNotchked="";
					
					req.setAttribute("searchlog", "Due Date :"+lastSearchDuedate1+" to "+lastSearchDuedate2);
			}else{//new search display from 0
				
				String childId_Name=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("childId")))?null:
							req.getParameter("childId");
				String recordNumber=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("recordNum")))?null:
					req.getParameter("recordNum");
				Date duedate1=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("duedate1")))?null:
						DateUtils.convertToDate(req.getParameter("duedate1"));
				Date duedate2;
				if(duedate1==null){
					duedate2=null;
				}else{
					duedate2=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("duedate2")))?new Date():
						DateUtils.convertToDate(req.getParameter("duedate2"));
				}
				Date vaccdate1=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccdate1")))?null:
					DateUtils.convertToDate(req.getParameter("vaccdate1"));
				Date vaccdate2;
				if(vaccdate1==null){
					vaccdate2=null;
				}else{
					vaccdate2=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccdate2")))?new Date():
					DateUtils.convertToDate(req.getParameter("vaccdate2"));
				}
				String armName=StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("armName"))?null:
					req.getParameter("armName");	
				String vaccName=StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccName"))?null:
					req.getParameter("vaccName");			
				VACCINATION_STATUS vaccstatus=StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccstatus"))?null:
					VACCINATION_STATUS.valueOf(req.getParameter("vaccstatus"));			
	//			boolean isNotChecked=StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccstatusNotchk"))?false:true;
	
				if(recordNumber!=null){
					try{
						long id=Long.parseLong(recordNumber);
						Vaccination pvr=sc.getVaccinationService().getVaccinationRecord(id,true,FetchMode.JOIN,FetchMode.JOIN);
						if(pvr!=null)pvlst.add(pvr);
					}catch (Exception e) {
						// TODO: handle exception
					}
				}else if(childId_Name!=null&&DataValidation.validate(REG_EX.NUMERIC, childId_Name
						,Integer.parseInt(Context.getIRSetting("child.child-id.min-length", "4"))
						,Integer.parseInt(Context.getIRSetting("child.child-id.max-length", "10")))){
					pvlst=sc.getVaccinationService()
								.findVaccinationRecordByCriteria(childId_Name, vaccName
										, duedate1, duedate2, vaccdate1, vaccdate2, vaccstatus
										, 0, currentRows,true,FetchMode.JOIN,FetchMode.JOIN);
				}else{
					pvlst=sc.getVaccinationService()
							.findVaccinationRecordByCriteriaIncludeName(childId_Name
									, vaccName, duedate1, duedate2, vaccdate1, vaccdate2
									, vaccstatus, armName,0, currentRows,true,FetchMode.JOIN,FetchMode.JOIN);
				}
				//override last searches
					lastSearchChildId=childId_Name==null?"":childId_Name;
					lastSearchRecordNum=recordNumber==null?"":recordNumber;
					lastSearchDuedate1=duedate1==null?"":new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT).format(duedate1);
					lastSearchDuedate2=duedate2==null?"":new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT).format(duedate2);
					lastSearchVaccdate1=vaccdate1==null?"":new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT).format(vaccdate1);
					lastSearchVaccdate2=vaccdate2==null?"":new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT).format(vaccdate2);
					lastSearchArmName=armName==null?"":armName;
					lastSearchVaccineName=vaccName==null?"":vaccName;
					lastSearchVaccstatus=(vaccstatus==null?"":vaccstatus.name());
	//				lastSearchVaccstatusNotchked=isNotChecked?"NOT":"";
					
				req.setAttribute("searchlog", "Child ID/Name :"+lastSearchChildId+" , Record Number :"+lastSearchRecordNum
						+" , Due Date :"+lastSearchDuedate1+" to "+lastSearchDuedate2+" , Vacc Date :"+lastSearchVaccdate1
						+" to "+lastSearchVaccdate2+" , Arm :"+lastSearchArmName
						+" , Vaccine :"+lastSearchVaccineName
						+" , Vaccine status :"+lastSearchVaccstatus);
			}
			totalRows=sc.getVaccinationService().LAST_QUERY_TOTAL_ROW__COUNT().intValue();
	//so that our count int is not lost or overridden
			req.setAttribute("lastSearchChildId", lastSearchChildId);
			req.setAttribute("lastSearchRecordNum", lastSearchRecordNum);
			req.setAttribute("lastSearchDuedate1", lastSearchDuedate1);
			req.setAttribute("lastSearchDuedate2", lastSearchDuedate2);
			req.setAttribute("lastSearchVaccdate1", lastSearchVaccdate1);
			req.setAttribute("lastSearchVaccdate2", lastSearchVaccdate2);
			req.setAttribute("lastSearchArmName", lastSearchArmName);
			req.setAttribute("lastSearchVaccineName", lastSearchVaccineName);
			req.setAttribute("lastSearchVaccstatus", lastSearchVaccstatus);
	//		req.setAttribute("lastSearchVaccstatusNotchked", lastSearchVaccstatusNotchked);
	
			model.put("arm", sc.getArmService().getAll(true));
			model.put("vaccine", sc.getVaccinationService().getAll(0, 100));		
			
			model.put("pvaccination", pvlst);
			totalPages=(int) Math.ceil(totalRows/currentRows);
			
			req.setAttribute("currentPage", currentPage);
			req.setAttribute("totalPages", totalPages);
			req.setAttribute("currentRows", currentRows);
			req.setAttribute("totalRows", totalRows);
			
			return new ModelAndView("viewVaccinationRecord","model",model);
		}catch (Exception e) {
			req.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}finally{
			sc.closeSession();
		}
	}
}