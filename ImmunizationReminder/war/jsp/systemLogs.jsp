
<%@page import="org.ird.immunizationreminder.web.utils.UserSessionUtils"%>
<%@page import="org.ird.immunizationreminder.context.SystemPermissions"%>
<%@page import="org.ird.immunizationreminder.service.exception.UserServiceException"%><input type="hidden" id="page_nav_id" value="_li7">
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="/WEB-INF/template/pagebody.jsp" %>

<div class="function-tag">View System Log Files</div>
<%
boolean perm=false;
try{
perm=UserSessionUtils.hasActiveUserPermission(SystemPermissions.VIEW_SYSTEM_LOGS,request);
}catch(UserServiceException e){
%>
<c:redirect url="login.htm"></c:redirect>
<%
}
if(perm){%>
<script type="text/javascript">
<!--
var win;
function viewDet(){
	win=window.open('hibernateStats.htm','HibernateStatistics','width=500,height=600,resizable=no,toolbar=no,location=no,scrollbars=yes,directories=no,status=no,menubar=no,copyhistory=no');
	win.focus();
}//-->
</script>
<br>
--<a href="#" onclick="viewDet();">View Hibernate Stats</a>
<br>
<br>
<br>
--<a href="downloadLog?logfile=vweblog" >View Current Web Log</a>
<br>
<br>
----<a href="downloadLog?logfile=dweblog" >Download Complete Web Log</a>
<br>
<br>
<br>
--<a href="downloadLog?logfile=vmodemlog" >View Current Modem Log</a>
<br>
<br>
----<a href="downloadLog?logfile=dmodemlog" >Download Complete Modem Log</a>
<br>
<br>
<br>
--<a href="downloadLog?logfile=vdailydetailedlog" >View Current Daily Detailed Log</a>
<br>
<br>
----<a href="downloadLog?logfile=ddailydetailedlog" >Download Complete Daily Detailed Log</a>
<br>
<br>
<br>
--<a href="downloadLog?logfile=vunsentreminder" >View Current Unsent Reminders </a>
<br>
<br>
----<a href="downloadLog?logfile=dunsentreminder" >Download Complete Unsent Reminder Log </a>
<br>
<br>
<br>
--<a href="downloadLog?logfile=vfailedflag" >View Current Failed Flags</a>
<br>
<br>
----<a href="downloadLog?logfile=dfailedflag" >Download Complete Failed Flag Log</a>
<br>
<br>
<br>
--<a href="downloadLog?logfile=vreceivedresponse" >View Current Received Messages </a>
<br>
<br>
----<a href="downloadLog?logfile=dreceivedresponse" >Download Complete Received Messages Log </a>
<br>
<br>
<br>
--<a href="downloadLog?logfile=vunsavedresponse" >View Current Unsaved Responses</a>
<br>
<br>
----<a href="downloadLog?logfile=dunsavedresponse" >Download Complete Unsaved Responses Log</a>
<br>
<br>
<br>
--<a href="downloadLog?logfile=vunsavedreminder" >View Current Unsaved Reminders </a>
<br>
<br>
----<a href="downloadLog?logfile=dunsavedreminder" >Download Complete Unsaved Reminders Log </a>
<br>
<br>
<br><!--//Donot work with hierarchy of files. need to have work done with servlet
--<a href="downloadLog?logfile=dcompleterecord" >Download Complete Log Record</a>
<br>
--><%}else{ %>

<span class="error" style="font-size: medium; color: red">
<c:out value="Sorry! You donot have permissions to perform task. Contact your system administrator."></c:out>
</span>

<%} %>
<%@ include file="/WEB-INF/template/sidebarReporting.jsp" %>
<%@ include file="/WEB-INF/template/footer.jsp" %>