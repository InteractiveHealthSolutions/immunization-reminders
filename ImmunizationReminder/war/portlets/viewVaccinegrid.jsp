<%@page import="org.ird.immunizationreminder.context.LoggedInUser"%>
<%@page import="org.ird.immunizationreminder.context.SystemPermissions"%>
<table class="datagrid" width="100%">
    <thead class="header">
        <tr>
        	<th> --- </th>
            <th>Vaccine Name</th>
            <th>Vaccine Num in form</th>
            <th>Vaccine Name in form</th>
            <th>Description</th>
            <th>Gap From Previous</th>
            <th>Gap To Next</th>
<%
boolean permuidcr=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERS_CREATOR_USER_ID);
boolean permunamecr=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERS_CREATOR_USER_NAME);
boolean perm=permuidcr||permunamecr;
if(perm){ 
%>            
           <th>Created By(<%if(permuidcr){%>Id,<%}%><%if(permunamecr){%>Name<%}%>)</th>
<%
}
%>      
            <th>Date created</th>

<%
boolean permuided=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERS_EDITOR_USER_ID);
boolean permunameed=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERS_EDITOR_USER_NAME);
perm=permuided||permunameed;
if(perm){ 
%>            
           <th>Last Editor(<%if(permuided){%>Id,<%}%><%if(permunameed){%>Name<%}%>)</th>
<%
}
%>       
            <th>Last updated</th>

        </tr>
    </thead>
      <tbody class="rows">
      
         <c:forEach items="${model.vaccine}" var="vacc">
   
      <!--
      <tbody class="alternaterows">
   	 -->
   		 <tr>
   		 	<td><a href="editVaccine.htm?editRecord=${vacc.name}">Edit</a></td>
            <td><c:out value="${vacc.name}"></c:out></td>
            <td><c:out value="${vacc.vaccineNumberInForm}"></c:out></td>
            <td><c:out value="${vacc.vaccineNameInForm}"></c:out></td>
            <td><c:out value="${vacc.description}"></c:out></td>
            <td><c:out value="${vacc.gapInWeeksFromPreviousVaccine} ${vacc.unitPrevGap}"></c:out></td>
            <td><c:out value="${vacc.gapInWeeksToNextVaccine} ${vacc.unitNextGap}"></c:out></td>            
<%if(permuidcr||permunamecr){ %>   
<td><%if(permuidcr){%>${vacc.createdByUserId},<%}%><%if(permunamecr){%>${vacc.createdByUserName}<%}%></td>
<%} %>
            <td><c:out value="${vacc.createdDate}"></c:out></td>
<%if(permuided||permunameed){ %>   
<td><%if(permuided){%>${vacc.lastEditedByUserId},<%}%><%if(permunameed){%>${vacc.lastEditedByUserName}<%}%></td>
<%} %>
            <td><c:out value="${vacc.lastUpdated}"></c:out></td>
       </tr>
    </c:forEach>
       
   </tbody>
 	<tfoot>
        <tr>
        	<td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <%if(permuidcr||permunamecr){ %>
            <td></td>    <%} %>    
            <%if(permuided||permunameed){ %>
            <td></td>    <%} %>                
       </tr>
    </tfoot>
</table>


