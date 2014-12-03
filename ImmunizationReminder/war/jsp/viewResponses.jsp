<%@page import="org.ird.immunizationreminder.web.utils.UserSessionUtils"%>
<%@page import="org.ird.immunizationreminder.context.SystemPermissions"%>
<%@page import="org.ird.immunizationreminder.service.exception.UserServiceException"%>
<input type="hidden" id="page_nav_id" value="_li2">
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="/WEB-INF/template/pagebody.jsp" %>

            <div class="function-tag">View Responses</div>
<%
boolean perms=false;
try{
perms=UserSessionUtils.hasActiveUserPermission(SystemPermissions.VIEW_RESPONSE_HISTORY,request);
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
function clearDate(){
	document.getElementById("date1").value="";
	document.getElementById("date2").value="";

}
//-->
</script>
<form id="searchfrm" name="searchfrm" action="viewResponses.htm" method="post">
<div style="overflow: scroll" >
    <table class="searchTable">
            <tr>
                <td>Enter child id or name </td>
                <td><input type="text" id="childId" name="childId" value="${lastSearchChildId}"/></td>
            </tr>
            <tr>
                <td>Enter date received</td>
                <td>
                	<input id="date1" name="date1" onclick='scwShow(this,event);' readonly="readonly" value="${lastSearchDate1}"/>
                    <input onclick="scwShow(scwID('date1'),event);" style="cursor:pointer;width: 16px;height:16px;background-image: url('images/calendar_icon.png');"/>
		  			<span class="searchCalendarTag">:  Start</span>
					<br>
                	<input id="date2" name="date2" onclick='scwShow(this,event);' readonly="readonly" value="${lastSearchDate2}"/>
                    <input onclick="scwShow(scwID('date2'),event);" style="cursor:pointer;width: 16px;height:16px;background-image: url('images/calendar_icon.png');"/>
                    <span class="searchCalendarTag">:  End<br></span>
		  			<a href="#" style=" width: 2cm;text-decoration: underline;cursor: pointer;font-style: italic ;font-size: small;" onclick="clearDate();">ClearDate</a>					
                </td>
            </tr>
            <tr>
                <td>Enter cell number</td>
                <td><input type="text" id="cellNum" name="cellNum" value="${lastSearchCellNum}"/> </td>
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
           </tr>
           <tr>
                <td></td>
                <td><br><input type="submit" value="Search" title="search" /> </td>
            </tr>
       
    </table>
    <input name="searchlog" type="hidden" value="${searchlog}">
    <div style="color: blue;font-size: small;font-style: italic">${searchlog}</div>
    <div style="color: red;font-size: small;font-style: italic">${message}</div>
    <div style="color: red;font-size: small;font-style: italic">${editmessage}</div>
    <input type="hidden" id="action" name="action" value="search">
    <input type="hidden" id="pagedir" name="pagedir" >
    
    <!-- for preserving search parameters for current pages -->
    <input type="hidden" id="lastSearchChildId" name="lastSearchChildId" value="${lastSearchChildId}">
    <input type="hidden" id="lastSearchDate1" name="lastSearchDate1" value="${lastSearchDate1}">
    <input type="hidden" id="lastSearchDate2" name="lastSearchDate2" value="${lastSearchDate2}">
    <input type="hidden" id="lastSearchCellNum" name="lastSearchCellNum" value="${lastSearchCellNum}">
    <input type="hidden" id="lastSearchArmName" name="lastSearchArmName" value="${lastSearchArmName}">

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
            	<%@ include file="/portlets/viewResponsegrid.jsp" %>
</div>
</form>
<%
}else{
%>
<span class="error" style="font-size: medium; color: red">
<c:out value="Sorry you donot have permissions to view Child Responses. Contact your system administrator."></c:out>
</span>
<%
}
%>
<br>
<br>
<br>
<%@ include file="/WEB-INF/template/sidebarResponses.jsp" %>
<%@ include file="/WEB-INF/template/footer.jsp" %>