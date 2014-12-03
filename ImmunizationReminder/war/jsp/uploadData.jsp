
<%@page import="org.ird.immunizationreminder.web.utils.UserSessionUtils"%>
<%@page import="org.ird.immunizationreminder.service.exception.UserServiceException"%>
<%@page import="org.ird.immunizationreminder.context.SystemPermissions"%><input type="hidden" id="page_nav_id" value="_li6">

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="/WEB-INF/template/pagebody.jsp" %>

            <div class="function-tag">Upload Children Data</div>
<%
boolean perm=false;
try{
perm=UserSessionUtils.hasActiveUserPermission(SystemPermissions.UPLOAD_DATA,request);
}catch(UserServiceException e){
%>
<c:redirect url="login.htm"></c:redirect>
<%
}if(perm){ 
%> 
<%@ include file="/portlets/uploadchildDataform.jsp" %>
<%
}else{
%>
<span class="error" style="font-size: medium; color: red">
<c:out value="Sorry you donot have permissions to perform task. Contact your system administrator."></c:out>
</span>
<%
}
%>
<%@ include file="/WEB-INF/template/sidebarChildren.jsp" %>
