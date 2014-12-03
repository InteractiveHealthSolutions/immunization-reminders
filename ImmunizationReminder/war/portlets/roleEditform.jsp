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

//-->
</script>
<script type="text/javascript">
<!--
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
            <td>
               <span class="error-message">${errorMessage}</span>
                <table border="0">
                        <tr>
                            <td>Role Name : <span class="error" style="font-size: small;color: red"><c:out value="*"/></span></td>
                            <td>
                            <spring:bind path="command.name">
                            <input type="text" name="name" readonly="readonly" value="<c:out value="${status.value}"/>"/><br/>
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
                            <textarea name="description" rows="4" cols="25" >${status.value}</textarea>
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
                                <br>
        						<br>
                        SELECT PERMISSIONS FOR ROLE<span class="error" style="font-size: small;color: red"><c:out value="*"/></span>
                                <br>
        						<br>
                        </td>

                        </tr>
            <tr>
            <td>
            			<div style="border: 1;border-style: dotted;">
							<select id="selectedPerms" name="selectedPerms" multiple="multiple" style="border-color: black;border-width: thin;height: 200px;width: 300px;border-style: none;overflow: visible;display: list-item;" >
							<c:forEach items="${rolePermissions}" var="pr">
							<option >${pr.name}</option>
							</c:forEach>
							</select>
						</div>
			</td>
			<td>
						<input type="text" readonly="readonly" value="&lt&lt" class="expandDataButton" onclick="addPerm();"/>
						<br/>
						<input type="text" readonly="readonly" value="&gt&gt" class="expandDataButton" onclick="removePerm();"/>
			</td>
			<td>
							<div style="border: 1;border-style: dotted;">
							<select id="allPerms" multiple="multiple" style="border-color: black;border-width: thin;height: 200px;width: 300px;border-style: none;overflow: visible ;display: list-item">
								<c:forEach items="${remainingPermissions}" var="perm">
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
            <td></td>
        </tr>
        <tr>
        <td>
                <br>
        <br>
        <input type="button" value="Submit" title="submit" align="right" onclick="frmSubmit();"/>
        </td>
        </tr>
</table>
</form>