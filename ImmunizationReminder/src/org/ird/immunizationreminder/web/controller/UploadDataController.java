package org.ird.immunizationreminder.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.immunizationreminder.context.LoggedInUser;
import org.ird.immunizationreminder.web.utils.CSVUtil;
import org.ird.immunizationreminder.web.utils.UserSessionUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

public class UploadDataController implements Controller{

	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		LoggedInUser user=UserSessionUtils.getActiveUser(req);
		if(user==null){
			return new ModelAndView(new RedirectView("login.htm"));
		}
		List<String> upload=(List<String>) req.getSession().getAttribute("uploadDataMessage");
		List<String> saved=(List<String>) req.getSession().getAttribute("savedlines");
		List<String> unsaved=(List<String>) req.getSession().getAttribute("unsavedlines");
		try{
		req.getSession().removeAttribute("uploadDataMessage");
		}catch (Exception e) {
			
		}
		try{
			req.getSession().removeAttribute("savedlines");
		}catch (Exception e) {
				
		}
		try{
			req.getSession().removeAttribute("unsavedlines");
		}catch (Exception e) {
					
		}
		
		if(upload!=null || saved!= null || unsaved!=null){
		/*String report=
			"<span style=\"font-size: 15px;font-weight: bolder;background-color: #dddddd;text-align: left;\">" +
			"Error Messages:"+
			"</span>"+
			"<table class=\"datalist\" width=\"100%\">" +
				"<tr>"+
					"<th>" +
					"Errors Found in Lines" +
					"</th>" +
				"</tr>";
		if(upload!=null){
				for (String string : upload) {
					
		report+="<tr>" +
					
					"<td>" +
					string+
					"</td>" +
				"</tr>" ;
				}
		}
		report+="</table>";
		
		report+=
			"<span style=\"font-size: 15px;font-weight: bolder;background-color: #dddddd;text-align: left;\">" +
			"Saved Entries:"+
			"</span>"+
			"<table class=\"datalist\" width=\"100%\">" +
				"<tr>"+
					"<th>" +
					"Errors Found in Lines" +
					"</th>" +	
					"<th>" +
					"Line Saved" +
					"</th>"+
					"<th>" +
					"Line id/num" +
					"</th>"+
					"<th>" +
					"Child id" +
					"</th>"+
					"<th>" +
					"Child name" +
					"</th>"+
				"</tr>";
		if(saved!=null){
				for (String string : saved) {
					String[] strr=string.split("\t");
		report+="<tr>" +
					"<td>" +
					strr[0].replace("\n", "<br>~~") +
					"</td>" +
					
					"<td>" +
					(strr.length>2?strr[1]:"Nil")+"--" +(strr.length>3?strr[2]:"Nil") +"--"+
					(strr.length>4?strr[3]:"Nil")+"--"+(strr.length>5?strr[4]:"Nil") +"--"+
					(strr.length>6?strr[5]:"Nil")+"--"+(strr.length>7?strr[6]:"Nil")  +"..........(contd.).." +
					"</td>" +
					"<td>" +
					(strr.length>2?strr[1]:"Nil") +
					"</td>" +
					"<td>" +
					(strr.length>3?strr[2]:"Nil") +
					"</td>" +
					"<td>" +
					(strr.length>5?strr[6]:"Nil") +
					"</td>" +
				"</tr>" ;
				}
		}
		report+="</table>";
		
		report+=
			"<span style=\"font-size: 15px;font-weight: bolder;background-color: #dddddd;text-align: left;\">" +
			"Un Saved Entries:"+
			"</span>"+
			"<table class=\"datalist\" width=\"100%\">" +
				"<tr>"+
					"<th>" +
					"Errors Found in Lines" +
					"</th>" +
					
					"<th>" +
					"Line Not Saved" +
					"</th>"+
					"<th>" +
					"Line id/num" +
					"</th>"+
					"<th>" +
					"Child id" +
					"</th>"+
					"<th>" +
					"Child name" +
					"</th>"+
				"</tr>";
		if(unsaved!=null){
				for (String string : unsaved) {
					String[] strr=string.split("\t");
		report+="<tr>" +
					"<td>" +
					strr[0].replace("\n", "<br>~~") +
					"</td>" +
					
					"<td>" +
					(strr.length>2?strr[1]:"Nil")+"--" +(strr.length>3?strr[2]:"Nil") +"--"+
					(strr.length>4?strr[3]:"Nil")+"--"+(strr.length>5?strr[4]:"Nil") +"--"+
					(strr.length>6?strr[5]:"Nil")+"--"+(strr.length>7?strr[6]:"Nil")  +"..........(contd.).." +
					"</td>" +
					"<td>" +
					(strr.length>2?strr[1]:"Nil") +
					"</td>" +
					"<td>" +
					(strr.length>3?strr[2]:"Nil") +
					"</td>" +
					"<td>" +
					(strr.length>5?strr[6]:"Nil") +
					"</td>" +
				"</tr>" ;
				}
		}
		report+="</table>";*/
		StringBuilder report=CSVUtil.getCsvUploadReport(upload, saved, unsaved);
		req.getSession().setAttribute("report", report.toString());
		req.setAttribute("report", report.toString());

		upload.add("Number of upload file errors :"+(upload==null?0:upload.size()));
		upload.add("Number of saved entries :"+(saved==null?0:saved.size()));
		upload.add("Number of unsaved  entries:"+(unsaved==null?0:unsaved.size()));
		
		}

		req.setAttribute("uploadDataMessage", upload);
		return new ModelAndView("uploadData");
	}
}