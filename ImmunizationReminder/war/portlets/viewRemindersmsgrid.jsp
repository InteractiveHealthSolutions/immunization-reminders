<%@page import="org.ird.immunizationreminder.context.LoggedInUser"%>
<%@page import="org.ird.immunizationreminder.context.SystemPermissions"%>
<script type="text/javascript">
<!--
var win;
function viewDet(obj){
	var rsid=obj.title;

	win=window.open('viewReminderSmswindow.htm?rsId='+rsid,'ReminderSmsDetails:'+rsid,'width=500,height=600,resizable=no,toolbar=no,location=no,scrollbars=yes,directories=no,status=no,menubar=no,copyhistory=no');
	win.focus();
}//-->
</script>
<table class="datagrid" width="100%">
    <thead class="header">
        <tr>
        <th>---</th>
<%
boolean perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_CELL_NUMBER);
if(perm){ 
%>        
            <th>Cell Number</th>
<%
}
%>             
            <th>Due DateTime</th>
            <th>Sent DateTime</th>
            <th>Reminder Day Num</th> 
            <th>Sms Status</th>
            <th>Child Id</th>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_NAME);
if(perm){ 
%>
            <th>Child Name</th>
<%
}
%> 
            <th>ARM</th>
            <th>Vaccine</th>
            <th>Vaccination Record Number</th>
        </tr>
    </thead>
   
   <c:forEach items="${model.remindersms}" var="rem">
      <tbody class="rows">
      <!--
      <tbody class="alternaterows">
   	 -->
   	 <tr>
		  	<td><a  title="${rem.rsmsRecordNum}" href="#"  onclick="viewDet(this);" >View</a></td>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_CELL_NUMBER);
if(perm){ 
%>   	 
            <td><c:out value="${rem.cellnumber}"></c:out></td>
<%
}
%> 
            <td><c:out value="${rem.dueDate}" /></td>
            <td><c:out value="${rem.sentDate}"></c:out></td>
            <td>${rem.dayNumber}</td>
            <td><c:out value="${rem.reminderStatus.REPRESENTATION}"></c:out></td>
            <td><c:out value="${rem.child.childId}"></c:out></td>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_NAME);
if(perm){ 
%>
            <td>${rem.child.firstName} ${rem.child.middleName} ${rem.child.lastName}</td>
<%
}
%> 
            <td><c:out value="${rem.child.displayArmName}"></c:out></td>
            <td><c:out value="${rem.vaccine.name}"></c:out></td>
            <td>${rem.vaccinationRecordNum}</td>
                       
        </tr>
    </tbody>

  </c:forEach>
 	<tfoot>
        <tr>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_CELL_NUMBER);
if(perm){ 
%>        
            <td></td>
<%
}
%> 
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_NAME);
if(perm){ 
%>
            <td></td>
<%
}
%> 
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>           
        </tr>
    </tfoot>
</table>


