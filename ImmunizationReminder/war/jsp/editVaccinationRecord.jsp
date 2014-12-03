<%@page import="org.ird.immunizationreminder.service.exception.UserServiceException"%>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="/WEB-INF/template/pagebody.jsp" %>
<%@page import="org.ird.immunizationreminder.web.utils.UserSessionUtils"%>
<%@page import="org.ird.immunizationreminder.context.SystemPermissions"%>
<input type="hidden" id="page_nav_id" value="_li6">
            <div class="function-tag">Edit Vaccination Record</div>
<%
boolean perm=false;
try{
perm=UserSessionUtils.hasActiveUserPermission(SystemPermissions.EDIT_VACCINATION_RECORD,request);
}catch(UserServiceException e){
%>
<c:redirect url="login.htm"></c:redirect>
<%
}if(perm){ 
%> 
<%@ include file="/portlets/vaccinationRecordEditform.jsp" %>
<%
}else{
%>
<span class="error" style="font-size: medium; color: red">
<c:out value="Sorry you donot have permissions to add Vaccination Record. Contact your system administrator."></c:out>
</span>
<%
}
%>
<%@ include file="/WEB-INF/template/sidebarVaccines.jsp" %>
<%@ include file="/WEB-INF/template/footer.jsp" %>