package org.ird.immuremsys.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.DateField;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

import org.ird.immuremsys.IMRSMidlet;
import org.ird.immuremsys.constants.ErrMsg;
import org.ird.immuremsys.constants.FormType;
import org.ird.immuremsys.http.IMRSRequestPayload;
import org.ird.immuremsys.http.RequestParam;
import org.ird.immuremsys.http.ResponseParam;
import org.ird.immuremsys.util.DateTimeUtil;
import org.ird.immuremsys.util.StringUtil;

public class UpdateVaccinationForm extends BaseForm implements CommandListener, ItemStateListener{

	private StringItem 	childIdStr;
	private StringItem 	childNameStr;
	private StringItem 	vaccinesRecievedStr;

	private StringItem 	prevVaccinationDetailsStr;
	private StringItem 	vaccineScheduledStr;

	private StringItem 	currVaccinationDuedateStr;
	
	private ChoiceGroup vaccine;
	private ChoiceGroup currvaccinationStatus;
	private TextField	reasonNotVaccinated;
	private StringItem 	calculatedNextVaccDate;
	private DateField	currvaccinationDate;
	private ChoiceGroup isLastVaccination;
	private ChoiceGroup nextVaccine;
	private DateField	nxtvaccinationDate;
	private TextField	additionalNote;

	private Hashtable queryData;
	
	private Command		cmdOK;
	private Command		cmdExit;
	
	private long currentVaccinationDueDate;
	private long birthdate;
	private long prevVaccDate;
	
	public UpdateVaccinationForm(String title, IMRSMidlet mainMidlet) {
		super(title, mainMidlet);
	}

	protected IMRSRequestPayload createRequestPayload() {
		IMRSRequestPayload payload = new IMRSRequestPayload();
		payload.addParam(RequestParam.REQ_TYPE, RequestParam.REQ_TYPE_SUBMIT.getParamName());
		payload.addParam(RequestParam.SUBMIT_FORM_TYPE, FormType.UPDATE_VACCINATION);
		payload.addParam(RequestParam.REQ_USER, getMainMidlet().getCurrentUserId());

		payload.addParam(RequestParam.UV_CHILDID, childIdStr.getText());
		payload.addParam(RequestParam.UV_CURR_VACCINATION_DATE, DateTimeUtil.getDateTime(currvaccinationDate.getDate()));
		payload.addParam(RequestParam.UV_CURR_VACCINE_RECIEVED, vaccine.getString(vaccine.getSelectedIndex()));
		if(isShown(isLastVaccination)){
			payload.addParam(RequestParam.UV_IS_LAST_VACCINATION, isLastVaccination.getString(isLastVaccination.getSelectedIndex()));
		}
		payload.addParam(RequestParam.UV_VACCINATION_STATUS, currvaccinationStatus.getString(currvaccinationStatus.getSelectedIndex()));
		
		if(isShown(nextVaccine) 
				&& nextVaccine.getString(nextVaccine.getSelectedIndex()).toLowerCase().indexOf("system calcu") == -1
				&& nextVaccine.getString(nextVaccine.getSelectedIndex()).toLowerCase().indexOf("none") == -1){
			payload.addParam(RequestParam.UV_NEXT_VACCINE, nextVaccine.getString(nextVaccine.getSelectedIndex()));
			if(isShown(nxtvaccinationDate)){
			payload.addParam(RequestParam.UV_NEXT_VACCINATION_DATE, DateTimeUtil.getDateTime(nxtvaccinationDate.getDate()));
			}
		}
		if(isShown(reasonNotVaccinated)){
			payload.addParam(RequestParam.UV_REASON_NOT_VACCINATED, reasonNotVaccinated.getString());
		}
		payload.addParam(RequestParam.UV_ADDITIONAL_NOTE, additionalNote.getString().trim());

		return payload;
	}


