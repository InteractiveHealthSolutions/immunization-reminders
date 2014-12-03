<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>Immunization Reminders System</title>
<meta name="keywords" content="" />
<meta name="description" content="" />
<link href="styles.css" rel="stylesheet" type="text/css" media="screen" />
</head>
<script type="text/javascript">
<!--
var pwdpopupWindow=null;
var centerWidth = (window.screen.width - 600) / 2;
var centerHeight = (window.screen.height - 400) / 2;

function changepwdpopup()
{
	pwdpopupWindow = window.open('changepwd.htm','change password','width=600,height=400,left='+centerWidth+',top='+centerHeight+',resizable=no,toolbar=no,location=no,scrollbars=no,directories=no,status=no,menubar=no,copyhistory=no');
	pwdpopupWindow.focus();
}

function parent_disable() {
if(pwdpopupWindow && !pwdpopupWindow.closed)
	pwdpopupWindow.focus();
}

//-->
</script>
<%@ include file="/jsp/include.jsp"%>

<body onFocus="parent_disable();" onclick="parent_disable();">
<div id="content">
<!-- header begins -->

<div id="header"> 
	<table width="100%">
	<tr><td>
	<div id="search">
			<p style="text-align: right;padding-right: 1cm;padding-top: 2px"><a href="#" onclick="changepwdpopup();">ChangePassword</a></p>
			<p style="text-align: right;padding-right: 1cm;padding-top: 2px"><a href="logout">Logout</a></p>
			<p style="text-align: right;padding-right: 1cm;padding-top: 2px;color: white;font-size: medium;font-weight: bolder;font-stretch: wider"><%=request.getSession().getAttribute("fullname") %></p>
	</div>
	<div id="logo">
		<h1>Immunization Reminders</h1>
		<h3>get every child vaccinated</h3>
	</div>
	</td>
	</tr>
	</table>
	<div id="menu">
			<table width="100%" style="padding: 3px">
				<tr>
					<td><a href="mainpage.htm" title="">Home</a></td>
					<td><a href="children.htm" title="">Children</a></td>
					<td><a href="vaccination.htm" title="">Vaccination</a></td>
					<td><a href="reporting.htm" title="">Reporting</a></td>
					<td><a href="reminders.htm" title="">Reminders</a></td>
					<td><a href="responses.htm" title="">Responses</a></td>
					<td><a href="users.htm" title="">Users</a></td>
				</tr>
			</table>
	</div>
</div>

<!-- header ends -->
