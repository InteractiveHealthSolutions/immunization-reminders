package org.ird.immunizationreminder.autosys.smser;

import java.util.Timer;

import org.ird.immunizationreminder.web.utils.IMRGlobals;

public class SmserSystem 
{
	private static SmserSystem _instance;

	private SmserSystem(){
		
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	public static void instantiateSmserSystem() {
		if(_instance == null)
		{
			_instance = new SmserSystem();
			
			Timer reminderPusherTimer = new Timer();
			reminderPusherTimer.schedule(new ReminderPusherJob(), 1000*60*5, 1000*60*IMRGlobals.REMINDER_PUSHER_TIMER_INTERVAL_MIN);
			
			Timer reminderUpdaterTimer = new Timer();
			reminderUpdaterTimer.schedule(new ReminderUpdaterJob(), 1000*60*5, 1000*60*IMRGlobals.REMINDER_UPDATER_TIMER_INTERVAL_MIN);
			
			Timer responseReaderTimer = new Timer();
			responseReaderTimer.schedule(new ResponseReaderJob(), 1000*60*5, 1000*60*IMRGlobals.RESPONSE_READER_TIMER_INTERVAL_MIN);
		}
		else
		{
			throw new InstantiationError("An instance of SmserSystem already exists");
		}
	}
	
	public static SmserSystem getInstance() {
		if(_instance == null){
			throw new InstantiationError("SmserSystem not instantiated");
		}
		return _instance;
	}
}
