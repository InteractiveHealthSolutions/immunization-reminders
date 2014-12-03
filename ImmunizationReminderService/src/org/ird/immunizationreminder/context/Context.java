package org.ird.immunizationreminder.context;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Timer;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.ird.immunizationreminder.dao.hibernatedao.HibernateUtil;
import org.ird.immunizationreminder.datamodel.entities.IrSetting;
import org.ird.immunizationreminder.datamodel.entities.User;
import org.ird.immunizationreminder.service.exception.UserServiceException;
import org.ird.immunizationreminder.service.utils.SecurityUtils;
import org.ird.immunizationreminder.utils.reporting.ExceptionUtil;
import org.ird.immunizationreminder.utils.reporting.LoggerUtil;

public class Context {
private static SessionFactory sessionFactory;
	
	static {
		sessionFactory = HibernateUtil.getSessionFactory();
	}
	private static Properties props;
	private static Timer timerLoginSession;
	private static HashMap<String,IrSetting> currentIrSettings=loadCurrentIrSettings();

	private static Hashtable<String,LoggedInUser> currentlyLoggedIn=new Hashtable<String, LoggedInUser>();
		
	public static Hashtable<String,LoggedInUser> getcurrentlyLoggedInUsers(){
		return currentlyLoggedIn;
	}
	

	private static HashMap<String, IrSetting> loadCurrentIrSettings() {
		HashMap<String,IrSetting> curIrSettings=new HashMap<String, IrSetting>();
		ServiceContext sc = Context.getServices();
		try{
			LoggerUtil.logIt("\nLoading IrSettings....");
			
			for (IrSetting setting : sc.getIRSettingService().getIrSettings()) {
				curIrSettings.put(setting.getName().trim(), setting);
			}
			LoggerUtil.logIt("\nIrSettings loaded successfully....");
		}catch (Exception e) {
			LoggerUtil.logIt("Error occurred while loading irsettings. Trace is:"+ExceptionUtil.getStackTrace(e));
		}
		sc.closeSession();
		return curIrSettings;
	}
	
	public static void reloadCurrentIrSettings(){
		LoggerUtil.logIt("\nReloading IrSettings....");
		currentIrSettings=loadCurrentIrSettings();
	}
	
	public static String getIRSetting(String name,String defaultvalue){
		if(currentIrSettings.get(name)!=null){
			return currentIrSettings.get(name).getValue();
		}
		LoggerUtil.logIt("\nIrSetting '"+name+"' was not found in memory table hence returning default value:"+defaultvalue);
		return defaultvalue;
	}
	/**
	 * send only validated values. this function donot validate setting values.
	 * @param name
	 * @param newValue
	 * @param user
	 * @return
	 */
	public static boolean updateIrSetting(String name,String newValue,LoggedInUser user) {
		String oldValue = currentIrSettings.get(name).getValue();
		String oldlastEditorId = currentIrSettings.get(name).getLastEditedByUserId();
		String oldlastEditorName = currentIrSettings.get(name).getLastEditedByUserName();
		Date oldlastEditdate = currentIrSettings.get(name).getLastUpdated();

		IrSetting setting = currentIrSettings.get(name.trim());
		setting.setLastEditor(user.getUser());
		setting.setValue(newValue.trim());
		
		ServiceContext sc = Context.getServices();

		try{
			LoggerUtil.logIt("\nUpdating IrSetting :"+name);
			sc.getIRSettingService().updateIrSetting(setting);
			sc.commitTransaction();
			LoggerUtil.logIt("\nIrSetting :"+name+" updated to :"+newValue);
			return true;
		}catch (Exception e) {
			LoggerUtil.logIt("\nError while updating IrSetting :"+name+":"+ExceptionUtil.getStackTrace(e));
			currentIrSettings.get(name).setValue(oldValue);
			currentIrSettings.get(name).setLastEditedByUserId(oldlastEditorId);
			currentIrSettings.get(name).setLastEditedByUserName(oldlastEditorName);
			currentIrSettings.get(name).setLastUpdated(oldlastEditdate);
			return false;
		}finally{
			sc.closeSession();
		}
	}
		
	public static HashMap<String, IrSetting> getIrSettingsInMemory() {
		return currentIrSettings;
	}
	
	static void removeUser(String username) {
		currentlyLoggedIn.remove(username);
	}
	public static LoggedInUser getUser(String username) throws UserServiceException{
		if(currentlyLoggedIn.get(username)!=null){
			return currentlyLoggedIn.get(username);
		}else{
			throw new UserServiceException(UserServiceException.SESSION_EXPIRED);
		}
	}
	public static Properties getProperties(){
		return props;
	}
	
	public static void logout(String username){
		removeUser(username);
	}
	public static void setProperties(Properties prop) {
		props=prop;
	}
	public static boolean isUserLoggedIn(String username) {
		return currentlyLoggedIn.get(username)!=null;
	}
	public static String authenticateUser(String username,String password) throws UserServiceException,Exception {
		if(timerLoginSession == null){
			int sessionExpiryTimeMin = Integer.parseInt(getProperties().getProperty("user.currently-loggedin-user.session-expire-time", "15"));
			timerLoginSession = new Timer("Timer Login Session");
			timerLoginSession.schedule(new RefreshUsers(), 1000 * 60 * 10, 1000*60* sessionExpiryTimeMin);
		}
		
		if(currentlyLoggedIn.get(username)!=null){
			throw new UserServiceException(UserServiceException.USER_ALREADY_LOGGED_IN);
		}
		
		currentlyLoggedIn.put(username,getAuthenticatedUser(username, password));
		return username;
	}
	public static LoggedInUser getAuthenticatedUser(String username,String password) throws UserServiceException, Exception{
		ServiceContext sc = Context.getServices();

		User user=sc.getUserService().getUser(username);
		sc.closeSession();

		if(user == null){
			throw new UserServiceException(UserServiceException.AUTHENTICATION_EXCEPTION);
		}

		if(password.compareTo(SecurityUtils.decrypt(user.getPassword(),user.getName()))!= 0 ){
			throw new UserServiceException(UserServiceException.AUTHENTICATION_EXCEPTION);
		}
		if(user.getStatus() == User.UserStatus.DISABLED){
			throw new UserServiceException(UserServiceException.ACCOUNT_DISABLED);
		}
		return new LoggedInUser(user);
	}
	public static void keepUserAlive(String username) throws UserServiceException{
		if(currentlyLoggedIn.get(username)!=null){
			currentlyLoggedIn.get(username).refreshDateTime();
		}else{
			throw new UserServiceException(UserServiceException.SESSION_EXPIRED);
		}
	}
	public static Statistics getStatistics(){
		Statistics stats = sessionFactory.getStatistics();
		stats.setStatisticsEnabled(true);
		return stats;
	}
	
	public static ServiceContext getServices(){
		return new ServiceContext(sessionFactory);
	}
}
