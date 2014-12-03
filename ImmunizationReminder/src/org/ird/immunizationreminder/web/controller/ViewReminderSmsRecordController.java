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
import org.ird.immunizationreminder.datamodel.entities.ReminderSms;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms.REMINDER_STATUS;
import org.ird.immunizationreminder.utils.date.DateUtils;
import org.ird.immunizationreminder.utils.validation.DataValidation;
import org.ird.immunizationreminder.utils.validation.REG_EX;
import org.ird.immunizationreminder.web.utils.IMRGlobals;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;

public class ViewReminderSmsRecordController implements Controller{

	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
    	int currentPage=0;
    	int totalPages=0;
    	int currentRows=25;//Fetch size
    	int totalRows=0;
		LoggedInUser user=UserSessionUtils.getActiveUser(req);
		if(user==null){
			return new ModelAndView(new RedirectView("login.htm"));
		}
		ServiceContext sc = Context.getServices();
		try{
			Map<String, Object> model=new HashMap<String, Object>();
			List<ReminderSms> rsmslst=new ArrayList<ReminderSms>();
			
			String lastSearchChildId=req.getParameter("lastSearchChildId");
	
			String lastSearchCellNum=req.getParameter("lastSearchCellNum");
	
			String lastSearchDuedate1=req.getParameter("lastSearchDuedate1");
	
			String lastSearchDuedate2=req.getParameter("lastSearchDuedate2");
			
			String lastSearchSentdate1=req.getParameter("lastSearchSentdate1");
	
			String lastSearchSentdate2=req.getParameter("lastSearchSentdate2");
	
			String lastSearchArmName=req.getParameter("lastSearchArmName");
			
			String lastSearchVaccineName=req.getParameter("lastSearchVaccineName");
	
			String lastSearchRemstatus=req.getParameter("lastSearchRemstatus");
	
			String lastSearchReminderName=req.getParameter("lastSearchReminderName");
	
			String lastSearchRemstatusNotchked=req.getParameter("lastSearchRemstatusNotchked");
			
			if(req.getParameter("action")!=null&&req.getParameter("action").compareTo("display")==0){//request is to navigate pages
				
				String childId_Name=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchChildId))?null:
					lastSearchChildId;
				String cellNumber=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchCellNum))?null:
					lastSearchCellNum;
				Date duedate1=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchDuedate1))?null:
					DateUtils.convertToDate(lastSearchDuedate1);
				Date duedate2=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchDuedate2))?null:
					DateUtils.convertToDate(lastSearchDuedate2);
				Date sentdate1=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchSentdate1))?null:
					DateUtils.convertToDate(lastSearchSentdate1);
				Date sentdate2=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchSentdate2))?null:
					DateUtils.convertToDate(lastSearchSentdate2);
				String armName=StringUtils.isEmptyOrWhitespaceOnly(lastSearchArmName)?null:
					lastSearchArmName;
				String vaccineName=StringUtils.isEmptyOrWhitespaceOnly(lastSearchVaccineName)?null:
					lastSearchVaccineName;			
				REMINDER_STATUS remStatus=StringUtils.isEmptyOrWhitespaceOnly(lastSearchRemstatus)?null:
					REMINDER_STATUS.valueOf(lastSearchRemstatus);			
				String remName=StringUtils.isEmptyOrWhitespaceOnly(lastSearchReminderName)?null:
					lastSearchReminderName;	
				boolean isNotChecked=StringUtils.isEmptyOrWhitespaceOnly(lastSearchRemstatusNotchked)?false:true;
				totalPages=Integer.parseInt(req.getParameter("totalPages"));
				currentPage=Integer.parseInt(req.getParameter("pagedir"));
				if(currentPage>=0 && currentPage<=totalPages){
					int startRecord=currentPage*currentRows;
					if(childId_Name!=null&&DataValidation.validate(REG_EX.NUMERIC, lastSearchChildId
							,Integer.parseInt(Context.getIRSetting("child.child-id.min-length", "4"))
							,Integer.parseInt(Context.getIRSetting("child.child-id.max-length", "10")))){
						rsmslst= sc.getReminderService().findReminderSmsRecordByCriteria(childId_Name
								, remName, vaccineName, duedate1, duedate2, sentdate1, sentdate2, remStatus
								, isNotChecked, startRecord, currentRows,true,FetchMode.JOIN);
					}else{
						rsmslst= sc.getReminderService().findReminderSmsRecordByCriteriaIncludeName
								(childId_Name, remName, vaccineName, duedate1, duedate2, sentdate1, sentdate2
								, cellNumber, remStatus, isNotChecked,armName, startRecord, currentRows,true,FetchMode.JOIN);
					}
				}
					req.setAttribute("searchlog", req.getParameter("searchlog"));
					
				}else if(req.getParameter("action")==null){//page is accessed first time
					Calendar c1 = Calendar.getInstance();
					c1.setTime(new Date(System.currentTimeMillis() - 1000*60*60*24*3));
					Calendar c2 = Calendar.getInstance();
					c2.setTime(new Date(System.currentTimeMillis() + 1000*60*60*24*1));
					
					rsmslst= sc.getReminderService().findReminderSmsRecordByCriteriaIncludeName
							(null, null, null, c1.getTime(), c2.getTime(), null, null
							, null, null, false,null, 0, currentRows,true,FetchMode.JOIN);					
					lastSearchChildId="";
					lastSearchCellNum="";
					lastSearchDuedate1=new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT).format(c1.getTime());
					lastSearchDuedate2=new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT).format(c2.getTime());
					lastSearchSentdate1="";
					lastSearchSentdate2="";
					lastSearchArmName="";
					lastSearchVaccineName="";
					lastSearchRemstatus="";
					lastSearchReminderName="";
					lastSearchRemstatusNotchked="";
					
					req.setAttribute("searchlog", "Due Date :"+lastSearchDuedate1+" to "+lastSearchDuedate2);
			}else{//new search display from 0
				
				String childId_Name=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("childId")))?null:
							req.getParameter("childId");
				String cellNumber=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("cellNum")))?null:
					req.getParameter("cellNum");
				Date duedate1=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("duedate1")))?null:
						DateUtils.convertToDate(req.getParameter("duedate1"));
				Date duedate2;
				if(duedate1==null){
					duedate2=null;
				}else{
					duedate2=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("duedate2")))?new Date():
						DateUtils.convertToDate(req.getParameter("duedate2"));
				}
				Date sentdate1=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("sentdate1")))?null:
					DateUtils.convertToDate(req.getParameter("sentdate1"));
				Date sentdate2;
				if(sentdate1==null){
					sentdate2=null;
				}else{
					sentdate2=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("sentdate2")))?new Date():
					DateUtils.convertToDate(req.getParameter("sentdate2"));
				}
				String armName=StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("armName"))?null:
					req.getParameter("armName");	
				String vaccName=StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccName"))?null:
					req.getParameter("vaccName");			
				REMINDER_STATUS remstatus=StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("remstatus"))?null:
					REMINDER_STATUS.valueOf(req.getParameter("remstatus"));			
				String remName=StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("remName"))?null:
							req.getParameter("remName");			
				boolean isNotChecked=StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("remstatusNotchk"))?false:true;
	
				if(childId_Name!=null&&DataValidation.validate(REG_EX.NUMERIC, childId_Name
						,Integer.parseInt(Context.getIRSetting("child.child-id.min-length", "4"))
						,Integer.parseInt(Context.getIRSetting("child.child-id.max-length", "10")))){
					rsmslst= sc.getReminderService().findReminderSmsRecordByCriteria(childId_Name
							, remName, vaccName, duedate1, duedate2, sentdate1, sentdate2, remstatus
							, isNotChecked, 0, currentRows,true,FetchMode.JOIN);
				}else{
					rsmslst= sc.getReminderService().findReminderSmsRecordByCriteriaIncludeName
							(childId_Name, remName, vaccName, duedate1, duedate2, sentdate1, sentdate2
							, cellNumber, remstatus, isNotChecked,armName, 0, currentRows,true,FetchMode.JOIN);
				}
				//override last searches
					lastSearchChildId=childId_Name==null?"":childId_Name;
					lastSearchCellNum=cellNumber==null?"":cellNumber;
					lastSearchDuedate1=duedate1==null?"":new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT).format(duedate1);
					lastSearchDuedate2=duedate2==null?"":new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT).format(duedate2);
					lastSearchSentdate1=sentdate1==null?"":new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT).format(sentdate1);
					lastSearchSentdate2=sentdate2==null?"":new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT).format(sentdate2);
					lastSearchArmName=armName==null?"":armName;
					lastSearchVaccineName=vaccName==null?"":vaccName;
					lastSearchRemstatus=(remstatus==null?"":remstatus.name());
					lastSearchReminderName=remName==null?"":remName;
					lastSearchRemstatusNotchked=isNotChecked?"NOT":"";
					
				req.setAttribute("searchlog", "Child ID/Name :"+lastSearchChildId+" , Cell Number :"+lastSearchCellNum
						+" , Due Date :"+lastSearchDuedate1+" to "+lastSearchDuedate2+" , Sent Date :"+lastSearchSentdate1
						+" to "+lastSearchSentdate2+" , Arm :"+lastSearchArmName
						+" , Reminder :"+lastSearchReminderName/*+" , Vaccine :"+lastSearchVaccineName*/
						+" , ReminderSms status :"+lastSearchRemstatusNotchked+" "+lastSearchRemstatus);
			}
			totalRows= sc.getReminderService().LAST_QUERY_TOTAL_ROW__COUNT().intValue();
	//so that our count int is not lost or overridden
			req.setAttribute("lastSearchChildId", lastSearchChildId);
			req.setAttribute("lastSearchCellNum", lastSearchCellNum);
			req.setAttribute("lastSearchDuedate1", lastSearchDuedate1);
			req.setAttribute("lastSearchDuedate2", lastSearchDuedate2);
			req.setAttribute("lastSearchSentdate1", lastSearchSentdate1);
			req.setAttribute("lastSearchSentdate2", lastSearchSentdate2);
			req.setAttribute("lastSearchArmName", lastSearchArmName);
			req.setAttribute("lastSearchVaccineName", lastSearchVaccineName);
			req.setAttribute("lastSearchRemstatus", lastSearchRemstatus);
			req.setAttribute("lastSearchReminderName", lastSearchReminderName);
			req.setAttribute("lastSearchRemstatusNotchked", lastSearchRemstatusNotchked);
	
			model.put("arm",  sc.getArmService().getAll(true));
			model.put("vaccine",  sc.getVaccinationService().getAll(0, 100));		
			model.put("reminder",  sc.getReminderService().getAllReminders(0,100,true,FetchMode.DEFAULT));
			
			model.put("remindersms", rsmslst);
			totalPages=(int) Math.ceil(totalRows/currentRows);
			
			req.setAttribute("currentPage", currentPage);
			req.setAttribute("totalPages", totalPages);
			req.setAttribute("currentRows", currentRows);
			req.setAttribute("totalRows", totalRows);
			
			return new ModelAndView("viewReminderSmsRecord","model",model);
		}catch (Exception e) {
			req.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}finally{
			sc.closeSession();
		}
	}
}