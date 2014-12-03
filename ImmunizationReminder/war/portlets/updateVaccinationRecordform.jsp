<%@page import="org.ird.immunizationreminder.datamodel.entities.Vaccination.VACCINATION_STATUS"%>
<%@page import="org.ird.immunizationreminder.datamodel.entities.Vaccination"%>

<%@page import="org.ird.immunizationreminder.datamodel.entities.Child"%>
<%@page import="org.ird.immunizationreminder.web.utils.IMRGlobals"%>

<script type='text/javascript' src='/ImmunizationReminder/dwr/interface/DWRVaccineService.js'></script>
<script type='text/javascript' src='/ImmunizationReminder/dwr/engine.js'></script>
<script type='text/javascript' src='/ImmunizationReminder/dwr/util.js'></script>
<script type="text/javascript">
<!--
var globalDf="<%=IMRGlobals.GLOBAL_DATE_FORMAT%>";

function checkClicked() {
	clearNextVaccination();
	if(!document.getElementById("isLastVaccination").checked){
		document.getElementById("nextVaccinationRecordDiv").style.display="table";
		return;
	}
	alert('Marking current record as last vaccination will flag child as completed followup. You will not be able to add any other vaccination in future any more. Uncheck if you donot agree.');
	document.getElementById("nextVaccinationRecordDiv").style.display="none";
}
function systemCalculatedCheckClicked() {
	clearNextVaccination();
	if(!document.getElementById("systemCalculated").checked){
		document.getElementById("nextVaccinationRecordDiv").style.display="table";
		return;
	}
	document.getElementById("nextVaccinationRecordDiv").style.display="none";
}
function clearNextVaccination(){
	document.getElementById("nextAssignedDate").value='';
	var radioObj=document.frm.nextVaccineName;
	if(radioObj){
		var radioLength = radioObj.length;
		for(var i = 0; i < radioLength; i++) {
			if(radioObj[i].checked) {
				radioObj[i].checked=false;
				break;
			}
		}
	}
	document.getElementById("nextVaccDescription").value='';
}

function subfrm(){
	var vaccname=document.getElementById("vaccineName").value;
	if(vaccname==null ||vaccname==''){
		alert('Please select vaccine that was given today.');
		return;
	}
	if(document.getElementById("vaccinationDate").value==null
			||document.getElementById("vaccinationDate").value==''){
		alert('You must specify a vaccination date');
		return;
	}
	if(makeDateFromString(document.getElementById("vaccinationDate").value)-scwDateNow>0){
		alert('Vaccination date cannot be after todays date');
		return;
	}
	if(document.getElementById("vaccineName").value==getCheckedValue(document.frm.nextVaccineName)){
		if(!confirm('Same vaccine name is specified for current and next vaccination. Ignore it ?')){
			return;
		}
	}

	if(!document.getElementById("isLastVaccination").checked && (document.getElementById("systemCalculated")==null ||!document.getElementById("systemCalculated").checked)){
		if(document.getElementById("nextAssignedDate").value==null
				||document.getElementById("nextAssignedDate").value==''){
			alert('You must specify a next vaccination date by selecting a vaccine or mark it as LastVaccination');
			return;
		}
		if(!verifyTimeValues()){
			return;
		}

		if(makeDateFromString(document.getElementById("nextAssignedDate").value)-scwDateNow<0){
			if(!confirm('Next assigned date was found before todays date. No reminder will be sent out for next vaccination. Do you still want to continue ?')){
				return;
			}
		}
		var currVdate=Date.parseExact(document.getElementById("vaccinationDate").value,globalDf);
		var vaccDuedate=Date.parseExact(document.getElementById("dueDate").value,globalDf);
		var currVdateValid=vaccDuedate.add(-3).days();

		if(currVdateValid.compareTo(currVdate) == 1){
			alert('Vaccination date can only be 3 days before due date.');
			return;
		}
		var nextassgdt=Date.parseExact(document.getElementById("nextAssignedDate").value,globalDf);

		var dateValid=nextassgdt.add(-4).days();
		var nowdate=Date.parseExact(scwDateNow.scwFormat(scwDateDisplayFormat),globalDf);
	
		if(nowdate.compareTo(dateValid) == 1){
			if(!confirm('Next assigned date was found too close to todays date. All reminder will not be sent out for next vaccination. Do you still want to continue ?')){
				return;
			}
		}
	}
	var selSt=document.getElementById("vaccinationStatus");
	for (var i=0; i<selSt.options.length; i++) {
		if (selSt.options[i].selected) {
			if(selSt.options[i].value=="<%=Vaccination.VACCINATION_STATUS.PENDING.name()%>"){
				alert('Vaccination status should be changed from pending to new one');
				return;
			}	        
		}
	}
	document.getElementById("frm").submit();
}
var afterVaccinationDateChanged=function (obj) {
	if(getCheckedValue(document.frm.nextVaccineName)!=''){
		var vaccinationdate=Date.parseExact(obj.value,globalDf);
		//vaccine that would be selected will be confirmed so whatever here is we should continue
			
		setAndCalculateNextAssignedDate();
	}
};

