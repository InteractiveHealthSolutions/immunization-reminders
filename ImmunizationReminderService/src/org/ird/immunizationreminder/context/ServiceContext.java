package org.ird.immunizationreminder.context;

import java.sql.Clob;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.ird.immunizationreminder.dao.hibernatedao.DAOArmDayReminderImpl;
import org.ird.immunizationreminder.dao.hibernatedao.DAOArmIdMapImpl;
import org.ird.immunizationreminder.dao.hibernatedao.DAOArmImpl;
import org.ird.immunizationreminder.dao.hibernatedao.DAOCsvUploadImpl;
import org.ird.immunizationreminder.dao.hibernatedao.DAOIrSettingImpl;
import org.ird.immunizationreminder.dao.hibernatedao.DAOChildImpl;
import org.ird.immunizationreminder.dao.hibernatedao.DAOResponseImpl;
import org.ird.immunizationreminder.dao.hibernatedao.DAOVaccinationImpl;
import org.ird.immunizationreminder.dao.hibernatedao.DAOPermissionImpl;
import org.ird.immunizationreminder.dao.hibernatedao.DAOReminderImpl;
import org.ird.immunizationreminder.dao.hibernatedao.DAOReminderSmsImpl;
import org.ird.immunizationreminder.dao.hibernatedao.DAORoleImpl;
import org.ird.immunizationreminder.dao.hibernatedao.DAOUserImpl;
import org.ird.immunizationreminder.dao.hibernatedao.DAOVaccineImpl;
import org.ird.immunizationreminder.dao.hibernatedao.HibernateUtil;
import org.ird.immunizationreminder.service.ArmService;
import org.ird.immunizationreminder.service.IRSettingService;
import org.ird.immunizationreminder.service.ChildService;
import org.ird.immunizationreminder.service.ReminderService;
import org.ird.immunizationreminder.service.ResponseService;
import org.ird.immunizationreminder.service.TransactionLogService;
import org.ird.immunizationreminder.service.UserService;
import org.ird.immunizationreminder.service.VaccinationService;
import org.ird.immunizationreminder.service.impl.ArmServiceImpl;
import org.ird.immunizationreminder.service.impl.IRSettingServiceImpl;
import org.ird.immunizationreminder.service.impl.ChildServiceImpl;
import org.ird.immunizationreminder.service.impl.ReminderServiceImpl;
import org.ird.immunizationreminder.service.impl.ResponseServiceImpl;
import org.ird.immunizationreminder.service.impl.TransactionLogServiceImpl;
import org.ird.immunizationreminder.service.impl.UserServiceImpl;
import org.ird.immunizationreminder.service.impl.VaccinationServiceImpl;

public class ServiceContext {

	private UserService userService;
	private ArmService	armService;
	private ChildService childService;
	private ReminderService reminderService;
	private ResponseService responseService;
//	private QueryService qs;
//	private FlagDataService fs;
	private IRSettingService irSettingService;
	private TransactionLogService logService;
	private VaccinationService vaccinationService;
	private Session session;
	
	public ServiceContext(SessionFactory sessionObj) 
	{
		session = sessionObj.openSession();
		
	DAOUserImpl udao=new DAOUserImpl(session);
	DAORoleImpl rdao=new DAORoleImpl(session);
	DAOPermissionImpl pdao= new DAOPermissionImpl(session);
	this.userService=new UserServiceImpl(udao, rdao,pdao);
	
	DAOChildImpl pt=new DAOChildImpl(session);
	//HibernateDAOChildCell pcell=new HibernateDAOChildCell(session);
	//HibernateDAOReminder rem=new HibernateDAOReminder(session);
	this.childService= new ChildServiceImpl(pt);
	
	DAOVaccineImpl vaccdao=new DAOVaccineImpl(session);
	DAOVaccinationImpl pvdao=new DAOVaccinationImpl(session);
	this.vaccinationService=new VaccinationServiceImpl(vaccdao,pvdao);
	
//	HibernateDAOChild p1=new HibernateDAOChild(session);
	DAOReminderImpl remdao=new DAOReminderImpl(session);
	DAOReminderSmsImpl remsmsdao=new DAOReminderSmsImpl(session);
	this.reminderService= new ReminderServiceImpl(remdao,remsmsdao);
	
	DAOResponseImpl daoresp=new DAOResponseImpl(session);
	this.responseService= new ResponseServiceImpl(daoresp);
	
//	HibernateDAODirectQuery d1=new HibernateDAODirectQuery(session);
//	this.qs=new QueryServiceImpl(d1);
//	
//	HibernateDAOFlagData fd=new HibernateDAOFlagData(session);
//	this.fs=new FlagDataServiceImpl(fd);
	
	DAOIrSettingImpl irdao=new DAOIrSettingImpl(session);
	this.irSettingService=new IRSettingServiceImpl(irdao);
	
	DAOCsvUploadImpl csvdao=new DAOCsvUploadImpl(session);
	this.logService=new TransactionLogServiceImpl(csvdao);
	
	DAOArmImpl armdao=new DAOArmImpl(session);
	DAOArmIdMapImpl armidmapdao=new DAOArmIdMapImpl(session);
	DAOArmDayReminderImpl armdayremdao=new DAOArmDayReminderImpl(session);
	this.armService=new ArmServiceImpl(armdao,armdayremdao,armidmapdao);
	
//	HibernateDAOUserSMS usmsh=new HibernateDAOUserSMS(session);
//	this.usmss=new UserSMSServiceImpl(usmsh);
	}
	public Clob createClob(String clobStr) {
		return Hibernate.getLobCreator(session).createClob(clobStr);
	}
	@Override
	protected void finalize() throws Throwable
	{
		closeSession();
	}
	public void beginTransaction(){
		session.beginTransaction();
	}
	public boolean isSessionOpen(){
		 return session.isOpen();
	}
	public void commitTransaction(){
		session.getTransaction().commit();
	}
	public void  flushSession() {
		session.flush();
	}
	public void rollbackTransaction() {
		session.getTransaction().rollback();
	}
	public void closeSession(){
		try{session.close();
		}catch (Exception e) {}
	}	
	public UserService getUserService(){
		if(!session.getTransaction().isActive()) beginTransaction();
		return userService;
	}
	public ChildService getChildService(){
		if(!session.getTransaction().isActive()) beginTransaction();
		return childService;
	}
	public ReminderService getReminderService(){
		if(!session.getTransaction().isActive()) beginTransaction();
		return reminderService;
	}
	public ResponseService getResponseService(){
		if(!session.getTransaction().isActive()) beginTransaction();
		return responseService;
	}
//	public QueryService getDirectQueryService(){
//		return qs;
//	}
//	public FlagDataService getFlagDataService(){
//		return fs;
//	}
	public IRSettingService getIRSettingService(){
		if(!session.getTransaction().isActive()) beginTransaction();
		return irSettingService;
	}
//	public UserSMSService getUserSMSService(){
//		return usmss;
//	}
	public ArmService getArmService(){
		if(!session.getTransaction().isActive()) beginTransaction();
		return armService;
	}
	public VaccinationService getVaccinationService() {
		if(!session.getTransaction().isActive()) beginTransaction();
		return vaccinationService;
	}
	public void setTransactionLogService(TransactionLogService logs) {
		if(!session.getTransaction().isActive()) beginTransaction();
		this.logService = logs;
	}
	public TransactionLogService getTransactionLogService() {
		if(!session.getTransaction().isActive()) beginTransaction();
		return logService;
	}
}