	public void init(Displayable prvDisplayable) {
		super.setPrevDisplayable(prvDisplayable);
		
		childIdStr = new StringItem("Child ID: ", "");
		childIdStr.setText(((String) queryData.get(ResponseParam.UV_CHILD_ID.getParamName())));
		
		childNameStr = new StringItem("Child Name: ", "");
		childNameStr.setText(((String) queryData.get(ResponseParam.UV_CHILD_NAME.getParamName())));
		
		vaccinesRecievedStr = new StringItem("Vaccines Recieved: ", "");
		vaccinesRecievedStr.setText(((String) queryData.get(ResponseParam.UV_VACC_RECIEVED_NAMES.getParamName())));
		
		prevVaccinationDetailsStr = new StringItem("Previous Vaccination: ", "");
		prevVaccinationDetailsStr.setText(((String) queryData.get(ResponseParam.UV_PREV_VACC_DETAILS.getParamName())));
		
		currentVaccinationDueDate = Long.parseLong(((String) queryData.get(ResponseParam.UV_CUR_VACC_DUEDATE.getParamName())));
		birthdate = Long.parseLong(((String) queryData.get(ResponseParam.UV_BIRTHDATE.getParamName())));

		currVaccinationDuedateStr = new StringItem("Vaccination Due Date: ", "");
		currVaccinationDuedateStr.setText(DateTimeUtil.getDate(new Date(currentVaccinationDueDate)));
		
		vaccineScheduledStr = new StringItem("Vaccine Scheduled: ", "");
		vaccineScheduledStr.setText(((String) queryData.get(ResponseParam.UV_CUR_VACC_NAME.getParamName())));
		
		if(vaccineScheduledStr.getText().toLowerCase().indexOf("measles2") != -1){
			prevVaccDate = Long.parseLong(((String) queryData.get(ResponseParam.UV_PREV_VACC_DATE.getParamName())));
		}
		
		vaccine = new ChoiceGroup("Vaccine Recieved today: ", ChoiceGroup.POPUP);
		vaccine.append(vaccineScheduledStr.getText(), null);
/*		vaccine.append("Penta1/OPV", null);
		vaccine.append("Penta2/OPV", null);
		vaccine.append("Penta3/OPV", null);
		vaccine.append("BCG/OPV", null);
		vaccine.append("Measles1", null);
		vaccine.append("Measles2", null);*/
		
		/*for (int i = 0; i < vaccine.size(); i++) {
			if(vaccine.getString(i).toLowerCase().equals(vaccineScheduledStr.getText().toLowerCase())){
				vaccine.setSelectedIndex(i, true);
			}
		}*/
		
		currvaccinationStatus  =new ChoiceGroup("Vaccination Status: ", ChoiceGroup.POPUP);
		currvaccinationStatus.append("Vaccinated", null);
		currvaccinationStatus.append("Late_Vaccinated", null);
		currvaccinationStatus.append("Not_Vaccinated", null);
		//currvaccinationStatus.append("Missed", null);

		reasonNotVaccinated = new TextField("Reason Child not/late vaccinated: ", "", 25, TextField.ANY);
		
		currvaccinationDate = new DateField("Vaccination Date: ", DateField.DATE);
		currvaccinationDate.setDate(new Date());
		
		additionalNote = new TextField("Additional Note: ", "", 100, TextField.ANY);

		isLastVaccination = new ChoiceGroup("Is Last Vaccination: ", ChoiceGroup.POPUP);
		isLastVaccination.append("No", null);
		isLastVaccination.append("Yes", null);

		nextVaccine = new ChoiceGroup("Next Vaccine: ", ChoiceGroup.POPUP);
		nextVaccine.append(calculateNextVaccineName(), null);
		
		nxtvaccinationDate = new DateField("Next Vaccination Due Date: ", DateField.DATE);
		nxtvaccinationDate.setDate(calculateNextVaccinationDate());
		
		calculatedNextVaccDate = new StringItem("Auto calculated next vaccination date: ", "");
		calculatedNextVaccDate.setText(DateTimeUtil.getDate(calculateNextVaccinationDate()));
		
		cmdOK = new Command ("Submit", Command.OK, 1);
		cmdExit = new Command ("Back", Command.BACK, 0);
		
		addFormElement(childIdStr); 
		addFormElement(childNameStr);
		addFormElement(vaccinesRecievedStr); 
		addFormElement(prevVaccinationDetailsStr);  
		addFormElement(currVaccinationDuedateStr); 
		//addFormElement(vaccineScheduledStr);        
		addFormElement(vaccine);                    
		addFormElement(currvaccinationStatus);      
		addFormElement(reasonNotVaccinated);        
		addFormElement(currvaccinationDate);
		addFormElement(additionalNote);
		addFormElement(isLastVaccination); 
		addFormElement(nextVaccine); 
		addFormElement(calculatedNextVaccDate);     
		addFormElement(nxtvaccinationDate);    
		
		hide(reasonNotVaccinated);
		
		handleIsLastVaccinationFeature();
		handleNextVaccineFeature();
		
		addCommand(cmdOK);
		addCommand(cmdExit);
		
		setItemStateListener(this);
		setCommandListener(this);
	}

