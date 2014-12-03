package org.ird.immunizationreminder.web.servlet;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.LoggedInUser;
import org.ird.immunizationreminder.context.ServiceContext;
import org.ird.immunizationreminder.utils.reporting.ExceptionUtil;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;

public class DownloadCsvUploadedServlet extends HttpServlet{
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
	
		String recordId=request.getParameter("recordId");
		
		response.setContentType("application/zip"); 
		response.setHeader("Content-Disposition", "attachment; filename=CSVUploaded_"+recordId+".csv"); 
		ByteArrayOutputStream fw = new ByteArrayOutputStream();

		ServiceContext sc = Context.getServices();

		ServletOutputStream out = response.getOutputStream();
		try {
			Scanner scanner = new Scanner(new FileInputStream(sc.getTransactionLogService().getCsvFile(Long.parseLong(recordId))));
			StringBuilder sb = new StringBuilder();
			while (scanner.hasNextLine()) {
				sb.append(scanner.nextLine()+"\n");
			}
			fw.write(sb.toString().getBytes());
			// copy binary contect to output stream
			out.write(fw.toByteArray());
			out.flush();
			out.close();
		} catch (Exception e) {
			out.write(("Error occurred while downloading csv ."+ExceptionUtil.getStackTrace(e)).getBytes());
		}finally{
			sc.closeSession();
		}
	}
}
