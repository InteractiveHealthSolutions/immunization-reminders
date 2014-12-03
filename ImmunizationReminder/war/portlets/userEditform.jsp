<script type="text/javascript">
<!--
window.onbeforeunload = function (e) {
	  var e = e || window.event;

	  // For IE and Firefox
	  if (e) {
	    e.returnValue = '';
	  }

	  // For Safari
	  return '';
	};

function addR(){
	var allrol=document.getElementById("allRoles");
	var selrol=document.getElementById("selectedRoles");
	for (i=0; i<allrol.options.length; i++) {
		if (allrol.options[i].selected) {
	        var opt = document.createElement("option");
	        
	        opt.text = allrol.options[i].text;
	        opt.value = allrol.options[i].value;
	        // Add an Option object to Drop Down/List Box
	        try{
	        document.getElementById("selectedRoles").options.add(opt,null);
	        allrol.remove(i,null);//stndrd
	        }catch (e) {
	        	document.getElementById("selectedRoles").options.add(opt);
		        allrol.remove(i);//ie only
			}
	        i--;
		}
		
	}
}
function remR(){
	var allrol=document.getElementById("allRoles");
	var selrol=document.getElementById("selectedRoles");
	for (i=0; i<selrol.options.length; i++) {
		if (selrol.options[i].selected) {
	        var opt = document.createElement("option");
	        
	        opt.text = selrol.options[i].text;
	        opt.value = selrol.options[i].value;
	        // Add an Option object to Drop Down/List Box
	        try{
	        document.getElementById("allRoles").options.add(opt,null);
	        selrol.remove(i,null);//stndrd
	        }catch (e) {
	        	document.getElementById("allRoles").options.add(opt);
		       	selrol.remove(i);//ie only
			}
	        i--;
		}
		
	}
}
function frmSub(){
	var sub=true;
	var selrol=document.getElementById("selectedRoles");
	if(selrol.options.length<1){
		alert("User must have atleast one role");
		sub=false;
	}
	if(sub){
		for(var count=0; count < selrol.options.length; count++) {
			selrol.options[count].selected = true;
		}
		document.getElementById("frm").submit();
	}
	
}
//-->
</script>

<form id="frm" method="post">

<c:if test="${session_expired == true}">
<c:redirect url="login.htm"></c:redirect>
</c:if>

