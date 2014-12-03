<%@page import="org.ird.immunizationreminder.context.SystemPermissions"%><input type="hidden" id="page_nav_id" value="_li2"><%@page import="org.ird.immunizationreminder.web.utils.UserSessionUtils"%>
<%@page import="org.ird.immunizationreminder.service.exception.UserServiceException"%>
<%@ include file="/WEB-INF/template/headersimple.jsp" %>
            <div class="function-tag">Edit Child Data</div>
<script type="text/javascript">
<!--
window.onbeforeunload = function (e) {
	  var e = e || window.event;
	  // For IE and Firefox
	  if (e) {
	    e.returnValue = '';
	  }
	  // For Safari
	  return '';
	};
//-->
</script>
<a href="children.htm" style="font-size: medium;font-stretch:ultra-expanded;text-shadow: maroon;text-decoration: underline;">Go back , Leave page without editing data..</a>

<%
boolean perm=false;
try{
perm=UserSessionUtils.hasActiveUserPermission(SystemPermissions.EDIT_CHILDREN_DATA,request);
}catch(UserServiceException e){
%>
<c:redirect url="login.htm"></c:redirect>
<%
}if(perm){ 
%> 

<%@ include file="/portlets/childEditform.jsp" %>
<%
}else{
%>
<span class="error" style="font-size: medium; color: red">
<c:out value="Sorry you donot have permissions to perform task. Contact your system administrator."></c:out>
</span>
<%
}
%>
<%@ include file="/WEB-INF/template/footersimple.jsp" %>