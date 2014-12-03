<%@page import="org.ird.immunizationreminder.context.LoggedInUser"%>
<%@page import="org.ird.immunizationreminder.context.SystemPermissions"%>
<script type="text/javascript"><!--
function showDiv(obj){
	var trId=obj.title;
	var divId=obj.title+"dv";
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
//-->
</script>
	<table class="datagrid" width="100%">
    <thead class="header">
        <tr>
       		<th>---</th>
<%
boolean perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.EDIT_REMINDERS);
if(perm){ 
%>        		
       		<th>---</th>
<%
}
%>        		
            <th>Reminder Name</th>
            <th>Description</th>
<%
boolean permuidcr=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERS_CREATOR_USER_ID);
boolean permunamecr=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERS_CREATOR_USER_NAME);
 perm=permuidcr||permunamecr;
if(perm){ 
%>            
            <th>Creator (<%if(permuidcr){%>Id,<%}%><%if(permunamecr){%>Name<%}%>)</th>
<%
}
%>            
            <th>Created On</th>
<%
boolean permuided=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERS_EDITOR_USER_ID);
boolean permunameed=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERS_EDITOR_USER_NAME);
perm=permuided||permunameed;
if(perm){ 
%>            
           <th>Last Editor (<%if(permuided){%>Id,<%}%><%if(permunameed){%>Name<%}%>)</th>
<%
}
%>             
            <th>Last Updated</th>
        </tr>
    </thead>
      <tbody class="rows">
      
         <c:forEach items="${model.reminder}" var="rem">
   
      <!--
      <tbody class="alternaterows">
   	 -->
   		 <tr>	
   	 		<td><input type="text" readonly="readonly" value="+" title="${rem.reminderId}r" class="expandDataButton" onclick="showDiv(this);"/></td>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.EDIT_REMINDERS);
if(perm){ 
%> 
            <td><a href="editReminder.htm?editRecord=${rem.name}">Edit</a></td>
<%}
%>            
            <td><c:out value="${rem.name}"></c:out></td>
            <td><c:out value="${rem.description}"></c:out></td>
<%if(permuidcr||permunamecr){ %>              
<td><%if(permuidcr){%>${rem.createdByUserId},<%}%><%if(permunamecr){%>${rem.createdByUserName}<%}%></td>
<%} %>
            <td>${rem.createdDate}</td>
<%if(permuided||permunameed){ %>   
<td><%if(permuided){%>${rem.lastEditedByUserId},<%}%><%if(permunameed){%>${rem.lastEditedByUserName}<%}%></td>
<%} %>
            <td>${rem.lastUpdated}</td> 
       </tr>
       <tr id="${rem.reminderId}r" style="display: none;" >
       <td></td>
       <td colspan="8">

       	<table id="${rem.reminderId}rdv" class="datagrid" width="100%">
           		 <thead class="header">
				       <tr>
				           <th>Reminder Text</th>
				       </tr>
				   </thead>
				   <tbody class="rows">
				   <c:forEach items="${rem.reminderText}" var="rtext">
	           		<tr>
	           		 <td>${rtext}</td>
	           		</tr>
	           		</c:forEach>
	           	   </tbody>
        </table>
       </td></tr>
    </c:forEach>
       
   </tbody>
 	<tfoot>
        <tr>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.EDIT_REMINDERS);
if(perm){ 
%>         
            <td></td>
<%} %>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <%if(permuidcr||permunamecr){ %>
            <td></td><%} %>
            <%if(permuided||permunameed){ %>
            <td></td>    <%} %> 
       </tr>
    </tfoot>
</table>


