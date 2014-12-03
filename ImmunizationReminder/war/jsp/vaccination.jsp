<input type="hidden" id="page_nav_id" value="_li2">
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="/WEB-INF/template/pagebody.jsp" %>
<div>
<table width="100%">
<tr>
<td width="20%">
<div>
<img alt="" src="images/img4.jpg" height="100px" width="100px" style="opacity:0.8;filter:alpha(opacity=80)">
<img alt="" src="images/img1.jpg" height="100px" width="100px" style="opacity:0.9;filter:alpha(opacity=90)">
<img alt="" src="images/img10.jpg" height="100px" width="100px" style="opacity:0.7;filter:alpha(opacity=70)">
<img alt="" src="images/img2.jpg" height="100px" width="100px" style="opacity:0.8;filter:alpha(opacity=80)">
</td>
<td width="60%" style="text-align: left;vertical-align: text-top">
<span style="color: blue;">The Vaccination interface </span>
<span style="font-size: small">
 allows you to view and manipulate all Vaccines or children vaccination records, provided that you have sufficient level of permissions and privileges. 
</span>
<br><br>
<span style="color: green;">View / Add Vaccines</span>
<br>
<span style="font-size: small">These interfaces allow you to view all vaccines existing in the system or add a new vaccine if needed. You can also edit vaccine by navigating to edit page provided against each vaccine's data. To add or modify vaccine data you must have permissions assigned to your role.
</span>
<br><br>
<span style="color: green;">View / Edit Vaccination record</span>
<br>
<span style="font-size: small">This interface allows you to view complete details of children vaccination record including status and other details.As reminders will be scheduled before 2 days of their due date hence a vaccination record can be edited i.e child's vaccination date can be moved forward or backward 
 only if newly assigned duedate minus least reminder day number has not passed and is 2 or more days forward. Only vaccination records that are PENDING can be changed their dates. No modifications can be made to records that have any other status other than PENDING.
</span>
<br><br>
<span style="color: green;">Update Vaccination record</span>
<br>
<span style="font-size: small">This interface allows you to update child vaccination record when it comes again for vaccination.
Its neccessary to assign a new vaccination date if child's followup process is not completed. otherwise make the vaccination record as last vaccination record.</span>
</td>
<td width="20%">
<div>
<img alt="" src="images/img5.jpg" height="100px" width="100px" style="opacity:0.8;filter:alpha(opacity=80)">
<img alt="" src="images/img6.jpg" height="100px" width="100px" style="opacity:0.9;filter:alpha(opacity=90)">
<img alt="" src="images/img13.jpeg" height="100px" width="100px" style="opacity:0.9;filter:alpha(opacity=90)">
<img alt="" src="images/img14.jpg" height="100px" width="100px" style="opacity:0.8;filter:alpha(opacity=80)">
</td>
</tr>
</table>

</div><%@ include file="/WEB-INF/template/sidebarVaccines.jsp" %>
<%@ include file="/WEB-INF/template/footer.jsp" %>