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
<script type="text/javascript"><!--
/*function editRt(){
	var remtxt=document.getElementById("reminderText");
	var txtbx=document.getElementById("val");
	for (i=0; i<remtxt.options.length; i++) {
		if (remtxt.options[i].selected) {
	        txtbx.value=remtxt.options[i].value;
	        document.getElementById("optIndex").value=i;
	        document.getElementById("action").value="edit";
	    }
	}
}*/
function deleteRt(){
	var remtxt=document.getElementById("remText");
	for (i=0; i<remtxt.options.length; i++) {
		if (remtxt.options[i].selected) {
			try{
		        remtxt.remove(i,null);//stndrd
		        }catch (e) {
		        	remtxt.remove(i);//ie only
				};
				i--;
	    }
	}
    document.getElementById("optIndex").value="";
    document.getElementById("action").value="";
}
/*function addRt(){
	var remtxt=document.getElementById("reminderText");
	var txtbx=document.getElementById("val");
	        txtbx.value=remtxt.options[i].value;
	        document.getElementById("action").value="add";    
}*/
function subAction(){
	var remtxt=document.getElementById("remText");
	var txtbxval=document.getElementById("val").value;
	if(trim(txtbxval)!=""){
		var opt = document.createElement("option");
        
        opt.text = txtbxval;
        opt.value = txtbxval;
        // Add an Option object to Drop Down/List Box
        try{
        document.getElementById("remText").options.add(opt,null);//stndrd
        }catch (e) {
        	document.getElementById("remText").options.add(opt);//ie only
		}
        document.getElementById("val").value="";
	}
}
function SubmitForm(){
	var remtxt=document.getElementById("remText");
	if(remtxt.options.length<1){
		alert("Atleast one Reminder Text should be associated with the reminder");
	}else{
	for(var count=0; count < remtxt.options.length; count++) {
		if (!remtxt.options[count].selected) {
			remtxt.options[count].selected = true;
		}
	}
	document.getElementById("frm").submit();
	}
}
function trim(s)
{
	var l=0; var r=s.length -1;
	while(l < s.length && s[l] == ' ')
	{	l++; }
	while(r > l && s[r] == ' ')
	{	r-=1;	}
	return s.substring(l, r+1);
}

//-->
</script>
<form id="frm" method="post" >
<c:if test="${session_expired == true}">
<c:redirect url="login.htm"></c:redirect>
</c:if>
<span class="error-message">${errorMessage}</span>
<table class="addOrEditTable">
<tr>
<td>
	<table border="0">
		<tr>
		<td>Reminder Name : <span class="error" style="font-size: small;color: red"><c:out value="*"/></span></td>
        <td>
        <spring:bind path="command.name">
        <input type="text" name="name" value="<c:out value="${status.value}"/>"/><br/>
        <span class="error" style="font-size: small;color: red"><c:out value="${status.errorMessage}"/></span>
        </spring:bind>
        </td>
		</tr>
		<tr>
		<td>Description : </td>
        <td>
        <spring:bind path="command.description">
        <input type="text" name="description" value="<c:out value="${status.value}"/>"/><br/>
        <span class="error" style="font-size: small;color: red"><c:out value="${status.errorMessage}"/></span>
        </spring:bind>
        </td>
		</tr>
		
	</table>
	</td>
	</tr>
	<tr>
	<td>	
		<div style="float: right;">Use Notations to insert child specific data in Reminder Text:
		<br>[[Child.FirstName]] for First name part of Current Child
		<br>[[Child.MiddleName]] for Middle name part of Current Child
		<br>[[Child.LastName]] for Last name part of Current Child
		<br>[[Child.LastName]] for Last name part of Current Child
		<br>[[Vaccination.Day]] for inserting the week day when vaccination due date was scheduled
		<br>[[Vaccination.Date]] for inserting vaccination due date
		</div>
	</td>
	</tr>
	<tr>
	<td>

		<table>
		            <tr>
                		<td>
                                <br>
        						<br>
                        SELECT REMINDER TEXT FOR REMINDER<span class="error" style="font-size: small;color: red"><c:out value="*"/></span>
                                <br>
        						<br>
                        </td>
						<td></td>
                        </tr>
		<tr>
		 <td>
            			<div style="border: 1;border-style: dotted;">
							<select id="remText" name="remText" multiple="multiple" style="border-color: black;border-width: thin;height: 200px;width: 400px;border-style: none;overflow: visible;display: list-item;" >
							<c:forEach items="${reminderText}" var="remt">
							<option >${remt}</option>
							</c:forEach>
							</select>
						</div>
			</td>
			<td>
						<input type="hidden" id="optIndex" value=""/> 
						<input type="hidden" id="action" value=""/>
						<input type="text" readonly="readonly" value="delete" style="color: black;background-color: gray;cursor: pointer;width: 2cm;" size="3" onclick="deleteRt();"/>
						<br>
						<br>
			<br><input id="val" type="text" ><input type="text" readonly="readonly" value="Add" title="" style="color: black;background-color: gray;cursor: pointer;width: 2cm;" size="4" onclick="subAction();"/>
			</td>
			</tr>
			<tr>
			<td>
			   	<br>
               <br>
	        	<input type="button" value="Submit" title="submit" align="right" onclick="SubmitForm();">
			</td>
			<td></td>
			</tr>
		</table>
			
	</td>
	</tr>
</table>
</form>