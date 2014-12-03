package org.ird.immunizationreminder.service.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.dao.DAOChild;
import org.ird.immunizationreminder.data.exception.ChildDataException;
import org.ird.immunizationreminder.datamodel.entities.Child;
import org.ird.immunizationreminder.datamodel.entities.Child.STATUS;
import org.ird.immunizationreminder.datamodel.entities.Vaccination;
import org.ird.immunizationreminder.service.ChildService;


public class ChildServiceImpl implements ChildService{
	
	DAOChild pdao;
	private Number LAST_QUERY_TOTAL_ROW__COUNT;
	////DAOReminder r;
	//DAOChildCell pc;

	public ChildServiceImpl(DAOChild pat/*,DAOReminder rem,DAOChildCell pcell*/){
		this.pdao=pat;
	//	r=rem;
		//pc=pcell;
	}
	private void setLASTS_ROWS_RETURNED_COUNT(Number LAST_QUERY_TOTAL_ROW__COUNT) {
		this.LAST_QUERY_TOTAL_ROW__COUNT = LAST_QUERY_TOTAL_ROW__COUNT;
	}
	@Override
	public Number LAST_QUERY_TOTAL_ROW__COUNT() {
		return LAST_QUERY_TOTAL_ROW__COUNT;
	}
	@Override
	public List<Child> getAllChildren(int firstResult, int fetchSize,boolean isreadonly/*,FetchMode armFetchmode*/) {
		List<Child> plst= pdao.getAll(firstResult, fetchSize,isreadonly);
		setLASTS_ROWS_RETURNED_COUNT(pdao.LAST_QUERY_TOTAL_ROW__COUNT());
		return plst;
	}

