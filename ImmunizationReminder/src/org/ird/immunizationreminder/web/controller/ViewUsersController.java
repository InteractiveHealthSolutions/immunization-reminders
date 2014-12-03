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
import org.ird.immunizationreminder.datamodel.entities.User;
import org.ird.immunizationreminder.datamodel.entities.User.UserStatus;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;

public class ViewUsersController implements Controller{

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
		req.setAttribute("resetUserPwdMsg", req.getSession().getAttribute("resetUserPwdMsg"));
		try {
			req.getSession().removeAttribute("resetUserPwdMsg");
		} catch (Exception e) {
			// TODO: handle exception
		}
		ServiceContext sc = Context.getServices();
		try{
			Map<String, Object> model=new HashMap<String, Object>();
			List<User> ulst=new ArrayList<User>();
			
			String lastSearchUserid=req.getParameter("lastSearchUserid");
	
			String lastSearchUsernamepart=req.getParameter("lastSearchUsernamepart");
	
			String lastSearchUseremail=req.getParameter("lastSearchUseremail");
	
			String lastSearchUserstatus=req.getParameter("lastSearchUserstatus");
	
			String lastSearchUserstatusNotchked=req.getParameter("lastSearchUserstatusNotchked");
			
			if(req.getParameter("action")!=null&&req.getParameter("action").compareTo("display")==0){//request is to navigate pages
				
				String userId=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchUserid))?null:
					lastSearchUserid;
				String userNamepart=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchUsernamepart))?null:
					lastSearchUsernamepart;		
				String useremail=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchUseremail))?null:
					lastSearchUseremail;
				UserStatus userStatus=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchUserstatus))?null:
					UserStatus.valueOf(lastSearchUserstatus);	
				boolean isnotChecked=(StringUtils.isEmptyOrWhitespaceOnly(lastSearchUserstatusNotchked))?false:
					true;	
				
				totalPages=Integer.parseInt(req.getParameter("totalPages"));
				currentPage=Integer.parseInt(req.getParameter("pagedir"));
				if(currentPage>=0 && currentPage<=totalPages){
					int startRecord=currentPage*currentRows;
					if(userId!=null){
						User usr=sc.getUserService().getUser(userId);
						if(usr!=null)ulst.add(usr);
					}else{
						ulst=sc.getUserService().findUserByCriteria(useremail, userNamepart
								, userStatus, isnotChecked, startRecord, currentRows);
					}
				}
					req.setAttribute("searchlog", req.getParameter("searchlog"));
					
				}else if(req.getParameter("action")==null){//pqge is acced first time
					ulst=sc.getUserService().getAllUsers(0, currentRows);
					
					lastSearchUserid="";
					lastSearchUsernamepart="";
					lastSearchUseremail="";
					lastSearchUserstatus="";
					lastSearchUserstatusNotchked="";
					
					req.setAttribute("searchlog", "All Users record");
			}else{//new search display from 0
				
				String userId=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("userid")))?null:
					req.getParameter("userid");
				String userNamepart=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("usernamepart")))?null:
					req.getParameter("usernamepart");		
				String useremail=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("useremail")))?null:
					req.getParameter("useremail");
				UserStatus userStatus=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("userstatus")))?null:
					UserStatus.valueOf(req.getParameter("userstatus"));	
				boolean isnotChecked=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("userstatusNotchk")))?false:
					true;	
				
				if(userId!=null){
					User usr=sc.getUserService().getUser(userId);
					if(usr!=null)ulst.add(usr);
				}else{
					ulst=sc.getUserService().findUserByCriteria(useremail, userNamepart
								, userStatus, isnotChecked, 0, currentRows);
				}
				//override last searches
				lastSearchUserid=userId==null?"":userId;
				lastSearchUsernamepart=userNamepart==null?"":userNamepart;
				lastSearchUseremail=useremail==null?"":useremail;
				lastSearchUserstatus=(userStatus==null?"":userStatus.name());
				lastSearchUserstatusNotchked=isnotChecked?"NOT":"";//if true then set to  a val else set empty to make false next time
	
				req.setAttribute("searchlog", "user ID:"+lastSearchUserid+" , user name part :"+lastSearchUsernamepart+
						" , user status : "+lastSearchUserstatusNotchked+" "+lastSearchUserstatus+
						" , user email :"+lastSearchUseremail);
			}
			
			req.setAttribute("lastSearchUserid", lastSearchUserid);
			req.setAttribute("lastSearchUsernamepart", lastSearchUsernamepart);
			req.setAttribute("lastSearchUserstatus", lastSearchUserstatus);
			req.setAttribute("lastSearchUseremail", lastSearchUseremail);
			req.setAttribute("lastSearchUserstatusNotchked", lastSearchUserstatusNotchked);
	
			model.put("user", ulst);
			totalRows=sc.getUserService().LAST_QUERY_TOTAL_ROW__COUNT().intValue();
			totalPages=(int) Math.ceil(totalRows/currentRows);
			
			req.setAttribute("currentPage", currentPage);
			req.setAttribute("totalPages", totalPages);
			req.setAttribute("currentRows", currentRows);
			req.setAttribute("totalRows", totalRows);
			
			return new ModelAndView("viewUsers","model",model);
		}catch (Exception e) {
			req.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView(new RedirectView("exception.htm"));		
		}finally{
			sc.closeSession();
		}
	}
}