function convertToDate(stringDate) {
	try{
		var datec=Date.parseExact(stringDate,globalDf);
		
		return datec;
	}
	catch (e) {
		alert(e);
		return null;
	}
}

function setAndCalculateNextAssignedDate() {
	var vaccinationdate=convertToDate(document.getElementById("vaccinationDate").value);
	var birthdate=convertToDate(document.getElementById("birthdate").value);
	//var nextassigneddate=vaccinationdate.add(gapInVacc*7).days();
	if(getCheckedValue(document.frm.nextVaccineName).toLowerCase().indexOf('measles2') != -1){
		 DWRVaccineService.calculateMeasles2DateFromMeasles1(
				 birthdate
				,vaccinationdate
				,{
				async: false,
				callback:function (recievedvcdate) {
					document.getElementById("nextAssignedDate").value=recievedvcdate;
					afterDateSelected(document.getElementById("nextAssignedDate"));
				}
		}); 
	}
	else{
		DWRVaccineService.calculateNextVaccinationDateFromPrev(
				vaccinationdate
				,getCheckedValue(document.frm.nextVaccineName) 
				,true
				,{
				async: false,
				callback:function (recievedvcdate) {
					document.getElementById("nextAssignedDate").value=recievedvcdate;
					afterDateSelected(document.getElementById("nextAssignedDate"));
				}
		});
	}
		//document.getElementById("nextAssignedDate").value=nextassigneddate.toString(globalDf);
		//afterDateSelected(document.getElementById("nextAssignedDate"));

}
//-->
</script>
<form method="post" id="frm" name="frm">
<c:if test="${session_expired == true}">
<c:redirect url="login.htm"></c:redirect>
</c:if>
<div id="childArmInfo">
	
