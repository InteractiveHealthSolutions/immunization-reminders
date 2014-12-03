package org.ird.immunizationreminder.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.data.exception.ChildDataException;
import org.ird.immunizationreminder.datamodel.entities.Child;
import org.ird.immunizationreminder.datamodel.entities.Child.STATUS;

public interface ChildService {
	Number LAST_QUERY_TOTAL_ROW__COUNT();

//	public void createChild(Child child) throws ChildDataException;
//	
	List<Child> getAllChildren(int firstResult, int fetchSize,boolean isreadonly/*,FetchMode armFetchmode*/);
	
	boolean isCellNumberAvailable(String cellnumber) throws ChildDataException ;
	
	Child getChildbyChildId(String childId,boolean isreadonly/*,FetchMode armFetchmode*/) throws ChildDataException;
	
	Child getChildbyCurrentCell(String currentCell,boolean isreadonly/*,FetchMode armFetchmode*/) throws ChildDataException;
	
	List<Child> findByEpiOrMrNumber(String epiOrMrNumber);
	
	List<Child> findChildByCriteria(String partOfFirstOrLastName, String clinic,
			STATUS Status, boolean putNotWithTeatmentStatus,String armName,
			int firstResult, int fetchsize,boolean isreadonly/*,FetchMode armFetchmode*/) throws ChildDataException;

	void updateChild(Child child);

	Serializable addChild(Child child) throws ChildDataException;
	
	List<Child> findByCurrentCellIgnoreInconsistency(String currentCellNumber,boolean isreadonly/*,FetchMode armFetchmode*/);

	Child mergeUpdateChild(Child child);

//	
//	public Child getChildById(String id) throws ChildDataException;
//	
//	public List<Child> getChildByFirstName(String firstName);
//	
//	public List<Child> getChildByLastName(String lastName);
//	
//	public List<Child> getChildByAnyName(String partOfName);//////should b in service layer
//	
//	public Child getChildByCurrentCellNum(String cellNum) throws ChildDataException;
//	
//	public List<Child> getChildrenUsedCellNum(String cell);////in service layer
//	
//	public boolean isChildUsingCellNum(String childId,String cellNum) throws ChildDataException;////in service layser convert to cell then in pateintcell
//	
//	public boolean hasChildUsedCellNum(String childId, String cellNum);///not here either patCell or service
//	
//	public List<Child> getChildByDateEnrolled(Date date);
//	
//	public List<Child> getChildByDateCompletion(Date date);
//	
//	public List<Child> getChildrenCurrentlyUnderTreatment();
//	
//	public List<Child> getChildrenHavingStatus(Status status);
//	
//	public boolean isChildUnderTreatment(String childId) throws ChildDataException;
//	
//	public void updateChild(Child child);
//	
//	public List<CellHistory>	searchCellRecord();
//	
//	public List<CellHistory>	searchCellRecord(String cell);///child+cell
//	
//	public List<CellHistory> searchCellRecordByChild(String childId) throws ChildDataException;/////in service tooooo
//	
//	public List<CellHistory> searchCellRecord(Date datesmall,Date dategreater);////child+cell
//	
//	public List<CellHistory> searchCellRecordByChild(String patinetIdentifier, Date datesmall,Date dategreater) throws ChildDataException;
//	
//	public List<CellHistory> searchCellRecord(String patinetIdentifier, String cellNum) throws ChildDataException;
//	
//	public List<CellHistory> searchCellRecord(String cellNum,Date datesmall,Date dategreater);
//	
//	public List<CellHistory> searchCellRecord(String patinetIdentifier,String cellNum,Date datesmall,Date dategreater) throws ChildDataException;
//		
//	public void changeChildCellNum(String childId,String cellnumber) throws ChildDataException;
//	
//	public void setChildNotUnderTreatment(String childId) throws ChildDataException;
//	
//	//public void addChildReminder(String childId,Reminder reminder) throws ReminderException, ChildDataException;
//	
//	//public List<Reminder> getChildReminders(String childID) throws ChildDataException;
//	
//	//public List<Child> getChildrenByReminder(int reminderId) throws ChildDataException;
//
//	public List<Child> getChildrenNotUnderTreatment();
//
//	public void setChildCompletedTreatment(String childId) throws ChildDataException;
//	
//	//public void removeReminder(Child child,Reminder reminder) throws ReminderException, ChildDataException;

	
	
}
