<%@ include file="/jsp/include.jsp"%>

<%@page import="org.ird.immunizationreminder.context.LoggedInUser"%>

<%@page import="org.ird.immunizationreminder.context.SystemPermissions"%>
<c:catch var="catchException">
<c:out value="${lmessage}"></c:out>
	<table  class="datalist">
          
      <tbody >
<tr>
      <th colspan="2" style="font-size: 15px;font-weight: bolder;background-color: #dddddd;text-align: left;">Vaccination Details</th>
</tr>
   	 <tr>
            <th>Record Number</th> <td><c:out value="${pvacc.vaccinationRecordNum}"></c:out></td>
     </tr>
     <tr>
            <th>Due DateTime</th> <td><c:out value="${pvacc.vaccinationDuedate}"></c:out></td>
     </tr>
     <tr>
            <th>Vaccination DateTime</th> <td><c:out value="${pvacc.vaccinationDate}"></c:out></td>
     </tr>
     <tr>
            <th>Vaccination Status</th> <td><c:out value="${pvacc.vaccinationStatus}"></c:out></td>
     </tr>
     <tr>
            <th>Is First Vaccination ?</th><td><c:out value="${pvacc.isFirstVaccination}"></c:out></td>
	</tr>
	<tr>
            <th>Is Last Vaccination ?</th><td><c:out value="${pvacc.isLastVaccination}"></c:out></td>
	</tr>
	<tr>
            <th>Previous Vaccination Record</th><td><c:out value="${pvacc.previousVaccinationRecordNum}"></c:out></td>
	</tr>
	<tr>
            <th>Next Vaccination Record</th><td><c:out value="${pvacc.nextVaccinationRecordNum}"></c:out></td>
	</tr>
     <tr>
            <th>Child Id</th> <td><c:out value="${pvacc.child.childId}"></c:out></td>
     </tr>
     
<%
boolean perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_NAME);
if(perm){ 
%>
<tr>
            <th>First Name</th><td><c:out value="${pvacc.child.firstName}"></c:out></td>
</tr>  
<tr>          
            <th>Middle Name</th><td><c:out value="${pvacc.child.middleName}"></c:out></td>
</tr>
<tr>            
            <th>Last Name</th><td><c:out value="${pvacc.child.lastName}"></c:out></td>
</tr>
<%
}
%>
<tr>
            <th>Child Responded ?</th><td><c:out value="${pvacc.childResponded}"></c:out></td>
</tr>
<tr>
            <th>Vaccine Name</th><td><c:out value="${pvacc.vaccine.name}"></c:out></td>
</tr>
<tr>
            <th>ARM</th><td><c:out value="${pvacc.child.displayArmName}"></c:out></td>
</tr>
<tr>
            <th>Next Assigned Date</th><td><fmt:formatDate value="${pvacc.nextAssignedDate}"  type="date"/></td>
</tr>
<tr>
            <th>Description</th><td><c:out value="${pvacc.description}"></c:out></td>
</tr>
<tr>
            <th>Dose</th><td><c:out value="${pvacc.dose}"></c:out></td>
</tr>
<tr>
            <th>Vaccinated by</th><td><c:out value="${pvacc.childVaccinatedBy}"></c:out></td>
</tr>
<tr>
            <th>Brought by</th><td><c:out value="${pvacc.childBroughtBy}"></c:out></td>
</tr>
<%
boolean permuid=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERS_CREATOR_USER_ID);
boolean permuname=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERS_CREATOR_USER_NAME);
perm=permuid||permuname;
if(perm){ 
%>
<tr>
            <th>Creator (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</th><td><%if(permuid){%>${pvacc.createdByUserId},<%}%><%if(permuname){%>${pvacc.createdByUserName}<%}%></td>
</tr>
<%
}
%>
<tr>
            <th>Created On</th><td><fmt:formatDate value="${pvacc.createdDate}" type="both"/></td>
</tr>
<%
permuid=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERS_EDITOR_USER_ID);
permuname=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERS_EDITOR_USER_NAME);
perm=permuid||permuname;
if(perm){ 
%>
<tr>
            <th>Last Editor (<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</th><td><%if(permuid){%>${pvacc.lastEditedByUserId},<%}%><%if(permuname){%>${pvacc.lastEditedByUserName}<%}%></td>
</tr>
<%
}
%>
<tr>
            <th>Last Updated</th><td><fmt:formatDate value="${pvacc.lastUpdated}" type="both"/></td>    
</tr>  
</tbody>


</table>
</c:catch>
<c:if test="${catchException!=null}">
<span class="error" style="font-size: xx-large; color: red">SESSION EXPIRED. LOGIN AGIAN !!!${catchException}
</span>
</c:if>