</div>
<br>
<br>
<span class="warning-message">${errorMessage}</span>
<span class="error-message">${errorMessagev}</span>
<div align="center">
<table class="addOrEditTable">
        <tr>
            <td>
                        	<div><span  class="error" style="font-size: small;color: red">
                            <spring:bind path="command">
                            <c:forEach items="${status.errorMessages}" var="error">
                             Error code: <c:out value="${error}"/><br> 
                             </c:forEach> 
                            </spring:bind>
                            </span>
                            </div>

                	<table border="0" width="100%">
                        <tr>
                            <td>Child (Id : Name)</td>
                            <td>
                            <div >${child.childId} : ${child.firstName} ${child.middleName} ${child.lastName}</div>
                            <input type="hidden" id="childId" value="${child.childId}">
                            <input type="hidden" id="birthdate" value="<fmt:formatDate value='${child.birthdate}' pattern = '<%=IMRGlobals.GLOBAL_DATE_FORMAT%>' />">
                            <input type="hidden" id="vaccinationRecordNumber" value="${command.vaccinationRecordNum}">
                            
                            </td>
                            <td colspan="2">
               
                            <div><br>
                   			<span style="color: maroon;">Previous Vaccination Details</span><br><br>
                   			<table>
                   			<tr>
                   			<td>Vaccination Record Num    </td><td>${previous_vaccination.vaccinationRecordNum}</td>
                   			</tr>
                   			<tr>
                   			<td>Vaccine                   </td><td>${previous_vaccination.vaccine.name}</td>
                   			</tr>
                   			<tr>
                   			<td>Description               </td><td>${previous_vaccination.description}</td>
                   			</tr>
                   			<tr>
                   			<td>Vaccination Due Date      </td><td>${previous_vaccination.vaccinationDuedate}</td>
                   			</tr>
                   			<tr>
                   			<td>Vaccination Date          </td><td>${previous_vaccination.vaccinationDate}</td>
                   			</tr>
                   			<tr>
                   			<td>Vaccination Status        </td><td>${previous_vaccination.vaccinationStatus}</td>
                   			</tr>
                   			<tr>
                   			<td>Previous Vaccination      </td><td>${previous_vaccination.previousVaccinationRecordNum}</td>
                   			</tr>
                            <tr>
                   			<td>Next assigned date        </td><td>${previous_vaccination.nextAssignedDate}</td>
                    		</tr>
                    		</table>
                    		</div>
                    		</td>
                        </tr>
                        <tr>
			                <td>Vaccine</td>
			                <td>     
			                <spring:bind path="command.vaccine">
			                <input type="text" readonly="readonly" id="vaccineName" name="name" value="${command.vaccine.name}">
			                </spring:bind>
			                <%-- <select id="vaccineName" name="vaccineName" onchange="currentVaccineChanged(this);">
			                	<option></option>
			                	<c:forEach  items="${vaccine}" var="vacc">
			                		<option title="${vacc.gapInWeeksFromPreviousVaccine}">${vacc.name}</option>
			                	</c:forEach>               
			                </select>
			                <script><!--
                            sel = document.getElementById("vaccineName");
                            val=document.getElementById("vaccineNameVal").value;
                            makeTextSelectedInDD(sel,val);
                            //-->
                            </script> --%>
			                </td>
                            <td></td>
                            <td></td>
                        </tr><!--
                        <tr>
                            <td>Child brought by  : </td>
                            <td>
                            <spring:bind path="command.childBroughtBy">
                            <input type="text" name="childBroughtBy" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
                            <td>Child vaccinated by : </td>
                            <td>
                            <spring:bind path="command.childVaccinatedBy">
                            <input type="text" name="childVaccinatedBy" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
                        </tr>
                        -->
                         <tr>
                            <td>Due Date  : </td>
                            <td>
                            <input id ="dueDate" readonly="readonly" type="text" value="<fmt:formatDate value="${command.vaccinationDuedate}" pattern = "dd-MM-yyyy" />"/><br/>
           					</td>
                            <td></td>
                            <td>
           					</td>
                        </tr>
                        <tr>
                            <td>Vaccination Status : </td>
                            <td>
                            <spring:bind path="command.vaccinationStatus">
                            <input type="hidden" value="${status.value}" id="vstVal"/>
                            <select id="vaccinationStatus" name="vaccinationStatus">
                            <c:forEach items="<%=Vaccination.VACCINATION_STATUS.values()%>" var="vstatus">
                            <option value="${vstatus}">${vstatus}:${vstatus.REPRESENTATION}</option>
                            </c:forEach>
                            </select>
                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					<br/>
                            <script><!--
                            sel = document.getElementById("vaccinationStatus");
                            val=document.getElementById("vstVal").value;
                            makeTextSelectedInDD(sel,val);
							//-->
                            </script>
                            </td>
           					<td><!--Vaccine Dose: --></td>
                            <td>
                            <!--<spring:bind path="command.dose">
                            <input type="text" name="dose" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					--></td>
           				</tr>
                         <tr>
                            <td>Description  : </td>
                            <td>
                            <spring:bind path="command.description">
                            <textarea name="description" rows="6" cols="30"  >${status.value}</textarea><br/>
                                <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
                            <td></td>
                            <td></td>                           
                        </tr>
                        <tr>
                            <td>Vaccination Date: </td>
                            <td>
                            <spring:bind path="command.vaccinationDate">
                            <input id="vaccinationDate" name="vaccinationDate" readonly="readonly" onclick="scwNextAction = afterVaccinationDateChanged.runsAfterSCW(this,document.getElementById('vaccinationDate'));scwShow(document.getElementById('vaccinationDate'),event);" value="<c:out value="${status.value}"/>"/>
                            <input onclick="scwNextAction = afterVaccinationDateChanged.runsAfterSCW(this,document.getElementById('vaccinationDate'));scwShow(document.getElementById('vaccinationDate'),event);" size="5" style="cursor:pointer;width: 16px;height:16px;background-image: url('images/calendar_icon.png');"/>
                            <br/>
                          	<span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
                            <td>Is last vaccination ? </td>
                            <td>
                            <spring:bind path="command.isLastVaccination">
			   				<input type="checkbox" id="isLastVaccination" name="isLastVaccination"  onclick="checkClicked();"/>
                          	<span class="error-message"><c:out value="${status.errorMessage}"/></span>
                            </spring:bind>
                            </td>
                        </tr>
                </table>
                	<div style="font-size: 30px;font-stretch: expanded;font-style: italic; color: green">
                     <span style="text-align: center;">*********************************************************************</span>
                     <br><br>Next Vaccination Details
                     <br> 
                     </div>
                     <c:if test="${(fn:containsIgnoreCase(command.vaccine.name,'Penta3')) || (fn:containsIgnoreCase(command.vaccine.name,'Measles2'))}">
                     <div style="font-size:medium; ;font-style: italic; color: maroon;">
                     <input type="checkbox" id="systemCalculated" name="systemCalculated"  onclick="systemCalculatedCheckClicked();"/>Cannot be specified, let it be system calculated.
			         </div>
			         </c:if>
                     <div id="nextVaccinationRecordDiv" >
                     <table width="100%">
                        <tr>
                            <td>Next Vaccination Date: </td>
                            <td>
                            <spring:bind path="command.nextAssignedDate">
                            <input id="nextAssignedDate" name="nextAssignedDate" value="<c:out value="${status.value}"/>"/>
                            <br/>
                          	<span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
							<td></td>
							<td></td>
                        </tr>
						<tr>
			                <td>Select Vaccine</td>
			                <td>
			                <table class="innerDataDisplayTable">
								<thead >
									<tr>
									<th>Vaccine name</th>
									<th>Gap from prev<br>vaccine (weeks)</th>
									</tr>
								</thead>
								<c:forEach  items="${vaccine}" var="vacc">
			                		<tr>
			                			<td><input type="radio"  name="nextVaccineName" value="${vacc.name}" title="${vacc.gapInWeeksFromPreviousVaccine}" onchange="nextVaccineChanged(this);"/>${vacc.name}</td>
			                			<td>${vacc.gapInWeeksFromPreviousVaccine}</td>
			                		</tr>
			                	</c:forEach>
							</table>   
			                <!--<select id="nextVaccineName" name="nextVaccineName">
			                	<c:forEach  items="${vaccine}" var="vacc">
			                		<option>${vacc.name}</option>
			                	</c:forEach>               
			                </select>-->
