<%@ include file="/jsp/include.jsp"%>
                
<%@page import="org.ird.immunizationreminder.datamodel.entities.User"%>
<form id="frm" method="post">
				<table border="0">
                        <tr>
                            <td>Reminder : </td>
                            <td>
                            <select name="reminder">
                            <c:forEach items="${model.reminder}" var="rem">
                            <option >${rem}</option>
                            </c:forEach>
                            </select><br/>
                            </td>
                        </tr>
                        <c:forEach items="${model.armDaySequence}" var="daynum">
                        <tr>
                        	<td>Day ${daynum} Reminder Time (hh:mm:ss) (16:23:32)</td>
                        	<td><input type="text" name="time${daynum}"/></td>
                        </tr>
                        </c:forEach>
                </table>
</form>