<script type="text/javascript">
<!--
var win;
function subform(){
	if(document.getElementById("fileip").value==''){
		alert('No file selected');
		return;
	}
	document.getElementById("frm").submit();
}
function detailedReport(){
	if(document.getElementById("detReport").value!=null &&
			document.getElementById("detReport").value!=''	){
		win=window.open('viewUploadReport.htm','Upload Report','width=700,height=600,resizable=no,toolbar=no,location=no,scrollbars=yes,directories=no,status=no,menubar=no,copyhistory=no');
		win.focus();
	}else{
		alert('No data report available');
	}
}
//-->
</script>
<form id="frm" enctype="multipart/form-data" action="uploadData" method="post">
	<span class="error" style="font-size: x-small; color: red">
	<c:out value="${message}"></c:out>
	</span>
<div style="overflow: scroll;max-width: 700px;max-height: 400px;">
	<c:forEach items="${uploadDataMessage}" var="msg">
	<span class="error" style="font-size: x-small; color: red">
	${msg}
	</span><br>
	</c:forEach>
</div>
<a href="#" onclick="detailedReport();">View Detailed Report</a>
<div style="display: none;">
<input id="detReport" name="detReport" type="hidden" value="${report}">
</div>
<table border="2" >
<tr>
<td colspan="2">
<p align="center"><b>UPLOAD THE FILE</b>
</td>
</tr>
<tr>
<td>

</td>
</tr>

<tr>
<td>Programme Enrolled
</td>
<td>
<input type="text" name="programEnrolled" value="Immunization Reminders">
</td>

</tr>
<tr>
<td>Center
</td>
<td>
<input type="text" name="center" value="Indus Hospital">
</td>

</tr>
<tr><td><b>Choose the file To Upload:</b>
</td>
<td><input id="fileip" name="file" type="file">
</td>
</tr>
<tr>
<td></td>
<td >
<input type="button" value="Upload File" onclick="subform();">
</td>
</tr>
</table>
<br>

</form>