	protected boolean validate() {
		if(vaccine.getString(vaccine.getSelectedIndex()).trim().equals("")){
			getMainMidlet().showAlert(ErrMsg.VACCINE_MISSING, null);
			return false;
		}
		else if(isShown(reasonNotVaccinated)
				&& reasonNotVaccinated.getString().trim().equals("")){
			getMainMidlet().showAlert(ErrMsg.REASON_NOT_VACCINATED, null);
			return false;
		}
		else if(DateTimeUtil.isDateInFuture(currvaccinationDate.getDate())){
			getMainMidlet().showAlert(ErrMsg.DATE_IN_FUTURE, null);
			return false;
		}
		else if(isShown(nextVaccine)
				&& nextVaccine.getString(nextVaccine.getSelectedIndex()).toLowerCase().indexOf("none") != -1){
			getMainMidlet().showAlert(ErrMsg.VACCINE_MISSING, null);
			return false;
		}
		
		if(vaccine.getString(vaccine.getSelectedIndex()).toLowerCase().indexOf("measles2") != -1){
			if(currvaccinationDate.getDate().getTime() < (prevVaccDate+1000*60*60*24*30*3L)){
				getMainMidlet().showAlert("Measles2 vaccination should be given atleast 3 months after Measles1", null);
				return false;
			}
			
			if(currvaccinationDate.getDate().getTime() < (birthdate+1000*60*60*24*30.5*12L)){
				getMainMidlet().showAlert("Measles2 vaccination should be given atleast 12 months from birthdate", null);
				return false;
			}
		}
		else if(currvaccinationDate.getDate().getTime() < (currentVaccinationDueDate-(1000*60*60*24*3))){
			getMainMidlet().showAlert(ErrMsg.CURR_VACC_DATE, null);
			return false;
		}
		
		return true;
	}
	
	public void setQueryData(Hashtable queryData) {
		this.queryData = queryData;
	}
	public Hashtable getQueryData() {
		return queryData;
	}

	public void commandAction(Command c, Displayable arg1) {
		if (c == cmdOK)
		{
			if (validate ())
			{
				IMRSRequestPayload request = createRequestPayload ();
				Hashtable model = getMainMidlet().sendToServer (request);
				if (model != null)
				{
					if ((String) model.get ("status") != null && ((String) model.get ("status")).equals ("error"))
					{
						getMainMidlet().showAlert ((String) model.get ("msg"), null);
					}
					else{
						cleanup();
						getMainMidlet().startForm(getMainMidlet().mridQueryForm, getMainMidlet().mainList);
						getMainMidlet().showAlert ("vaccination updated successfully.",null);
					}
				}
				else
				{
					System.out.println ("model is null");
					getMainMidlet().showAlert (ErrMsg.UNKNOWN_ERROR, null);
				}
			}
		}
		else if (c == cmdExit)
		{
			cleanup();
			getMainMidlet().startForm(getMainMidlet().mridQueryForm, getMainMidlet().mainList);
		}
	}
	protected void cleanup(){
		deleteAll();
		removeCommand(cmdExit);
		removeCommand(cmdOK);
	}
	
