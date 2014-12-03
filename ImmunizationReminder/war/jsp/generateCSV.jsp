
<%@page import="org.ird.immunizationreminder.context.SystemPermissions"%>
<%@page import="org.ird.immunizationreminder.web.utils.UserSessionUtils"%>
<%@page import="org.ird.immunizationreminder.service.exception.UserServiceException"%><input type="hidden" id="page_nav_id" value="_li7">


<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="/WEB-INF/template/pagebody.jsp" %>

            <div class="function-tag">Generate CSV</div>

<%
boolean perm=false;
try{
perm=UserSessionUtils.hasActiveUserPermission(SystemPermissions.GENERATE_CSV,request);
}catch(UserServiceException e){
%>
<c:redirect url="login.htm"></c:redirect>
<%
}if(perm){ 
%> 
<script type="text/javascript"><!--
function generate(){

	var date1_var=document.getElementById("date1").value;
	var date2_var=document.getElementById("date2").value;

	if(date1_var==null||date1_var==""){
		alert('Please specify a from date..');
	}else{
		document.getElementById("frm").action='csv?date1='+date1_var+'&date2='+date2_var;
		document.getElementById("frm").submit();
	}
}	
//-->
</script>
<form id="frm" method="post">
<table class="searchTable">
	<tr>
	<td>
	<span class="error" style="font-size: x-small; color: red">
	<c:out value="${message}"></c:out>
	</span>
	</td>
	</tr>
  <tr>
    <td>GENERATE CSV FILE<br><br></td>
    <td></td>
  </tr>
  <tr>
                <td>Select Arm</td>
                <td>     
                <input type="hidden" id="armNameVal" value="${lastSearchArmName}"/> 
                <select id="armName" name="armName">
                	<option></option>
                	<c:forEach  items="${arm}" var="arm">
                		<option>${arm.armName}</option>
                	</c:forEach>               
                </select>
                <script><!--
                    sel = document.getElementById("armName");
                    val=document.getElementById("armNameVal").value;
                  	for (i=0; i<sel.options.length; i++) {
                  		if (sel.options[i].text == val) {
                  			sel.selectedIndex = i;
                  		}
                	}
                //-->
                </script>
                </td>
  </tr>
    <tr>
    <td>Enter Past Due Date Range of Vaccination</td>
                <td>
                	<input id="date1" name="date1" onclick='scwShow(this,event);' readonly="readonly" value=""/>
                    <input onclick="scwShow(scwID('date1'),event);" style="cursor:pointer;width: 16px;height:16px;background-image: url('images/calendar_icon.png');"/>
					<span class="searchCalendarTag">:  Start</span>
					<br>
					<input id="date2" name="date2" onclick='scwShow(this,event);' readonly="readonly" value=""/>
                    <input onclick="scwShow(scwID('date2'),event);" style="cursor:pointer;width: 16px;height:16px;background-image: url('images/calendar_icon.png');"/>
                	<span class="searchCalendarTag">:  End<br></span>
                
                </td>
                
     </tr>
    <tr>
    <td></td>
    <td></td>
  </tr>
  <tr>
  	<td></td>
    <td><input type="submit" value="Generate" onclick="generate();" /></td>
  </tr>
</table>
</form>
<%
}else{
%>
<span class="error" style="font-size: medium; color: red">
<c:out value="Sorry you donot have permissions to perform task. Contact your system administrator."></c:out>
</span>
<%
}
%>
<%@ include file="/WEB-INF/template/sidebarReporting.jsp" %>
<%@ include file="/WEB-INF/template/footer.jsp" %>