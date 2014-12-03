<%@page import="org.ird.immunizationreminder.context.LoggedInUser"%>
<%@page import="org.ird.immunizationreminder.context.SystemPermissions"%>
<script type='text/javascript' src='/ImmunizationReminder/dwr/interface/DWRCsvService.js'></script>
<script type='text/javascript' src='/ImmunizationReminder/dwr/engine.js'></script>
<script type='text/javascript' src='/ImmunizationReminder/dwr/util.js'></script>
<script type="text/javascript">
<!--
var newwindow;

function viewreport(obj){
	var id=obj.title;
	DWRCsvService.getCsvUploaded(id,resultReport);
}
var resultReport=function(result){
	newwindow=window.open();
	newdocument=newwindow.document;
	newdocument.write(" <style>"+
"	 .datalist		{border: 0px solid #808080 ;padding: 4px;text-align: left;}"+
"	 .datalist th	{font-family: arial;font-weight: bold; font-size: 10;color:#808080;padding: 4px;background-color:#F0E1DA; }"+
"	 .datalist td	{font-family: arial;font-size: 10;color: #808990;padding: 4px;background-color: #FEF9F9;}"+
"  </style>");
	newdocument.write(result);
	newdocument.close();
	newwindow.focus();
};
//-->
</script>
	<table class="datagrid" width="100%">
    <thead class="header">
        <tr>
            <th>---</th>
       		<th>---</th>
       		<th>Record Number</th>
       		<th>Date Uploaded</th>
			<th>Total Rows</th>
            <th>Rows Rejected</th>
            <th>Rows Saved</th>
            <th>Upload Process Errors</th>
<%
boolean permuid=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERS_EDITOR_USER_ID);
boolean permuname=((LoggedInUser)request.getAttribute("user")).hasPermission(SystemPermissions.VIEW_USERS_EDITOR_USER_NAME);
boolean perm=permuid||permuname;
if(perm){ 
%>            
           <th>Uploader(<%if(permuid){%>Id,<%}%><%if(permuname){%>Name<%}%>)</th>
<%
}
%>             
        </tr>
    </thead>
      <tbody class="rows">
         <c:forEach items="${model.csvreports}" var="csvr">
      <!--
      <tbody class="alternaterows">
   	 -->
   		 <tr>
   	 		<td><a href="downloadCsvUploaded?recordId=${csvr.recordNumber}" >download csv</a></td>
   	 		<td><a href="#" onclick="viewreport(this);" title="${csvr.recordNumber}">view report</a></td>
   	 		<td>${csvr.recordNumber}</td>
            <td>${csvr.dateUploaded}</td>
            <td>${csvr.numberOfRows}</td>
            <td><c:out value="${csvr.numberOfRowsRejected}"></c:out></td>
            <td><c:out value="${csvr.numberOfRowsSaved}"></c:out></td>
            <td><c:out value="${csvr.uploadErrors}"></c:out></td>
<%if(permuid||permuname){ %>   
<td><%if(permuid){%>${csvr.uploadedByUserId},<%}%><%if(permuname){%>${csvr.uploadedByUserName}<%}%></td>
<%} %>
       </tr>
    </c:forEach>
       
   </tbody>
 	<tfoot>
        <tr>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <%if(permuid||permuname){ %>
            <td></td>    <%} %>                   
       </tr>
    </tfoot>
</table>