	private void handleIsLastVaccinationFeature(){
		String currVacc = StringUtil.getString(vaccine).trim().toLowerCase();
		
		///donot change sequence of checks
		if(StringUtil.getString(currvaccinationStatus).trim().toLowerCase().indexOf("not_vaccinated") != -1){
			if(isShown(isLastVaccination)) hide(isLastVaccination); 
		}
		else if(currVacc.indexOf("measles2") != -1
				|| currVacc.indexOf("penta3") != -1){
			if(!isShown(isLastVaccination)) show(isLastVaccination);
		}
		else{
			if(isShown(isLastVaccination)) hide(isLastVaccination); 
		}
		
		if(isShown(isLastVaccination)
				&& isLastVaccination.getString(isLastVaccination.getSelectedIndex()).toLowerCase().equals("yes")){
			if(isShown(nextVaccine)) hide(nextVaccine);
			if(isShown(calculatedNextVaccDate)) hide(calculatedNextVaccDate);
			if(isShown(nxtvaccinationDate)) hide(nxtvaccinationDate);
			
			getMainMidlet().showAlert(ErrMsg.LAST_VACCINATION_SELECTED, null);
		}
		else if(isShown(isLastVaccination)
				&& isLastVaccination.getString(isLastVaccination.getSelectedIndex()).toLowerCase().equals("no")){
			if(!isShown(nextVaccine)) show(nextVaccine);
			if(!isShown(calculatedNextVaccDate)) show(calculatedNextVaccDate);
			if(!isShown(nxtvaccinationDate)) show(nxtvaccinationDate);
		}
	}
	public void handleNextVaccineFeature(){
		nextVaccine.deleteAll();
		nextVaccine.append(calculateNextVaccineName(), null);
		
		if(isShown(nextVaccine) && nextVaccine.getString(nextVaccine.getSelectedIndex()).toLowerCase().indexOf("system") != -1){
			if(isShown(calculatedNextVaccDate)) hide(calculatedNextVaccDate);
			if(isShown(nxtvaccinationDate)) hide(nxtvaccinationDate);
		}
		else if(isShown(nextVaccine)&& nextVaccine.getString(nextVaccine.getSelectedIndex()).toLowerCase().indexOf("system") == -1){
			if(!isShown(calculatedNextVaccDate)) show(calculatedNextVaccDate);
			if(!isShown(nxtvaccinationDate)) show(nxtvaccinationDate);
		}
	}
	private Date calculateNextVaccinationDate(){
		Calendar cal=Calendar.getInstance();
		cal.setTime(currvaccinationDate.getDate());
		///DOnot change sequence of checks
		if(StringUtil.getString(currvaccinationStatus).trim().toLowerCase().indexOf("not_vaccinated") != -1){
			cal.setTime(addDays(cal.getTime() , 3));
		}
		else if(vaccine.getString(vaccine.getSelectedIndex()).toLowerCase().indexOf("measles1") != -1){
			Date expectedDate = new Date((long) (birthdate+(1000*60*60*24*30.5*12L)));
			Date m1limitDate = new Date(cal.getTime().getTime()+ (1000*60*60*24*30*3L));
			if(m1limitDate.getTime() > expectedDate.getTime()){
				cal.setTime(m1limitDate);
			}
			else{
				cal.setTime(expectedDate);
			}
		}
		else{
			cal.setTime(addDays(cal.getTime() , 30));
		}

		if(vaccine.getString(vaccine.getSelectedIndex()).toLowerCase().indexOf("measles") != -1){
			/*if(cal.get(Calendar.DAY_OF_WEEK) <= Calendar.WEDNESDAY){//<4
				cal.setTime(addDays(cal.getTime(),(Calendar.WEDNESDAY - cal.get(Calendar.DAY_OF_WEEK))));
			}
			else */
			if(cal.get(Calendar.DAY_OF_WEEK) <= Calendar.SATURDAY){//<7
				cal.setTime(addDays(cal.getTime(),( Calendar.SATURDAY - cal.get(Calendar.DAY_OF_WEEK))));
			}
		}
		else{
			if (cal.get( Calendar.DAY_OF_WEEK ) == Calendar.SUNDAY) {
				cal.setTime(addDays(cal.getTime(), 1 ));
			}
		}
		
		return cal.getTime();
	}

	public String calculateNextVaccineName(){
		String currVacc = StringUtil.getString(vaccine).trim().toLowerCase();
		
		//If it is not vaccinated then it should schedule current vaccine for next visit
		if(StringUtil.getString(currvaccinationStatus).trim().toLowerCase().indexOf("not_vaccinated") != -1){
			return StringUtil.getString(vaccine);
		}
		if(currVacc.indexOf("measles1") != -1){
			return  "Measles2";
		}
		else if(currVacc.indexOf("measles2") != -1){
			return "System calculated";
		}
		else if(currVacc.indexOf("penta1") != -1){
			return "Penta2/OPV";
		}
		else if(currVacc.indexOf("penta2") != -1){
			return "Penta3/OPV";
		}
		else if(currVacc.indexOf("penta3") != -1){
			return "System calculated";
		}
		else if(currVacc.indexOf("bcg") != -1){
			return "Penta1/OPV";
		}
		else{
			return "None";
		}
	}
	public static Date addDays(Date date, int daysToAdd){
		Calendar cal = Calendar.getInstance();

		final long mils = date.getTime();
		final long MILLIS_DAY = 1000 * 60 * 60 * 24;
		final long reqMils = mils + (MILLIS_DAY * daysToAdd);
		
		Date reqDate = new Date(reqMils);

		cal.setTime(reqDate);

		return cal.getTime();
	}
	
