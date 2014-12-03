package org.ird.immuremsys.ui;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import org.ird.immuremsys.IMRSMidlet;
import org.ird.immuremsys.util.HomeListItem;

public class HomeList extends List implements CommandListener{
	private IMRSMidlet			mainMidlet;
	private Command				cmdOK;
	private Command				cmdExit;
	private Displayable			prevDisplayable;

	public HomeList (String title, IMRSMidlet mainMidlet)
	{
		super (title, Choice.IMPLICIT);
		this.mainMidlet = mainMidlet;
		cmdOK = new Command ("Choose", Command.OK, 0);
		cmdExit = new Command ("Back", Command.EXIT, 1);
	}

	public Displayable getPrevDisplayable ()
	{
		return prevDisplayable;
	}

	public void setPrevDisplayable (Displayable prevDisplayable)
	{
		this.prevDisplayable = prevDisplayable;
	}

	private void loadItems ()
	{
		append(HomeListItem.UPDATE_VACCINATION.NAME, null);
	}

	public void init ()
	{
		deleteAll ();
		loadItems ();

		addCommand (cmdOK);
		addCommand (cmdExit);
		setCommandListener (this);
	}

	public void commandAction (Command c, Displayable d)
	{
		if (c == cmdOK)
		{
			String itemName = getString (this.getSelectedIndex ());

			if (itemName.equals(HomeListItem.UPDATE_VACCINATION.NAME))
			{
				mainMidlet.mridQueryForm.setFormType (HomeListItem.UPDATE_VACCINATION.getFormType());
			}

			mainMidlet.startForm(mainMidlet.mridQueryForm, this);
		}
		else if (c == cmdExit)
		{
			mainMidlet.showExitConfirmation(this);
		}
	}
}
