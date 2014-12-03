<%@page import="org.ird.immunizationreminder.context.LoggedInUser"%>
<%@page import="org.ird.immunizationreminder.context.SystemPermissions"%>
<table class="datagrid" width="100%">
    <thead class="header">
        <tr>
<%
boolean perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_CELL_NUMBER);
if(perm){ 
%>        
            <th>Cell Number</th>
<%
}
%> 
            <th>Recieve Date</th>
            <th>Recieve Time</th>
            <th>System Recieve Time</th>
            <th>Response Text</th>
            <th>Response Type</th>
            <th>Child Id</th>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_NAME);
if(perm){ 
%>
            <th>ChildName</th>
<%
}
%>           
            <th>Arm</th>
  
        </tr>
    </thead>
   
   <c:forEach items="${model.response}" var="resp">
      <tbody class="rows">
      <!--
      <tbody class="alternaterows">
   	 -->
   	 <tr>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_CELL_NUMBER);
if(perm){ 
%>   	 
            <td><c:out value="${resp.cellNo}"></c:out></td>
<%
}
%>            
            <td><fmt:formatDate value="${resp.recieveDate}" pattern="yyyy-MM-dd"/></td>
            <td><c:out value="${resp.recieveTime}"></c:out></td>
            <td><c:out value="${resp.actualSystemDate}"></c:out></td>
            <td>${resp.responseText}</td>
            <td><c:out value="${resp.responseType}"></c:out></td>
            <td><c:out value="${resp.child.childId}"></c:out></td>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_CHILDREN_CHILD_NAME);
if(perm){ 
%>
            <td>${resp.child.firstName}" ${resp.child.middleName} ${resp.child.lastName}</td>
<%
}
%>                   
            <td><c:out value="${resp.child.arm.armName}"></c:out></td>
    
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
%>          <td></td>
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
        </tr>
    </tfoot>
</table>