<script type="text/javascript">
<!--  
/* function currentVaccineChanged(obj){
	var gapInVacc=parseInt(obj.options[obj.selectedIndex].title);
	if(gapInVacc<0){
		if(!confirm('No gap-from-previous-vaccine is defined. Seems vaccine isnt included in study. Are you sure to change vaccine ?')){
			makeTextSelectedInDD(document.getElementById("vaccineName"),document.getElementById("vaccineNameVal").value);
			return;
		}
	}
	DWRVaccineService.hasPatientTakenVaccine(document.getElementById("childId").value,obj.value,{
		async: false,
		callback:function (recievedvc) {
		var recievedvaccmsg=recievedvc;

		if(!isNumber(recievedvaccmsg) && recievedvaccmsg.toLowerCase()!='no'){//parse recievedvaccmsg.toLowerCase()=='yes'){
			alert("An Error Occurred while getting vaccination details , message is :"+recievedvaccmsg);
			makeTextSelectedInDD(document.getElementById("vaccineName"),document.getElementById("vaccineNameVal").value);
			return;
		}

		if(recievedvaccmsg.toLowerCase()!='no' && Number(recievedvaccmsg)!=Number(document.getElementById("vaccinationRecordNumber").value)){//parse recievedvaccmsg.toLowerCase()=='yes'){
			if(!confirm('Child have recieved vaccine. Do you still want to choose this vaccine ?')){
				makeTextSelectedInDD(document.getElementById("vaccineName"),document.getElementById("vaccineNameVal").value);
				return;
			}
		}
		}
	});
} */
function nextVaccineChanged(obj){
	if(document.getElementById("vaccinationDate").value==null
			||document.getElementById("vaccinationDate").value==''){
		alert('Specify a vaccination date first');
		obj.checked=false;
		return;
	}
	var gapInVacc=parseInt(obj.title);
	if(gapInVacc<0){
		if(!confirm('No gap-from-previous-vaccine is defined. Seems vaccine isnot included in study. Are you sure to change vaccine ?'))
		{
			obj.checked=false;
			document.getElementById("nextAssignedDate").value='';
			return;
		}
	}
	DWRVaccineService.hasPatientTakenVaccine(document.getElementById("childId").value,obj.value,{
							async: false,
		   					callback:function (recievedvc) {
		   							var recievedvaccmsg=recievedvc;

		   							if(!isNumber(recievedvaccmsg) && recievedvaccmsg.toLowerCase()!='no'){//parse recievedvaccmsg.toLowerCase()=='yes'){
		   								alert("An Error Occurred while getting vaccination details , message is :"+recievedvaccmsg);
		   								obj.checked=false;
		   								document.getElementById("nextAssignedDate").value='';
		   								return;
		   							}

		   							if(recievedvaccmsg.toLowerCase()!='no' && Number(recievedvaccmsg)!=Number(document.getElementById("vaccinationRecordNumber").value)){//parse recievedvaccmsg.toLowerCase()=='yes'){
		   								if(!confirm('Child have recieved vaccine. Do you still want to choose this vaccine ?')){
		   									obj.checked=false;
		   									document.getElementById("nextAssignedDate").value='';
		   									return;
		   								}
		   							}
		   							setAndCalculateNextAssignedDate();
		   					}
							});
}
//-->
</script>
			                </td>
							<td></td>
							<td></td>
						</tr>                        
						<tr>
							<td>Description</td>
							<td><textarea  id="nextVaccDescription" name="nextVaccDescription" rows="6" cols="30"  ></textarea>
                            </td>
							<td></td>
							<td></td>
						</tr>
						<c:forEach items="${arm.armday}" var="armdaynum">
                        <tr>
                        	<td>Day ${armdaynum.id.dayNumber} Reminder Time (hh:mm:ss) (16:23:32)</td>
                        	<c:choose>
                        	<c:when test="${armdaynum.isDefaultTimeEditable}">
                        	<td><input type="text" name="time${armdaynum.id.dayNumber}" value="${armdaynum.defaultReminderTime}"/></td>
                        	</c:when>
                        	<c:otherwise>
                        	<td><input type="text" name="time${armdaynum.id.dayNumber}" readonly="readonly" value="${armdaynum.defaultReminderTime}"/></td>
                        	</c:otherwise>
                        	</c:choose>
							<td></td>
							<td></td>                        	
                        </tr>						
						</c:forEach>
				
                    </table>                      
					</div>
			</td>
		</tr>
        <tr>
            <td>                        
            <br>
            </td>
        </tr>
        <tr>
        <td>
        <input id="submitBtn" type="button" value="submit and proceed to next step" title="submit and proceed to next step" onclick="subfrm();" style="width: 400px;">
        <%
        boolean shouldEnablevacc=true;
		Object shouldEnable= request.getAttribute("shouldenableVaccination");
		if(shouldEnable!=null){
			shouldEnablevacc=(Boolean)shouldEnable;
		}
		%>
        <script type="text/javascript">
        <!--
			if(<%=shouldEnablevacc%>){
				document.getElementById("submitBtn").disabled=false;
			} else {
				document.getElementById("submitBtn").disabled=true;
			}
        //-->
        </script>
        </td>
        </tr>
</table>
</div>
</form>
<script><!--
	var frmvalidator  = new Validator("frm");

	frmvalidator.addValidation("childBroughtBy","alnum_s","child brought property value must only be alphabetic");
	frmvalidator.addValidation("childVaccinatedBy","alnum_s","child vaccinated property value must only be alphabetic");

	//-->
</script>