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
import org.ird.immunizationreminder.datamodel.entities.User;
import org.ird.immunizationreminder.service.exception.UserServiceException;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class AddUserController extends SimpleFormController{
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		final String MESSAGE_USER_ADDED_SUCCESSFULLY="User added successfully.";
		final String TEXT_URL="Add another user";
		
		String NEWLY_ADDED_USER_URL;
		final String NEWLY_ADDED_USER_MESSAGE="View Your newly added User here. ";
		final String ALL_USERS_URL="viewUsers.htm";
		final String ALL_USERS_MESSAGE="View all users. ";
		
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			return new ModelAndView(new RedirectView("login.htm"));
		}
		
		ServiceContext sc = Context.getServices();

		String[] roles=request.getParameterValues("selectedRoles");
		List<Role> role=new ArrayList<Role>();
		User irduser=(User) command;
		try{
			for (String r : roles) {
				role.add(sc.getUserService().getRole(r));
			}
			
			for (Role rol : role) {
				irduser.addRole(rol);
			}
			
			irduser.setCreator(user.getUser());
			
			sc.getUserService().createUser(irduser);
			sc.commitTransaction();
			
			request.setAttribute("message", MESSAGE_USER_ADDED_SUCCESSFULLY);
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
		
		NEWLY_ADDED_USER_URL="window.open('viewUsers.htm?editOrUpdateMessage="+MESSAGE_USER_ADDED_SUCCESSFULLY+"&action=search&userid="+irduser.getName()+"','_self')";
		request.setAttribute("NEWLY_ADDED_ENTITY_URL", NEWLY_ADDED_USER_URL);
		request.setAttribute("NEWLY_ADDED_ENTITY_MESSAGE", NEWLY_ADDED_USER_MESSAGE);
		request.setAttribute("ALL_ENTITYS_URL", ALL_USERS_URL);
		request.setAttribute("ALL_ENTITYS_MESSAGE", ALL_USERS_MESSAGE);
		
		request.setAttribute("url", "addUser.htm");
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
				List<Role> roles=new ArrayList<Role>();
				List<Role> notAllowedRoles=new ArrayList<Role>();

				List<Role> role=sc.getUserService().getAllRoles();
				
			for (Role r : role) {
				if(user.getUser().hasRole(r.getName())){
					roles.add(r);
				}
				else {
					boolean addr=true;
					for (Permission p : r.getPermissions()) {
						if(!user.getUser().hasPermission(p.getName())){
							addr=false;
							break;
						}
					}
					if(addr){
						roles.add(r);
					}else{
						notAllowedRoles.add(r);
					}
				}
			}
				model.put("roles", roles);
				model.put("notAllowedRoles", notAllowedRoles);
			}catch (Exception e) {
				request.setAttribute("roleErrorMsg"	, e.getMessage());
			}
			finally{
				sc.closeSession();
			}
		}
		return model;	
	}
}