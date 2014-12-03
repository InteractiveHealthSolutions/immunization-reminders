<%@page import="org.ird.immunizationreminder.web.utils.UserSessionUtils"%>
<%@page import="org.ird.immunizationreminder.context.SystemPermissions"%>
<%@page import="org.ird.immunizationreminder.service.exception.UserServiceException"%>
<%@page import="org.ird.immunizationreminder.datamodel.entities.Child"%>
<%@page import="com.mysql.jdbc.StringUtils"%>
<input type="hidden" id="page_nav_id" value="_li2">

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="/WEB-INF/template/pagebody.jsp" %>
            <div class="function-tag">View Children</div>

<%
boolean perms=false;
try{
perms=UserSessionUtils.hasActiveUserPermission(SystemPermissions.VIEW_CHILDREN_DATA,request);
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
<form id="searchfrm" name="searchfrm" action="viewChildren.htm" method="post">
<div style="overflow: scroll" >
    <table class="searchTable">
            <tr>
                <td>Enter child id</td>
                <td><input type="text" id="childid" name="childid" value="${lastSearchChildid}"/> </td>
                <td>Enter part of child name</td>
                <td><input type="text" id="childnamepart" name="childnamepart" value="${lastSearchChildnamepart}"/> </td>  
            </tr>
            <tr>
                <td>Enter current cell number</td>
                <td><input type="text" id="currentcell" name="currentcell" value="${lastSearchCurrentcell}"/> </td>
                <td>Enter center name</td>
                <td><input type="text" id="clinic" name="clinic" value="${lastSearchClinic}"/> </td>  
            </tr>
            <tr>
                <td>Select arm</td>
                <td>     
                <input type="hidden" id="armNameVal" value="${lastSearchArmName}"/> 
                <select id="armName" name="armName">
                	<option></option>
                	<c:forEach  items="${model.arm}" var="arm">
                		<option>${arm.armName}</option>
                	</c:forEach>               
                </select>
                <script><!--
                    sel = document.getElementById("armName");
                    val=document.getElementById("armNameVal").value;
                  	for (i=0; i<sel.options.length; i++) {
                  		if (sel.options[i].text == val) {
                  			sel.selectedIndex = i;
                  		}
                	}
                //-->
                </script>
                </td>
                <td>Select follow up status</td>
                <td>
                <input type="hidden" id="followupstatusVal" value="${lastSearchFollowupstatus}"/> 
                <select id="followupstatus" name="followupstatus">
                	<option></option>
                	<c:forEach  items="<%=Child.STATUS.values()%>" var="status">
                		<option>${status}</option>
                	</c:forEach>               
                </select><br>
                <input type="checkbox" name="followupstatusNotchk" value="NOT" <%try{if(!StringUtils.isEmptyOrWhitespaceOnly((String)request.getAttribute("lastSearchFollowupstatusNotchked"))){%>checked="checked" <%}}catch(Exception e){}%>/>Exclude selected status
                <script>
                <!--
                    sel = document.getElementById("followupstatus");
                    val=document.getElementById("followupstatusVal").value;
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
                <td>Enter Epi/MR Number</td>
                <td><input type="text" id="mrNumber" name="mrNumber" value="${lastSearchMrNum}"/></td>
                <td><div style="font-size: small;color: maroon;"><c:forEach items="<%=Child.STATUS.values()%>" var="trt">
           		${trt} :  ${trt.REPRESENTATION}<br>
           		</c:forEach></div></td>
                <td ><input type="submit" value="Search" title="search" /></td>
            </tr>
    </table>
    <input name="searchlog" type="hidden" value="${searchlog}">
    <div style="color: blue;font-size: small;font-style: italic">${searchlog}</div>
    <div style="color: red;font-size: small;font-style: italic">${editOrUpdateMessage}</div>
    <div style="color: red;font-size: small;font-style: italic">${editmessage}</div>
    <input type="hidden" id="action" name="action" value="search">
    <input type="hidden" id="pagedir" name="pagedir" >
    
    <!-- for preserving search parameters for current pages -->
    <input type="hidden" id="lastSearchChildid" name="lastSearchChildid" value="${lastSearchChildid}">
    <input type="hidden" id="lastSearchChildnamepart" name="lastSearchChildnamepart" value="${lastSearchChildnamepart}">
    <input type="hidden" id="lastSearchCurrentcell" name="lastSearchCurrentcell" value="${lastSearchCurrentcell}">
    <input type="hidden" id="lastSearchClinic" name="lastSearchClinic" value="${lastSearchClinic}">
    <input type="hidden" id="lastSearchArmName" name="lastSearchArmName" value="${lastSearchArmName}">
    <input type="hidden" id="lastSearchFollowupstatusNotchked" name="lastSearchFollowupstatusNotchked" value="${lastSearchFollowupstatusNotchked}">
    <input type="hidden" id="lastSearchFollowupstatus" name="lastSearchFollowupstatus" value="${lastSearchFollowupstatus}">
    <input type="hidden" id="lastSearchMrNum" name="lastSearchMrNum" value="${lastSearchMrNum}">
    
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
            	<%@ include file="/portlets/viewChildgrid.jsp" %>
</div>
</form>
<%
}else{
%>
<span class="error" style="font-size: medium; color: red">
<c:out value="Sorry you donot have permissions to view Children details. Contact your system administrator."></c:out>
</span>
<%
}
%>
<br><br>
<%@ include file="/WEB-INF/template/sidebarChildren.jsp" %>
<%@ include file="/WEB-INF/template/footer.jsp" %>