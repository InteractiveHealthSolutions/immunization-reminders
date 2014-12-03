<%@page import="org.ird.immunizationreminder.context.LoggedInUser"%>
<%@page import="org.ird.immunizationreminder.context.SystemPermissions"%>

<%@page import="org.ird.immunizationreminder.datamodel.entities.Vaccination.VACCINATION_STATUS"%>
<%@page import="org.ird.immunizationreminder.datamodel.entities.Vaccination"%>
<script type="text/javascript">
<!--
var win;
function viewDet(obj){
	var pvid=obj.title;

	win=window.open('viewVaccinationwindow.htm?pvId='+pvid,'VaccinationDetails:'+pvid,'width=500,height=600,resizable=no,toolbar=no,location=no,scrollbars=yes,directories=no,status=no,menubar=no,copyhistory=no');
	win.focus();
}//-->
</script>
<table class="datagrid" width="100%">
    <thead class="header">
        <tr>
        	<th>View All Details</th> 
<%
boolean perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.EDIT_VACCINATION_RECORD);
if(perm){ 
%>
            <th>Correct Record Data</th>
<%
}
%>   
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.UPDATE_VACCINATION);
if(perm){ 
%> 
        	<th>Update Pending Vaccines</th>
<%
}
%>          
			<th>Record Number</th> 
            <th>Vaccine</th>
            <th>Due DateTime</th>
            <th>Vaccination DateTime</th>
            <th>Status</th>
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
            <th>Next Assigned Date</th> 
        </tr>
    </thead>
<c:set var="vaccStPen" value="<%=Vaccination.VACCINATION_STATUS.PENDING.toString()%>"></c:set>
   <c:forEach items="${model.pvaccination}" var="pvacc">
      <tbody class="rows">
      <!--
      <tbody class="alternaterows">
   	 -->
   	 <tr>
   	 <td><a  title="${pvacc.vaccinationRecordNum}" href="#"  onclick="viewDet(this);" >details</a></td>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.EDIT_VACCINATION_RECORD);
if(perm){ 
%>
<td><a href="editVaccinationRecord.htm?vaccRecNum=${pvacc.vaccinationRecordNum}">correct</a></td>

<%
}
%>    
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.UPDATE_VACCINATION);
if(perm){ 
%>    	 
<c:choose>
<c:when test="${pvacc.vaccinationStatus==vaccStPen}">
   	 		<td><a href="updateVaccinationRecord.htm?record_id=${pvacc.vaccinationRecordNum}">vaccinate</a></td>
</c:when>
<c:otherwise>
			<td>received</td>
</c:otherwise>
</c:choose>
<%
}
%>         
			<td><c:out value="${pvacc.vaccinationRecordNum}"></c:out></td>
            <td><c:out value="${pvacc.vaccine.name}"></c:out></td>
            <td><c:out value="${pvacc.vaccinationDuedate}" /></td>
            <td><c:out value="${pvacc.vaccinationDate}"></c:out></td>
            <td><c:out value="${pvacc.vaccinationStatus.REPRESENTATION}"></c:out></td>
            <td><a title="${pvacc.child.childId}" href="#"  onclick="viewChildDetails(this.title);" >${pvacc.child.childId}</a></td>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_NAME);
if(perm){ 
%>
            <td>${pvacc.child.firstName} ${pvacc.child.middleName} ${pvacc.child.lastName}</td>
<%
}
%> 
            <td><c:out value="${pvacc.child.arm.armName}"></c:out></td>
            <td><fmt:formatDate value="${pvacc.nextAssignedDate}" pattern="yyyy-MM-dd"/></td>
        </tr>
    </tbody>

  </c:forEach>
 	<tfoot>
        <tr>
            <td></td>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.EDIT_VACCINATION_RECORD);
if(perm){ 
%>
            <td></td>
<%
}
%>    
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.UPDATE_VACCINATION);
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


