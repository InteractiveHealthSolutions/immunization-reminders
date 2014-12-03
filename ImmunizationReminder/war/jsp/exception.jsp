<%@ include file="/WEB-INF/template/header.jsp" %>
<span class="error" style="font-size: large; color: red">
<c:out value="An Exception occurred during request processing. Check database connections and try again."></c:out>
<c:out value="If problem persists check for any inconsistency in database, and contact your system vendor."></c:out>
<br>
<br>
</span>
<span class="error" style="font-size: medium; color: red">
<c:out value="${exceptionTrace}"></c:out>
<br>
<br>
</span>
<%@ include file="/WEB-INF/template/footer.jsp" %>