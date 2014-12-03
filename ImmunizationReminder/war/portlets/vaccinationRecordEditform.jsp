<%@page	import="org.ird.immunizationreminder.datamodel.entities.Vaccination.VACCINATION_STATUS"%>
<%@page	import="org.ird.immunizationreminder.datamodel.entities.Vaccination"%>
<%@page import="org.ird.immunizationreminder.datamodel.entities.Child"%>
<%@page import="org.ird.immunizationreminder.web.utils.IMRGlobals"%>


<%@page import="org.ird.immunizationreminder.datamodel.entities.Vaccination.VACCINATION_STATUS"%>
<script type='text/javascript'	src='/ImmunizationReminder/dwr/interface/DWRVaccineService.js'></script>
<script type='text/javascript' src='/ImmunizationReminder/dwr/engine.js'></script>
<script type='text/javascript' src='/ImmunizationReminder/dwr/util.js'></script>
<script type="text/javascript">
<!--
var globalDf="<%=IMRGlobals.GLOBAL_DATE_FORMAT%>";

window.onbeforeunload = function (e) {
	  var e = e || window.event;
	  // For IE and Firefox
	  if (e) {
	    e.returnValue = '';
	  }
	  // For Safari
	  return '';
};

function subfrm(){
	var vaccname=document.getElementById("vaccineName").options.length;
	if(vaccname==0){
		alert('Vaccine list was found to be empty. You cannot continue. Check database connections.');
		return;
	}
	if(document.getElementById("vaccineName").value==''){
		alert('Please select a vaccine name.');
		return;
	}
	if(document.getElementById("vaccinationDuedate").value==null
			||document.getElementById("vaccinationDuedate").value==''){
		alert('You must specify a vaccination due date');
		return;
	}
	
	if(!verifyTimeValues()){
		return;
	}
	document.getElementById("frm").submit();
}
var afterVaccinationDateSelected=function (obj) {
	var vaccineDD=document.getElementById("vaccineName");
	if(document.getElementById("currnextvaccinationstatus").innerHTML == 
							"<%=Vaccination.VACCINATION_STATUS.PENDING.toString()%>"){
		var date1=Date.parseExact(obj.value,globalDf);

		setAndCalculateNextAssignedDate();
		}
};
function verifyTimeValues() {
	var inputs = document.getElementsByTagName('input');

	for (var i=0; i < inputs.length; i++)
	{
	   if (inputs[i].getAttribute('type') == 'text')
	   {
		   var reg = /^time([0-9_\-\.])+/;
		   if(reg.test(inputs[i].name)==true){
			   var reg2 = /^([0-1][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]/;
				if(reg2.test(inputs[i].value)==false){
					alert(inputs[i].name+" is not a valid time value");
					return false;
				}
		   }
	   }
	}
	return true;
}

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
					alert('Next vaccintion duedate will be changed to '+recievedvcdate.toString(globalDf));
					document.getElementById("currnextvaccinationduedate").innerHTML=
						"<span style=\"text-decoration: line-through\">"+document.getElementById("currnextvaccinationduedate").innerHTML+"</span>";
					document.getElementById("editednextvaccinationduedate").innerHTML="***"+recievedvcdate.toString(globalDf);
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
					alert('Next vaccintion duedate will be changed to '+recievedvcdate.toString(globalDf));
					document.getElementById("currnextvaccinationduedate").innerHTML=
						"<span style=\"text-decoration: line-through\">"+document.getElementById("currnextvaccinationduedate").innerHTML+"</span>";
					document.getElementById("editednextvaccinationduedate").innerHTML="***"+recievedvcdate.toString(globalDf);

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
<div id="childArmInfo"></div>
<span class="error-message">${errorMessage}</span> 
<span class="error-message">${errorMessagev}</span>
<table class="addOrEditTable" width="100%">
	<tr>
		<td>
		<div><span class="error" style="font-size: small; color: red;">
		<spring:bind path="command">
			<c:forEach items="${status.errorMessages}" var="error">
            	Error code: <c:out value="${error}" />
				<br>
			</c:forEach>
		</spring:bind></span>
		</div>
		<table width="100%" border="1px;">
			<tr>
				<td>
				<c:choose>
				<c:when test="${not empty previous_vaccination}">
				<div>
				<span style="font-size: medium;color: maroon;">Previous Vaccination Details</span><br>
				<br>
				<table width="100%" border="1px;">
					<tr>
						<td>Vaccination Record Num</td>
						<td>${previous_vaccination.vaccinationRecordNum}</td>
					</tr>
					<tr>
						<td>Vaccine</td>
						<td>${previous_vaccination.vaccine.name}</td>
					</tr>
					<tr>
						<td>Description</td>
						<td>${previous_vaccination.description}</td>
					</tr>
					<tr>
						<td>Vaccination Due Date</td>
						<td>${previous_vaccination.vaccinationDuedate}</td>
					</tr>
					<tr>
						<td>Vaccination Date</td>
						<td>${previous_vaccination.vaccinationDate}</td>
					</tr>
					<tr>
						<td>Vaccination Status</td>
						<td>${previous_vaccination.vaccinationStatus}</td>
					</tr>
					<tr>
						<td>Previous Vaccination</td>
						<td>${previous_vaccination.previousVaccinationRecordNum}</td>
					</tr>
					<tr>
						<td>Next assigned date</td>
						<td>${previous_vaccination.nextAssignedDate}</td>
					</tr>
				</table>
				</div>
				</c:when>
				<c:otherwise>
					<div style="font-size: medium;color: maroon;">No previous vaccination record exists.</div>
				</c:otherwise>
				</c:choose>
				</td>
			</tr>
		</table>
		<br><br>
		<span style="font-size: medium;color: maroon;">Current Vaccination Details</span><br>
		<table border="1" width="100%">
			<tr>
				<td>Child (Id : Name) <input type="hidden" id="childId"	value="${child.childId}">
				<input type="hidden" id="birthdate" value="<fmt:formatDate value='${child.birthdate}' pattern = '<%=IMRGlobals.GLOBAL_DATE_FORMAT%>' />">
				<input type="hidden" id="vaccinationRecordNumber" value="${command.vaccinationRecordNum}">
				</td>
				<td>
				<div style="font-size: medium; color: black">${child.childId} : ${child.firstName} ${child.middleName} ${child.lastName}</div>
				<br>
				</td>
			</tr>
			<tr>
				<td>Arm Enrolled</td>
				<td style="font-size: medium; color: black">
				<div>${child.displayArmName}</div>
				<br>
				</td>
			</tr>
			<tr>
				<td>Select Vaccine</td>
				<td><input id="vaccNameVal" type="hidden" readonly="readonly"	value="${command.vaccine.name}">
				<select id="vaccineName" name="vaccineName" onchange="vaccineChanged(this);">
					<option></option>
					<c:forEach items="${vaccine}" var="vacc">
						<option title="${vacc.gapInWeeksFromPreviousVaccine}">${vacc.name}</option>
					</c:forEach>
				</select> 
<script>
<!--
sel = document.getElementById("vaccineName");
val=document.getElementById("vaccNameVal").value;
makeTextSelectedInDD(sel,val);
 
function vaccineChanged(obj){
	var gapInVacc=parseInt(obj.options[obj.selectedIndex].title);
	if(gapInVacc<0){
		if(!confirm('No gap-from-previous-vaccine is defined. Seems vaccine isnt included in study. Are you sure to change vaccine ?')){
			makeTextSelectedInDD(document.getElementById("vaccineName"),document.getElementById("vaccNameVal").value);
			return;
		}
	}
	DWRVaccineService.hasPatientTakenVaccine(document.getElementById("childId").value,obj.value,{
			async: false,
 			callback:function (recievedvc) {
			var recievedvaccmsg=recievedvc;

			if(!isNumber(recievedvaccmsg) && recievedvaccmsg.toLowerCase()!='no'){//parse recievedvaccmsg.toLowerCase()=='yes'){
				alert("An Error Occurred while getting vaccination details , message is :"+recievedvaccmsg);
				makeTextSelectedInDD(document.getElementById("vaccineName"),document.getElementById("vaccNameVal").value);
				return;
			}

			if(recievedvaccmsg.toLowerCase()!='no' && Number(recievedvaccmsg)!=Number(document.getElementById("vaccinationRecordNumber").value)){//parse recievedvaccmsg.toLowerCase()=='yes'){
				if(!confirm('Child have recieved vaccine or any other vaccination is referencing it. Do you still want to change ?')){
					makeTextSelectedInDD(document.getElementById("vaccineName"),document.getElementById("vaccNameVal").value);
					return;
				}
			}
			if(document.getElementById("vaccinationStatus").value != "<%=Vaccination.VACCINATION_STATUS.PENDING.toString()%>"){
				afterVaccinationDateSelected(document.getElementById("vaccinationDate"));
			}
			}
	});
}
//-->
</script>
				</td>
			</tr>
			<tr>
				<td>Description :</td>
				<td><spring:bind path="command.description">
					<textarea name="description" rows="6" cols="30">${status.value}</textarea>
					<br>
					<span class="error-message"><c:out value="${status.errorMessage}" /></span>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td>Vaccination Status :</td>
				<td><spring:bind path="command.vaccinationStatus">
					<input type="hidden" value="${status.value}" id="vstVal" />
					<select id="vaccinationStatus" name="vaccinationStatus">
						<c:forEach items="<%=Vaccination.VACCINATION_STATUS.values()%>"	var="vstatus">
							<option value="${vstatus}" disabled="disabled">${vstatus}:${vstatus.REPRESENTATION}</option>
						</c:forEach>
					</select>
					<span class="error-message"><c:out value="${status.errorMessage}" /></span>
				</spring:bind> <br />
				<script><!--
                            sel = document.getElementById("vaccinationStatus");
                            val=document.getElementById("vstVal").value;

                            for (i=0; i<sel.options.length; i++) {
                                if(val == "<%=Vaccination.VACCINATION_STATUS.LATE_VACCINATED.toString()%>" 
                               				|| val == "<%=Vaccination.VACCINATION_STATUS.VACCINATED.toString()%>"){
                                	if(sel.options[i].value == "<%=Vaccination.VACCINATION_STATUS.LATE_VACCINATED.toString()%>"){
                                		sel.options[i].disabled=false;
                                	}
                                	if(sel.options[i].value == "<%=Vaccination.VACCINATION_STATUS.VACCINATED.toString()%>"){
                                		sel.options[i].disabled=false;
                                	}
                                }
                        		if (sel.options[i].value == val) {
                        			sel.selectedIndex = i;
                        		}
                        	}
                        	//-->
                </script>
                </td>
			</tr>
			
			<c:set var="vaccStPen" value="<%=Vaccination.VACCINATION_STATUS.PENDING.toString()%>"></c:set>
			<tr>
				<td>Vaccination Date: <input id="vaccinationDateVal" type="hidden" value="${command.vaccinationDate}"/></td>
				<c:choose>
					<c:when test="${command.vaccinationStatus != vaccStPen}">
						<td><spring:bind path="command.vaccinationDate">
							<input id="vaccinationDate" name="vaccinationDate"
								readonly="readonly"
								onclick="scwNextAction = afterVaccinationDateSelected.runsAfterSCW(this,document.getElementById('vaccinationDate'));scwShow(document.getElementById('vaccinationDate'),event);"
								value="<c:out value="${status.value}"/>" />
							<input
								onclick="scwNextAction = afterVaccinationDateSelected.runsAfterSCW(this,document.getElementById('vaccinationDate'));scwShow(document.getElementById('vaccinationDate'),event);"
								size="5"
								style="cursor: pointer; width: 16px; height: 16px; background-image: url('images/calendar_icon.png');" />
							<br />
							<span class="error-message"><c:out
								value="${status.errorMessage}" /></span>
						</spring:bind>
						<div style="color: green;width: 300px">
						(Be very careful if editing this date as if next vaccination 
						is pending its effect will be refelcted in reminder's schedule. 
						The duedate of next vaccination will be updated as well as 
						reminders will be rescheduled. Otherwise if next vacciantion 
						have incurred only this date will be updated.)</div>
						</td>
					</c:when>
					<c:otherwise>
						<td style="color: maroon;"><br>
						Not vaccinated yet. <br>
						Navigate to 'Update vaccination' to update <br>
						vaccination status and vaccination date.<br><br></td>
					</c:otherwise>
				</c:choose>
			</tr>
			<c:choose>
			<c:when test="${command.vaccinationStatus == vaccStPen}">
			<tr>
				<td>Vaccination Due Date:</td>
				<td><spring:bind path="command.vaccinationDuedate">
					<input id="vaccinationDuedate" name="vaccinationDuedate"
						readonly="readonly"
						onclick="scwNextAction = afterDateSelected.runsAfterSCW(this,document.getElementById('vaccinationDuedate'));scwShow(document.getElementById('vaccinationDuedate'),event);"
						value="<c:out value="${status.value}"/>" />
					<input
						onclick="scwNextAction = afterDateSelected.runsAfterSCW(this,document.getElementById('vaccinationDuedate'));scwShow(document.getElementById('vaccinationDuedate'),event);"
						size="5"
						style="cursor: pointer; width: 16px; height: 16px; background-image: url('images/calendar_icon.png');" />
					<br />
					<span class="error-message"><c:out
						value="${status.errorMessage}" /></span>
				</spring:bind>
				<div style="color: green;width: 300px">(Although vaccination
				duedate is calculated on the basis of previous vaccination date by
				system automatically but this interface allows to edit vaccination
				duedate manually if needed in any special circumstances. Be very
				careful if editing this date.)</div>
				</td>
			</tr>
			<tr>
				<td colspan="2">
				<div
					style="font-size: 30px; font-stretch: expanded; font-style: italic; color: green">
				<br>
				Reminder SMS Details</div>
				</td>
			</tr>
			<c:forEach items="${arm.armday}" var="armdaynum">
				<tr>
					<td>Day ${armdaynum.id.dayNumber} Reminder Time (hh:mm:ss)
					(16:23:32)</td>
					<c:choose>
						<c:when test="${armdaynum.isDefaultTimeEditable}">
							<td><input type="text" name="time${armdaynum.id.dayNumber}"
								value="${armdaynum.defaultReminderTime}" /></td>
						</c:when>
						<c:otherwise>
							<td><input type="text" name="time${armdaynum.id.dayNumber}"
								readonly="readonly" value="${armdaynum.defaultReminderTime}" /></td>
						</c:otherwise>
					</c:choose>
				</tr>
			</c:forEach>
		</c:when>
		<c:otherwise>
				<td>Vaccination Due Date:</td>
				<td ><spring:bind path="command.vaccinationDuedate">
					<input id="vaccinationDuedate" name="vaccinationDuedate"
						readonly="readonly"	value="<c:out value="${status.value}"/>" />
						<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
				</spring:bind>
				</td>
		</c:otherwise>
		</c:choose>
		</table>
		<br><br>
		<c:choose>
		<c:when test="${not empty next_vaccination}">
		<table width="100%" border="1px;">
			<tr>
				<td>
				<div><br>
				<span style="font-size: medium;color: maroon;">Next Vaccination Details</span><br>
				<br>
				<table width="100%" border="1px;">
					<tr>
						<td>Vaccination Record Num</td>
						<td>${next_vaccination.vaccinationRecordNum}</td>
					</tr>
					<tr>
						<td>Vaccine</td>
						<td>${next_vaccination.vaccine.name}<input id="nextvaccinationDateVal" name="nextvaccinationDateVal" type="hidden" value="${next_vaccination.vaccine.name}"/></td>
					</tr>
					<tr>
						<td>Description</td>
						<td>${next_vaccination.description}</td>
					</tr>
					<tr>
						<td>Vaccination Due Date</td>
						<td><div id="currnextvaccinationduedate">${next_vaccination.vaccinationDuedate}</div><div id="editednextvaccinationduedate"></div></td>
					</tr>
					<tr>
						<td>Vaccination Date</td>
						<td>${next_vaccination.vaccinationDate}</td>
					</tr>
					<tr>
						<td>Vaccination Status</td>
						<td><div id="currnextvaccinationstatus">${next_vaccination.vaccinationStatus}</div></td>
					</tr>
					<tr>
						<td>Previous Vaccination</td>
						<td>${next_vaccination.previousVaccinationRecordNum}</td>
					</tr>
					<tr>
						<td>Next assigned date</td>
						<td>${next_vaccination.nextAssignedDate}</td>
					</tr>
				</table>
				</div>
				</td>
			</tr>
		</table>
		</c:when>
		<c:otherwise>
		<div style="font-size: medium;color: maroon;">No next vaccination record exists.</div>
		</c:otherwise>
		</c:choose>
		</td>
	</tr>

	<tr>
		<td><br></td>
	</tr>
	<tr>
		<td><input type="button" value="submit and proceed to next step" title="submit and proceed to next step" onclick="subfrm();" style="width: 400px;"></td>
	</tr>
</table>
</form>