<%@page import="org.ird.immunizationreminder.datamodel.entities.Vaccine"%>
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

function frmSubmit(){
	var vname=document.getElementById("vname");
	if(vname==null || vname==''){
		alert("Vaccine Name cannot be empty");
		return;
	}
		document.getElementById("frm").submit();
}
//-->
</script>
<form id="frm" name="frm" method="post">
<c:if test="${session_expired == true}">
<c:redirect url="login.htm"></c:redirect>
</c:if>
<table class="addOrEditTable">

        <tr>
            <td>
               <span class="error-message">${errorMessage}</span>
                <table border="0">
                        <tr>
                            <td>Vaccine Name : <span class="error" style="font-size: small;color: red"><c:out value="*"/></span></td>
                            <td>
                            <spring:bind path="command.name">
                            <input type="text" id="vname" name="name" readonly="readonly" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error" style="font-size: small;color: red"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
                            </td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>Vaccine Num in form : <span class="error" style="font-size: small;color: red"><c:out value="*"/></span></td>
                            <td>
                            <spring:bind path="command.vaccineNumberInForm">
                            <input type="text" id="vaccineNumberInForm" name="vaccineNumberInForm" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error" style="font-size: small;color: red"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
                            </td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>Vaccine Name in form: <span class="error" style="font-size: small;color: red"><c:out value="*"/></span></td>
                            <td>
                            <spring:bind path="command.vaccineNameInForm">
                            <input type="text" id="vaccineNameInForm" name="vaccineNameInForm" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error" style="font-size: small;color: red"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
                            </td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>Gap From Previous: <span class="error" style="font-size: small;color: red"><c:out value="*"/></span></td>
                            <td>
                            <spring:bind path="command.gapInWeeksFromPreviousVaccine">
                            <input type="text" id="gapInWeeksFromPreviousVaccine" name="gapInWeeksFromPreviousVaccine" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error" style="font-size: small;color: red"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
                            </td>
                            <td>Unit of Gap</td>
                            <td>
                            <spring:bind path="command.unitPrevGap">
                            <input type="hidden" value="${status.value}" id="pgunitVal"/>
                            <select id="unitPrevGap" name="unitPrevGap">
                           		 <c:forEach items="<%=Vaccine.UNIT_GAP.values()%>" var="gpv">
                            		<option>${gpv}</option>
                           		 </c:forEach>
                                </select><br/>
                            <script><!--
                            	sel = document.getElementById("unitPrevGap");
                          	 	val=document.getElementById("pgunitVal").value;
                           	 	makeTextSelectedInDD(sel,val);
                           	 	
                            //-->
                            </script>                                
                                <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
                            </td>
                        </tr>
                        <tr>
                            <td>Gap To Next: <span class="error" style="font-size: small;color: red"><c:out value="*"/></span></td>
                            <td>
                            <spring:bind path="command.gapInWeeksToNextVaccine">
                            <input type="text" id="gapInWeeksToNextVaccine" name="gapInWeeksToNextVaccine" value="<c:out value="${status.value}"/>"/><br/>
                            <span class="error" style="font-size: small;color: red"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind>
                            </td>
                            <td>Unit of Gap</td>
                            <td><spring:bind path="command.unitNextGap">
                            <input type="hidden" value="${status.value}" id="ngunitVal"/>
                            <select id="unitNextGap" name="unitNextGap">
                           		 <c:forEach items="<%=Vaccine.UNIT_GAP.values()%>" var="gnv">
                            		<option value="${gnv}">${gnv}</option>
                           		 </c:forEach>
                                </select><br/>
                            <script><!--
                            	sel = document.getElementById("unitNextGap");
                          	 	val=document.getElementById("ngunitVal").value;
                           	 	makeTextSelectedInDD(sel,val);
                            //-->
                            </script>                                
                                <span class="error-message"><c:out value="${status.errorMessage}"/></span>
           					</spring:bind></td>
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