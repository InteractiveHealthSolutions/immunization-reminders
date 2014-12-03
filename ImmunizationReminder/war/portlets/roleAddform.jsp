<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
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
function addPerm(){
	var allPer=document.getElementById("allPerms");
	var selPer=document.getElementById("selectedPerms");
	for (i=0; i<allPer.options.length; i++) {
		if (allPer.options[i].selected) {
	        var opt = document.createElement("option");
	        
	        opt.text = allPer.options[i].text;
	        opt.value = allPer.options[i].value;
	        // Add an Option object to Drop Down/List Box
	        try{
	        document.getElementById("selectedPerms").options.add(opt,null);
	        allPer.remove(i,null);//stndrd
	        }catch (e) {
	        	document.getElementById("selectedPerms").options.add(opt);
		        allPer.remove(i);//ie only
			}
	        i--;
		}
	}
}
function removePerm(){
	var allPer=document.getElementById("allPerms");
	var selPer=document.getElementById("selectedPerms");
	for (i=0; i<selPer.options.length; i++) {
		if (selPer.options[i].selected) {
	        var opt = document.createElement("option");
	        
	        opt.text = selPer.options[i].text;
	        opt.value = selPer.options[i].value;
	        // Add an Option object to Drop Down/List Box
	        try{
	        document.getElementById("allPerms").options.add(opt,null);
	        selPer.remove(i,null);//stndrd
	        }catch (e) {
	        	document.getElementById("allPerms").options.add(opt);
		       	selPer.remove(i);//ie only
			}
	        i--;
		}
	}
}
function frmSubmit(){
	var r=document.getElementById("rolename").value.toLowerCase();
	if(r =="admin"||r=="administrator"||r=="role_administrator"){
		alert("Role can not have name '"+r+"'. Please choose some other name.");
		return;
	}
	var selPer=document.getElementById("selectedPerms");
	if(selPer.options.length<1){
		alert("Role must have atleast one permission");
	}else{
		for(var count=0; count < selPer.options.length; count++) {
			selPer.options[count].selected = true;
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
<table class="addOrEditTable">

        <tr>
            <td><div>${permissionErrorMsg}</div>
                <table >
                        <tr>
                            <td>Role Name : <span class="error" style="font-size: small;color: red"><c:out value="*"/></span></td>
                            <td>
                            <spring:bind path="command.name">
                            <input type="text" id="rolename" name="name" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error" style="font-size: small;color: red"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
                            </td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>Description : </td>
                            <td>    
                            <spring:bind path="command.description">
                            <textarea name="description" rows="4" cols="25">
    						</textarea>
    						<span class="error" style="font-size: small;color: red"><c:out value="${status.errorMessage}"/></span>
    						</spring:bind>
    						</td>
    						<td></td>
                            <td></td>
                        </tr>
                  </table>
            </td>
        </tr>
        <tr>
            <td>
            <table>
            <tr>
            <td>
            			<div >
							<select id="selectedPerms" name="selectedPerms" multiple="multiple" style="border-color: black;border-width: thin;height: 200px;width: 300px;border-style: none;overflow: visible;display: list-item;" >
							</select>
						</div>
			</td>
			<td>
						<input type="text" readonly="readonly" value="&lt&lt" class="expandDataButton" onclick="addPerm();"/>
						<br/>
						<input type="text" readonly="readonly" value="&gt&gt" class="expandDataButton" onclick="removePerm();"/>
			</td>
			<td>
							<div >
							<select id="allPerms" multiple="multiple" style="border-color: black;border-width: thin;height: 200px;width: 300px;border-style: none;overflow: visible ;display: list-item">
								<c:forEach items="${permissions}" var="perm">
									<option>${perm.name}</option>
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
        <input type="button" value="Submit" title="submit" onclick="frmSubmit();"/>
        </td>
        </tr>
</table>
</form>