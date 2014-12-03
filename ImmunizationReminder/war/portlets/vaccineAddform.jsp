
<%@page import="org.ird.immunizationreminder.datamodel.entities.Vaccine"%>
  <script type='text/javascript' src='/ImmunizationReminder/dwr/engine.js'></script>
  <script type='text/javascript' src='/ImmunizationReminder/dwr/interface/DWRVaccineService.js'></script>
<script type='text/javascript' src='/ImmunizationReminder/dwr/util.js'></script>

<script type="text/javascript">
<!--
window.onbeforeunload = function (e) {
	  var e = e || window.event;

	  // For IE and Firefox
	  if (e) {
	    e.returnValue = '';
	  }
	  // For Safari
	  return '';
	};
function addVaccine() {
	
	var vaccname=document.getElementById("vaccineName").value;
	var vaccnumInform=document.getElementById("vaccineNumberInForm").value;
	var vaccnameInform=document.getElementById("vaccineNameInForm").value;
	var gapfrom=document.getElementById("gapInWeeksFromPreviousVaccine").value;
	var unitgapfrom=document.getElementById("unitPrevGap").value;
	var gapto=document.getElementById("gapInWeeksToNextVaccine").value;
	var unitgapto=document.getElementById("unitNextGap").value;
	
	clearVaccinationMessagesDiv();
	
	if( vaccname!=null && vaccname!='' &&
		vaccnumInform!=null && vaccnumInform!='' &&
		vaccnameInform!=null && vaccnameInform!='' &&
		gapfrom!=null && gapfrom!='' &&
		gapto!=null && gapto!=''){
		
		if(confirm("Are you sure you want to add vaccine with name '"+vaccname+"' ?")){
			var description=document.getElementById("description").value;
			DWRVaccineService.addVaccine(vaccname,vaccnumInform,vaccnameInform,description,gapfrom,unitgapfrom,gapto,unitgapto,success);
		}else{
			return;
		}
	}else{
		alert('Mendatory fields are missing for vaccine');
	}
}
var success=function (e) {
	document.getElementById("vaccineName").value="";
	document.getElementById("description").value="";
	document.getElementById("vaccineNumberInForm").value="";
	document.getElementById("vaccineNameInForm").value="";
	
	showNormalVaccinationMsg(e);
};

function clearVaccinationMessagesDiv() {
	document.getElementById("VaccinationMessagesdiv").innerHTML="";
	document.getElementById("divVaccinationMessagesContainer").style.display="none";
}
function showNormalVaccinationMsg(errormsg) {
	document.getElementById("divVaccinationMessagesContainer").style.display="table";
	document.getElementById("VaccinationMessagesdiv").innerHTML="<p><span style=\"color:green\">"+errormsg+"</span></p>";
}
function showErrorVaccinationMsg(errormsg){
	document.getElementById("divVaccinationMessagesContainer").style.display="table";
	document.getElementById("VaccinationMessagesdiv").innerHTML="<p><span style=\"color:red\">"+errormsg+"</span></p>";
}
//-->
</script>
<div id="divVaccinationMessagesContainer" style=" display: none;">
<div id="VaccinationMessagesdiv" style="color: red;"><a href="#" onclick="clearVaccinationMessagesDiv();">Clear</a></div>
</div>
<table>
	<tr>
		<td>Name<span class="mendatory-field">*</span></td>
		<td><input id="vaccineName" name="vaccineName" type="text"/></td>
	</tr>
	<tr>
		<td>Vaccine Num in form<span class="mendatory-field">*</span></td>
		<td><input id="vaccineNumberInForm" name="vaccineNumberInForm" type="text"/></td>
	</tr>
	<tr>
		<td>Vaccine Name in form<span class="mendatory-field">*</span></td>
		<td><input id="vaccineNameInForm" name="vaccineNameInForm" type="text"/></td>
	</tr>
	<tr>
		<td>Description</td>
		<td><textarea id="description" name="description" ></textarea></td>
	</tr>
	<tr>
		<td>Gap From Previous</td>
		<td><input id="gapInWeeksFromPreviousVaccine" name="gapInWeeksFromPreviousVaccine" /></td>
	</tr>
		<tr>
		<td>Previous Gap Unit</td>
		<td><select id="unitPrevGap" name="unitPrevGap">
			<c:forEach items="<%=Vaccine.UNIT_GAP.values()%>" var="gap_value">
				<option value="${gap_value}">${gap_value}</option>
			</c:forEach>
		</select></td>
	</tr>
	<tr>
		<td>Gap To Next</td>
		<td><input id="gapInWeeksToNextVaccine" name="gapInWeeksToNextVaccine" /></td>
	</tr>
		<tr>
		<td>Next Gap Unit</td>
		<td><select id="unitNextGap" name="unitNextGap">
			<c:forEach items="<%=Vaccine.UNIT_GAP.values()%>" var="gap_value">
				<option value="${gap_value}">${gap_value}</option>
			</c:forEach>
		</select></td>
	</tr>
	<tr>
		<td></td>
		<td><input id="vaccSubmitbtn" type="button" value="Submit" onclick="addVaccine();"/></td>
	</tr>
</table>