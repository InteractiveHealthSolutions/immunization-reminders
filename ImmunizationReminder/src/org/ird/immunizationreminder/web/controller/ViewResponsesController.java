package org.ird.immunizationreminder.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.ird.immunizationreminder.datamodel.entities.Response;
import org.ird.immunizationreminder.utils.date.DateUtils;
import org.ird.immunizationreminder.utils.validation.DataValidation;
import org.ird.immunizationreminder.utils.validation.REG_EX;
import org.ird.immunizationreminder.web.utils.IMRGlobals;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;

public class ViewResponsesController implements Controller{

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
			List<Response> prlst=new ArrayList<Response>();
			
			String lastSearchChildId=req.getParameter("lastSearchChildId");
	
			String lastSearchDate1=req.getParameter("lastSearchDate1");
	
			String lastSearchDate2=req.getParameter("lastSearchDate2");
	
			String lastSearchCellNum=req.getParameter("lastSearchCellNum");
			
			String lastSearchArmName=req.getParameter("lastSearchArmName");
	
			if(req.getParameter("action")!=null&&req.getParameter("action").compareTo("display")==0){//request is to navigate pages
				
				String childId_Name=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchChildId))?null:
					lastSearchChildId;
				Date date1=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchDate1))?null:
					DateUtils.convertToDate(lastSearchDate1);
				Date date2=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchDate2))?null:
					DateUtils.convertToDate(lastSearchDate2);
				String cellNumber=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchCellNum))?null:
					lastSearchCellNum;
				String armName=StringUtils.isEmptyOrWhitespaceOnly(lastSearchArmName)?null:
					lastSearchArmName;
				
				totalPages=Integer.parseInt(req.getParameter("totalPages"));
				currentPage=Integer.parseInt(req.getParameter("pagedir"));
				if(currentPage>=0 && currentPage<=totalPages){
					int startRecord=currentPage*currentRows;
					if(childId_Name!=null&&DataValidation.validate(REG_EX.NUMERIC, lastSearchChildId
							,Integer.parseInt(Context.getIRSetting("child.child-id.min-length", "4"))
							,Integer.parseInt(Context.getIRSetting("child.child-id.max-length", "10")))){
						prlst=sc.getResponseService()
								.getByCriteriaIncludeChildId(childId_Name, date1, date2
										, cellNumber, null, startRecord, currentRows,true,FetchMode.JOIN);
					}else{
						prlst=sc.getResponseService().getByCriteriaIncludeName(childId_Name, date1
								, date2, cellNumber,armName, null, startRecord, currentRows,true,FetchMode.JOIN);
					}
				}
					req.setAttribute("searchlog", req.getParameter("searchlog"));
					
				}else if(req.getParameter("action")==null){//pqge is acced first time
					prlst=sc.getResponseService().getAllResponseRecord(0, currentRows,true,FetchMode.JOIN);
					
					lastSearchChildId="";
					lastSearchDate1="";
					lastSearchDate2="";
					lastSearchCellNum="";
					lastSearchArmName="";
					
					req.setAttribute("searchlog", "All Child-Response record");
			}else{//new search display from 0
				
				String childId_Name=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("childId")))?null:
							req.getParameter("childId");
				Date date1=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("date1")))?null:
						DateUtils.convertToDate(req.getParameter("date1"));
				Date date2;
				if(date1==null){
					date2=null;
				}else{
					date2=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("date2")))?new Date():
						DateUtils.convertToDate(req.getParameter("date2"));
				}
				String cellNumber=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("cellNum")))?null:
						req.getParameter("cellNum");
				String armName=StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("armName"))?null:
					req.getParameter("armName");			
				if(childId_Name!=null&&DataValidation.validate(REG_EX.NUMERIC, childId_Name
						,Integer.parseInt(Context.getIRSetting("child.child-id.min-length", "4"))
						,Integer.parseInt(Context.getIRSetting("child.child-id.max-length", "10")))){
					prlst=sc.getResponseService()
							.getByCriteriaIncludeChildId(childId_Name, date1, date2
									, cellNumber, null, 0, currentRows,true,FetchMode.JOIN);
				}else{
					prlst=sc.getResponseService().getByCriteriaIncludeName(childId_Name, date1
							, date2, cellNumber,armName, null, 0, currentRows,true,FetchMode.JOIN);
				}
				//override last searches
					lastSearchChildId=childId_Name==null?"":childId_Name;
					lastSearchDate1=date1==null?"":new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT).format(date1);
					lastSearchDate2=date2==null?"":new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT).format(date2);
					lastSearchCellNum=cellNumber==null?"":cellNumber;
					lastSearchArmName=armName==null?"":armName;
					
				req.setAttribute("searchlog", "Child ID/Name :"+lastSearchChildId+" , Date :"+lastSearchDate1+" to "+lastSearchDate2+" , Cell Number :"+lastSearchCellNum+" , Arm :"+lastSearchArmName);
			}
			
			req.setAttribute("lastSearchChildId", lastSearchChildId);
			req.setAttribute("lastSearchDate1", lastSearchDate1);
			req.setAttribute("lastSearchDate2", lastSearchDate2);
			req.setAttribute("lastSearchCellNum", lastSearchCellNum);
			req.setAttribute("lastSearchArmName", lastSearchArmName);
			
			model.put("arm", sc.getArmService().getAll(true));
	
			model.put("response", prlst);
			totalRows=sc.getResponseService().LAST_QUERY_TOTAL_ROW__COUNT().intValue();
			totalPages=(int) Math.ceil(totalRows/currentRows);
			
			req.setAttribute("currentPage", currentPage);
			req.setAttribute("totalPages", totalPages);
			req.setAttribute("currentRows", currentRows);
			req.setAttribute("totalRows", totalRows);
			
			return new ModelAndView("viewResponses","model",model);
		}catch (Exception e) {
			req.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}finally{
			sc.closeSession();
		}
	}
}