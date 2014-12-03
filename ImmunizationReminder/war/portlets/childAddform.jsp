<%@page import="org.ird.immunizationreminder.datamodel.entities.Child"%>
<%@page import="org.ird.immunizationreminder.web.utils.IMRGlobals"%><script type="text/javascript">
<!--
function subfrm(){
	var c1=document.getElementById("cell1").value;
	var c2=document.getElementById("cell2").value;
	var p1=document.getElementById("pid1").value;
	var p2=document.getElementById("pid2").value;

	if(document.getElementById("Status").value!="<%=Child.STATUS.FOLLOW_UP.toString()%>"){
		alert("Child's Status sholud be followup while adding to the system.");
		return;
	}
	if(p1!=p2){
		alert("both child ids donot match");
		return;
	}else if(c1!=c2){
		alert("both cell numbers donot match");
		return;
	}else{
		document.getElementById("frm").submit();
	}
}
//-->
</script>
<form method="post" id="frm" name="frm" >
<c:if test="${session_expired == true}">
<c:redirect url="login.htm"></c:redirect>
</c:if>
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
                            <input type="text" id="pid1" name="childId" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
                            </td>
                            <td>
                            </td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>Re-enter Child Id : <span class="mendatory-field">*</span></td>
                            <td>
                            <spring:bind path="command.childId">
                            <input type="text" id="pid2" value="<c:out value="${command.childId}"/>"/><br/>
           					</spring:bind>
                            </td>
                            <td></td>
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
                            <td>Last Name   :</td>
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
                            <td></td>  
                             <td></td>  
                        </tr>
                        <tr>
	                        <td>
	                         <spring:bind path="command.estimatedBirthdate">
			   				<input type="checkbox" id="estimatedBirthdate" name="estimatedBirthdate"  onclick="checkClicked();"/> Birthdate Not Known ?
                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
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
                            <td>Landmark      :</td>
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
                        <!--<tr>
                            <td>ARM Enrolled:</td>
                            <td>
                            	<select id="sysarms" name="sysarms">
                           		 <c:forEach items="${sysarms}" var="arm">
                           			 <option >${arm.armName}</option>
                            	 </c:forEach>
                           		</select>
                            </td>
                            <td></td>
                            <td></td>
                        </tr>
                        -->
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
                            <select id="Status" name="status">
                            <c:forEach items="<%=Child.STATUS.values()%>" var="tstatus"> 
                            <option >${tstatus}</option>
                            </c:forEach> 
                            </select>
                            <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind><br/>
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
                            <td>Center Name    : <span class="mendatory-field">*</span></td>
                            <td>
                            <spring:bind path="command.clinic">
                            <input type="text" name="clinic" value="Indus Hospital"/><br/>
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
        <input type="button" value="Submit and proceed to vaccination record form" title="submit and proceed to vaccination record form" onclick="subfrm();" align="right" style="width: 400px;">
        </td>
        </tr>
</table>
</div>
</form>