	public void itemStateChanged(Item i) {
		/*if(i ==  vaccine){
			String vaccstr = vaccine.getString(vaccine.getSelectedIndex()).toLowerCase();
			if(vaccinesRecievedStr.getText() != null 
					&& vaccinesRecievedStr.getText().trim().toLowerCase().indexOf(vaccstr.substring(0, vaccstr.indexOf("/"))) != -1){
				getMainMidlet().showAlert(ErrMsg.VACCINE_GIVEN, null);
			}
		}
		else*/ 
		if(i == currvaccinationDate){
			if(StringUtil.getString(vaccine).toLowerCase().indexOf("measles2") == -1){//if not M2
			final long currVcdt = currvaccinationDate.getDate().getTime();
			Calendar c1 = Calendar.getInstance();
			c1.setTime(addDays(new Date(currVcdt),3));
			if(c1.getTime().getTime() < currentVaccinationDueDate){
				getMainMidlet().showAlert(ErrMsg.CURR_VACC_DATE, null);
				currvaccinationDate.setDate(new Date());
				return;
			}
			}
			if(isShown(nxtvaccinationDate)){
				calculatedNextVaccDate.setText(DateTimeUtil.getDate(calculateNextVaccinationDate()));
				nxtvaccinationDate.setDate(calculateNextVaccinationDate());
			}
		}
		else if(i == isLastVaccination){
			handleIsLastVaccinationFeature();
			
			handleNextVaccineFeature();
		}
/*		else if(i == nextVaccine){
			String nxvcc = nextVaccine.getString(nextVaccine.getSelectedIndex()).toLowerCase();
			
			if(nxvcc.trim().equals("system calculated")){
				if(isShown(nxtvaccinationDate)) hide(nxtvaccinationDate);
				if(isShown(nxtvaccinationDate)) hide(calculatedNextVaccDate);
				return;
			}
			if(nxvcc.equals(vaccine.getString(vaccine.getSelectedIndex()).toLowerCase())){
				getMainMidlet().showAlert(ErrMsg.VACCINE_CURRENTLY_GIVEN, null);
			}
			else if(vaccinesRecievedStr.getText() != null 
					&& vaccinesRecievedStr.getText().trim().toLowerCase().indexOf(nxvcc.substring(0, nxvcc.indexOf("/"))) != -1){
				getMainMidlet().showAlert(ErrMsg.VACCINE_GIVEN, null);
			}
		}*/
		else if(i == currvaccinationStatus){
			String vststr = currvaccinationStatus.getString(currvaccinationStatus.getSelectedIndex()).toLowerCase();
			if(/*vststr.equals("missed")|| */
					vststr.equals("not_vaccinated")
					|| vststr.equals("late_vaccinated")){
				if(!isShown(reasonNotVaccinated)) show(reasonNotVaccinated);
			}
			else {
				if(isShown(reasonNotVaccinated)) hide(reasonNotVaccinated);
			}

			handleIsLastVaccinationFeature();
			handleNextVaccineFeature();
			calculatedNextVaccDate.setText(DateTimeUtil.getDate(calculateNextVaccinationDate()));
			nxtvaccinationDate.setDate(calculateNextVaccinationDate());
		}
		else if (i == nxtvaccinationDate){
			Calendar cal = Calendar.getInstance();
			cal.setTime(addDays(calculateNextVaccinationDate(), -2));

			Calendar nextDat = Calendar.getInstance();
			nextDat.setTime(new Date(nxtvaccinationDate.getDate().getTime()));

			if(cal.getTime().getTime() > nxtvaccinationDate.getDate().getTime()){
				nxtvaccinationDate.setDate(calculateNextVaccinationDate());
				getMainMidlet().showAlert(ErrMsg.NEXT_VACCINATION_DATE_INVALID, null);
			}
			else if(nextDat.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
				nextDat.setTime(addDays(nextDat.getTime() , 1) );
				nxtvaccinationDate.setDate(nextDat.getTime());
				getMainMidlet().showAlert(ErrMsg.NEXT_VACCINATION_SUNDAY, null);
			}
		}
	}
}
