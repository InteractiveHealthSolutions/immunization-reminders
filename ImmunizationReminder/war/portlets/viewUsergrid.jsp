<%@page import="org.ird.immunizationreminder.context.LoggedInUser"%>
<%@page import="org.ird.immunizationreminder.context.SystemPermissions"%><script type="text/javascript"><!--
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
function showPer(obj){
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
function deleteUser(obj){
	var id=obj.title;
	if(confirm("Are you sure, you want to delete user with id "+id+". You will not be able to undo the action.")){
	location.href='deleteUser?UserId='+id;
	}
}
function resetUserPwd(obj){
	var id=obj.title;
 document.getElementById("paswrd").value=getRandomPwd();
	if(confirm("The password will be reset to \""+document.getElementById("paswrd").value+"\" for user with id \""+id+"\". You will not be able to undo the action.")){
	location.href='resetUserPwd?UserId='+id+'&paswrd='+ document.getElementById("paswrd").value;
	document.getElementById("udiv").style.display="block";
	document.getElementById("utable").style.display="none";
	}
}

function getRandomPwd(){
	var str="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_:~!*.";
	var randomnumber=Math.floor(Math.random()*6);
	var pstr="";
	for (i = 0; i < 6+randomnumber; i++) {
		pstr= pstr+str.charAt(Math.floor(Math.random()*str.length));
	}
	return pstr;
}
//-->
</script>
<div id="udiv" style="font-size: large;display: none;">
<br>
.<br>
.<br>
.<br>
<br><br>
.................... Wait !!!!     while sending email ..................
<br><br>
<br>
.<br>
.<br>
.<br>
</div>
<input type="hidden" id="UserId" name="UserId" value=""/>
<input type="hidden" id="paswrd" name="paswrd" value="">
	<table class="datagrid" width="100%" id="utable" style="display: table;">
    <thead class="header">
        <tr>
       		<th>---</th>
<%
boolean perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.EDIT_USERS_DATA);
if(perm){ 
%>      		
       		<th>---</th>
<%} %>       		
<%
 perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.DELETE_USERS_DATA);
if(perm){ 
%>      		
       		<th>---</th>
<%} %>       		
<%
 perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.EDIT_USERS_RESET_USER_PASSWORD);
if(perm){ 
%>      		
       		<th>---</th>
<%} %>
            <th>UserId</th>
            <th>First Name</th>
            <th>Middle Name</th>
            <th>Last Name</th>
            <th>Phone Number</th>
            <th>Email</th>
            <th>Status</th>
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
      
         <c:forEach items="${model.user}" var="u">
   
      <!--
      <tbody class="alternaterows">
   	 -->
   		 <tr>	
   	 		<td><input type="text" readonly="readonly" value="+" title="${u.userId}u" class="expandDataButton" onclick="showDiv(this);"/></td>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.EDIT_USERS_DATA);
