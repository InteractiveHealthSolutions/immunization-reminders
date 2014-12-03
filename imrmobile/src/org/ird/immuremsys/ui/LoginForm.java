package org.ird.immuremsys.ui;

import java.util.Date;
import java.util.Hashtable;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDletStateChangeException;

import org.ird.immuremsys.IMRSMidlet;
import org.ird.immuremsys.constants.ErrMsg;
import org.ird.immuremsys.constants.FormType;
import org.ird.immuremsys.http.IMRSRequestPayload;
import org.ird.immuremsys.http.RequestParam;
import org.ird.immuremsys.util.DateTimeUtil;
import org.ird.immuremsys.util.RecordStoreUtil;

public class LoginForm extends BaseForm implements CommandListener
{
	private static final String USERNAME_KEY = "username";
	private static final String PASSWORD_KEY = "password";

	private TextField	username;
	private TextField	password;
	private TextField	tt;

	private Command		cmdOK;
	private Command		cmdExit;

	public LoginForm (String title, IMRSMidlet tbrMidlet)
	{
		super (title, tbrMidlet);
	}

	public boolean validate ()
	{
		if (username == null || username.getString ().trim ().length () == 0)
		{
			getMainMidlet().showAlert (ErrMsg.USERNAME_MISSING, null);
			return false;
		}
		else if (password == null || password.getString ().trim ().length () == 0)
		{
			getMainMidlet().showAlert (ErrMsg.PASSWORD_MISSING, null);
			return false;
		}
		return true;
	}

	public void commandAction (Command c, Displayable d)
	{
		if (c == cmdOK)
		{
			if (validate ())
			{
				RecordStoreUtil.getInstance().clearRecordStore();
				System.out.println(RecordStoreUtil.getInstance().writeRecord(USERNAME_KEY, username.getString()));
				System.out.println(RecordStoreUtil.getInstance().writeRecord(PASSWORD_KEY, password.getString()));

				IMRSRequestPayload request = createRequestPayload ();
				Hashtable model = getMainMidlet().sendToServer (request);
				if (model != null)
				{
					if ((String) model.get ("status") != null && ((String) model.get ("status")).equals ("error"))
					{
						getMainMidlet().showAlert ((String) model.get ("msg"), null);
					}
					else
					{
						getMainMidlet().setCurrentUser (username.getString().trim());
						getMainMidlet().setCurrentUserId(username.getString().trim());

						getMainMidlet().startMainMenu ();
					}
				}
				else
				{
					System.out.println ("model is null");
					getMainMidlet().showAlert (ErrMsg.LOGIN_ERROR, null);
				}
			}
		}
		else if (c == cmdExit)
		{
			cleanup();

			try
			{
				getMainMidlet().destroyApp (false);
				getMainMidlet().notifyDestroyed ();
			}
			catch (MIDletStateChangeException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void init (Displayable prvDisplayable)
	{
		super.setPrevDisplayable(null);
		
		username = new TextField ("Username", "", 20, TextField.ANY);
		password = new TextField ("Password", "", 20, TextField.PASSWORD);
		tt = new TextField("ttest", "5555", 5, TextField.NUMERIC);
		cmdOK = new Command ("Login", Command.OK, 1);
		cmdExit = new Command ("Quit", Command.BACK, 0);
		
		String user = RecordStoreUtil.getInstance().searchRecordStoreByKey(USERNAME_KEY);
		if(!user.trim().equals("")){
			username.setString(user);
		}
		String pwd = RecordStoreUtil.getInstance().searchRecordStoreByKey(PASSWORD_KEY);
		if(!pwd.trim().equals("")){
			password.setString(pwd);
		}
		setCommandListener (this);
		addCommand (cmdOK);
		addCommand (cmdExit);
		append (username);
		append (password);
		append(tt);
	}

	protected IMRSRequestPayload createRequestPayload ()
	{
		IMRSRequestPayload payload = new IMRSRequestPayload();

		String usernameString = username.getString ();
		String passwordString = password.getString ();

		payload.addParam(RequestParam.REQ_TYPE, RequestParam.REQ_TYPE_SUBMIT.getParamName());
		payload.addParam(RequestParam.SUBMIT_FORM_TYPE, FormType.LOGIN);
		payload.addParam(RequestParam.LG_USERNAME, usernameString);
		payload.addParam(RequestParam.LG_PASSWORD, passwordString);
		payload.addParam(RequestParam.LG_PHONETIME, DateTimeUtil.getDateTime (new Date ()));

		return payload;
	}

	protected void cleanup() {
		deleteAll();
		removeCommand(cmdExit);
		removeCommand(cmdOK);
	}
}
