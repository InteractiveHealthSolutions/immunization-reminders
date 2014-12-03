
package org.ird.immunizationreminder.dao.hibernatedao;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.jmx.StatisticsService;
import org.ird.immunizationreminder.utils.reporting.ExceptionUtil;
import org.ird.immunizationreminder.utils.reporting.LoggerUtil;

/**
 * This class holds common methods and utilities that are used across the hibernate related classes
 */
public class HibernateUtil {
	
	 private static /*final*/ SessionFactory sessionFactory;
	 
/*	    static {
	        try {
	            // Create the SessionFactory from hibernate.cfg.xml
	            sessionFactory = new Configuration().configure().buildSessionFactory();
	        } catch (Throwable ex) {
	            // Make sure you log the exception, as it might be swallowed
	            System.err.println("Initial SessionFactory creation failed." + ex);
	            throw new ExceptionInInitializerError(ex);
	        }
	    }*/
	 
/*	    public static SessionFactory getSessionFactory() {
	        return sessionFactory;
	    }*/
	    public synchronized static SessionFactory getSessionFactory() {

	        if (sessionFactory == null) {
	            try {
	                sessionFactory = new Configuration().configure().buildSessionFactory();

	                MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();

                    final ObjectName objectName = new ObjectName("org.hibernate:type=statistics");
                    final StatisticsService mBean = new StatisticsService();

                    mBean.setStatisticsEnabled(true);
                    mBean.setSessionFactory(sessionFactory);
                    mbeanServer.registerMBean(mBean, objectName);

	            } catch (MalformedObjectNameException e) {
	                	LoggerUtil.logIt("unable to register mbean"+ExceptionUtil.getStackTrace(e));
	                    throw new RuntimeException(e);
	            } catch (InstanceAlreadyExistsException e) {
	                	LoggerUtil.logIt("unable to register mbean" +ExceptionUtil.getStackTrace(e));
	                    throw new RuntimeException(e);
	            } catch (MBeanRegistrationException e) {
	                	LoggerUtil.logIt("unable to register mbean" +ExceptionUtil.getStackTrace(e));
	                    throw new RuntimeException(e);
	            } catch (NotCompliantMBeanException e) {
	                	LoggerUtil.logIt("unable to register mbean" +ExceptionUtil.getStackTrace(e));
	                    throw new RuntimeException(e);
	            }
	        }
	            return sessionFactory;
	    }
	    
	   /* public static void saveItem(Object name){
	    	
	    	Session session = sessionFactory.getCurrentSession();
			try{
				
				session.beginTransaction();
				
				try{
					session.saveOrUpdate(name);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
				session.getTransaction().commit();
			}
			catch(Exception ex){
				ex.printStackTrace();
				session.close();
			}
	    }
	 */

}
