package org.ird.immunizationreminder.context;

import java.util.Date;
import java.util.TimerTask;

public class RefreshUsers extends TimerTask {

    @Override
	public void run() {

    	synchronized (this) {
			for (LoggedInUser item : Context.getcurrentlyLoggedInUsers().values()) {
				long now=new Date().getTime();
				long date=item.getDateTime().getTime();
				long difMillis=now-date;
				int sessionExpiryTimeMin = 15;
				try{
					sessionExpiryTimeMin = Integer.parseInt(Context.getProperties().getProperty("user.currently-loggedin-user.session-expire-time", "15"));
				}
				catch (Exception e) {
					e.printStackTrace();
				}

				if(difMillis > (1000*60* sessionExpiryTimeMin)){
					Context.removeUser(item.getUser().getName());
				}
			}
    	}
	}
}
