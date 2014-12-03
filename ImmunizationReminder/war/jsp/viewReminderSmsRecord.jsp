<%@page import="org.ird.immunizationreminder.web.utils.UserSessionUtils"%>
<%@page import="org.ird.immunizationreminder.context.SystemPermissions"%>
<%@page import="org.ird.immunizationreminder.service.exception.UserServiceException"%>

<%@page import="org.ird.immunizationreminder.datamodel.entities.ReminderSms"%>
<%@page import="com.mysql.jdbc.StringUtils"%>

<%@page import="org.ird.immunizationreminder.datamodel.entities.ReminderSms.REMINDER_STATUS"%><input type="hidden" id="page_nav_id" value="_li2">
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="/WEB-INF/template/pagebody.jsp" %>

            <div class="function-tag">View Reminder Sms Record</div>
<%
boolean perms=false;
try{
perms=UserSessionUtils.hasActiveUserPermission(SystemPermissions.VIEW_REMINDER_SMS_HISTORY,request);
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
function clearDatedue(){
	document.getElementById("duedate1").value="";
	document.getElementById("duedate2").value="";

}
function clearDatesent(){
	document.getElementById("sentdate1").value="";
	document.getElementById("sentdate2").value="";

}
//-->
</script>
<form id="searchfrm" name="searchfrm" action="viewReminderSmsRecord.htm" method="post">
<div style="overflow: scroll" >
    <table class="searchTable">
            <tr>
                <td>Enter child id or name </td>
                <td><input type="text" id="childId" name="childId" value="${lastSearchChildId}"/></td>
                <td>Enter cell number</td>
                <td><input type="text" id="cellNum" name="cellNum" value="${lastSearchCellNum}"/> </td>
            </tr>
            <tr>
                <td>Enter due date</td>
                <td>
                	<input id="duedate1" name="duedate1" onclick='scwShow(this,event);' readonly="readonly" value="${lastSearchDuedate1}"/>
                    <input onclick="scwShow(scwID('duedate1'),event);" style="cursor:pointer;width: 16px;height:16px;background-image: url('images/calendar_icon.png');"/>
					<span class="searchCalendarTag">:  Start</span>
					<br>
                	<input id="duedate2" name="duedate2" onclick='scwShow(this,event);' readonly="readonly" value="${lastSearchDuedate2}"/>
                    <input onclick="scwShow(scwID('duedate2'),event);" style="cursor:pointer;width: 16px;height:16px;background-image: url('images/calendar_icon.png');"/>
		  			<span class="searchCalendarTag">:  End<br></span>
		  			<a href="#" style=" text-decoration: underline;cursor: pointer;font-style: italic ;font-size: small;" onclick="clearDatedue();">ClearDate</a>					
                </td>
                <td>Enter sent date</td>
                <td>
                	<input id="sentdate1" name="sentdate1" onclick='scwShow(this,event);' readonly="readonly" value="${lastSearchSentdate1}"/>
                    <input onclick="scwShow(scwID('sentdate1'),event);" style="cursor:pointer;width: 16px;height:16px;background-image: url('images/calendar_icon.png');"/>
					<span class="searchCalendarTag">:  Start</span>
					<br>
                	<input id="sentdate2" name="sentdate2" onclick='scwShow(this,event);' readonly="readonly" value="${lastSearchSentdate2}"/>
                    <input onclick="scwShow(scwID('sentdate2'),event);" style="cursor:pointer;width: 16px;height:16px;background-image: url('images/calendar_icon.png');"/>
		  			<span class="searchCalendarTag">:  End<br></span>
		  			<a href="#" style=" text-decoration: underline;cursor: pointer;font-style: italic ;font-size: small;" onclick="clearDatesent();">ClearDate</a>					
                </td>                
            </tr>
            <tr>
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
                <td><!--Select Vaccine--></td>
                <td>     
                <!--<input type="hidden" id="vaccNameVal" value="${lastSearchVaccineName}"/> 
                <select id="vaccName" name="vaccName">
                	<option></option>
                	<c:forEach  items="${model.vaccine}" var="vacc">
                		<option>${vacc.name}</option>
                	</c:forEach>               
                </select>
                <script>
                    sel = document.getElementById("vaccName");
                    val=document.getElementById("vaccNameVal").value;
                  	for (i=0; i<sel.options.length; i++) {
                  		if (sel.options[i].text == val) {
                  			sel.selectedIndex = i;
                  		}
                	}
                //
                </script>
                -->
                </td>
           </tr>
            <tr>
                <td>Select reminder status</td>
                <td>  
                <input type="hidden" id="remstatusVal" value="${lastSearchRemstatus}"/> 
                <select id="remstatus" name="remstatus">
                	<option></option>
                	<c:forEach  items="<%=ReminderSms.REMINDER_STATUS.values()%>" var="remst">
                		<option>${remst}</option>
                	</c:forEach>               
                </select>
                <br><input type="checkbox" name="remstatusNotchk" value="NOT" <%try{if(!StringUtils.isEmptyOrWhitespaceOnly((String)request.getAttribute("lastSearchRemstatusNotchked"))){%>checked="checked" <%}}catch(Exception e){}%>/>Exclude selected status
                <script><!--
                    sel = document.getElementById("remstatus");
                    val=document.getElementById("remstatusVal").value;
                  	for (i=0; i<sel.options.length; i++) {
                  		if (sel.options[i].text == val) {
                  			sel.selectedIndex = i;
                  		}
                	}
                //-->
                </script>
                </td>
                <td>Select reminder</td>
                <td>     
                <input type="hidden" id="remNameVal" value="${lastSearchReminderName}"/> 
                <select id="remName" name="remName">
                	<option></option>
                	<c:forEach  items="${model.reminder}" var="rem">
                		<option>${rem.name}</option>
                	</c:forEach>               
                </select>
                <script><!--
                    sel = document.getElementById("remName");
                    val=document.getElementById("remNameVal").value;
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
                <td><div style="font-size: small;color: maroon;"><c:forEach items="<%=ReminderSms.REMINDER_STATUS.values()%>" var="rr">
           		${rr}: ${rr.REPRESENTATION}<br>
           		</c:forEach></div></td>
                <td><br><input type="submit" value="Search" title="search"/> </td>
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
    <input type="hidden" id="lastSearchCellNum" name="lastSearchCellNum" value="${lastSearchCellNum}">
    <input type="hidden" id="lastSearchDuedate1" name="lastSearchDuedate1" value="${lastSearchDuedate1}">
    <input type="hidden" id="lastSearchDuedate2" name="lastSearchDuedate2" value="${lastSearchDuedate2}">
    <input type="hidden" id="lastSearchSentdate1" name="lastSearchSentdate1" value="${lastSearchSentdate1}">
    <input type="hidden" id="lastSearchSentdate2" name="lastSearchSentdate2" value="${lastSearchSentdate2}">
    <input type="hidden" id="lastSearchArmName" name="lastSearchArmName" value="${lastSearchArmName}">
    <input type="hidden" id="lastSearchVaccineName" name="lastSearchVaccineName" value="${lastSearchVaccineName}">
    <input type="hidden" id="lastSearchRemstatus" name="lastSearchRemstatus" value="${lastSearchRemstatus}">
    <input type="hidden" id="lastSearchReminderName" name="lastSearchReminderName" value="${lastSearchReminderName}">
    <input type="hidden" id="lastSearchRemstatusNotchked" name="lastSearchRemstatusNotchked" value="${lastSearchRemstatusNotchked}">

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
            	<%@ include file="/portlets/viewRemindersmsgrid.jsp" %>
</div>
</form>
<%
}else{
%>
<span class="error" style="font-size: medium; color: red">
<c:out value="Sorry you donot have permissions to view Child Reminder Sms Record. Contact your system administrator."></c:out>
</span>
<%
}
%>
<br>
<br>
<br>
<%@ include file="/WEB-INF/template/sidebarReminders.jsp" %>
<%@ include file="/WEB-INF/template/footer.jsp" %>