package org.ird.immunizationreminder.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.FetchMode;
import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.dao.DAOResponse;
import org.ird.immunizationreminder.datamodel.entities.Response;
import org.ird.immunizationreminder.datamodel.entities.Response.RESPONSE_TYPE;
import org.ird.immunizationreminder.service.ResponseService;
import org.ird.immunizationreminder.utils.date.DateUtils;

public class ResponseServiceImpl implements ResponseService{

	DAOResponse daopr;
	private Number LAST_QUERY_TOTAL_ROW__COUNT;
	public ResponseServiceImpl(DAOResponse daopr){
		this.daopr=daopr;
	}
	private void setLASTS_ROWS_RETURNED_COUNT(Number LAST_QUERY_TOTAL_ROW__COUNT) {
		this.LAST_QUERY_TOTAL_ROW__COUNT = LAST_QUERY_TOTAL_ROW__COUNT;
	}
	@Override
	public Number LAST_QUERY_TOTAL_ROW__COUNT() {
		return LAST_QUERY_TOTAL_ROW__COUNT;
	}
	@Override
	public List<Response> getAllResponseRecord(int firstResult,int fetchSize,boolean isreadonly,FetchMode childFetchMode) {
		List<Response> resplst=daopr.getAll(firstResult, fetchSize,isreadonly,childFetchMode);
		setLASTS_ROWS_RETURNED_COUNT(daopr.LAST_QUERY_TOTAL_ROW__COUNT());
		return resplst;
	}

	@Override
	public List<Response> getByCriteriaIncludeName(String partOfFirstOrLastName,Date begindate
					,Date enddate,String cellNumber, String armName, RESPONSE_TYPE responseType, int firstResult, int fetchsize,boolean isreadonly,FetchMode childFetchMode) {
		if(begindate!=null){
			begindate=DateUtils.truncateDatetoDate(begindate);
		}
		if(enddate!=null){
			enddate=DateUtils.roundoffDatetoDate(enddate);
		}

		if(cellNumber!=null){
			int cellln=Integer.parseInt(Context.getIRSetting("cellnumber.number-length-without-zero", "10"));
			cellNumber=cellln>cellNumber.length()?cellNumber:cellNumber.substring(cellNumber.length()-cellln);
		}
		List<Response> resplst=daopr.findByCriteriaIncludeName(partOfFirstOrLastName, begindate, enddate
				, cellNumber,armName, responseType, firstResult, fetchsize,isreadonly,childFetchMode);
		setLASTS_ROWS_RETURNED_COUNT(daopr.LAST_QUERY_TOTAL_ROW__COUNT());
		return resplst;
	}
//if child id is given arm name doesnt matter
	@Override
	public List<Response> getByCriteriaIncludeChildId(String child_id
			,Date begindate,Date enddate,String cellNumber, RESPONSE_TYPE responseType, int firstResult, int fetchsize,boolean isreadonly,FetchMode childFetchMode) {
		if(begindate!=null){
			begindate=DateUtils.truncateDatetoDate(begindate);
		}
		if(enddate!=null){
			enddate=DateUtils.roundoffDatetoDate(enddate);
		}
		if(cellNumber!=null){
			int cellln=Integer.parseInt(Context.getIRSetting("cellnumber.number-length-without-zero", "10"));
			cellNumber=cellln>cellNumber.length()?cellNumber:cellNumber.substring(cellNumber.length()-cellln);
		}
		List<Response> resplst=daopr.findByCriteria(child_id, begindate, enddate, cellNumber, responseType, firstResult, fetchsize,isreadonly,childFetchMode);
		setLASTS_ROWS_RETURNED_COUNT(daopr.LAST_QUERY_TOTAL_ROW__COUNT());
		return resplst;
	}

	@Override
	public Serializable addResponseRecord(Response response) {
		return daopr.save(response);
	}
	@Override
	public void updateResponseRecord(Response response) {
		daopr.update(response);
	}
	@Override
	public List<Response> findByVaccinationRecord(long vacciantionRecordNum,boolean isreadonly,FetchMode childFetchMode) {
		List<Response> resplst=daopr.findByVaccinationRecord(vacciantionRecordNum,isreadonly,childFetchMode);
		setLASTS_ROWS_RETURNED_COUNT(resplst.size());
		return resplst;
	}
}
