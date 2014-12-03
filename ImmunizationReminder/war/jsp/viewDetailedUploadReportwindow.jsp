<%@ include file="/jsp/include.jsp"%>

<%@page import="org.ird.immunizationreminder.context.LoggedInUser"%>

<%@page import="org.ird.immunizationreminder.context.SystemPermissions"%>
<c:catch var="catchException">
<c:out value="${lmessage}"></c:out>
<div style="font-size: small;">
	${detReport}
</div>
</c:catch>
<c:if test="${catchException!=null}">
<span class="error" style="font-size: xx-large; color: red">SESSION EXPIRED. LOGIN AGIAN !!!${catchException}
</span>
</c:if>

