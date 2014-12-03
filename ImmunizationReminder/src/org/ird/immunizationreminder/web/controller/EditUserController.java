package org.ird.immunizationreminder.web.controller;

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
import org.ird.immunizationreminder.datamodel.entities.Permission;
import org.ird.immunizationreminder.datamodel.entities.Role;
import org.ird.immunizationreminder.datamodel.entities.User;
import org.ird.immunizationreminder.datamodel.entities.User.UserStatus;
import org.ird.immunizationreminder.utils.reporting.ExceptionUtil;
import org.ird.immunizationreminder.utils.reporting.LoggerUtil;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class EditUserController extends SimpleFormController{

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		final String MESSAGE_USER_EDITED_SUCCESSFULLY="User data edited successfully.";

		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			return new ModelAndView(new RedirectView("login.htm"));
		}

		String[] roles=request.getParameterValues("selectedRoles");
		List<Role> roleList=new ArrayList<Role>();
		User iruser=(User) command;
		ServiceContext sc = Context.getServices();
		try{
			if(!iruser.isDefaultAdministrator()){
				iruser.getRoles().clear();
				for (String r : roles) {
					roleList.add(sc.getUserService().getRole(r));
				}
				for (Role rl : roleList) {
					iruser.addRole(rl);
				}
				String St=request.getParameter("status");
				iruser.setStatus(User.UserStatus.valueOf(St));
			}
			iruser.setLastEditor(user.getUser());
			iruser.setLastUpdated(new Date());
			sc.getUserService().updateUser(iruser);
			sc.commitTransaction();
			LoggerUtil.logIt("User '"+iruser.getUserId()+":"+iruser.getFullName()+"' edited by '"+user.getUser().getName()+":"+user.getUser().getFullName()+"' ");

		}catch (Exception e) {
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}
		finally{
			sc.closeSession();
		}
		return new ModelAndView(new RedirectView("viewUsers.htm?editOrUpdateMessage="+MESSAGE_USER_EDITED_SUCCESSFULLY+"&action=search&userid="+iruser.getName()));
	}
	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		User iruser = new User();

		if(user==null){
			boolean session_expired = true;
			request.setAttribute("session_expired", session_expired);
		}else{
			String rec=request.getParameter("editRecord");
			ServiceContext sc = Context.getServices();
			try{
				iruser=sc.getUserService().getUser(rec);
			}catch (Exception e) {
				LoggerUtil.logIt(ExceptionUtil.getStackTrace(e));
				request.setAttribute("errorMessage", "An error occurred while retrieving User. Error message is:"+e.getMessage());
			}finally{
				sc.closeSession();
			}
		}
		return iruser;
	}
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		User iruser=(User) command;
		Map<String, Object> model=new HashMap<String, Object>();
		boolean session_expired=false;
		List<Role> roles=new ArrayList<Role>();
		List<Role> remainingRoles=new ArrayList<Role>();

		List<Role> notAllowedRoles=new ArrayList<Role>();

		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			session_expired=true;
			request.setAttribute("session_expired", session_expired);
		}
		else{
			ServiceContext sc = Context.getServices();
			try {
				roles = sc.getUserService().getAllRoles();
			} catch (Exception e) {
				LoggerUtil.logIt(ExceptionUtil.getStackTrace(e));
				request.setAttribute("errorMessage","An error occurred while retrieving Roles list. Error message is:" + e.getMessage());
			}
			finally{
				sc.closeSession();
			}

			for (Role r : roles) {
				if (user.getUser().hasRole(r.getName())) {
					remainingRoles.add(r);
				} else {
					boolean addr = true;
					for (Permission p : r.getPermissions()) {
						if (!user.getUser().hasPermission(p.getName())) {
							addr = false;
							break;
						}
					}
					if (addr) {
						remainingRoles.add(r);
					} else {
						notAllowedRoles.add(r);
					}
				}
			}

			for (Role r : iruser.getRoles()) {
				for (int i = 0; i < remainingRoles.size(); i++) {
					if (remainingRoles.get(i).getRoleId() == r.getRoleId()) {
						remainingRoles.remove(i);
						break;
					}
				}
			}

			boolean isUserAllowedToEdit = true;
			for (Role role : notAllowedRoles) {
				if (iruser.hasRole(role.getName())) {
					isUserAllowedToEdit = false;
					break;
				}
			}

			List<String> u_status = new ArrayList<String>();
			for (UserStatus string : User.UserStatus.values()) {
				u_status.add(string.toString());
			}

			model.put("user_status", u_status);
			model.put("userRoles", iruser.getRoles());
			model.put("remainingRoles", remainingRoles);
			model.put("notAllowedRoles", notAllowedRoles);
			model.put("isUserAllowedToEdit", isUserAllowedToEdit);
		}
		return model;
	}
}