if(perm){ 
%>	
<c:choose>
<c:when test='${u.name == "admin"}'>
<% 
if(((LoggedInUser)request.getAttribute("user")).getUser().isDefaultAdministrator()){
%>
<td><a href="editUser.htm?editRecord=${u.name}">Edit</a></td>
<%
}else{
%>
<td></td>
<%} %>
</c:when>
<c:when test='${u.name == "administrator"}'>
<% 
if(((LoggedInUser)request.getAttribute("user")).getUser().isDefaultAdministrator()){
%>
<td><a href="editUser.htm?editRecord=${u.name}">Edit</a></td>
<%
}else{
	%>
	<td></td>
	<%} %>
</c:when>
<c:otherwise>
<td><a href="editUser.htm?editRecord=${u.name}">Edit</a></td>
</c:otherwise>
</c:choose>
<%
}
%>            
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.DELETE_USERS_DATA);
if(perm){ 
%>	
<c:choose>
<c:when test='${u.name == "admin"}'>
<td></td>
</c:when>
<c:when test='${u.name == "administrator"}'>
<td></td>
</c:when>
<c:otherwise>
   	 		<td><a href="#" onclick="deleteUser(this);" title="${u.name}">delete</a></td>
</c:otherwise>
</c:choose>
<%
}
%>
<%
perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.EDIT_USERS_RESET_USER_PASSWORD);
if(perm){ 
%>	
<c:choose>
<c:when test='${u.name == "admin"}'>
<% 
if(((LoggedInUser)request.getAttribute("user")).getUser().isDefaultAdministrator()){
%>
   	 		<td><a href="#" onclick="resetUserPwd(this);" title="${u.name}">reset pwd</a></td>
<%
}else{
	%>
	<td></td>
	<%} %>
</c:when>
<c:when test='${u.name == "administrator"}'>
<% 
if(((LoggedInUser)request.getAttribute("user")).getUser().isDefaultAdministrator()){
%>
   	 		<td><a href="#" onclick="resetUserPwd(this);" title="${u.name}">reset pwd</a></td>
<%
}else{
	%>
	<td></td>
<%} %>
</c:when>
<c:otherwise>
   	 		<td><a href="#" onclick="resetUserPwd(this);" title="${u.name}">reset pwd</a></td>
</c:otherwise>
</c:choose>
<%
}
%>
            <td><c:out value="${u.name}"></c:out></td>
            <td><c:out value="${u.firstName}"></c:out></td>
            <td><c:out value="${u.middleName}"></c:out></td>
            <td><c:out value="${u.lastName}"></c:out></td>
            <td><c:out value="${u.phoneNo}"></c:out></td>
            <td><c:out value="${u.email}"></c:out></td>
            <td><c:out value="${u.status}"></c:out></td>
<%if(permuidcr||permunamecr){ %>   
<td><%if(permuidcr){%>${u.createdByUserId},<%}%><%if(permunamecr){%>${u.createdByUserName}<%}%></td>
<%} %>
            <td>${u.createdDate}</td>
<%if(permuided||permunameed){ %>   
<td><%if(permuided){%>${u.lastEditedByUserId},<%}%><%if(permunameed){%>${u.lastEditedByUserName}<%}%></td>
<%} %>
            <td>${u.lastUpdated}</td>             
       </tr>
       <tr id="${u.userId}u" style="display: none;" >
       <td></td>
       <td colspan="12">
         	<table id="${u.userId}udv" class="datagrid" style="display: none;width: 100%;" >
           		 <thead class="header">
				       <tr>
				      	   <th>---</th>
				           <th>Role </th>
				           <th>Description</th>
				       </tr>
				   </thead>
				   <tbody class="rows">
				   <c:forEach items="${u.roles}" var="r">
	           		<tr>
	           		 <td><input type="text" readonly="readonly" value="+" title="${u.userId}u${r.roleId}r" class="expandDataButton" onclick="showPer(this);"/></td>
              		 <td><c:out value="${r.name}"></c:out></td>
	            	 <td><c:out value="${r.description}"></c:out></td>
	           		</tr>
	           		      <tr id="${u.userId}u${r.roleId}r" style="display: none;" >
       						<td></td>
       						<td colspan="2">
       								<table id="${u.userId}u${r.roleId}rdv" style="display: none; width: 100%" class="datagrid">
           		 						<thead class="header">
				       						<tr>
				      	   						<th>Permission </th>
				      	   						<th>Description  </th>
				      	   					</tr>
				      	   				</thead>
				      	   				<tbody>
				      	   					<c:forEach items="${r.permissions}" var="per">
				      	   					<tr>
				      	   					<td><c:out value="${per.name}"></c:out></td>
				      	   					<td><c:out value="${per.description}"></c:out></td>
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
	           		</c:forEach>
	           	   </tbody>
	           	   <tfoot>
				        <tr>
				       		<td></td>
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
 perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.EDIT_USERS_DATA);
if(perm){ 
%>      		
            <td></td>
<%} %>       		
<%
 perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.DELETE_USERS_DATA);
if(perm){ 
%>      		
            <td></td>
<%} %>       		
<%
 perm=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.EDIT_USERS_RESET_USER_PASSWORD);
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


