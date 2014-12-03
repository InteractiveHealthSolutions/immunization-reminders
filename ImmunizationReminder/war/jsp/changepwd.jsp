<input type="hidden" id="page_nav_id" value="home">
<%@ include file="/WEB-INF/template/headersimple.jsp" %>
<script type='text/javascript' src='/ImmunizationReminder/dwr/interface/DWRUserService.js'></script>
<script type='text/javascript' src='/ImmunizationReminder/dwr/engine.js'></script>
<script type='text/javascript' src='/ImmunizationReminder/dwr/util.js'></script>
<script type="text/javascript">
<!--
function change() {

	showMsg("Please wait while changing password....");
	opwd=document.getElementById("opwd").value;
	npwd=document.getElementById("npwd").value;
	cpwd=document.getElementById("cpwd").value;
	if(opwd!='' && npwd!='' && cpwd !=''){
		if(confirm("Are you sure you want to change password ?")){
			document.getElementById("changebtn").style.disabled=true;
			DWRUserService.changePassword(opwd,npwd,cpwd,success);
		}else{
			return;
		}
	}else{
		alert('No value specified in either of password boxes');
	}
}
var success=function (msg) {
	document.getElementById("opwd").value="";
	document.getElementById("npwd").value="";
	document.getElementById("cpwd").value="";
	
	showMsg(msg);
	document.getElementById("changebtn").style.disabled=false;
	
};
function showMsg(msg){
	document.getElementById("messageDiv").innerHTML="<p><span style=\"color:green\">"+msg+"</span></p>";
}
//-->
</script>
<form id="frm" method="post" >
            <div class="function-tag">Change Your Password</div>
<br>
<br>
<div id="messageDiv"></div>
            
<table>
  <tr>
    <td>Enter Old Password</td>
    <td><input id="opwd" name="opwd" type="password" /></td>
  </tr>
    <tr>
    <td>Enter New Password</td>
    <td><input id="npwd" name="npwd" type="password" /></td>
  </tr>
    <tr>
    <td>Confirm Password</td>
    <td><input id="cpwd" name="cpwd" type="password" /></td>
  </tr>
  <tr>
    <td><!--<input type="hidden" id="action" name="action" value="change"/>--></td>
    <td><input id="changebtn" type="button" value="Change" onclick="change();"/><br><br>
    <input id="exitbtn" type="button" value="OK, Return back" onclick="exit();" style="display: none;"/>
    </td>
  </tr>
</table>
</form>
<%@ include file="/WEB-INF/template/footersimple.jsp" %>