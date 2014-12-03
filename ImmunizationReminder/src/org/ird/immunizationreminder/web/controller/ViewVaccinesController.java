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
import org.ird.immunizationreminder.datamodel.entities.Vaccine;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

public class ViewVaccinesController implements Controller{

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
			List<Vaccine> vaccl=new ArrayList<Vaccine>();
			if(req.getParameter("action")!=null&&req.getParameter("action").compareTo("display")==0){//request is to navigate pages
				String lastSearchVaccName=req.getParameter("lastSearchVaccName").trim();
				totalPages=Integer.parseInt(req.getParameter("totalPages"));
				currentPage=Integer.parseInt(req.getParameter("pagedir"));
				if(currentPage>=0 && currentPage<=totalPages){
					int startRecord=currentPage*currentRows;
					vaccl= sc.getVaccinationService().findVaccine(lastSearchVaccName, startRecord, currentRows);
					req.setAttribute("searchlog", req.getParameter("searchlog"));
					req.setAttribute("lastSearchVaccName", lastSearchVaccName);
				}
			}else if(req.getParameter("action")==null){//pqge is acced first time
				vaccl= sc.getVaccinationService().getAll(0, currentRows);
				req.setAttribute("searchlog", "All Vaccines");
				req.setAttribute("lastSearchVaccName", "");
			}else{//new search display from 0
				String searchCritVname=req.getParameter("vaccineName");
				vaccl=sc.getVaccinationService().findVaccine(searchCritVname, 0, currentRows);
				req.setAttribute("searchlog", "Vaccine Name : "+searchCritVname);
				req.setAttribute("lastSearchVaccName", searchCritVname);		
			}
			model.put("vaccine", vaccl);
			totalRows=sc.getVaccinationService().LAST_QUERY_TOTAL_ROW__COUNT().intValue();
			totalPages=(int) Math.ceil(totalRows/currentRows);
			
			req.setAttribute("currentPage", currentPage);
			req.setAttribute("totalPages", totalPages);
			req.setAttribute("currentRows", currentRows);
			req.setAttribute("totalRows", totalRows);
			
			return new ModelAndView("viewVaccines","model",model);
		}catch (Exception e) {
			req.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}finally{
			sc.closeSession();
		}
	}
}