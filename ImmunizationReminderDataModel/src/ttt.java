import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.ird.immunizationreminder.datamodel.entities.Arm;


public class ttt {

	public static void main(String[] args) {
		
		System.out.println(new Arm().toString());
		SessionFactory sf=	new Configuration().configure().buildSessionFactory();
		Session ss=sf.openSession();
		Transaction t=ss.beginTransaction();
		
		List l;
		l = ss.createQuery("from Child where childId = 100").list();
		ss.close();
		System.out.println(l.get(0).toString().replace(";", "\n"));

		/*for (Child object : l) {
			System.out.println(object);
		}*/
	}
}
