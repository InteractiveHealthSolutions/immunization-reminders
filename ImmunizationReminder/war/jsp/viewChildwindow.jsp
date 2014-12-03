<%@ include file="/jsp/include.jsp"%>

<%@page import="org.ird.immunizationreminder.context.LoggedInUser"%>
<%@page import="org.ird.immunizationreminder.context.SystemPermissions"%><c:catch var="catchException">
<c:out value="${lmessage}"></c:out>
	<table  class="datalist">
          
      <tbody >
<tr>
      <th colspan="2" style="font-size: 15px;font-weight: bolder;background-color: #dddddd;text-align: left;">Child Details</th>
</tr>
<%
boolean perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_IDENTIFIER);
if(perm){ 
%>
   	 <tr>
           <th>Child Id</th><td><c:out value="${model.child.childId}"></c:out></td>
     </tr>
<%
}
%>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_NAME);
if(perm){ 
%>
<tr>
            <th>First Name</th><td><c:out value="${model.child.firstName}"></c:out></td>
</tr>  
<tr>          
            <th>Middle Name</th><td><c:out value="${model.child.middleName}"></c:out></td>
</tr>
<tr>            
            <th>Last Name</th><td><c:out value="${model.child.lastName}"></c:out></td>
</tr>
<%
}
%>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_FATHER_NAME);
if(perm){ 
%>            
<tr>
            <th>Father Name</th><td><c:out value="${model.child.fatherName}"></c:out></td>
</tr>
<%
}
%>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_CELL_NUMBER);
if(perm){ 
%>
<tr>
            <th>Current Cell Number</th><td><c:out value="${model.child.currentCellNo}"></c:out></td>
</tr>
<tr>
            <th>Alternate Cell Number</th><td><c:out value="${model.child.alternateCellNo}"></c:out></td>
</tr>
<%
}
%>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_BIRTHDATE);
if(perm){ 
%>
<tr>
            <th>Date of Birth</th><td><fmt:formatDate value="${model.child.birthdate}"  type="date"/></td>
</tr>
<tr>
             <th>Is Birthdate Estimated ?</th><td><c:out value="${model.child.estimatedBirthdate}"></c:out></td>
</tr>
<%
}
%>            

<tr>
            <th>Age (weeks)</th><td><c:out value="${model.child.age}"></c:out></td>
</tr>
<tr>
             <th>Gender</th><td><c:out value="${model.child.gender}"></c:out></td>
</tr>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_ADDRESS);
if(perm){ 
%>
<tr>
            <th>House Number</th><td><c:out value="${model.child.houseNum}"></c:out></td>
</tr>
<tr>
            <th>Street Number</th><td><c:out value="${model.child.streetNum}"></c:out></td>
</tr>
<tr>            
			<th>Sector</th><td><c:out value="${model.child.sector}"></c:out></td>
</tr>
<%
}
%>
<tr>
            <th>Colony</th><td><c:out value="${model.child.colony}"></c:out></td>
</tr>
<tr>            
			<th>Uc Number</th><td><c:out value="${model.child.ucNum}"></c:out></td>
</tr>
<tr>
            <th>Town</th><td><c:out value="${model.child.town}"></c:out></td>
</tr>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_ADDRESS);
if(perm){ 
%>
<tr>
            <th>Landmark</th><td><c:out value="${model.child.landmark}"></c:out></td>
</tr>
<%
}
%>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_PHONE_NUMBER);
if(perm){ 
%>
<tr>
            <th>Phone Number</th><td><c:out value="${model.child.phoneNo}"></c:out></td>
</tr>
<%
}
%>
<tr>
            <th>Follow Up Status</th><td><c:out value="${model.child.status}"></c:out></td>
</tr>
<tr>
            <th>Has Completed ?</th><td><c:out value="${model.child.hasCompleted}"></c:out></td>
</tr>
<tr>
            <th>Termination Date</th><td><c:out value="${model.child.terminationDate}"></c:out></td>
</tr>
<tr>
            <th>Termination Reason</th><td><c:out value="${model.child.terminationReason}"></c:out></td>
</tr>
<tr>
             <th>ARM</th><td><c:out value="${model.child.displayArmName}"></c:out></td>
</tr>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_PROGRAMME_DETAILS);
if(perm){ 
%>
<tr>
             <th>Program Enrolled</th><td><c:out value="${model.child.programmeEnrolled}"></c:out></td>
</tr>
<tr>
             <th>Epi Reg/MR Number</th><td><c:out value="${model.child.mrNumber}"></c:out></td>
</tr>
<tr>
             <th>Center</th><td><c:out value="${model.child.clinic}"></c:out></td>
</tr>
<%
}
%>
<tr>
            <th>Date Enrolled</th><td><fmt:formatDate value="${model.child.dateEnrolled}" type="date"/></td>
</tr>
<tr>
            <th>Completion Date</th><td><fmt:formatDate value="${model.child.dateOfCompletion}" type="date"/></td>
</tr>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_DESCRIPTIVE_NOTE);
if(perm){ 
%>
<tr>
            <th>Description</th><td><c:out value="${model.child.description}"></c:out></td>
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
            <th>Creator (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</th><td><%if(permuid){%>${model.child.createdByUserId},<%}%><%if(permuname){%>${model.child.createdByUserName}<%}%></td>
</tr>
<%
}
%>
<tr>
            <th>Created On</th><td><fmt:formatDate value="${model.child.createdDate}" type="both"/></td>
</tr>
<%
permuid=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERS_EDITOR_USER_ID);
permuname=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERS_EDITOR_USER_NAME);
perm=permuid||permuname;
if(perm){ 
%>
<tr>
            <th>Last Editor (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</th><td><%if(permuid){%>${model.child.lastEditedByUserId},<%}%><%if(permuname){%>${model.child.lastEditedByUserName}<%}%></td>
</tr>
<%
}
%>
<tr>
            <th>Last Updated</th><td><fmt:formatDate value="${model.child.lastUpdated}" type="both"/></td>    
</tr> 
</tbody>
</table>
      <span style="font-size: 20px;font-weight: bolder;background-color: #dddddd;text-align: left;">Vaccine/Reminder Details</span>
<table class="datalist">
    <thead >
        <tr>
            <th>Vaccine</th>
            <th>Status</th>
            <th>Reminders Pending</th>
            <th>Is Any Reminder Late ?</th>
            <th>Max Days Late ?</th>
            <th>Due DateTime</th>
            <th>Record Number</th>          
        </tr>
    </thead>
   
   <c:forEach items="${model.vaccs.record}" var="rec">
      <tbody class="rows">
      <!--
      <tbody class="alternaterows">
   	 -->
   	 <tr>
            <td><c:out value="${rec.vaccination.vaccine.name}"></c:out></td>
            <td><c:out value="${rec.vaccination.vaccinationStatus}"></c:out></td>
            <td><c:out value="${rec.remindersPending}"></c:out></td>
            <td><c:out value="${rec.anyReminderLate}"></c:out></td>
            <td><c:out value="${rec.maxDaysLate}"></c:out></td>
            <td><c:out value="${rec.vaccination.vaccinationDuedate}" /></td>
			<td><c:out value="${rec.vaccination.vaccinationRecordNum}"></c:out></td>
        </tr>
    </tbody>

  </c:forEach>

</table>

</c:catch>
<c:if test="${catchException!=null}">
<span class="error" style="font-size: xx-large; color: red">SESSION EXPIRED. LOGIN AGIAN !!!${catchException}
</span>
</c:if>

