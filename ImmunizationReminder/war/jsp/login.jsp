 <%@ include file="/jsp/include.jsp" %>
 <%@page import="org.ird.immunizationreminder.web.utils.UserSessionUtils"%>
 <%@ include file="/WEB-INF/template/headersimple.jsp" %>
<%
if(UserSessionUtils.getActiveUser(request)!=null){
%>
<c:redirect url="mainpage.htm"></c:redirect>
<%	
}
%>
<center>
	<h1>Welcome to Immunization Reminder Study</h1>
	<br/>
	<form method="post">
		<table width="25%" border="1">
			<tr>
				<td align="center" bgcolor="lightblue">Log on</td>
			</tr>
			<tr>
				<td>
					<table border="0" width="100%">
					<tr>
					<td colspan="2">
					
						<span class="error" style="font-size: x-small; color: red">
						<c:out value="${logmessage}"></c:out>
						</span>
						</td></tr>
						<tr>
						<td width="33%" align="right">Username: </td>
							<td width="66%" align="left">
								<spring:bind path="credentials.username">
								<input type="text" 
								       name="username" 
								       value=""/>	
								        <span class="error" style="font-size: x-small; color: red">
								        <c:out value="${status.errorMessage}"/></span>
								       	
								</spring:bind>
							</td>
						</tr>
						<tr>
							<td width="33%" align="right">Password: </td>
							<td width="66%" align="left">
								<spring:bind path="credentials.password">
									<input type="password" name="password" />

								 	<span class="error" style="font-size: x-small; color: red">
								 	<c:out value="${status.errorMessage}"/></span>
								</spring:bind>
							</td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<input type="submit" align="middle" value="Logon">
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
</center>
<script type="text/javascript">
 document.getElementById('username').focus();
</script>
<br>
<br>
<br>
<br>
 <%@ include file="/WEB-INF/template/footersimple.jsp" %>