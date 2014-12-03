<%@page import="org.ird.immunizationreminder.datamodel.entities.User"%><script type="text/javascript">
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
	var pwd1=document.getElementById("password").value;
	var pwd2=document.getElementById("cpassword").value;
	if(pwd1!=pwd2){
		sub=false;
		alert("Passwords donot match");
	}
	if(sub){
		for(var count=0; count < selrol.options.length; count++) {
			selrol.options[count].selected = true;
		}
		document.getElementById("frm").submit();
	}
}//-->
</script>

<form id="frm" method="post">

<c:if test="${session_expired == true}">
<c:redirect url="login.htm"></c:redirect>
</c:if>
<table class="addOrEditTable">

        <tr>
            <td><div>${roleErrorMsg}</div>
                <table border="0">
                        <tr>
                            <td>User Name(Id) : <span class="error" style="font-size: small;color: red"><c:out value="*"/></span></td>
                            <td>
                            <spring:bind path="command.name">
                            <input type="text" name="name" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error" style="font-size: small;color: red"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
                            </td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>Password       : <span class="error" style="font-size: small;color: red"><c:out value="*"/></span></td>
                            <td>
                            <spring:bind path="command.clearTextPassword">
                            <input type="password" id="password" name="clearTextPassword" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error" style="font-size: small;color: red"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
           					</td>
                        </tr>
                         <tr>
                            <td>Confirm Password       : </td>
                            <td>
                            <input type="password" id="cpassword" name="cpassword" value=""/><br/>
                            </td>
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
                            <td>Email: <span style="font-size: small;"><c:out value="Your passwords will be sent to this email."/></span><span class="error" style="font-size: small;color: red"><c:out value="*"/></span></td>
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
                        <tr>
                            <td>User Status : </td>
                            <td>
                            <spring:bind path="command.status">
                            <select name="status">
                            <c:forEach items="<%=User.UserStatus.values()%>" var="ustatus">
                            <option >${ustatus}</option>
                            </c:forEach>
                            </select><br/>
                            <span class="error" style="font-size: small;color: red"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
                            </td>
                        </tr>
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
            			<div >
							<select id="selectedRoles" name="selectedRoles" multiple="multiple" style="border-color: black;border-width: thin;height: 200px;width: 300px;border-style: none;overflow: visible;display: list-item;" >
							</select>
						</div>
					</td>
					<td>
						<input type="text" readonly="readonly" value="&lt&lt" class="expandDataButton" onclick="addR();"/>
						<br/>
						<input type="text" readonly="readonly" value="&gt&gt" class="expandDataButton" onclick="remR();"/>
					</td>
					<td>
							<div >
							<select id="allRoles" multiple="multiple" style="border-color: black;border-width: thin;height: 200px;width: 300px;border-style: none;overflow: visible ;display: list-item;">
								<c:forEach items="${roles}" var="role">
									<option>${role.name}</option>
								</c:forEach>
							</select>
						</div>
					</td>
					</tr>
                       
                       
                </table>

            </td>
        </tr>
        <tr>
        <td>
        <input type="button" value="Submit" title="submit" onclick="frmSub();"/>
        </td>
        </tr>
</table>
</form>
