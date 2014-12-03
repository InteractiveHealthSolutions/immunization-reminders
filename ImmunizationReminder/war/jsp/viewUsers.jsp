<%@page import="org.ird.immunizationreminder.web.utils.UserSessionUtils"%>
<%@page import="org.ird.immunizationreminder.context.SystemPermissions"%>
<%@page import="org.ird.immunizationreminder.service.exception.UserServiceException"%>

<%@page import="org.ird.immunizationreminder.datamodel.entities.User"%>
<%@page import="com.mysql.jdbc.StringUtils"%><input type="hidden" id="page_nav_id" value="_li2">
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="/WEB-INF/template/pagebody.jsp" %>

            <div class="function-tag">View Users</div>

<%
boolean perms=false;
try{
perms=UserSessionUtils.hasActiveUserPermission(SystemPermissions.VIEW_USERS_DATA,request);
}catch(UserServiceException e){
%>
<c:redirect url="login.htm"></c:redirect>
<%
}if(perms){ 
%> 
<script type="text/javascript"><!--
function viewNewPage(obj){
	document.getElementById("action").value="display";
	var pgNum=obj.title;
	document.getElementById("pagedir").value=pgNum;
	document.getElementById("searchfrm").submit();
}
//-->
</script>
<form id="searchfrm" name="searchfrm" action="viewUsers.htm" method="post">
<div style="overflow: scroll" >
<span style="font-size: small;color: green;">entering user id will override all other search parameters</span>
    <table class="searchTable">
            <tr>
                <td>Enter user id</td>
                <td><input type="text" id="userid" name="userid" value="${lastSearchUserid}"/> </td>
                <td>Enter part of user name</td>
                <td><input type="text" id="usernamepart" name="usernamepart" value="${lastSearchUsernamepart}"/> </td>  
            </tr>
            <tr>
                <td>Enter email</td>
                <td><input type="text" id="useremail" name="useremail" value="${lastSearchUseremail}"/> </td>
                <td>Select status</td>
                <td>
                <input type="hidden" id="userstatusVal" value="${lastSearchUserstatus}"/> 
                <select id="userstatus" name="userstatus">
                	<option></option>
                	<c:forEach  items="<%=User.UserStatus.values()%>" var="status">
                		<option>${status}</option>
                	</c:forEach>               
                </select><br>
                <input type="checkbox" name="userstatusNotchk" value="NOT" <%try{if(!StringUtils.isEmptyOrWhitespaceOnly((String)request.getAttribute("lastSearchUserstatusNotchked"))){%>checked="checked" <%}}catch(Exception e){}%>/>Exclude selected status
                <script><!--
                    sel = document.getElementById("userstatus");
                    val=document.getElementById("userstatusVal").value;
                  	for (i=0; i<sel.options.length; i++) {
                  		if (sel.options[i].text == val) {
                  			sel.selectedIndex = i;
                  		}
                	}
                //-->
                </script>
                </td>  
            </tr>
            <tr>
                <td></td>
                <td></td>
                <td></td>
                <td><input type="submit" value="Search" title="search"/> </td>
            </tr>
    </table>
    <input name="searchlog" type="hidden" value="${searchlog}">
    <div style="color: blue;font-size: small;font-style: italic">${searchlog}</div>
    <div style="color: red;font-size: small;font-style: italic">${resetUserPwdMsg}</div>
    <div style="color: red;font-size: small;font-style: italic">${editmessage}</div>
    <input type="hidden" id="action" name="action" value="search">
    <input type="hidden" id="pagedir" name="pagedir" >
    
    <!-- for preserving search parameters for current pages -->
    <input type="hidden" id="lastSearchUserid" name="lastSearchUserid" value="${lastSearchUserid}">
    <input type="hidden" id="lastSearchUsernamepart" name="lastSearchUsernamepart" value="${lastSearchUsernamepart}">
    <input type="hidden" id="lastSearchUseremail" name="lastSearchUseremail" value="${lastSearchUseremail}">
    <input type="hidden" id="lastSearchUserstatusNotchked" name="lastSearchUserstatusNotchked" value="${lastSearchUserstatusNotchked}">
    <input type="hidden" id="lastSearchUserstatus" name="lastSearchUserstatus" value="${lastSearchUserstatus}">
    
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
            	<%@ include file="/portlets/viewUsergrid.jsp" %>
</div>
</form>
<%
}else{
%>
<span class="error" style="font-size: medium; color: red">
<c:out value="Sorry you donot have permissions to view Users. Contact your system administrator."></c:out>
</span>
<%
}
%>
<%@ include file="/WEB-INF/template/sidebarUsers.jsp" %>
<%@ include file="/WEB-INF/template/footer.jsp" %>