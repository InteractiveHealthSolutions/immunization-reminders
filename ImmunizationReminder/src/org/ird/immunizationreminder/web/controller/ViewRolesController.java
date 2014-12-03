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
import org.ird.immunizationreminder.datamodel.entities.Role;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

public class ViewRolesController implements Controller{

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
			List<Role> rolel=new ArrayList<Role>();
			if(req.getParameter("action")!=null&&req.getParameter("action").compareTo("display")==0){//request is to navigate pages
				String lastSearchRoleName=req.getParameter("lastSearchRoleName").trim();
				totalPages=Integer.parseInt(req.getParameter("totalPages"));
				currentPage=Integer.parseInt(req.getParameter("pagedir"));
				if(currentPage>=0 && currentPage<=totalPages){
					rolel=sc.getUserService().getRolesByName(lastSearchRoleName);
					req.setAttribute("searchlog", req.getParameter("searchlog"));
					req.setAttribute("lastSearchRoleName", lastSearchRoleName);
				}
			}else if(req.getParameter("action")==null){//pqge is acced first time
				rolel=sc.getUserService().getAllRoles();
				req.setAttribute("searchlog", "All Roles");
				req.setAttribute("lastSearchRoleName", "");
			}else{//new search display from 0
				String searchCritRname=req.getParameter("roleName");
				rolel=sc.getUserService().getRolesByName(searchCritRname);
				req.setAttribute("searchlog", "Role Name : "+searchCritRname);
				req.setAttribute("lastSearchRoleName", searchCritRname);		
			}
			model.put("role", rolel);///////////////////////////////
	//role not implment limit pages structure hence overrinding and skipping current structure
			totalRows=rolel.size();
			totalPages=0;
			currentPage=0;
			
			req.setAttribute("currentPage", currentPage);
			req.setAttribute("totalPages", totalPages);
			req.setAttribute("currentRows", currentRows);
			req.setAttribute("totalRows", totalRows);
			
			return new ModelAndView("viewRoles","model",model);
		}catch (Exception e) {
			req.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}finally{
			sc.closeSession();
		}
	}
}