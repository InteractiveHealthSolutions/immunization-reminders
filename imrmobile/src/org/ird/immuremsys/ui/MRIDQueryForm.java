package org.ird.immuremsys.ui;

import java.util.Hashtable;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.TextField;

import org.ird.immuremsys.IMRSMidlet;
import org.ird.immuremsys.constants.ErrMsg;
import org.ird.immuremsys.constants.FormType;
import org.ird.immuremsys.http.IMRSRequestPayload;
import org.ird.immuremsys.http.RequestParam;

public class MRIDQueryForm extends BaseForm implements CommandListener, ItemStateListener
{
	Command			cmdOK;
	Command			cmdBack;

	TextField		idField;
	TextField		mrField;
	ChoiceGroup		idType;
	ChoiceGroup pendingVaccine;

	private String	formType;

	public String getFormType ()
	{
		return formType;
	}

	public void setFormType (String formType)
	{
		this.formType = formType;
	}

	public MRIDQueryForm (String title, IMRSMidlet mainMIDlet)
	{
		super (title, mainMIDlet);

		cmdOK = null;
		cmdBack = null;

		idField = null;
		mrField = null;
		idType = null;
	}

	public void commandAction (Command c, Displayable d)
	{
		if (c == cmdOK)
		{
			if (validate ())
			{
				Hashtable model = getMainMidlet().sendToServer (createRequestPayload ());

				if (model != null)
				{
					if ((String) model.get ("status") != null && ((String) model.get ("status")).equals ("error"))
					{
						getMainMidlet().showAlert ((String) model.get ("msg"), null);
					}
					else
					{
						if(formType.equals(FormType.UPDATE_VACCINATION)){
							getMainMidlet().updateVaccinationForm.setQueryData (model);
							getMainMidlet().updateVaccinationForm.setPrevDisplayable (getPrevDisplayable());
							cleanup ();
							getMainMidlet().startForm (getMainMidlet().updateVaccinationForm, this);
						}
					}
				}
			}
		}
		else if (c == cmdBack)
		{
			cleanup();
			getMainMidlet().startMainMenu();
		}
	}

	protected IMRSRequestPayload createRequestPayload()
	{
		String id = idField.getString ();
		String mr = mrField.getString ();
		String type = idType.getString (idType.getSelectedIndex ());
		String vacc = pendingVaccine.getString (pendingVaccine.getSelectedIndex ());

		IMRSRequestPayload payload = new IMRSRequestPayload();
		payload.addParam(RequestParam.REQ_TYPE, RequestParam.REQ_TYPE_FETCH.getParamName());
		payload.addParam(RequestParam.FETCH_FORM_TYPE, FormType.UPDATE_VACCINATION);
		payload.addParam(RequestParam.QUERY_ID_TYPE, type);
		payload.addParam(RequestParam.QUERY_VACC_PENDING, vacc);

		if(idType.getSelectedIndex () == 1){
			payload.addParam(RequestParam.CHILD_ID, id);
		}
		else if(idType.getSelectedIndex () == 0){
			payload.addParam(RequestParam.MR_NUMBER, mr);
		}

		return payload;
	}

	public void init (Displayable prvDisplayable)
	{
		super.setPrevDisplayable(prvDisplayable);
		
		idField = new TextField ("ID", "", 10, TextField.NUMERIC);
		idField.setConstraints (TextField.UNEDITABLE);

		mrField = new TextField ("MR", "", 11, TextField.NUMERIC);
		idType = new ChoiceGroup ("Search using", ChoiceGroup.POPUP);
		idType.append ("EPI/MR Number", null);
		idType.append ("Child ID", null);
		pendingVaccine = new ChoiceGroup("Vaccine to Update", ChoiceGroup.EXCLUSIVE);
		pendingVaccine.append("BCG/OPV", null);
		pendingVaccine.append("Penta1/OPV", null);
		pendingVaccine.append("Penta2/OPV", null);
		pendingVaccine.append("Penta3/OPV", null);
		pendingVaccine.append("Measles1", null);
		pendingVaccine.append("Measles2", null);

		cmdOK = new Command ("Submit", Command.OK, 0);
		cmdBack = new Command ("Home", Command.BACK, 1);

		append (idType);
		append (mrField);
		append (idField);
		append(pendingVaccine);
		addCommand (cmdOK);
		addCommand (cmdBack);

		setCommandListener (this);
		setItemStateListener (this);
	}

	protected boolean validate ()
	{
		boolean result = true;
		if (idType.getSelectedIndex () == 1 && (idField.getString () == null || idField.getString ().length () == 0))
		{
			getMainMidlet().showAlert (ErrMsg.ID_MISSING, null);
			result = false;
		}
		else if (idType.getSelectedIndex () == 0 && (mrField.getString () == null || mrField.getString ().length () == 0))
		{
			getMainMidlet().showAlert (ErrMsg.MR_MISSING, null);
			result = false;
		}

		return result;
	}

	public void itemStateChanged (Item i)
	{
		if (i == idType)
		{
			int index = idType.getSelectedIndex ();

			if (index == 1)
			{
				mrField.setString ("");
				mrField.setConstraints (TextField.UNEDITABLE);
				idField.setConstraints (TextField.NUMERIC);
			}
			else if (index == 0)
			{
				idField.setString ("");
				idField.setConstraints (TextField.UNEDITABLE);
				mrField.setConstraints (TextField.NUMERIC);
			}
		}
	}

	protected void cleanup() {
		deleteAll();
		removeCommand(cmdBack);
		removeCommand(cmdOK);
	}
}
