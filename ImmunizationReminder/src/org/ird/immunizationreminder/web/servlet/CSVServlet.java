package org.ird.immunizationreminder.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.LoggedInUser;
import org.ird.immunizationreminder.context.ServiceContext;
import org.ird.immunizationreminder.data.exception.ChildDataException;
import org.ird.immunizationreminder.datamodel.entities.Child;
import org.ird.immunizationreminder.utils.date.DateUtils;
import org.ird.immunizationreminder.utils.reporting.LoggerUtil;
import org.ird.immunizationreminder.web.APVRRAnalysisGridRow;
import org.ird.immunizationreminder.web.utils.CSVUtil;
import org.ird.immunizationreminder.web.utils.IRUtils;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;

import com.mysql.jdbc.StringUtils;

public class CSVServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			response.sendRedirect(request.getContextPath() +"/login.htm");
			return;
		}
		ServiceContext sc = Context.getServices();
		
		String date1=request.getParameter("date1");
		if(date1==null||StringUtils.isEmptyOrWhitespaceOnly(date1)){
			response.getOutputStream().println("Start date was found to be null");
			return;
		}
		
		String date2=request.getParameter("date2");
		date2=(StringUtils.isNullOrEmpty(date2))?DateUtils.convertToString(new Date()):date2;
		String lastSearchArmName=StringUtils.isEmptyOrWhitespaceOnly(request.getParameter("armName"))?null:request.getParameter("armName");
		
		request.setAttribute("lastSearchArmName", lastSearchArmName);
		List<APVRRAnalysisGridRow> recordList=new ArrayList<APVRRAnalysisGridRow>();
		List<Child> p;
		try {
			p = sc.getChildService().findChildByCriteria(null, null, null, false
							, lastSearchArmName, 0, Integer.MAX_VALUE-1,true);
		} catch (ChildDataException e1) {
			e1.printStackTrace();
			response.getOutputStream().println("Some error occurred."+e1.getMessage());
			sc.closeSession();
			return;
		}
		try{
			if( date1!=null ){
				for (Child child : p) {
					recordList.add(IRUtils.addRecord(child,DateUtils.convertToDate(date1)
							,DateUtils.convertToDate(date2)));
				}
			
				CSVUtil.makeCsv(response, recordList,lastSearchArmName==null?"ALL":lastSearchArmName);
			LoggerUtil.logIt("CSV downloaded by '"+user.getUser().getName()+":"+user.getUser().getFullName()+"'");
			}
		}catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("exceptionTrace", e);
			response.sendRedirect(request.getContextPath() +"/exception.htm");
		}
		finally{
			sc.closeSession();
		}
	}
}
