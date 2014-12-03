<script type="text/javascript" src="/ImmunizationReminder/date.js">
</script>
<%@page import="org.ird.immunizationreminder.web.utils.IMRGlobals"%>
<script type="text/javascript">
<!--

function chk() {
	alert(document.getElementById("tb").value);
	   var reg = /^[0-2][0-3]:[0-5][0-9]:[0-5][0-9]/;
		alert(reg.test(document.getElementById("tb").value));

		
}
var globalDf="<%=IMRGlobals.GLOBAL_DATE_FORMAT%>";

function mydiff(datesmaller,dategreater,interval) {
    var second=1000, minute=second*60, hour=minute*60, day=hour*24, week=day*7;
    var date1=datesmaller;
    var date2=dategreater;
    var timediff = date2 - date1;
    if (isNaN(timediff)) return NaN;
    switch (interval) {
        case "years": return date2.getFullYear() - date1.getFullYear();
        case "months": return (
            ( date2.getFullYear() * 12 + date2.getMonth() )
            -
            ( date1.getFullYear() * 12 + date1.getMonth() )
        );
        case "weeks"  : return Math.floor(timediff / week);
        case "days"   : return Math.floor(timediff / day); 
        case "hours"  : return Math.floor(timediff / hour); 
        case "minutes": return Math.floor(timediff / minute);
        case "seconds": return Math.floor(timediff / second);
        default: return undefined;
    }
}
function myd(){
	alert('here');
	alert(new Date());
	alert(Date.today());
	alert(Date.parseExact("12-01-2011",globalDf));
	alert(Date.today()-Date.parseExact("12-01-2011",globalDf));
	alert(mydiff(Date.today(),Date.parseExact("12-01-2011",globalDf), "weeks"));
}
//-->
</script>
<input type="text" id="tb"/>
<input type="button" onclick="myd();"/>
 <br/>
