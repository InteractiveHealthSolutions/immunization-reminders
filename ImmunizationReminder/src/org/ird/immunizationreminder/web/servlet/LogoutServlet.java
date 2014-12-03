package org.ird.immunizationreminder.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ird.immunizationreminder.context.Context;

public class LogoutServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession httpSession = req.getSession();
		try{
		Context.logout(httpSession.getAttribute("username").toString());
		for (Cookie c : req.getCookies()) {
			if(c.getName().compareTo("username")==0){
				c.setMaxAge(0);
				resp.addCookie(c);
				break;
			}
		}
		}catch (Exception e) {
		}
		httpSession.removeAttribute("username");
		resp.sendRedirect(req.getContextPath() + "/login.htm");
		
		httpSession.invalidate();
	}
	
}