	@Override
	public boolean isCellNumberAvailable(String cellNumber) throws ChildDataException {
		setLASTS_ROWS_RETURNED_COUNT(0);
		if(cellNumber!=null){
			int cellln=Integer.parseInt(Context.getIRSetting("cellnumber.number-length-without-zero", "10"));
			cellNumber=cellln>cellNumber.length()?cellNumber:cellNumber.substring(cellNumber.length()-cellln);
		}
		return pdao.findByCurrentCellNumber(cellNumber,true)==null;
	}
	@Override
	public List<Child> findChildByCriteria(String partOfFirstOrLastName,
			String clinic, STATUS Status,
			boolean putNotWithTeatmentStatus, String armName, int firstResult,
			int fetchsize,boolean isreadonly/*,FetchMode armFetchmode*/) throws ChildDataException {
		List<Child> plst=pdao.findByCriteria(partOfFirstOrLastName, clinic, Status, putNotWithTeatmentStatus, armName, firstResult, fetchsize,isreadonly);
		setLASTS_ROWS_RETURNED_COUNT(pdao.LAST_QUERY_TOTAL_ROW__COUNT());
		return plst;
	}
	@Override
	public Child getChildbyCurrentCell(String cellNumber,boolean isreadonly/*,FetchMode armFetchmode*/) throws ChildDataException {
		if(cellNumber!=null){
			int cellln=Integer.parseInt(Context.getIRSetting("cellnumber.number-length-without-zero", "10"));
			cellNumber=cellln>cellNumber.length()?cellNumber:cellNumber.substring(cellNumber.length()-cellln);
		}		
		Child p= pdao.findByCurrentCellNumber(cellNumber,isreadonly);
		setLASTS_ROWS_RETURNED_COUNT(pdao.LAST_QUERY_TOTAL_ROW__COUNT());
		return p;
	}
	@Override
	public Child getChildbyChildId(String childId,boolean isreadonly/*,FetchMode armFetchmode*/) throws ChildDataException {
		Child p=pdao.findByChildID(childId,isreadonly);
		setLASTS_ROWS_RETURNED_COUNT(pdao.LAST_QUERY_TOTAL_ROW__COUNT());
		return p;
	}
	@Override
	public Serializable addChild(Child child) throws ChildDataException{
		if(pdao.findByChildID(child.getChildId(),true)!=null){
			throw new ChildDataException(ChildDataException.CHILD_EXISTS);
		}
		List<Child> pat=findByCurrentCellIgnoreInconsistency(child.getCurrentCellNo(),true);
		for (Child p : pat) {
			if(p.getStatus().equals(STATUS.FOLLOW_UP)){
				throw new ChildDataException(ChildDataException.CELL_NUM_OCCUPIED);
			}
		}
		List l=findByEpiOrMrNumber(child.getMrNumber());
		if(l.size()>0){
			throw new ChildDataException(ChildDataException.EPI_NUMBER_EXISTS,"Epi Reg number occupied by "+l.size()+" child(ren)");
		}
		return pdao.save(child);
	}
	@Override
	public void updateChild(Child child){
		pdao.update(child);
	}
	@Override
	public Child mergeUpdateChild(Child child){
		return (Child) pdao.merge(child);
	}
	@Override
	public List<Child> findByCurrentCellIgnoreInconsistency(String currentCellNumber,boolean isreadonly/*,FetchMode armFetchmode*/) {
		if(currentCellNumber!=null){
			int cellln=Integer.parseInt(Context.getIRSetting("cellnumber.number-length-without-zero", "10"));
			currentCellNumber=cellln>currentCellNumber.length()?currentCellNumber:currentCellNumber.substring(currentCellNumber.length()-cellln);
		}	
		List<Child> pl=pdao.findByCurrentCellIgnoreInconsistency(currentCellNumber,isreadonly);
		setLASTS_ROWS_RETURNED_COUNT(pl.size());
		return pl;
	}
	@Override
	public List<Child> findByEpiOrMrNumber(String epiOrMrNumber) {
		List<Child> pl=pdao.findByEpiOrMrNumber(epiOrMrNumber);
		setLASTS_ROWS_RETURNED_COUNT(pl.size());
		return pl;
	}
	/*@Override
	public void addChildReminder(String childId, Reminder reminder) throws ReminderException, ChildDataException {
		
		if(r.getReminder(reminder.getReminderId()).size()== 0 ){
			throw new ReminderException(ReminderException.REMINDER_NOT_EXIST);
		}
		List<Child> l= h.getChildById(childId);
		
		if(l.size()==0){
			throw new ChildDataException(ChildDataException.CHILD_DOESNOT_EXIST);
		}
		Child child=(Child)l.get(0);
		
		child.addReminder(reminder);
		h.updateChild(child);
	}*/
///** cell number must be validated before saving it as this function donot perform cell number validations
// * also it must be made sure that cell number is not occupied by any other child*/
//	@Override
//	public void changeChildCellNum(String childId, String cellnumber) throws ChildDataException {
//
//		List<Child> l= h.getChildById(childId);
//		
//		if(l.size()==0){
//			throw new ChildDataException(ChildDataException.CHILD_DOESNOT_EXIST,ChildDataException.CHILD_DOESNOT_EXIST);
//		}
//		Child child=(Child)l.get(0);
//		CellHistory pcell=new CellHistory(child,cellnumber);
//		pcell.setDateAdded(new Date());
//		pcell.setAddedByUserId(child.getLastEditedByUserId());
//		pcell.setAddedByUserName(child.getLastEditedByUserName());
//		pc.addChildCellRecord(pcell);
//		child.setCellNoLatest(cellnumber);
//		h.updateChild(child);
//	}
//
//	@Override////////////////////////cahnge 4 childId
//	public void createChild(Child child) throws ChildDataException {
//		ChildValidations.validateChild(child);
//		List<Child> l= h.getChildById(child.getChildId());
//		if(l.size()!=0){
//			throw new ChildDataException(ChildDataException.CHILD_EXISTS,ChildDataException.CHILD_EXISTS);
//		}
//		List<Child> l2=h.getChildByCurrentCellNum(child.getCellNoLatest());
//		if(l2.size()!=0){
//			throw new ChildDataException(ChildDataException.CELL_NUM_OCCUPIED,ChildDataException.CELL_NUM_OCCUPIED);
//		}
//		h.addChild(child);
//		
//		CellHistory c=new CellHistory();
//		c.setCellNo(child.getCellNoLatest());
//		c.setChild(h.getChildById(child.getChildId()).get(0));
//		c.setDateAdded(new Date());
//		c.setAddedByUserId(child.getCreatedByUserId());
//		c.setAddedByUserName(child.getCreatedByUserName());
//		pc.addChildCellRecord(c);
//	}
//
//	@Override
//	public List<Child> getAllChildren() {
//		List<Child> pat= h.getAllChildren();
//		return pat;
//	}
//
//	@Override
//	public List<Child> getChildByAnyName(String partOfName) {
//		List<Child> l=h.getChildByFirstName(partOfName);
//		l.addAll(h.getChildByLastName(partOfName));
//		List<Child> l2 =new ArrayList<Child>();
//		try{
//		l2.add(l.get(0));
//		}catch (Exception e) {
//		}
//		for (Child p : l) {
//			if(!l2.contains(p)){
//				l2.add(p);
//			}
//		}
//		return l2;
//	}
//
//	@Override
//	public Child getChildByCurrentCellNum(String cellNum) throws ChildDataException {
//		String cell=cellNum;
//		try{
//		int clen=Integer.parseInt(Context.getIRSetting("cellnumber.number-length-without-zero", "10"));
//		cell=cellNum.substring(cellNum.length()-clen);
//		}catch (Exception e) {
//		}
//		List<Child> l=h.getChildByCurrentCellNum(cell);
//		if(l.size()==0){
//			throw new ChildDataException(ChildDataException.CHILD_DOESNOT_EXIST,ChildDataException.CHILD_DOESNOT_EXIST);
//		}
//		Child p=l.get(0);
//		return p;
//	}
//
//	@Override
//	public List<Child> getChildByDateCompletion(Date date) {
//		List<Child> p=h.getChildByDateCompletion(date);
//		return p;
//	}
//
//	@Override
//	public List<Child> getChildByDateEnrolled(Date date) {
//		List<Child> p=h.getChildByDateEnrolled(date);
//		return p;
//	}
//
//	@Override
//	public List<Child> getChildByFirstName(String firstName) {
//		List<Child> l=h.getChildByFirstName(firstName);
//		return l;
//	}
//
//	@Override
//	public Child getChildById(String id) throws ChildDataException {
//		List<Child> l=h.getChildById(id);
//		if(l.size()==0){
//			throw new ChildDataException(ChildDataException.CHILD_DOESNOT_EXIST,ChildDataException.CHILD_DOESNOT_EXIST);
//		}
//		return l.get(0);
//	}
//
//	@Override
//	public List<Child> getChildByLastName(String lastName) {
//		List<Child> l=h.getChildByLastName(lastName);
//		return l;
//	}
//
//	/*@Override
//	public List<Reminder> getChildReminders(String childID) throws ChildDataException {
//		List<Child> l=h.getChildById(childID);
//		if(l.size()==0){
//			throw new ChildDataException(ChildDataException.CHILD_DOESNOT_EXIST);
//		}
//		
//		Child p=l.get(0);
//		List<Reminder> r=new ArrayList<Reminder>();
//		r.addAll(p.getReminders());
//
//		return r;
//		
//	}*/
//
//	/*@Override
//	public List<Child> getChildrenByReminder(int reminderId) throws ChildDataException {
//		List<Reminder> l=r.getReminder(reminderId);
//		if(l.size()==0){
//			throw new ChildDataException(ReminderException.REMINDER_NOT_EXIST);
//		}
//		Reminder rem=l.get(0);
//		List<Child> p=new ArrayList<Child>();
//		p.addAll(rem.getChild());
//		return p;
//		
//	}*/
//
//	@Override
//	public List<Child> getChildrenCurrentlyUnderTreatment() {
//		List<Child> p=h.getChildrenCurrentlyUnderTreatment();
//		return p;
//	}
//
//	@Override
//	public boolean isChildUnderTreatment(String childId) throws ChildDataException {
//		List<Child> l=h.getChildById(childId);
//		if(l.size()==0){
//			throw new ChildDataException(ChildDataException.CHILD_DOESNOT_EXIST,ChildDataException.CHILD_DOESNOT_EXIST);
//		}
//		Child p=l.get(0);
//		return p.isUnderTreatment();
//	}
//	
//	@Override
//	public List<Child> getChildrenUsedCellNum(String cell) {
//		List<CellHistory> l=pc.searchCellhistorybyCell(cell);
//		List<Child> p=new ArrayList<Child>();
//		if(l.size()!=0){
//			for (CellHistory c : l) {
//				Child pat=h.getChildById(c.getChild().getChildId()).get(0);
//				if(!p.contains(pat)){
//					p.add(pat);
//				}
//			}
//		}
//		return p;
//	}
//
//	@Override
//	public boolean hasChildUsedCellNum(String childId, String cellNum) {
//		List<CellHistory> l=pc.searchCellhistorybyCell(cellNum);
//		if(l.size()!=0){
//			for (CellHistory c : l) {
//				if(c.getChild().getChildId().compareTo(childId)==0){
//					return true;
//				}
//			}
//		}
//		return false;
//	}
//
//	@Override
//	public boolean isChildUsingCellNum(String childId, String cellNum) throws ChildDataException {
//		List<Child> p=h.getChildById(childId);
//		if(p.size()==0){
//			throw new ChildDataException(ChildDataException.CHILD_DOESNOT_EXIST,ChildDataException.CHILD_DOESNOT_EXIST);
//		}
//		return p.get(0).getCellNoLatest().compareTo(cellNum)==0;
//	}
//
//
//	public List<Child> getChildrenNotUnderTreatment(){
//		List<Child> p=h.getChildNotUnderTreatment();
//		return p;
//	}
//	
//	@Override
//	public void setChildCompletedTreatment(String child) throws ChildDataException{
//		List<Child> p=h.getChildById(child);
//		if(p.size()==0){
//			throw new ChildDataException(ChildDataException.CHILD_DOESNOT_EXIST,ChildDataException.CHILD_DOESNOT_EXIST);
//		}
//		Child pat=p.get(0);
//		pat.setChildStatus(Status.COMPLETED_TREATMENT);
//		h.updateChild(pat);
//	}
//
//	@Override
//	public void setChildNotUnderTreatment(String childId) throws ChildDataException {
//		List<Child> p=h.getChildById(childId);
//		if(p.size()==0){
//			throw new ChildDataException(ChildDataException.CHILD_DOESNOT_EXIST,ChildDataException.CHILD_DOESNOT_EXIST);
//		}
//		Child pat=p.get(0);
//		pat.setChildStatus(Status.NOT_UNDER_TREATMENT);
//		h.updateChild(pat);
//	}
//
//	@Override
//	public void updateChild(Child child) {
//		h.updateChild(child);
//	}
//
//	@Override
//	public List<CellHistory> searchCellRecord(String cellNum) {
//		String cell=cellNum;
//		try{
//		int clen=Integer.parseInt(Context.getIRSetting("cellnumber.number-length-without-zero", "10"));
//		cell=cellNum.substring(cellNum.length()-clen);
//		}catch (Exception e) {
//		}
//		return pc.searchCellhistorybyCell(cell);
//	}
//
//	@Override
//	public List<CellHistory> searchCellRecord(Date datesmall,Date dategreater) {
//		return pc.searchCellHistory(datesmall,dategreater);
//	}
//
//	@Override
//	public List<CellHistory> searchCellRecord(String patinetIdentifier,
//			String cellNum) throws ChildDataException {
//		String cell=cellNum;
//		try{
//		int clen=Integer.parseInt(Context.getIRSetting("cellnumber.number-length-without-zero", "10"));
//		cell=cellNum.substring(cellNum.length()-clen);
//		}catch (Exception e) {
//		}
//		List<Child> p=h.getChildById(patinetIdentifier);
//		if(p.size()==0){
//			throw new ChildDataException(ChildDataException.CHILD_DOESNOT_EXIST,ChildDataException.CHILD_DOESNOT_EXIST);
//		}
//		Child pat=p.get(0);
//		return pc.searchCellHistory(pat.getpId(), cell);
//	}
//
//	@Override
//	public List<CellHistory> searchCellRecord(String cellNum, Date datesmall,Date dategreater) {
//		String cell=cellNum;
//		try{
//		int clen=Integer.parseInt(Context.getIRSetting("cellnumber.number-length-without-zero", "10"));
//		cell=cellNum.substring(cellNum.length()-clen);
//		}catch (Exception e) {
//		}
//		return pc.searchCellHistory(cell, datesmall,dategreater);
//	}
//
//	@Override
//	public List<CellHistory> searchCellRecord(String patinetIdentifier,
//			String cellNum, Date datesmall,Date dategreater) throws ChildDataException {
//		String cell=cellNum;
//		try{
//		int clen=Integer.parseInt(Context.getIRSetting("cellnumber.number-length-without-zero", "10"));
//		cell=cellNum.substring(cellNum.length()-clen);
//		}catch (Exception e) {
//		}
//		List<Child> p=h.getChildById(patinetIdentifier);
//		if(p.size()==0){
//			throw new ChildDataException(ChildDataException.CHILD_DOESNOT_EXIST,ChildDataException.CHILD_DOESNOT_EXIST);
//		}
//		Child pat=p.get(0);
//		return pc.searchCellHistory(pat.getpId(),datesmall,dategreater,cell);
//	}
//
//	@Override
//	public List<CellHistory> searchCellRecordByChild(String patinetIdentifier)
//			throws ChildDataException {
//		List<Child> p=h.getChildById(patinetIdentifier);
//		if(p.size()==0){
//			throw new ChildDataException(ChildDataException.CHILD_DOESNOT_EXIST,ChildDataException.CHILD_DOESNOT_EXIST);
//		}
//		Child pat=p.get(0);
//		return pc.searchCellHistory(pat.getpId());
//	}
//
//	@Override
//	public List<CellHistory> searchCellRecordByChild(
//			String patinetIdentifier, Date datesmall,Date dategreater) throws ChildDataException {
//		
//		List<Child> p=h.getChildById(patinetIdentifier);
//		if(p.size()==0){
//			throw new ChildDataException(ChildDataException.CHILD_DOESNOT_EXIST,ChildDataException.CHILD_DOESNOT_EXIST);
//		}
//		Child pat=p.get(0);
//		return pc.searchCellHistory(pat.getpId(), datesmall,dategreater);
//		
//	}
//
//	@Override
//	public List<CellHistory> searchCellRecord() {
//		return pc.getAllDataFromChildCell();
//	}
//
//	@Override
//	public List<Child> getChildrenHavingStatus(
//			Status status) {
//		return h.getChildrenHavingStatus(status);
//	}
//
//
//	/*@Override
//	public void removeReminder(Child child, Reminder reminder) throws ReminderException, ChildDataException {
//		
//		if(r.getReminder(reminder.getReminderId()).size()== 0 ){
//			throw new ReminderException(ReminderException.REMINDER_NOT_EXIST);
//		}
//		
//		child.removeReminder(reminder);
//		h.updateChild(child);
//
//	}*/

}
