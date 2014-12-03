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
import org.ird.immunizationreminder.service.exception.UserServiceException;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class AddRoleController extends SimpleFormController{

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		
			final String MESSAGE_ROLE_ADDED_SUCCESSFULLY="Role added successfully.";
			final String TEXT_URL="Add another role";
			
			String NEWLY_ADDED_ROLE_URL;
			final String NEWLY_ADDED_ROLE_MESSAGE="View Your newly added Role here. ";
			final String ALL_ROLES_URL="viewRoles.htm";
			final String ALL_ROLES_MESSAGE="View all roles. ";
			
			LoggedInUser  user=UserSessionUtils.getActiveUser(request);
			if(user==null){
				return new ModelAndView(new RedirectView("login.htm"));
			}
			
			ServiceContext sc = Context.getServices();

			String[] perms=request.getParameterValues("selectedPerms");
			List<Permission> permissions=new ArrayList<Permission>();
			Role role=(Role) command;
			try{
				for (String p : perms) {
					permissions.add(sc.getUserService().getPermission(p,false));
				}
				
				role.addPermissions(permissions);
				role.setCreator(user.getUser());
				
				sc.getUserService().addRole(role);
				sc.commitTransaction();
				
				request.setAttribute("message", MESSAGE_ROLE_ADDED_SUCCESSFULLY);
			}
			catch (UserServiceException e) {
					request.setAttribute("errormessage", e.getMessage());
			}
			catch (Exception e) {
				request.getSession().setAttribute("exceptionTrace", e);
				return new ModelAndView(new RedirectView("exception.htm"));
			}
			finally{
					sc.closeSession();
			}
			NEWLY_ADDED_ROLE_URL="window.open('viewRoles.htm?editOrUpdateMessage="+MESSAGE_ROLE_ADDED_SUCCESSFULLY+"&action=search&roleName="+role.getName()+"','_self')";
			request.setAttribute("NEWLY_ADDED_ENTITY_URL", NEWLY_ADDED_ROLE_URL);
			request.setAttribute("NEWLY_ADDED_ENTITY_MESSAGE", NEWLY_ADDED_ROLE_MESSAGE);
			request.setAttribute("ALL_ENTITYS_URL", ALL_ROLES_URL);
			request.setAttribute("ALL_ENTITYS_MESSAGE", ALL_ROLES_MESSAGE);
			
			request.setAttribute("url", "addRole.htm");
			request.setAttribute("urlText", TEXT_URL);
			
			return new ModelAndView(getSuccessView());
		}
		
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		boolean session_expired=false;
		Map<String, Object> model=new HashMap<String, Object>();
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			session_expired=true;
			request.setAttribute("session_expired", session_expired);
		}else{
			ServiceContext sc = Context.getServices();
			try{
				List<Permission> perms=sc.getUserService().getAllPermissions(true);
				model.put("permissions", perms);
			}catch (Exception e) {
				request.setAttribute("permissionErrorMsg", e.getMessage());
			}
			finally{
				sc.closeSession();
			}
		}
		return model;
	}
}
