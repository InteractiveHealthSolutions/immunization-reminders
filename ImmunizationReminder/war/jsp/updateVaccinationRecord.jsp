
<%@page import="org.ird.immunizationreminder.context.SystemPermissions"%><input type="hidden" id="page_nav_id" value="_li2"><%@page import="org.ird.immunizationreminder.web.utils.UserSessionUtils"%>
<%@page import="org.ird.immunizationreminder.service.exception.UserServiceException"%>
<%@ include file="/WEB-INF/template/headersimple.jsp" %>
            <div class="function-tag">Update Vaccination Record</div>
<script type="text/javascript">
<!--

//-->
</script>
<a href="viewVaccinationRecord.htm" style="font-size: medium;font-stretch:ultra-expanded;text-shadow: maroon;text-decoration: underline;" onclick="goBack();">Go back , Leave page without updating record..</a>

<%
boolean perm=false;
try{
perm=UserSessionUtils.hasActiveUserPermission(SystemPermissions.UPDATE_VACCINATION,request);
}catch(UserServiceException e){
%>
<c:redirect url="login.htm"></c:redirect>
<%
}if(perm){ 
%> 

<%@ include file="/portlets/updateVaccinationRecordform.jsp" %>
<%
}else{
%>
<span class="error" style="font-size: medium; color: red">
<c:out value="Sorry you donot have permissions to update vaccination record. Contact your system administrator."></c:out>
</span>
<%
}
%>
<%@ include file="/WEB-INF/template/footersimple.jsp" %>