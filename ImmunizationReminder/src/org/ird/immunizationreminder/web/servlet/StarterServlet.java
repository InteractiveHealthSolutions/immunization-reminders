package org.ird.immunizationreminder.web.servlet;

import ird.xoutTB.emailer.exception.EmailException;

import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.ird.immunizationreminder.autosys.smser.EmailEngine;
import org.ird.immunizationreminder.autosys.smser.SmserSystem;
import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.utils.reporting.LoggerUtil;
import org.ird.immunizationreminder.web.utils.IMRGlobals;

public class StarterServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void init() throws ServletException {
		Properties prop = null;
		String propfilename = "ImmunizationReminder.properties";
		try{
			System.out.println(">>>>LOADING SYSTEM PROPERTIES...");
			FileInputStream f=new FileInputStream(getServletContext().getRealPath("/WEB-INF/"+propfilename));
			prop=new Properties();
			prop.load(f);
			Context.setProperties(prop);
			System.out.println("......PROPERTIES LOADED SUCCESSFULLY......");
			LoggerUtil.logIt("......PROPERTIES LOADED SUCCESSFULLY......");
		}
		catch (Exception e) {
			throw new ServletException("Loading properties threw exception: Make sure application contains properties file "+propfilename+" having correct format and valid values",e);
		}
		
		String reminderPusherprop = "service.sms-pusher.runup-min";
		String reminderUpdaterprop = "service.sms-updater.runup-min";
		String responseReaderprop = "service.response-reader.runup-min";
		String smstarseelProjectIdprop = "smstarseel.project-id";
		try{
			
			IMRGlobals.REMINDER_PUSHER_TIMER_INTERVAL_MIN = Integer.parseInt(Context.getProperties().getProperty(reminderPusherprop));
			IMRGlobals.REMINDER_UPDATER_TIMER_INTERVAL_MIN = Integer.parseInt(Context.getProperties().getProperty(reminderUpdaterprop));
			IMRGlobals.RESPONSE_READER_TIMER_INTERVAL_MIN = Integer.parseInt(Context.getProperties().getProperty(responseReaderprop));
			IMRGlobals.SMS_TARSEEL_PROJECT_ID = Integer.parseInt(Context.getProperties().getProperty(smstarseelProjectIdprop));
		}
		catch (Exception e) {
			throw new ServletException("Accessing properties threw exception: Make sure application contains properties file "+propfilename+" having following properties with valid values ("+reminderPusherprop+","+reminderUpdaterprop+","+responseReaderprop+","+smstarseelProjectIdprop+")",e);
		}
		
		//setup email server
		try {
			System.out.println(">>>>INSTANTIATING EMAIL ENGINE......");
			
			EmailEngine.instantiateEmailEngine(prop);
			
			System.out.println("......EMAIL ENGINE STARTED SUCCESSFULLY......".toLowerCase());
			LoggerUtil.logIt("......EMAIL ENGINE STARTED SUCCESSFULLY......");
		} 
		catch (EmailException e) {
			throw new ServletException("Loading EmailEngine threw exception",e);
		}
		
		System.out.println(">>>>INSTANTIATING AUTOMATED SERVICES......");
		SmserSystem.instantiateSmserSystem();
	}
}
