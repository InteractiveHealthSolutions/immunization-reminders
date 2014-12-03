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
import org.ird.immunizationreminder.datamodel.entities.IrSetting;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

public class ViewIrSettingsController implements Controller{

	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
    	int currentPage=0;
    	int totalPages=0;
    	int currentRows=100;//Fetch size
    	int totalRows=0;
		LoggedInUser user=UserSessionUtils.getActiveUser(req);
		if(user==null){
			return new ModelAndView(new RedirectView("login.htm"));
		}
		ServiceContext sc = Context.getServices();
		try{
			Map<String, Object> model=new HashMap<String, Object>();
			List<IrSetting> irl=new ArrayList<IrSetting>();
			if(req.getParameter("action")!=null&&req.getParameter("action").compareTo("display")==0){//request is to navigate pages
				String lastSearchSettingName=req.getParameter("lastSearchSettingName").trim();
				totalPages=Integer.parseInt(req.getParameter("totalPages"));
				currentPage=Integer.parseInt(req.getParameter("pagedir"));
				if(currentPage>=0 && currentPage<=totalPages){
					int startRecord=currentPage*currentRows;
					irl=sc.getIRSettingService().matchSetting(lastSearchSettingName);
					req.setAttribute("searchlog", req.getParameter("searchlog"));
					req.setAttribute("lastSearchSettingName", lastSearchSettingName);
				}
			}else if(req.getParameter("action")==null){//pqge is acced first time
				irl=sc.getIRSettingService().getIrSettings();
				req.setAttribute("searchlog", "All Settings");
				req.setAttribute("lastSearchSettingName", "");
			}else{//new search display from 0
				String searchCritname=req.getParameter("settingName");
				irl=sc.getIRSettingService().matchSetting(searchCritname);
				req.setAttribute("searchlog", "Setting Name : "+searchCritname);
				req.setAttribute("lastSearchSettingName", searchCritname);		
			}
			model.put("irsetting", irl);
			totalRows=irl.size();
			totalPages=(int) Math.ceil(totalRows/currentRows);
			
			req.setAttribute("currentPage", currentPage);
			req.setAttribute("totalPages", totalPages);
			req.setAttribute("currentRows", currentRows);
			req.setAttribute("totalRows", totalRows);
			
			return new ModelAndView("viewIrsettings","model",model);
		}catch (Exception e) {
			req.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}finally{
			sc.closeSession();
		}
	}
}