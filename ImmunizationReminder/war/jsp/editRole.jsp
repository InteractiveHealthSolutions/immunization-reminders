
<%@page import="org.ird.immunizationreminder.web.utils.UserSessionUtils"%>
<%@page import="org.ird.immunizationreminder.context.SystemPermissions"%>
<%@page import="org.ird.immunizationreminder.service.exception.UserServiceException"%>
<input type="hidden" id="page_nav_id" value="_li6">

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="/WEB-INF/template/pagebody.jsp" %>
            <div class="function-tag">Edit Role</div>
<%
boolean perm=false;
try{
perm=UserSessionUtils.hasActiveUserPermission(SystemPermissions.EDIT_ROLES,request);
}catch(UserServiceException e){
%>
<c:redirect url="login.htm"></c:redirect>
<%
}if(perm){ 
%> 
<%@ include file="/portlets/roleEditform.jsp" %>
<%
}else{
%>
<span class="error" style="font-size: medium; color: red">
<c:out value="Sorry you donot have permissions to edit roles. Contact your system administrator."></c:out>
</span>
<%
}
%>
<%@ include file="/WEB-INF/template/sidebarUsers.jsp" %>
<%@ include file="/WEB-INF/template/footer.jsp" %>