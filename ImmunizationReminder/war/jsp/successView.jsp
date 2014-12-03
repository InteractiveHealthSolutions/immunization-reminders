<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="/WEB-INF/template/pagebody.jsp" %>
<br/>
<br/>
<span style="color: red; font-style: italic">
<c:out value="${errormessage}"></c:out>
</span>
<span style="color: purple; font-style: italic">
<c:out value="${message}"></c:out>
</span>
<br/>
<br/>
<a href="${url}" >${urlText}</a>
<br/><br/>
<c:if test="${errormessage == null || errormessage == ''}">
OR
<br/><br/>
<a href="#" onclick="${NEWLY_ADDED_ENTITY_URL}" >${NEWLY_ADDED_ENTITY_MESSAGE}  ..</a>
<br/><br/>
</c:if>
OR
<br/><br/>

<a href="${ALL_ENTITYS_URL}" >${ALL_ENTITYS_MESSAGE}  ..</a>


<%@ include file="/WEB-INF/template/sidebar.jsp" %>
<%@ include file="/WEB-INF/template/footer.jsp" %>