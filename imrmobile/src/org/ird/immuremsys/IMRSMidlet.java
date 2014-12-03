package org.ird.immuremsys;

import java.util.Hashtable;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import org.ird.immuremsys.constants.IMRMobileGlobals;
import org.ird.immuremsys.http.HttpSender;
import org.ird.immuremsys.http.IMRSRequestPayload;
import org.ird.immuremsys.http.MessageEntry;
import org.ird.immuremsys.http.RequestParam;
import org.ird.immuremsys.ui.BaseForm;
import org.ird.immuremsys.ui.HomeList;
import org.ird.immuremsys.ui.LoginForm;
import org.ird.immuremsys.ui.MRIDQueryForm;
import org.ird.immuremsys.ui.UpdateVaccinationForm;

public class IMRSMidlet extends MIDlet implements CommandListener {

	private String				currentUser;
	private String				currentUserId;
	private String				labId;
	private int					currentRole;
	private Display 			display;
	private Command 			exit;
	private HttpSender 			hs;
	public HomeList				mainList;
	public  MRIDQueryForm		mridQueryForm;
	public  LoginForm			loginForm;
	public  UpdateVaccinationForm updateVaccinationForm;
	public String getCurrentUserId() {
		return currentUserId;
	}
	public void setCurrentUserId(String currentUserId) {
		this.currentUserId = currentUserId;
	}
	public String getLabId() {
		return this.labId;
	}
	public void setLabId(String labId) {
		this.labId = labId;
	}
	public String getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}
	public int getCurrentRole() {
		return currentRole;
	}
	public void setCurrentRole(int currentRole) {
		this.currentRole = currentRole;
	}
	private void setDisplay(Displayable d) {
		display.setCurrent(d);
	}
	public IMRSMidlet() {
		this.display = Display.getDisplay(this);
		
		hs = new HttpSender();
		loginForm = new LoginForm("Login Form", this);
		mainList = new HomeList("Home Menu", this);
		mridQueryForm = new MRIDQueryForm("Query Form", this);
		updateVaccinationForm = new UpdateVaccinationForm("Update Vaccination Form", this);
		this.exit = new Command("Exit", Command.EXIT, 0x01);
	}
	
	protected void startApp() throws MIDletStateChangeException {
		if (display == null)
			display = Display.getDisplay (this);

		loginForm.init (null);

		setDisplay (loginForm);
	}
	public void showAlert (String msg, String imgName)
	{
		Alert alert;
		alert = new Alert ("ALERT!");
		alert.setString (msg);
		alert.setTimeout (Alert.FOREVER);

		setDisplay (alert);
	}

	public void showExitConfirmation(Displayable currentDisplayable) {
		Alert alert = new Alert("Alert");
		alert.setString("Do you want to exit IMRS application?");
		alert.addCommand(new Command("Yes", Command.OK, 1));
		alert.addCommand(new Command("No", Command.CANCEL, 1));
		alert.setCommandListener(new CommandListener() {
			public void commandAction(Command c, Displayable d) {
				if (c.getLabel().equals("Yes")) {
					try {
						destroyApp(false);
						notifyDestroyed();
					} catch (MIDletStateChangeException e) {
						e.printStackTrace();
					}
				}
				if (c.getLabel().equals("No")) {
					startMainMenu();
				}
			}
		});
		setDisplay(alert);
	}

	public void startMainMenu ()
	{
		mainList.init ();
		setDisplay (mainList);
	}
	public void startForm(BaseForm form, Displayable previousDisplayable)
	{
		form.init (previousDisplayable);
		setDisplay (form);
	}
	public void destroyApp(boolean unconditional) throws MIDletStateChangeException {}

	protected void pauseApp() {}

	public void commandAction(Command command, Displayable displayable) {
		if (command == this.exit) {
			this.notifyDestroyed();
		}
	}
	public Hashtable sendToServer (IMRSRequestPayload payload)
	{
		payload.addParam(RequestParam.APP_VER, Integer.toString(IMRMobileGlobals.IMR_VERSION)); // append version here in payload

		try
		{
			MessageEntry ms = new MessageEntry (IMRMobileGlobals.BASE_URL, payload.getRequestPayload(), true, MessageEntry.REQUEST_METHOD_HTTP_POST, false);
			hs.doPost (IMRMobileGlobals.BASE_URL, ms);
		}
		catch (Exception e)
		{
			e.printStackTrace ();
			return null;
		}

		return hs.model;
	}
}
