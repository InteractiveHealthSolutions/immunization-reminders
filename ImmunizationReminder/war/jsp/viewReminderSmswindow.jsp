<%@ include file="/jsp/include.jsp"%>

<%@page import="org.ird.immunizationreminder.context.LoggedInUser"%>

<%@page import="org.ird.immunizationreminder.context.SystemPermissions"%>
<c:catch var="catchException">
<c:out value="${lmessage}"></c:out>
	<table  class="datalist">
          
      <tbody >
<tr>
      <th colspan="2" style="font-size: 15px;font-weight: bolder;background-color: #dddddd;text-align: left;">Reminder Sms Details</th>
</tr>
<tr>
      <th>ARM</th>     <td><c:out value="${model.remsms.child.displayArmName}"></c:out></td>
	 </tr>
	 <c:catch var="remindersmsexc">
   	 <tr>
            <th>Reminder Name</th> <td><c:out value="${model.remsms.reminder.name}"></c:out></td>
     </tr>
     </c:catch>
     <c:if test="${remindersmsexc!=null}">
     <tr>
     	<th>Reminder Name</th> <td>no reminder found yet</td>
     </tr>
     </c:if>
     <tr>
            <th>Due Date</th> <td><fmt:formatDate value="${model.remsms.dueDate}" type="date"/></td>
     </tr>
     <tr>
            <th>Due Time</th><td><c:out value="${model.remsms.dueTime}"></c:out></td>
	</tr>
	<tr>
            <th>Day Number</th><td><c:out value="${model.remsms.dayNumber}"></c:out></td>
	</tr>
	<tr>
            <th>Sent Date Time</th><td><c:out value="${model.remsms.sentDate}"></c:out></td>
	</tr>
	<tr>
            <th>Reminder Status</th><td><c:out value="${model.remsms.reminderStatus}"></c:out></td>
	</tr>
	<tr>
            <th>Sms Cancel Reason</th><td><c:out value="${model.remsms.smsCancelReason}"></c:out></td>
	</tr>
     <tr>
            <th>Text</th> <td><c:out value="${model.remsms.text}"></c:out></td>
     </tr>
     <tr>
            <th>Cell Number</th> <td><c:out value="${model.remsms.cellnumber}"></c:out></td>
     </tr>
<tr>
            <th>Is Sms Late</th><td><c:out value="${model.remsms.isSmsLate}" /></td>
</tr>
<tr>
            <th>Difference in days</th><td><c:out value="${model.remsms.dayDifference}"></c:out></td>
</tr>
<tr>
            <th>Difference in hours</th><td><c:out value="${model.remsms.hoursDifference}"></c:out></td>
</tr>
	 <!--<c:catch var="vaccineexc">
   	 <tr>
            <th>Vaccine Name</th> <td><c:out value=""></c:out></td>
     </tr>
     </c:catch>
     <c:if test="${vaccineexc!=null}">
     	<th>Vaccine Name</th> <td>no vaccine????????????????? found yet</td>
     </c:if>
     -->
     <tr>
            <th>Vaccination Record Number</th> <td><c:out value="${model.remsms.vaccinationRecordNum}"></c:out></td>
     </tr>
     <tr>
            <th>Child Id</th> <td><c:out value="${model.remsms.child.childId}"></c:out></td>
     </tr>
     
<%
boolean perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_NAME);
if(perm){ 
%>
<tr>
            <th>First Name</th><td><c:out value="${model.remsms.child.firstName}"></c:out></td>
</tr>  
<tr>          
            <th>Middle Name</th><td><c:out value="${model.remsms.child.middleName}"></c:out></td>
</tr>
<tr>            
            <th>Last Name</th><td><c:out value="${model.remsms.child.lastName}"></c:out></td>
</tr>
<%
}
%>

<%
boolean permuid=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERS_CREATOR_USER_ID);
boolean permuname=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERS_CREATOR_USER_NAME);
perm=permuid||permuname;
if(perm){ 
%>
<tr>
            <th>Creator (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</th><td><%if(permuid){%>${model.remsms.createdByUserId},<%}%><%if(permuname){%>${model.remsms.createdByUserName}<%}%></td>
</tr>
<%
}
%>
<tr>
            <th>Created On</th><td><fmt:formatDate value="${model.remsms.createdDate}" type="both"/></td>
</tr>
<%
permuid=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERS_EDITOR_USER_ID);
permuname=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERS_EDITOR_USER_NAME);
perm=permuid||permuname;
if(perm){ 
%>
<tr>
            <th>Last Editor (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</th><td><%if(permuid){%>${model.remsms.lastEditedByUserId},<%}%><%if(permuname){%>${model.remsms.lastEditedByUserName}<%}%></td>
</tr>
<%
}
%>
<tr>
            <th>Last Updated</th><td><fmt:formatDate value="${model.remsms.lastUpdated}" type="both"/></td>    
</tr>  
</tbody>


</table>
</c:catch>
<c:if test="${catchException!=null}">
<span class="error" style="font-size: xx-large; color: red">SESSION EXPIRED. LOGIN AGIAN !!!${catchException}
</span>
</c:if>

