package org.ird.immunizationreminder.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.LoggedInUser;
import org.ird.immunizationreminder.context.ServiceContext;
import org.ird.immunizationreminder.datamodel.entities.User;
import org.ird.immunizationreminder.utils.reporting.LoggerUtil;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;

public class DeleteUserServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		LoggedInUser user=UserSessionUtils.getActiveUser(req);
		if(user==null){
			resp.sendRedirect(req.getContextPath() + "/login.htm");
			return;
		}
		ServiceContext sc = Context.getServices();

		String id=req.getParameter("UserId");
		try {

			User fd = sc.getUserService().getUser(id);
			fd.setRoles(null);

			sc.getUserService().updateUser(fd);
			sc.commitTransaction();

			sc.getUserService().deleteUser(fd);
			sc.commitTransaction();

			LoggerUtil.logIt("User '" + id + "' was deleted by '"+ user.getUser().getName() + ":"+ user.getUser().getFullName() + "'");
			req.getSession().setAttribute("deleteUserMsg","User was deleted successfully");
		}
		catch (Exception e) {
			req.getSession().setAttribute("deleteUserMsg", "User was not deleted");
			LoggerUtil.logIt("An attempt to User '"+id+"' to be deleted by '"+user.getUser().getName()+":"+user.getUser().getFullName()+"' was failed with error \n"+e.getMessage());
		}
		finally{
			sc.closeSession();
		}
		resp.sendRedirect(req.getContextPath() + "/viewUsers.htm");
	}
}