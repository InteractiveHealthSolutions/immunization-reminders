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
import org.ird.immunizationreminder.datamodel.entities.Permission;
import org.ird.immunizationreminder.datamodel.entities.Role;
import org.ird.immunizationreminder.utils.reporting.ExceptionUtil;
import org.ird.immunizationreminder.utils.reporting.LoggerUtil;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class EditRoleController extends SimpleFormController{
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		 String message="Role edited successfully.";
	
			LoggedInUser user=UserSessionUtils.getActiveUser(request);
			if(user==null){
				return new ModelAndView(new RedirectView("login.htm"));
			}
		String[] perms=request.getParameterValues("selectedPerms");
		List<Permission> permissions=new ArrayList<Permission>();
		Role role=(Role) command;
		role.getPermissions().clear();
		
		ServiceContext sc = Context.getServices();
		try{
			for (String p : perms) {
				permissions.add(sc.getUserService().getPermission(p,false));
			}
			role.addPermissions(permissions);
			role.setLastEditor(user.getUser());
			sc.getUserService().updateRole(role);
			sc.commitTransaction();
			LoggerUtil.logIt("Role '"+role.getName()+"' edited by '"+user.getUser().getName()+":"+user.getUser().getFullName()+"' ");
		}catch (Exception e) {
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}finally{
			sc.closeSession();
		}
		return new ModelAndView(new RedirectView("viewRoles.htm?editOrUpdateMessage="+message+"&action=search&roleName="+role.getName()));
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		Role role = new Role();

		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			boolean session_expired = true;
			request.setAttribute("session_expired", session_expired);
		}else{
			String rec=request.getParameter("editRecord");
			ServiceContext sc = Context.getServices();
			try{
				role=sc.getUserService().getRole(rec);
			}catch (Exception e) {
				LoggerUtil.logIt(ExceptionUtil.getStackTrace(e));
				request.setAttribute("errorMessage", "An error occurred while retrieving Role. Error message is:"+e.getMessage());
			}finally{
				sc.closeSession();
			}
		}
		return role;
	}
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		Role role=(Role) command;
		Map<String, Object> model=new HashMap<String, Object>();
		List<Permission> perms=new ArrayList<Permission>();
	
		boolean session_expired=false;
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			session_expired=true;
			request.setAttribute("session_expired", session_expired);
		}
		else{
			ServiceContext sc = Context.getServices();
			try{
				perms=sc.getUserService().getAllPermissions(true);
			}
			catch (Exception e) {
				LoggerUtil.logIt(ExceptionUtil.getStackTrace(e));
				request.setAttribute("errorMessage", "An error occurred while retrieving Permission list. Error message is:"+e.getMessage());
			}
			finally{
				sc.closeSession();
			}
			
			for (Permission p : role.getPermissions()) {
				for (int i = 0; i < perms.size(); i++) {
					if(perms.get(i).getPermissionId()==p.getPermissionId()){
						perms.remove(i);
						i--;
					}
				}
			}
			model.put("rolePermissions", role.getPermissions());
			model.put("remainingPermissions", perms);
		}
		return model;
	}

}
