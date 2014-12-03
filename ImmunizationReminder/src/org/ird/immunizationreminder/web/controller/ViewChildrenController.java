package org.ird.immunizationreminder.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.LoggedInUser;
import org.ird.immunizationreminder.context.ServiceContext;
import org.ird.immunizationreminder.datamodel.entities.Child;
import org.ird.immunizationreminder.datamodel.entities.Child.STATUS;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;

public class ViewChildrenController implements Controller{

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
		req.setAttribute("editOrUpdateMessage", req.getParameter("editOrUpdateMessage"));
		ServiceContext sc = Context.getServices();
		try{
			Map<String, Object> model=new HashMap<String, Object>();
			List<Child> plst=new ArrayList<Child>();
			
			String lastSearchChildid=req.getParameter("lastSearchChildid");
	
			String lastSearchChildnamepart=req.getParameter("lastSearchChildnamepart");
	
			String lastSearchCurrentcell=req.getParameter("lastSearchCurrentcell");
	
			String lastSearchClinic=req.getParameter("lastSearchClinic");
	
			String lastSearchArmName=req.getParameter("lastSearchArmName");
			
			String lastSearchFollowupstatusNotchked=req.getParameter("lastSearchFollowupstatusNotchked");
	
			String lastSearchFollowupstatus=req.getParameter("lastSearchFollowupstatus");
			
			String lastSearchMrNum=req.getParameter("lastSearchMrNum");

			if(req.getParameter("action")!=null&&req.getParameter("action").compareTo("display")==0){//request is to navigate pages
				
				String childId=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchChildid))?null:
					lastSearchChildid;
				String childNamepart=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchChildnamepart))?null:
					lastSearchChildnamepart;		
				String currentCell=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchCurrentcell))?null:
					lastSearchCurrentcell;
				STATUS Status=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchFollowupstatus))?null:
					STATUS.valueOf(lastSearchFollowupstatus);	
				boolean isnotChecked=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchFollowupstatusNotchked))?false:
					true;	
				String armName=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchArmName))?null:
					lastSearchArmName;	
				String clinic=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchClinic))?null:
					lastSearchClinic;	
				String mrNum=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchMrNum))?null:
					lastSearchMrNum;	
				totalPages=Integer.parseInt(req.getParameter("totalPages"));
				currentPage=Integer.parseInt(req.getParameter("pagedir"));
				if(currentPage>=0 && currentPage<=totalPages){
					int startRecord=currentPage*currentRows;
					if(childId!=null){
						Child pat= sc.getChildService().getChildbyChildId(childId , true);
						if(pat!=null)plst.add(pat);
					}
					if(currentCell!=null){
						Child pat= sc.getChildService().getChildbyCurrentCell(currentCell , true);
						if(pat!=null)plst.add(pat);				
					}
					if(mrNum!=null){
						List<Child> pat= sc.getChildService().findByEpiOrMrNumber(mrNum);
						if(pat!=null && pat.size()>0)plst.addAll(pat);				
					}
					if(childId==null&&currentCell==null&&mrNum==null){
						plst= sc.getChildService().findChildByCriteria(childNamepart, clinic, Status, isnotChecked, armName, startRecord, currentRows , true);
					}
				}
					req.setAttribute("searchlog", req.getParameter("searchlog"));
					
				}else if(req.getParameter("action")==null){//pqge is acced first time
					plst= sc.getChildService().getAllChildren(0, currentRows , true);
					
					lastSearchChildid="";
					lastSearchChildnamepart="";
					lastSearchCurrentcell="";
					lastSearchFollowupstatusNotchked="";
					lastSearchFollowupstatus="";
					lastSearchClinic="";
					lastSearchArmName="";
					lastSearchMrNum="";
					
					req.setAttribute("searchlog", "All Child record");
				}else{//new search display from 0
				
				String childId=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("childid")))?null:
					req.getParameter("childid");
				String childNamepart=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("childnamepart")))?null:
					req.getParameter("childnamepart");		
				String currentCell=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("currentcell")))?null:
					req.getParameter("currentcell");
				STATUS Status=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("followupstatus")))?null:
					STATUS.valueOf(req.getParameter("followupstatus"));	
				boolean isnotChecked=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("followupstatusNotchk")))?false:
					true;	
				String armName=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("armName")))?null:
					req.getParameter("armName");	
				String clinic=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("clinic")))?null:
					req.getParameter("clinic");	
				String mrNum=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("mrNumber")))?null:
					req.getParameter("mrNumber");
				if(childId!=null){
					Child pat= sc.getChildService().getChildbyChildId(childId , true);
					if(pat!=null)plst.add(pat);
				}
				if(currentCell!=null){
					Child pat= sc.getChildService().getChildbyCurrentCell(currentCell , true);
					if(pat!=null)plst.add(pat);				
				}
				if(mrNum!=null){
					List<Child> pat= sc.getChildService().findByEpiOrMrNumber(mrNum);
					if(pat!=null && pat.size()>0)plst.addAll(pat);				
				}
				if(childId==null&&currentCell==null&&mrNum==null){
					plst= sc.getChildService().findChildByCriteria(childNamepart, clinic, Status, isnotChecked, armName, 0, currentRows , true);
				}
				//override last searches
				lastSearchChildid=childId==null?"":childId;
				lastSearchChildnamepart=childNamepart==null?"":childNamepart;
				lastSearchCurrentcell=currentCell==null?"":currentCell;
				lastSearchFollowupstatusNotchked=isnotChecked?"NOT":"";
				lastSearchFollowupstatus=(Status==null?"":Status.name());//if true then set to  a val else set empty to make false next time
				lastSearchArmName=armName==null?"":armName;
				lastSearchClinic=clinic==null?"":clinic;
				lastSearchMrNum=mrNum==null?"":mrNum;
				req.setAttribute("searchlog", "child ID:"+lastSearchChildid+" , child name part :"+lastSearchChildnamepart+
						" , followup status : "+lastSearchFollowupstatusNotchked+" "+lastSearchFollowupstatus+
						" , clinic :"+lastSearchClinic+" , current cell :"+lastSearchCurrentcell+" , Arm :"+lastSearchArmName+" , MR Num :"+lastSearchMrNum);
			}
			
			req.setAttribute("lastSearchChildid", lastSearchChildid);
			req.setAttribute("lastSearchChildnamepart", lastSearchChildnamepart);
			req.setAttribute("lastSearchFollowupstatusNotchked", lastSearchFollowupstatusNotchked);
			req.setAttribute("lastSearchCurrentcell", lastSearchCurrentcell);
			req.setAttribute("lastSearchFollowupstatus", lastSearchFollowupstatus);
			req.setAttribute("lastSearchArmName", lastSearchArmName);
			req.setAttribute("lastSearchClinic", lastSearchClinic);
			req.setAttribute("lastSearchMrNum", lastSearchMrNum);

			model.put("arm",  sc.getArmService().getAll(true));
			model.put("child", plst);
			totalRows= sc.getChildService().LAST_QUERY_TOTAL_ROW__COUNT().intValue();
			totalPages=(int) Math.ceil(totalRows/currentRows);
			
			req.setAttribute("currentPage", currentPage);
			req.setAttribute("totalPages", totalPages);
			req.setAttribute("currentRows", currentRows);
			req.setAttribute("totalRows", totalRows);
			
			return new ModelAndView("viewChildren","model",model);
		}catch (Exception e) {
			req.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}finally{
			sc.closeSession();
		}
	}
}