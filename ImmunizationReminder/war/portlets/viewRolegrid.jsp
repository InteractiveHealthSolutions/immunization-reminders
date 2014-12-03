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
    <thead class="header" >
        <tr>
       		<th>---</th>
<%
boolean perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.EDIT_ROLES);
if(perm){ 
%>       		
       		<th>---</th>
<%
}
%>        		
            <th>Role Name</th>
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
      
         <c:forEach items="${model.role}" var="role">
   
      <!--
      <tbody class="alternaterows">
   	 -->
   		 <tr>	
   	 		<td><input type="text" readonly="readonly" value="+" title="${role.roleId}rl" class="expandDataButton" onclick="showDiv(this);"/></td>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.EDIT_ROLES);
if(perm){ 
%>  
<c:choose>
<c:when test='${role.name == "admin"}'>
<td></td>
</c:when>
<c:when test='${role.name == "administrator"}'>
<td></td>
</c:when>
<c:when test='${role.name == "role_administrator"}'>
<td></td>
</c:when>
<c:when test='${role.name == "ADMIN"}'>
<td></td>
</c:when>
<c:when test='${role.name == "ADMINISTRATOR"}'>
<td></td>
</c:when>
<c:when test='${role.name == "ROLE_ADMINISTRATOR"}'>
<td></td>
</c:when>
<c:otherwise>
            <td><a href="editRole.htm?editRecord=${role.name}">Edit</a></td>
</c:otherwise>
</c:choose>
<%
}
%> 
            <td><c:out value="${role.name}"></c:out></td>
            <td><c:out value="${role.description}"></c:out></td>
<%if(permuidcr||permunamecr){ %>            
<td><%if(permuidcr){%>${role.createdByUserId},<%}%><%if(permunamecr){%>${role.createdByUserName}<%}%></td>
<%} %>
            <td>${role.createdDate}</td>
<%if(permuided||permunameed){ %>              
<td><%if(permuided){%>${role.lastEditedByUserId},<%}%><%if(permunameed){%>${role.lastEditedByUserName}<%}%></td>
<%} %>
            <td>${role.lastUpdated}</td> 
        </tr>
       <tr id="${role.roleId}rl" style="display: none;" >
       <td></td>
       <td colspan="7">

       	<table id="${role.roleId}rldv" class="datagrid" width="100%">
           		 <thead class="header">
				       <tr>
				           <th>Permission</th>
				           <th>Description</th>
				       </tr>
				   </thead>
				   <tbody class="rows">
				   <c:forEach items="${role.permissions}" var="perm">
	           		<tr>
	           		 <td><c:out value="${perm.name}"></c:out></td>
	            	 <td><c:out value="${perm.description}"></c:out></td>
	           		</tr>
	           		</c:forEach>
	           	   </tbody>
	           	   <tfoot>
				        <tr>
				       		<td></td>
				            <td></td>
				        </tr>
				    </tfoot>
           		</table>

       </td></tr>
    </c:forEach>
       
   </tbody>
 	<tfoot>
        <tr>

<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.EDIT_ROLES);
if(perm){ 
%> 
            <td></td>
<%} %>
            <td></td>
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