<c:if test="${isUserAllowedToEdit != true}">
<span style="color: red;font-size: small;">The user you are attempting to edit have more privileges than you. Hence you will not be able to make any changes to the user data. Please contact your system administrator.
</span>
</c:if>
<table class="addOrEditTable">

        <tr>
            <td>
            <span class="error-message">${errorMessage}</span>
            <table border="0">
                        <tr>
                            <td>User Name(Id) : <span class="error" style="font-size: small;color: red"><c:out value="*"/></span></td>
                            <td>
                            <spring:bind path="command.name">
                            <c:set var="u_name" value="${status.value}" ></c:set>
                            <input type="text" name="name" value="<c:out value="${status.value}"/>" readonly="readonly"/><br/>
                            <span class="error" style="font-size: small;color: red"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
                            </td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>First Name  : <span class="error" style="font-size: small;color: red"><c:out value="*"/></span></td>
                            <td>
                            <spring:bind path="command.firstName">
                            <input type="text" name="firstName" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error" style="font-size: small;color: red"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
           				</tr>
           				<tr>
                            <td>Middle Name : </td>
                            <td>
                            <spring:bind path="command.middleName">
                            <input type="text" name="middleName" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error" style="font-size: small;color: red"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
                        </tr>
                        <tr>
                            <td>Last Name   : </td>
                            <td>
                            <spring:bind path="command.lastName">
                            <input type="text" name="lastName" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error" style="font-size: small;color: red"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
                       </tr>
                        <tr>
                            <td>Email       : </td>
                            <td>
                            <spring:bind path="command.email">
                            <input type="text" name="email" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error" style="font-size: small;color: red"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
                        </tr>
                         <tr>
                            <td>Phone Number: </td>
                            <td>
                            <spring:bind path="command.phoneNo">
                            <input type="text" name="phoneNo" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error" style="font-size: small;color: red"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
                        </tr>
                        <c:choose>
                        <c:when test="${(u_name == 'admin') || (u_name == 'administrator') || (u_name == 'ADMIN') || (u_name == 'ADMINISTRATOR')}">
                        </c:when>
                        <c:otherwise>
						<tr>
                            <td>User Status : </td>
                            <td>
                            <spring:bind path="command.status">
                            <input type="hidden" value="${status.value}" id="stVal"/>
                            <select name="status">
                            <c:forEach items="${user_status}" var="ustatus">
                            <option >${ustatus}</option>
                            </c:forEach>
                            </select><br/>
                            <script><!--
                            sel = document.getElementById("status");
                            val=document.getElementById("stVal").value;
                        	for (i=0; i<sel.options.length; i++) {
                        		if (sel.options[i].text == val) {
                        			sel.selectedIndex = i;
                        		}
                        	}//-->
                            </script>
                            <span class="error" style="font-size: small;color: red"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
                            </td>
                        </tr>                        
                        </c:otherwise>
                        </c:choose>
                        
                </table>

            </td>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
            <td>
                        <c:choose>
                        <c:when test="${(u_name == 'admin') || (u_name == 'administrator') || (u_name == 'ADMIN') || (u_name == 'ADMINISTRATOR')}">
                        </c:when>
                        <c:otherwise>
                       	<c:if test="${fn:length(notAllowedRoles) > 0}">		
                        <span style="color: red;font-size: small;">You donot have privileges to assign following roles while creating any user (role have more privileges than your role).
						</span>
						<br>
                        <div style="color: blue;font-style: italic">
                        		<c:forEach items="${notAllowedRoles}" var="rol">
									-- ${rol.name}<br>
								</c:forEach>
                        </div>
                        </c:if>   
                        <table border="0">
                        		<tr>
                        		<td>
	                                <br>
	        						<br>
	                        		SELECT ROLES FOR USER<span class="error" style="font-size: small;color: red"><c:out value="*"/></span>
	                                <br>
	        						<br>
                        		</td>
                       			</tr>                   			
                       			<tr>
            					<td>
			            			<div style="border: 1;border-style: dotted;">
										<select id="selectedRoles" name="selectedRoles" multiple="multiple" style="border-color: black;border-width: thin;height: 200px;width: 300px;border-style: none;overflow: visible;display: list-item;" >
										<c:forEach items="${userRoles}" var="rl">
										<option >${rl.name}</option>
										</c:forEach>
										</select>
									</div>
								</td>
								<td>
									<input type="text" readonly="readonly" value="&lt&lt" class="expandDataButton" onclick="addR();"/>
									<br/>
									<input type="text" readonly="readonly" value="&gt&gt" class="expandDataButton" onclick="remR();"/>
											</td>
											<td>
										<div style="border: 1;border-style: dotted;">
										<select id="allRoles" multiple="multiple" style="border-color: black;border-width: thin;height: 200px;width: 300px;border-style: none;overflow: visible ;display: list-item;">
											<c:forEach items="${remainingRoles}" var="role">
												<option>${role.name}</option>
											</c:forEach>
										</select>
									</div>
								</td>
								</tr>
                       
                       
                			</table>                        
                        </c:otherwise>
                        </c:choose>
	           </td>
        </tr>
        <tr>
        <td>
        <br>
        <br>
        <c:choose>
        <c:when test="${(u_name == 'admin') || (u_name == 'administrator') || (u_name == 'ADMIN') || (u_name == 'ADMINISTRATOR')}">
       		<input type="submit" value="Submit" title="submit" align="right"/>
        </c:when>
        <c:otherwise>
        <c:if test="${isUserAllowedToEdit == true}">
        <input type="button" value="Submit" title="submit" align="right" onclick="frmSub();"/>
        </c:if>
        </c:otherwise>
        </c:choose>
        </td>
        </tr>
</table>
</form>
