<%@page import="org.ird.immunizationreminder.context.LoggedInUser"%>
<%@page import="org.ird.immunizationreminder.context.SystemPermissions"%>
<%@page import="org.ird.immunizationreminder.datamodel.entities.Child"%>
	<table class="datagrid" width="100%">
    <thead class="header">
        <tr>
<%
boolean perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.EDIT_CHILDREN_DATA);
if(perm){ 
%> 
        	<th>---</th>
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
			<th>---</th>

<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_IDENTIFIER);
if(perm){ 
%>
            <th>Child Id</th>
<%
}
%>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_NAME);
if(perm){ 
%>
            <th>Name</th>
<%
}
%>

<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_CELL_NUMBER);
if(perm){ 
%>
            <th>Cell Number</th>
<%
}
%>
            <th>Age (weeks)</th>
            <th>Gender</th>
            <th>Colony</th>
            <th>Town</th>
            <th>Status</th>
            <th>Arm</th>
            <th>Center Name</th>
        </tr>
    </thead>
   
   <c:forEach items="${model.child}" var="child"><!-- child class that is short version of Pateint -->
      <tbody class="rows">
      <!--
      <tbody class="alternaterows">
   	 -->
   	 <tr>
<c:set var="pStUT" value="<%=Child.STATUS.FOLLOW_UP.toString()%>"></c:set>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.EDIT_CHILDREN_DATA);
if(perm){ 
%>    	 
			<td><a href="editChild.htm?child_id=${child.childId}">edit</a></td>
<%
}
%>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.UPDATE_VACCINATION);
if(perm){ 
%>    	 

<c:choose>
<c:when test="${child.status==pStUT}">
   	 		<td><a href="viewVaccinationRecord.htm?action=search&childId=${child.childId}">update vaccinations</a></td>
</c:when>
<c:otherwise>
			<td>no vaccine pending</td>
</c:otherwise>
</c:choose>
<%
}
%>
			<td><a title="${child.childId}" href="#"  onclick="viewChildDetails(this.title);" >details</a></td>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_IDENTIFIER);
if(perm){ 
%>
            <td><c:out value="${child.childId}"></c:out></td>
<%
}
%>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_NAME);
if(perm){ 
%>
            <td>${child.firstName} ${child.lastName}</td>
<%
}
%>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_CELL_NUMBER);
if(perm){ 
%>
            <td><c:out value="${child.currentCellNo}"></c:out></td>
<%
}
%>

            <td><c:out value="${child.age}"></c:out></td>
            <td><c:out value="${child.gender}"></c:out></td>
            <td><c:out value="${child.colony}"></c:out></td>
            <td><c:out value="${child.town}"></c:out></td>
            <td><c:out value="${child.status.REPRESENTATION}"></c:out></td>
            <td><c:out value="${child.arm.armName}"></c:out></td>
            <td><c:out value="${child.clinic}"></c:out></td>
        </tr>
    </tbody>

  </c:forEach>
 	<tfoot>
        <tr>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.EDIT_CHILDREN_DATA);
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
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_IDENTIFIER);
if(perm){ 
%>
            <td></td>
<%
}
%>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_NAME);
if(perm){ 
%>
            <td></td>
<%
}
%>
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
            <td></td>
            <td></td>
        </tr>
    </tfoot>
</table>


