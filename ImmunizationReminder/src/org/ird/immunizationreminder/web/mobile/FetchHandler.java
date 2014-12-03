package org.ird.immunizationreminder.web.mobile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.ServiceContext;
import org.ird.immunizationreminder.data.exception.ChildDataException;
import org.ird.immunizationreminder.datamodel.entities.Child;
import org.ird.immunizationreminder.datamodel.entities.Vaccination;
import org.ird.immunizationreminder.datamodel.entities.Vaccination.VACCINATION_STATUS;
import org.ird.immunizationreminder.utils.date.DateUtils;
import org.ird.immunizationreminder.web.utils.IMRGlobals;

public class FetchHandler {

	public static void handleFetch(HttpServletRequest request, HttpServletResponse resp) throws TransformerException, IOException{
		String query = request.getParameter(RequestParam.FETCH_FORM_TYPE.getParamName());
		String childIdType = request.getParameter(RequestParam.QUERY_ID_TYPE.getParamName());

		ServiceContext sc = Context.getServices();
		Child child = null;
		
		if(childIdType.toLowerCase().indexOf("mr") != -1){
			String mr = request.getParameter(RequestParam.MR_NUMBER.getParamName());
			
			List<Child> childlst = sc.getChildService().findByEpiOrMrNumber(mr);
			if(childlst.size() > 1){
				IMRSMobileServlet.sendResponse(resp, XmlResponse.getErrorResponse("Data Inconsistent. Given Mr number exists on Multiple Children. Plz contact your Datbase Administrator").docToString());
				return;
			}
			else if(childlst.size() == 1){
				child = childlst.get(0);
			}
		}
		else if(childIdType.toLowerCase().indexOf("child id") != -1){
			String childid = request.getParameter(RequestParam.CHILD_ID.getParamName());

			try {
				child = sc.getChildService().getChildbyChildId(childid, true);
			} catch (ChildDataException e) {
				e.printStackTrace();
				IMRSMobileServlet.sendResponse(resp, XmlResponse.getErrorResponse(e.getMessage()).docToString());
				return;
			}
		}
		
		if(child == null){
			IMRSMobileServlet.sendResponse(resp, XmlResponse.getErrorResponse("Child not found. Plz recheck given ID or MR number").docToString());
			return;
		}
		
		if(query.equalsIgnoreCase(FormType.UPDATE_VACCINATION)){
			String vaccineType = request.getParameter(RequestParam.QUERY_VACC_PENDING.getParamName());

			try{
				Vaccination pndvacc = null;
				Vaccination prevVaccination = null;
				List<Vaccination> vaccinatedVacclst = null;
				List<Vaccination> pndvacclst=sc.getVaccinationService().findVaccinationRecordByCriteria(
						child.getChildId(), vaccineType, null, null, null, null, VACCINATION_STATUS.PENDING, 0, 10
						, false , FetchMode.JOIN , FetchMode.JOIN);
	
				if(pndvacclst.size()==0){
					IMRSMobileServlet.sendResponse(resp, XmlResponse.getErrorResponse("No vaccination with given name "+vaccineType+" was found to be pending for given child_id.").docToString());
					return;
				}
				else if(pndvacclst.size()>1){
					IMRSMobileServlet.sendResponse(resp, XmlResponse.getErrorResponse("Multiple vaccinations were found to be pending with given name "+vaccineType+" for given child_id. Contact your Database Administrator.").docToString());
					return;
				}
				else if(pndvacclst.size()==1){
					Calendar cal=Calendar.getInstance();
					cal.add(Calendar.DATE, 4);
					//if date of vaccination is far from today AND vaccine is not M2
					if(!DateUtils.truncateDatetoDate(pndvacclst.get(0).getVaccinationDuedate()).before(cal.getTime())
							&& !pndvacclst.get(0).getVaccine().getName().toLowerCase().contains("measles2")){
						IMRSMobileServlet.sendResponse(resp, XmlResponse.getErrorResponse("Note: "
												+ "\nChild`s Vaccination Duedate is on "
												+ new SimpleDateFormat("MMM dd, yyyy")
														.format(pndvacclst.get(0).getVaccinationDuedate())
												+ "\nand you are can update vaccination information only three days before vaccination due date and onwards." 
												+ "\nHowever to change/edit this vaccination record navigate to edit vaccination in Vaccinations menu in web Application").docToString());
						return;
					}
					else{
						pndvacc = pndvacclst.get(0);
					}
					prevVaccination = sc.getVaccinationService().getVaccinationRecord(
							pndvacclst.get(0).getPreviousVaccinationRecordNum(),false,FetchMode.SELECT,FetchMode.SELECT);
				}
				
				vaccinatedVacclst = sc.getVaccinationService().findVaccinationRecordByCriteria(
						child.getChildId(), null, null, null, null, null, null, 0, 10
						, false , FetchMode.JOIN , FetchMode.JOIN);
				
				String vaccinesRecieved = "";
				for (Vaccination vc : vaccinatedVacclst) {
					if(!vc.getVaccinationStatus().equals(VACCINATION_STATUS.PENDING)
							||!vc.getVaccinationStatus().equals(VACCINATION_STATUS.NOT_VACCINATED)
							||!vc.getVaccinationStatus().equals(VACCINATION_STATUS.MISSED)){
						vaccinesRecieved += vc.getVaccine().getName()+ " : ";
					}
				}
				
				String prevVaccDetStr = "";
				if(prevVaccination != null){
					try{
						prevVaccDetStr += "Vaccine:" + prevVaccination.getVaccine().getName();
						prevVaccDetStr += "\nDue Date:" + new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT).format(prevVaccination.getVaccinationDuedate());
						prevVaccDetStr += "\nVaccination Date:" + new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT).format(prevVaccination.getVaccinationDate());
						prevVaccDetStr += "\nNext Assigned Date:" + new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT).format(prevVaccination.getNextAssignedDate());
					}catch (NullPointerException e) {
						IMRSMobileServlet.sendResponse(resp, XmlResponse.getErrorResponse("Oops .. Some error occurred in finding dates from previous vaccination details. Contact your Database Administrator and fix missing values for any of these (vaccine, vaccination due date, vaccination date or next assigned date).").docToString());
					}
				}
				XmlResponse xmr = XmlResponse.getSuccessResponse();
				xmr.addElement(ResponseParam.UV_CHILD_ID.getParamName(), child.getChildId());
				xmr.addElement(ResponseParam.UV_CHILD_NAME.getParamName(), child.getFirstName());
				xmr.addElement(ResponseParam.UV_PREV_VACC_DETAILS.getParamName(), prevVaccDetStr);
				xmr.addElement(ResponseParam.UV_CUR_VACC_DUEDATE.getParamName(), Long.toString(pndvacc.getVaccinationDuedate().getTime())/*new SimpleDateFormat(IMRGlobals.GLOBAL_DATE_FORMAT).format(pndvacc.getVaccinationDuedate())*/);
				xmr.addElement(ResponseParam.UV_VACC_RECIEVED_NAMES.getParamName(), vaccinesRecieved);
				xmr.addElement(ResponseParam.UV_CUR_VACC_NAME.getParamName(), (pndvacc.getVaccine() == null ? "N/A" : pndvacc.getVaccine().getName()));
				xmr.addElement(ResponseParam.UV_BIRTHDATE.getParamName(), Long.toString(child.getBirthdate().getTime()));
				if(prevVaccination != null){
				xmr.addElement(ResponseParam.UV_PREV_VACC_DATE.getParamName(), Long.toString(prevVaccination.getVaccinationDate().getTime()));
				}
				IMRSMobileServlet.sendResponse(resp, xmr.docToString());

			}catch (Exception e) {
				e.printStackTrace();
				IMRSMobileServlet.sendResponse(resp, XmlResponse.getErrorResponse("Oops .. Some error unknown occurred. "+e.getMessage()+". Contact your Program Vendor.").docToString());
				return;
			}
		}
	}
}
