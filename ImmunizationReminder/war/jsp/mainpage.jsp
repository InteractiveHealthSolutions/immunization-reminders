<input type="hidden" id="page_nav_id" value="_li1">

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="/WEB-INF/template/pagebody.jsp" %>

<table width="100%" style="color: green;font-size: large;font-stretch: ultra-condensed;text-decoration: underline;">
	<tr>
	<td>
	Upload Enrollment Csv<br><br>
	<a class="imganch" href="uploadData.htm">
	<img alt="Upload Children Enrollment Csv" src="images/babyCsv.JPG" class="anchorimg" style="opacity:0.8;filter:alpha(opacity=80);">
	</a>
	</td>
	
	<td>
	Update Pending Vaccines<br><br>
	<a class="imganch" href="viewVaccinationRecord.htm">
	<img alt="Update Pending Vaccinations" src="images/img13.jpeg" class="anchorimg" style="opacity:0.8;filter:alpha(opacity=80)">
	</a>
	</td>

	<td>
	View Enrolled Children<br><br>
	<a class="imganch" href="viewChildren.htm">
	<img alt="View Enrolled Children" src="images/babiesgroup.jpeg" class="anchorimg" style="opacity:0.8;filter:alpha(opacity=80)">
	</a>
	</td>
	<td>
	View All Vaccinations<br><br>
	<a class="imganch" href="viewVaccinationRecord.htm">
	<img alt="View All Vaccination Data" src="images/childvaccination.JPG" class="anchorimg" style="opacity:0.8;filter:alpha(opacity=80)">
	</a>
	</td>
	</tr>
</table>
<br>
<br>
<table width="100%" style="color: green;font-size: large;font-stretch: ultra-condensed;text-decoration: underline;">
	<tr>
	<td>
	Analyze Deeply<br><br>
	<a class="imganch" href="analyzeReminder.htm">
	<img alt="Analyze Deeply" src="images/marketAnalysis.jpg" class="anchorimg" style="opacity:0.8;filter:alpha(opacity=80);">
	</a>
	</td>
	
	<td>
	Reminder History<br><br>
	<a class="imganch" href="viewReminderSmsRecord.htm">
	<img alt="Reminder History" src="images/reminder.jpg" class="anchorimg" style="opacity:0.8;filter:alpha(opacity=80)">
	</a>
	</td>

	<td>
	Responses<br><br>
	<a class="imganch" href="viewResponses.htm">
	<img alt="Responses" src="images/welldone.jpg" class="anchorimg" style="opacity:0.8;filter:alpha(opacity=80)">
	</a>
	</td>
	<td>
	View All Users<br><br>
	<a class="imganch" href="viewUsers.htm">
	<img alt="View All Users" src="images/users.jpg" class="anchorimg" style="opacity:0.8;filter:alpha(opacity=80)">
	</a>
	</td>
	</tr>
</table>
<span style="font-size: small">

<br>
<br>

<span style="">
 The System manages vaccination process of children enrolled in one of 3 possible ARMs of study.
 As a child/child reaches the center and is vaccinated for the first time it is enrolled into the 
 system with some demographic information, the ARM in which child is enrolled, cell number to contact
 or send messages, the programme details, current followup status, and also details for current 
 vaccination with next assigned date for vaccination and timings for reminder SMS' to be sent 
 depending on ARM.
<br>
 As child is added to the system its demographic details are saved with child_id and current 
 cell number being ensured unique and identifiable. A cell number can only be assigned to any child
 if it is not occupied by any other child enrolled in the system and is currently underfollowup.
<br>
 As child is enrolled into the system when it comes for its first vaccination at any center so 
 first vaccination record will not contain any reminderSms data for first vaccination. Now child 
 must be assigned next vaccination date with some other information about vaccination or if child 
 is not going to recieve any more vaccinations, current vaccination must be marked as last vaccination. 
<br>
 The vaccination record is logged into database. New vaccination record is set its status PENDING 
 and ReminderSms' are created and queued up for sending on specified time and date according to 
 defined rule by ARM in which child is enrolled.
<br>
 Now Reminders will be scheduled before 2 days of their due date. Before 2 days of their due date 
 a vaccination record can be edited i.e child's vaccination date can be moved forward or backward 
 if newly assigned duedate minus least reminder day number has not passed and is 2 or more days forward. 
 Once reminders have been scheduled a reminder sms' can not be stopped from sending out. The 
 reminder sms's will be scheduled if child is under followup. If child is not underfollowup all 
 currently unscheduled PENDING reminders will be marked as CANCELLED.
<br>
<br>
<br>
</span>
<span style="font-style: italic;font-weight: bolder;font-stretch: ultra-expanded"> 
Currently Existing ARMs:
<br>
<br>
</span>
<span style="font-weight: bolder;"> 
 1) SMS REMINDER ARM
</span>	
<br>
<br>

 Single SMS at each of the following times:
<br>
<br>

    **  3 days before immunization<br>
    
    **  1 day before immunization<br>
    
    **  Day of immunization<br>
<br>
<br>
   
<span style="font-weight: bolder"> 
 2) INTERACTIVE SMS ARM
</span>	
<br>
<br>

 Single SMS at each of the following times:
<br>
<br>

    **  3 days before immunization<br>
    
    **  1 day before immunization<br>
    
    **  Day of immunization<br>
    
<br>

 On the day of immunization, study participants are required to respond back through SMS notifying us that child got vaccinated or if not, the reason for delay in immunization. In case of no response, 2 additional reminders will be sent at:
<br>
<br>

    **  1 day after scheduled immunization date<br>
    
    **  1 week after scheduled immunization date<br>
<br>
<br>
    
<span style="font-weight: bolder"> 
 3) CONTROL ARM
</span>	
<br>
<br>

 No SMS
<br><br>
</span>
<%@ include file="/WEB-INF/template/sidebar.jsp" %>
<%@ include file="/WEB-INF/template/footer.jsp" %>