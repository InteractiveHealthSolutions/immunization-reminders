package org.ird.immunizationreminder.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.LoggedInUser;
import org.ird.immunizationreminder.context.ServiceContext;
import org.ird.immunizationreminder.data.exception.ChildDataException;
import org.ird.immunizationreminder.datamodel.entities.Child;
import org.ird.immunizationreminder.service.exception.UserServiceException;
import org.ird.immunizationreminder.utils.date.DateUtils;
import org.ird.immunizationreminder.utils.validation.DataValidation;
import org.ird.immunizationreminder.utils.validation.REG_EX;
import org.ird.immunizationreminder.web.APVRRAnalysisGridRow;
import org.ird.immunizationreminder.web.utils.IMRGlobals;
import org.ird.immunizationreminder.web.utils.IRUtils;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;

public class AnalyzeReminderController implements Controller{

	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse httpresponse) throws Exception {

		LoggedInUser user=UserSessionUtils.getActiveUser(req);
		if(user==null){
			return new ModelAndView(new RedirectView("login.htm"));
		}

		Map<String, Object> model=new HashMap<String, Object>();
		ServiceContext sc = Context.getServices();
		try{
	
			model.put("arm", sc.getArmService().getAll(true));
			
			String action=req.getParameter("action");
			
			if(action!=null && action.compareTo("display")==0){
				PagedListHolder record=(PagedListHolder)req.getSession().getAttribute("pagedList");
				if(record==null){
					req.getSession().setAttribute("logmessage", UserServiceException.SESSION_EXPIRED);
					return new ModelAndView(new RedirectView("login.htm"));
				}
				int page=Integer.parseInt(req.getParameter("pagedir"));
				if(page>=0&&page<=record.getPageCount()-1){
					record.setPage(page);
				}
				req.setAttribute("searchlog", req.getParameter("searchlog"));
	
				model.put("record", record.getPageList());
				return new ModelAndView("analyzeReminder","model",model);
			}
			try{
			PagedListHolder pagedListh=new PagedListHolder();
			pagedListh.setPageSize(5);
			
			SimpleDateFormat sd=new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT);
	
			String pIdent=StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("childId"))?null:
				req.getParameter("childId");
			String armName=StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("armName"))?null:
				req.getParameter("armName");
			Date date1=StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("date1"))?null:
				DateUtils.convertToDate(req.getParameter("date1"));
			Date date2;
			if(date1==null){
				date2=null;
			}else{
				date2=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("date2")))?new Date():
					DateUtils.convertToDate(req.getParameter("date2"));
			}
			List<Child> p=new ArrayList<Child>();
			List<APVRRAnalysisGridRow> recordList=new ArrayList<APVRRAnalysisGridRow>();
			//if page being loaded first time
			if(req.getParameter("action")==null){
				//setting date for today
				pIdent = "1";
			}
			
			try {
				if(pIdent!=null&&DataValidation.validate(REG_EX.NUMERIC, pIdent
						,Integer.parseInt(Context.getIRSetting("child.child-id.min-length", "4"))
						,Integer.parseInt(Context.getIRSetting("child.child-id.max-length", "10")))){
					Child pat=sc.getChildService().getChildbyChildId(pIdent,true);
					if(pat!=null)p.add(pat);
				}else{
					p = sc.getChildService().findChildByCriteria(pIdent, null, null, false
							, armName, 0, Integer.MAX_VALUE-1,true);
				}
			} catch (ChildDataException e1) {
				req.setAttribute("message", e1.getMessage());
			}
			try{
				for (Child child : p) {
					recordList.add(IRUtils.addRecord(child,date1,date2));
				}
					
			}catch (Exception e) {
				req.getSession().setAttribute("exceptionTrace", e);
				return new ModelAndView(new RedirectView("exception.htm"));
			}
				req.setAttribute("searchlog", "Child ID :"+pIdent+" , ARM :"+armName+" , Date :"+date1+"  to  "+date2);
	
				req.setAttribute("lastSearchChildId", pIdent==null?"":pIdent);
				req.setAttribute("dt1Search",date1==null?"":sd.format(date1));
				req.setAttribute("dt2Search", date2==null?"":sd.format(date2));
				req.setAttribute("lastSearchArmName", armName==null?"":armName);
				pagedListh.setSource(recordList);
				req.getSession().setAttribute("pagedList", pagedListh);
				model.put("record", pagedListh.getPageList());
				return new ModelAndView("analyzeReminder","model",model);
	
			}catch(Exception e){
				req.getSession().setAttribute("exceptionTrace", e);
				return new ModelAndView(new RedirectView("exception.htm"));
			}
		}finally{
			sc.closeSession();
		}
	}
}
