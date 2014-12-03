  <script type='text/javascript' src='/ImmunizationReminder/dwr/interface/DWRIrSettingService.js'></script>
  <script type='text/javascript' src='/ImmunizationReminder/dwr/engine.js'></script>
  <script type='text/javascript' src='/ImmunizationReminder/dwr/util.js'></script>

<%@page import="org.ird.immunizationreminder.context.LoggedInUser"%>
<%@page import="org.ird.immunizationreminder.context.SystemPermissions"%>
<script type="text/javascript"><!--
var win;
function showDiv(obj){
	var trId=obj.title;
	var divId=obj.title+"0";
	act=obj.value;
	if(act=="+"){
		document.getElementById(trId).style.display="table-row";
		document.getElementById(divId).style.display="table";

		obj.value="-";
	}
	else{
		document.getElementById(trId).style.display="none";
		document.getElementById(divId).style.display="none";
		
		obj.value="+";
	}
}
function editSetting(obj){
	var val=prompt("Please enter new value for field");
	if (val != '' && val != null) {
		var id=obj.title;
		if(confirm("Are you sure, you want to update setting with name "+id+". You will not be able to undo the action.")){
			DWRIrSettingService.updateIrSetting(id,val,updateresult);
		}
	}
}
var updateresult=function(result){
	showNormalMsg(result);
};
function clearMessagesDiv() {
	document.getElementById("Messagesdiv").innerHTML="";
	document.getElementById("divMessagesContainer").style.display="none";
}
function showNormalMsg(errormsg) {
	document.getElementById("divMessagesContainer").style.display="table";
	document.getElementById("Messagesdiv").innerHTML="<p><span style=\"color:green\">"+errormsg+"</span></p>";
}

//-->
</script>
<div id="divMessagesContainer" style=" display: none;">
<div id="Messagesdiv" style="color: red;"><a href="#" onclick="clearMessagesDiv();">Clear</a></div>
</div>
	<table class="datagrid" width="100%">
    <thead class="header">
        <tr>
   		
<%
boolean perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.EDIT_IRSETTINGS);
if(perm){ 
%>      		
       		<th>---</th>
<%} %>             
			<th>Setting Name</th>
            <th>Value</th>
            <th>Description</th>
            <th>LastUpdated</th>
<%
boolean permuid=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERS_EDITOR_USER_ID);
boolean permuname=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERS_EDITOR_USER_NAME);
 perm=permuid||permuname;
if(perm){ 
%>            
           <th>Last Editor(<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</th>
<%
}
%>             
        </tr>
    </thead>
      <tbody class="rows">
      
         <c:forEach items="${model.irsetting}" var="irs">
   
      <!--
      <tbody class="alternaterows">
   	 -->
   		 <tr>
   		 <%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.EDIT_IRSETTINGS);
if(perm){ 
%>      		
   	 		<td><a href="#" onclick="editSetting(this);" title="${irs.name}">edit</a></td>
<%} %> 
            <td><c:out value="${irs.name}"></c:out></td>
            <td><DIV style="word-wrap: break-word; width: 200px"><c:out value="${irs.value}"></c:out></DIV></td>
            <td><c:out value="${irs.description}"></c:out></td>
            <td><c:out value="${irs.lastUpdated}"></c:out></td>
<%if(permuid||permuname){ %>   
<td><%if(permuid){%>${irs.lastEditedByUserId},<%}%><%if(permuname){%>${irs.lastEditedByUserName}<%}%></td>
<%} %>
       </tr>
    </c:forEach>
       
   </tbody>
 	<tfoot>
        <tr>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.EDIT_IRSETTINGS);
if(perm){ 
%>      		
            <td></td>
<%} %>  
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <%if(permuid||permuname){ %>
            <td></td>    <%} %>                   
       </tr>
    </tfoot>
</table>


