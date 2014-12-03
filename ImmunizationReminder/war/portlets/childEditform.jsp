<%@page import="org.ird.immunizationreminder.datamodel.entities.Child"%>
<%@page import="org.ird.immunizationreminder.web.utils.IMRGlobals"%>
<script type="text/javascript">
<!--

function subfrm(){
	var c1=document.getElementById("cell1").value;
	var c2=document.getElementById("cell2").value;

	if(c1!=c2){
		alert("both cell numbers donot match");
		return;
	}
	if(document.getElementById("terminationDatediv").style.display!="none"){
		if(trim(document.getElementById("terminationReason").value)==''){
			alert('Please specify a termination reason.');
			return;
		}
		if(document.getElementById("terminationDate").value==''){
			alert('Please specify a termination date.');
			return;
		}
	}
		document.getElementById("frm").submit();
}
//-->
</script>
<form method="post" id="frm" name="frm">
<c:if test="${session_expired == true}">
<c:redirect url="login.htm"></c:redirect>
</c:if>
<div id="childArmInfo">
	
</div>
<br>
<br>
<span class="error-message">${errorMessage}</span>
<div align="center">
<table class="addOrEditTable">
   <tr>
            <td>
                        	<div><span  class="error" style="font-size: small;color: red">
                            <spring:bind path="command">
                            <c:forEach items="${status.errorMessages}" var="error">
                             Error code: <c:out value="${error}"/><br> 
                             </c:forEach> 
                            </spring:bind>
                            </span>
                            </div>
                <table border="0" width="100%">
                        <tr>
                            <td>Child Id : <span class="mendatory-field">*</span></td>
                            <td>
                            <spring:bind path="command.childId">
                            <input type="text" id="pid1" name="childId" readonly="readonly" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
                            </td>
                            <td>
                            </td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>First Name  : <span class="mendatory-field">*</span></td>
                            <td>
                            <spring:bind path="command.firstName">
                            <input type="text" name="firstName" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
                            <td>Middle Name : </td>
                            <td>
                            <spring:bind path="command.middleName">
                            <input type="text" name="middleName" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
                        </tr>
                        <tr>
                            <td>Last Name   : </td>
                            <td>
                            <spring:bind path="command.lastName">
                            <input type="text" name="lastName" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
           					<td>Father Name: </td>
                            <td>
                            <spring:bind path="command.fatherName">
                            <input type="text" name="fatherName" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
           				</tr>
           				<spring:bind path="command.currentCellNo">
                        <tr>
                            <td>Current Cell Number : <span class="mendatory-field">*</span></td>
                            <td>
                            <input id="cell1" type="text" name="currentCellNo" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</td>
							<td></td>
							<td></td>
                        </tr>
                        <tr>
                        	<td>Re-enter Current Cell Number : <span class="mendatory-field">*</span></td>
                            <td>
                            <input type="text" id="cell2" value="<c:out value="${command.currentCellNo}"/>"/><br/>
           					</td>
							<td></td>
							<td></td>           					
           				</tr>
                        </spring:bind>
                        <tr>
           					<td>Gender      : </td>
                            <td>
                            <spring:bind path="command.gender">
                            <input type="hidden" value="${status.value}" id="genVal"/>
                            <select id="gender" name="gender">
                           		 <c:forEach items="<%=Child.GENDER.values()%>" var="gen_value">
                            		<option >${gen_value}</option>
                           		 </c:forEach>
                                </select><br/>
                            <script><!--
                            	sel = document.getElementById("gender");
                          	 	val=document.getElementById("genVal").value;
                           	 	makeTextSelectedInDD(sel,val);
                            //-->
                            </script>                                
                                <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
                            </td> 
                            <td></td>    
                            <td></td>      				
                        </tr>         
                        <tr>
                            <td >Date Of Birth:<span class="mendatory-field">*</span></td>
                            <td>
                           		<spring:bind path="command.birthdate">
                          		<input id="birthdate" name="birthdate" onclick='clearAge();scwShow(this,event);' readonly="readonly" value="<c:out value="${status.value}"/>"/>
                           		<input onclick="clearAge();scwShow(scwID('birthdate'),event);" size="5" style="cursor:pointer;width: 16px;height:16px;background-image: url('images/calendar_icon.png');"/>
                           		<br/>
                           		<div id="bdAgeDiv" style="font-size: smaller;font-style: italic;color: green;" ></div>
                            	<span class="error-message"><c:out value="${status.errorMessage}"/></span>
           						</spring:bind>
           					</td>
                            <td colspan="2"><span class="mendatory-field">dont forget to edit measles1 record if DOB is changed</span></td>  
                        </tr>
                        <tr>
	                        <td><spring:bind path="command.estimatedBirthdate"><input type="checkbox" id="estimatedBirthdate" name="estimatedBirthdate"  onclick="checkClicked();"/> Birthdate Not Known ?
                            <br><span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind></td>
	                        <td>
	                        <div id="agediv" style="display: none;">Age (in weeks)<br><input id="age" type="text" value="${command.age}" onchange="ageChange();"/></div></td>
	                        <td></td>
	                        <td>
	                    <script type="text/javascript">
						<!--
						if(document.getElementById("estimatedBirthdate").checked==true){
								showAge();
						}
						function ageChange() {
							var agein=document.getElementById("age");
							var reg = /^[0-9]+/;
							if(reg.test(agein.value)){
								document.getElementById("birthdate").value=(Date.today().add(-parseInt(agein.value)).weeks()).toString("<%=IMRGlobals.GLOBAL_DATE_FORMAT%>");
								document.getElementById("bdAgeDiv").innerHTML="age in weeks :"+agein.value;
								
							}else{
								alert('Invalid Age');
							}
						}
						function checkClicked(){
							if(document.getElementById("estimatedBirthdate").checked==true){
								showAge();
							}else{//hide age div
								document.getElementById("agediv").style.display="none";
							}
						}
						function clearAge() {
							document.getElementById("agediv").style.display="none";
							document.getElementById("estimatedBirthdate").checked=false;
							document.getElementById("bdAgeDiv").innerHTML="";
							
						}
						function showAge() {
							document.getElementById("agediv").style.display="table";
							var globalDf="<%=IMRGlobals.GLOBAL_DATE_FORMAT%>";
							var agein=document.getElementById("age");
							if(document.getElementById("birthdate").value==null ||document.getElementById("birthdate").value==''){
								agein.value=-1;
								document.getElementById("bdAgeDiv").innerHTML="No age specified.";
							}else{
								var dobr=Date.parseExact(document.getElementById("birthdate").value,globalDf);
								agein.value = -datediff(Date.today(),Date.parseExact(document.getElementById("birthdate").value,globalDf), "weeks");
								document.getElementById("bdAgeDiv").innerHTML="age in weeks :"+agein.value;
								
							}
						}
						//-->
						</script>
	                        </td>
                        </tr>
                        <tr>
                            <td>Alternate Cell Number : </td>
                            <td>
                            <spring:bind path="command.alternateCellNo">
                            <input type="text" name="alternateCellNo" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
           					<td></td>
           					<td></td>
                        </tr>
                        <tr>
                        	<td><br></td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td><h3 style="color: gray">ADDRESS : </h3></td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>House number : </td>
                            <td>
                            <spring:bind path="command.houseNum">
                            <input type="text" name="houseNum" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
                            <td>Street number: </td>
                            <td>
                            <spring:bind path="command.streetNum">
                            <input type="text" name="streetNum" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
                        </tr>
                        <tr>
                            <td>Sector      : </td>
                            <td>
                            <spring:bind path="command.sector">
                            <input type="text" name="sector" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
                            <td>Town         : </td>
                            <td>
                            <spring:bind path="command.town">
                            <input type="text" name="town" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
                        </tr>
                        <tr>
                            <td>Colony       : </td>
                            <td>
                            <spring:bind path="command.colony">
                            <input type="text" name="colony" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
                            <td>Landmark      : </td>
                            <td>
                            <spring:bind path="command.landmark">
                            <input type="text" name="landmark" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
                        </tr>                                                
                       <tr>
                            <td>Phone Number: </td>
                            <td>
                            <spring:bind path="command.phoneNo">
                            <input type="text" name="phoneNo" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>Description  : </td>
                            <td>
                            <spring:bind path="command.description">
                            <textarea name="description" rows="6" cols="30">${status.value}</textarea><br/>
                                <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
                            <td></td>
                            <td></td>                           
                        </tr>
                        <tr>
                        	<td><br></td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td><h3 style="color: gray">PROGRAMME : </h3></td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>Program Enrolled:</td>
                            <td>
                            <spring:bind path="command.programmeEnrolled">
                            <input type="text" name="programmeEnrolled" value="Immunization Reminders"/><br/>
                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
                            <td>Date Enrolled   : <span class="mendatory-field">*</span></td>
                            <td>
                            <spring:bind path="command.dateEnrolled">
                            <input name="dateEnrolled" readonly="readonly" onclick='scwShow(this,event);'  value="<c:out value="${status.value}"/>"/>
                            <input onclick="scwShow(scwID('dateEnrolled'),event);" size="5" style="cursor:pointer;width: 16px;height:16px;background-image: url('images/calendar_icon.png');"/>
                            <br/>
                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
                        </tr>
                        <tr>
                            <td>Date of Completion: </td>
                            <td>
                            <spring:bind path="command.dateOfCompletion">
                            <input name="dateOfCompletion" readonly="readonly" onclick='scwShow(this,event);' value="<c:out value="${status.value}"/>"/>
                            <input onclick="scwShow(scwID('dateOfCompletion'),event);" size="5" style="cursor:pointer;width: 16px;height:16px;background-image: url('images/calendar_icon.png');"/>
                            <br/>
                          	<span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
                            <td>Status : </td>
                            <td>
	                            <spring:bind path="command.status">
	                            <input type="hidden" value="${status.value}" id="trtVal"/>
	                            <select id="Status" name="status" onchange="statusChanged();">
	                            <c:forEach items="<%=Child.STATUS.values()%>" var="tstatus">
	                            <option value="${tstatus}" >${tstatus}:${tstatus.REPRESENTATION}</option>
	                            </c:forEach>
	                            </select>
	                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
	           					</spring:bind><br/>
                            </td>
                        </tr>
                        <tr>
                           <td></td>
                           <td></td>
                           <td></td>
                           <td>
                           <br>
                           <div id="UneditableTerminationdiv" style="display: none">
                           Termination Reason : ${command.terminationReason}<br>
                           Termination Date   : ${command.terminationDate}
                           </div>
							<div id="terminationDatediv">
							Enter Termination Reason
							<br>
							<spring:bind path="command.terminationReason">
                            <textarea id="terminationReason" name="terminationReason" rows="3" cols="30" >
                                ${status.value}</textarea><br/>
                                <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
							<br>
							Termination Date
							<br>
							<spring:bind path="command.terminationDate">
                            <input id="terminationDate" name="terminationDate" readonly="readonly" onclick='scwShow(this,event);' value="<c:out value="${status.value}"/>"/>
                            <input onclick="scwShow(scwID('terminationDate'),event);" size="5" style="cursor:pointer;width: 16px;height:16px;background-image: url('images/calendar_icon.png');"/>
                            
                            <br/>
                          	<span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</div>
                            <script><!--
                            sel = document.getElementById("Status");
                            val=document.getElementById("trtVal").value;
                            if(val=="<%=Child.STATUS.COMPLETED.toString()%>"||val=="<%=Child.STATUS.TERMINATED.toString()%>"){
                            	sel.disabled=true;
                            }
                            hideTermination();
                        	for (i=0; i<sel.options.length; i++) {
								if (sel.options[i].value == val&&sel.options[i].value == "<%=Child.STATUS.TERMINATED.toString()%>") {
                        			sel.selectedIndex = i;
                        			showUneditableTermination();
                        		}else if (sel.options[i].value == val) {
                        			sel.selectedIndex = i;
                        		}
                        	}
                        	function showTermination() {
                        		document.getElementById("terminationDatediv").style.display="table";
                        		document.getElementById("terminationReason").disabled=false;
                        		document.getElementById("terminationDate").disabled=false;
							}
							function showUneditableTermination() {
								document.getElementById("UneditableTerminationdiv").style.display="table";
							}
							function hideTermination() {
                        		document.getElementById("terminationReason").disabled=true;
                        		document.getElementById("terminationDate").disabled=true;
                        		document.getElementById("terminationDatediv").style.display="none";							
                        	}
							function statusChanged(){
								hideTermination();
								if(document.getElementById("Status").value=="<%=Child.STATUS.COMPLETED.toString()%>"){
									alert('A child Status could be completed only if it have recieved it last vaccination.');
									for (i=0; i<sel.options.length; i++) {
		                        		if (sel.options[i].value == "<%=Child.STATUS.FOLLOW_UP.toString()%>") {
		                        			sel.selectedIndex = i;
		                        			break;
		                        		}
		                        	}
									return;//in real it will never come to this point as completed is disabled.
								}
								if(document.getElementById("Status").value=="<%=Child.STATUS.TERMINATED.toString()%>"){
									if(!confirm('All currently pending reminders and vaccinations will be cancelled.')){
										for (i=0; i<sel.options.length; i++) {
			                        		if (sel.options[i].value == "<%=Child.STATUS.FOLLOW_UP.toString()%>") {
			                        			sel.selectedIndex = i;
			                        			return;
			                        		}
			                        	}
									}else{
										if(!confirm('Are you sure!!')){
											for (i=0; i<sel.options.length; i++) {
				                        		if (sel.options[i].value == "<%=Child.STATUS.FOLLOW_UP.toString()%>") {
				                        			sel.selectedIndex = i;
				                        			return;
				                        		}
				                        	}
										}
										alert('Enter termination reason and date.');
										showTermination() ;
									}
		                        	for (i=0; i<sel.options.length; i++) {
		                        		if (sel.options[i].value == "<%=Child.STATUS.FOLLOW_UP.toString()%>") {
		                        			sel.options[i].disabled = true;
		                        			break;
		                        		}
		                        	}
								}
							}
                        	//-->
                            </script>
                           </td>
                        
                        </tr>
                        <tr>
                            <td>Epi Reg/MR Number      : <span class="mendatory-field">*</span></td>
                            <td>
                            <spring:bind path="command.mrNumber">
                            <input type="text" name="mrNumber" value="${status.value}"/><br/>
                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
                            <td>Center Name     : <span class="mendatory-field">*</span></td>
                            <td>
                            <spring:bind path="command.clinic">
                            <input type="text" name="clinic" value="${status.value}"/><br/>
                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
                        </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>                        
            <br>
            </td>
        </tr>
         <tr>
        <td>
        <input id="subbtn"  type="button" value="Submit and proceed" title="submit and proceed" onclick="subfrm();" align="right" style="width: 400px;">
        </td>
        </tr>
</table>
</div>
</form>
