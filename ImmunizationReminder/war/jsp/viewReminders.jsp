<%@page import="org.ird.immunizationreminder.web.utils.UserSessionUtils"%>
<%@page import="org.ird.immunizationreminder.context.SystemPermissions"%>
<%@page import="org.ird.immunizationreminder.service.exception.UserServiceException"%>
<input type="hidden" id="page_nav_id" value="_li2">
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="/WEB-INF/template/pagebody.jsp" %>

            <div class="function-tag">View Reminders</div>
<%
boolean perms=false;
try{
perms=UserSessionUtils.hasActiveUserPermission(SystemPermissions.VIEW_REMINDERS,request);
}catch(UserServiceException e){
%>
<c:redirect url="login.htm"></c:redirect>
<%
}if(perms){ 
%> 
<script type="text/javascript">
<!--
function viewNewPage(obj){
	document.getElementById("action").value="display";
	var pgNum=obj.title;
	document.getElementById("pagedir").value=pgNum;
	document.getElementById("searchfrm").submit();
}
//-->
</script>
<form id="searchfrm" name="searchfrm" action="viewReminders.htm" method="post">
<div style="overflow: scroll" >
    <table class="searchTable">
             <tr>
                <td>Enter reminder name</td>
                <td><input type="text" id="reminderName" name="reminderName" value="${lastSearchRemName}"/> </td>
           </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Search" title="search"/> </td>
                <td></td>
                <td></td>
            </tr>
       
    </table>
    <input name="searchlog" type="hidden" value="${searchlog}">
    <div style="color: blue;font-size: small;font-style: italic">${searchlog}</div>
    <div style="color: red;font-size: small;font-style: italic">${editOrUpdateMessage}</div>
    <div style="color: red;font-size: small;font-style: italic">${editmessage}</div>
    <input type="hidden" id="action" name="action" value="search">
    <input type="hidden" id="pagedir" name="pagedir" >
    
    <!-- for preserving search parameters for current pages -->
    <input type="hidden" id="lastSearchRemName" name="lastSearchRemName" value="${lastSearchRemName}">

    <input type="hidden" id="currentPage" name="currentPage" value="${currentPage}">
    <input type="hidden" id="totalPages" name="totalPages" value="${totalPages}">
    <input type="hidden" id="currentRows" name="currentRows" value="${currentRows}">
    <input type="hidden" id="totalRows" name="totalRows" value="${totalRows}">
    
         	<table>
            	<tr>
            <% 	
            	int currentPage=(Integer)request.getAttribute("currentPage");
            	int totalPages=(Integer)request.getAttribute("totalPages");
            	String pagesText=(currentPage+1)+" of "+(totalPages+1);
            	int currentRows=(Integer)request.getAttribute("currentRows");
            	int totalRows=(Integer)request.getAttribute("totalRows");
            %>            	
            	<td  style="font-size: small;color: purple;">Total~Rows:(<%=totalRows%>)<br>Page:(<%=pagesText%>)</td>
            	<td  style="padding-left: 5px"><%if(currentPage>0){ %><a href="#" title="<%=currentPage-1 %>" onclick="viewNewPage(this);"/>Prev</a> <%}else{%>Prev<%} %>, <c:forEach begin="0" end="<%= totalPages %>" var="i"><a href="#"title="<c:out value="${i}" />" onclick="viewNewPage(this);"><c:if test="${i == currentPage}">>></c:if>${i+1}</a> ,</c:forEach>
            	<%if(currentPage<totalPages){ %><a href="#" title="<%=currentPage+1 %>" onclick="viewNewPage(this);"/>Next</a><%}else{%>Next<%} %>
            	</td>
            	</tr>
            </table>
            	<%@ include file="/portlets/viewRemindergrid.jsp" %>
</div>
</form>
<%
}else{
%>
<span class="error" style="font-size: medium; color: red">
<c:out value="Sorry you donot have permissions to view Reminders. Contact your system administrator."></c:out>
</span>
<%
}
%>
<br>
<br>
<br>
<%@ include file="/WEB-INF/template/sidebarReminders.jsp" %>
<%@ include file="/WEB-INF/template/footer.jsp" %>