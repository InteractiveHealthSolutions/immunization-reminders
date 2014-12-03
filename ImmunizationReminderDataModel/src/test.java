/*import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.QueryStatistics;
import org.hibernate.stat.SecondLevelCacheStatistics;
import org.hibernate.stat.Statistics;
import org.hibernate.tuple.VersionProperty;
import org.hibernate.type.VersionType;
import org.ird.immunizationreminder.dao.hibernatedao.DAOArmDayReminderImpl;
import org.ird.immunizationreminder.data.exception.ChildDataException;
import org.ird.immunizationreminder.datamodel.entities.Arm;
import org.ird.immunizationreminder.datamodel.entities.ArmDayReminder;
import org.ird.immunizationreminder.datamodel.entities.ArmIdMap;
import org.ird.immunizationreminder.datamodel.entities.Child;
import org.ird.immunizationreminder.datamodel.entities.Reminder;
import org.ird.immunizationreminder.datamodel.entities.Response;
import org.ird.immunizationreminder.datamodel.entities.Vaccination;
import org.ird.immunizationreminder.datamodel.entities.Vaccine;
import org.ird.immunizationreminder.datamodel.entities.Child.STATUS;
import org.ird.immunizationreminder.utils.validation.DataValidation;
import org.ird.immunizationreminder.utils.validation.REG_EX;

import com.mysql.jdbc.StringUtils;


public class test {
@SuppressWarnings("unchecked")
public static void main(String[] args) {
	System.out.println(org.hibernate.Version.getVersionString());
	Child pat=new Child();
	pat.setGender("M");
	pat.setStatus("UT");
	for (STATUS string : STATUS.values()) {
		System.out.println(string.name());
	}
	System.out.print((Vaccine)null);
	System.out.print(25/25);
	System.out.println(DataValidation.validate(REG_EX.CELL_NUMBER, "+3343872952"));
	String p=ChildDataException.CELL_NUM_OCCUPIED;
	System.out.println(ChildDataException.CELL_NUM_OCCUPIED==p);
	SessionFactory sf=	new Configuration().configure().buildSessionFactory();
	Session ss=sf.openSession();
org.hibernate.Transaction t=ss.beginTransaction();
Criteria cri=ss.createCriteria(ArmDayReminder.class);
	//cri.createAlias("arm", "a").add(Restrictions.eq("a.armId",Integer.parseInt("1")));
	//cri.createAlias("vaccine", "v").add(Restrictions.eq("v.vaccineId",Integer.parseInt("")));
	//cri.createAlias("reminder", "r").add(Restrictions.eq("r.reminderId",Integer.parseInt("")));
	cri.add(Restrictions.eq("id.dayNumber",Integer.parseInt("-3")));
List<Response> listf = ss.createQuery("from Response r select fetch r.child as p order by r.recieveDate desc")
.setReadOnly(true).setFirstResult(0).setMaxResults(111).list();
for (Response oob : listf) {

	System.out.println("resp num:"+oob.getRecordNum());
	System.out.println("child id:"+oob.getChild().getChildId());
	System.out.println("child name:"+oob.getChild().getFirstName());
	System.out.println("arm name:"+oob.getChild().getArm().getArmName());
	//System.out.println("child name:"+oob.getChild().getFirstName());
	for (ArmDayReminder aaarm : oob.getArmday()) {
		System.out.println("---deft time arm day:"+aaarm.getDefaultReminderTime());
		System.out.println("---day num arm day:"+aaarm.getId().getDayNumber());
		System.out.println("---arm nae arm day:"+aaarm.getArm().getArmName());
	}
	
}
Criteria cri=ss.createCriteria(Arm.class)
.setFetchMode("armday", FetchMode.JOIN)
.setReadOnly(true);

@SuppressWarnings("unused")
List<Arm> a = cri.list();
ss.close();
for ( Arm oob : a) {
	System.out.println("arm name:"+oob.getArmName());
	///System.out.println("child name:"+oob.getFirstName());
	//System.out.println("child arm:"+oob.getArm().getArmName());
	for (ArmDayReminder aaarm : oob.getArmday()) {
		System.out.println("---deft time arm day:"+aaarm.getDefaultReminderTime());
		System.out.println("---day num arm day:"+aaarm.getId().getDayNumber());
		//System.out.println("---arm nae arm day:"+aaarm.getArm().getArmName());
	}
	
}
//Vaccination armid=(Vaccination) ss.get("Vaccination",1);
List<Child> pp=ss.createQuery("from Child").list();
Child child=new Child();
child.setFirstName("only first naem");
ss.save(child);
t.commit();
Statistics stat3 = sf.getStatistics();
System.out.print(stat3.toString());
try {
	for (Response object : pp) {
		//System.out.println(object.getArm().getArmName());
	}
} catch (Exception e) {
	// TODO: handle exception
}

Reminder rem=new Reminder();
rem.setName("REMINDER_VACCINE");
rem.setCreatedByUserId("administrator");
rem.setCreatedByUserName("administrator");
rem.setDescription("firest test reminder");
rem.setCreatedDate(new Date());
Set<String> rt=new HashSet<String>();
rt.add("my text 1");
rt.add("text 2 to be sent");
rt.add("3rd text will be sent");
rt.add("text num 4");
rt.add("5th text going to be sent");
Statistics stat2 = sf.getStatistics();
System.out.print(stat2.toString());
rt.add("6th text s ready");
rem.setReminderText(rt);
System.out.println("rem writttten:"+ss.save(rem));
t.commit();
ss.close();
//List a = sf.getCurrentSession().createQuery("from Arm ").setFirstResult(1).setMaxResults(5).list();
	//ss.close();
	
	
	
Statistics stats = sf.getStatistics();
stats.setStatisticsEnabled(true);
//Number of connection requests. Note that this number represents 
//the number of times Hibernate asked for a connection, and 
//NOT the number of connections (which is determined by your 
//pooling mechanism).
System.out.println(stats.getConnectCount());
//Number of flushes done on the session (either by client code or 
//by hibernate).
System.out.println(stats.getFlushCount());
//The number of completed transactions (failed and successful).
System.out.println(stats.getTransactionCount());
//The number of transactions completed without failure
System.out.println(stats.getSuccessfulTransactionCount());
//The number of sessions your code has opened.
System.out.println(stats.getSessionOpenCount());
//The number of sessions your code has closed.
System.out.println(stats.getSessionCloseCount());
//All of the queries that have executed.
System.out.println(stats.getQueries());
//Total number of queries executed.
System.out.println(stats.getQueryExecutionCount());
//Time of the slowest query executed.
System.out.println(stats.getQueryExecutionMaxTime());
//the number of collections fetched from the DB.
System.out.println(stats.getCollectionFetchCount());
// The number of collections loaded from the DB.
System.out.println(stats.getCollectionLoadCount());
// The number of collections that were rebuilt
System.out.println(stats.getCollectionRecreateCount());
// The number of collections that were 'deleted' batch.
System.out.println(stats.getCollectionRemoveCount());
// The number of collections that were updated batch.
System.out.println(stats.getCollectionUpdateCount());
 
// The number of your objects deleted.
System.out.println(stats.getEntityDeleteCount());
// The number of your objects fetched.
System.out.println(stats.getEntityFetchCount());
// The number of your objects actually loaded (fully populated).
System.out.println(stats.getEntityLoadCount());
// The number of your objects inserted.
System.out.println(stats.getEntityInsertCount());
// The number of your object updated.
System.out.println(stats.getEntityUpdateCount());
double queryCacheHitCount  = stats.getQueryCacheHitCount();
double queryCacheMissCount = stats.getQueryCacheMissCount();
double queryCacheHitRatio =
  queryCacheHitCount / (queryCacheHitCount + queryCacheMissCount);
QueryStatistics queryStats = stats.getQueryStatistics("from Arm ");
//total hits on cache by this query.
queryStats.getCacheHitCount();
//total misses on cache by this query.
queryStats.getCacheMissCount();
//total number of objects put into cache by this query execution
queryStats.getCachePutCount();
//Number of times this query has been invoked
queryStats.getExecutionCount();
//average time for invoking this query.
queryStats.getExecutionAvgTime();
//maximum time incurred by query execution
queryStats.getExecutionMaxTime();
//minimum time incurred by query execution
queryStats.getExecutionMinTime();
//Number of rows returned over all invocations of this query
queryStats.getExecutionRowCount();
EntityStatistics entityStats = stats.getEntityStatistics("org.ird.immunizationreminder.datamodel.entities.Arm"); // or Sale.class.getName();
//exactly the same as the global values, but for a single entity class.
entityStats.getFetchCount();
entityStats.getLoadCount();
entityStats.getInsertCount();
entityStats.getUpdateCount();
entityStats.getDeleteCount();
SecondLevelCacheStatistics cacheStats = stats.getSecondLevelCacheStatistics("org.ird.immunizationreminder.datamodel.entities.arm.cache");
cacheStats.getElementCountInMemory();
cacheStats.getElementCountOnDisk();
cacheStats.getEntries();
cacheStats.getHitCount();
cacheStats.getMissCount();
cacheStats.getPutCount();
cacheStats.getSizeInMemory();


}

}
*/