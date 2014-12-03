<input type="hidden" id="page_nav_id" value="_li2">

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="/WEB-INF/template/pagebody.jsp" %>
<div>
<table width="100%">
<tr>
<td width="20%">
<div>
<img alt="" src="images/img10.jpg" height="150px" width="150px" style="opacity:0.8;filter:alpha(opacity=80)">
<img alt="" src="images/img1.jpg" height="150px" width="150px" style="opacity:0.9;filter:alpha(opacity=90)">
<img alt="" src="images/img3.jpg" height="150px" width="150px" style="opacity:0.7;filter:alpha(opacity=70)">
</div>
</td>
<td width="60%" style="text-align: left;vertical-align: text-top">
<span style="color: blue;">The "Child's" data interface </span>
<span style="font-size: small">
 allows you to view and manipulate all data including child's demographic details and associated vaccinations with them, provided that you have sufficient level of permissions and privileges. 
</span>
<br><br>
<span style="color: green;">View Children and Edit their Details.</span>
<br>
<span style="font-size: small">This interface allows you to view complete details of children including their demographic and programme details. You can also edit child data or update vaccination record if it has been vaccinated and assigned new date. Use this interface by navigating to edit page provided against each child's summary data. To view complete data or to modify any field you must have permissions assigned to your role. If you are unable to modify data or have any difficulty in viewing child details, just contact your system administrator to assign you respective privileges.
</span>
<br><br>
<span style="color: green;">Add a child</span>
<br>
<span style="font-size: small">Again you must have permission to add any child data into the system. With the interface you can add a child to the system, record the first vacciantion it recieved and assign a new vaccination date to continue process. Remember that you can not assign a "child id" ,already assigned to another child already existing in the system, to any other child being added to the system. Neither you can assign a cell number to the child details being added to the system, that is currently occupied by any other child currently under follow up. Also be very careful while assigning "Child id" as you will not be able to modify the field.
</span>

</td>
<td width="20%">
<div>
<img alt="" src="images/img5.jpg" height="150px" width="150px" style="opacity:0.8;filter:alpha(opacity=80)">
<img alt="" src="images/img13.jpeg" height="150px" width="150px" style="opacity:0.9;filter:alpha(opacity=90)">
<img alt="" src="images/img14.jpg" height="150px" width="150px" style="opacity:0.7;filter:alpha(opacity=70)">
</div>
</td>
</tr>
</table>

</div>
<%@ include file="/WEB-INF/template/sidebarChildren.jsp" %>
<%@ include file="/WEB-INF/template/footer.jsp" %>