function viewChildDetails(childId){
	var win;

	win=window.open('viewChildwindow.htm?pId='+childId,'ChildDetails:'+childId,'width=500,height=600,resizable=no,toolbar=no,location=no,scrollbars=yes,directories=no,status=no,menubar=no,copyhistory=no');
	win.focus();
}
function datediff(datesmaller,dategreater,interval) {
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

function isNumber (o) {
	return ! isNaN (o-0);
}

function makeDateFromString(dategiven) {
    var str1 = dategiven;
    var dt1  = parseInt(str1.substring(0,2),10);
    var mon1 = parseInt(str1.substring(3,5),10)-1;
    var yr1  = parseInt(str1.substring(6,10),10);
    
    var date1 = new Date(yr1, mon1, dt1);
    return date1;
}
function trim(s)
{
	if(s.length==0) return '';
	var l=0; var r=s.length -1;
	while(l < s.length && s[l] == ' ')
	{	l++; }
	while(r > l && s[r] == ' ')
	{	r-=1;	}
	return s.substring(l, r+1);
}
var afterDateSelected=function (obj) {
	var date1=Date.parseExact(obj.value,globalDf);

	if(date1.is().sunday()){
		alert('selected date is sunday, moving date to monday.');
		date1.add(1).days();
		obj.value=date1.toString(globalDf);
	}
};
function makeTextSelectedInDD(selectControl,valueToSelect){
	var sel = selectControl;
    var val = valueToSelect;
	for (i=0; i<sel.options.length; i++) {
		if (sel.options[i].value == val) {
			sel.selectedIndex = i;
		}
	}
}

//return the value of the radio button that is checked
//return an empty string if none are checked, or
//there are no radio buttons
function getCheckedValue(radioObj) {
	if(!radioObj)
		return "";
	var radioLength = radioObj.length;
	if(radioLength == undefined)
		if(radioObj.checked)
			return radioObj.value;
		else
			return "";
	for(var i = 0; i < radioLength; i++) {
		if(radioObj[i].checked) {
			return radioObj[i].value;
		}
	}
	return "";
}
function getGapFromChecked(radioObj) {
	if(!radioObj) return -10;
	var radioLength = radioObj.length;
	if(radioLength == undefined)
		if(radioObj.checked) return parseInt(radioObj.title);
		else return -10;
	for(var i = 0; i < radioLength; i++) {
		if(radioObj[i].checked) {
			return parseInt(radioObj[i].title);
		}
	}
	return -10;
}


function verifyTimeValues() {
	var inputs = document.getElementsByTagName('input');
	for (var i=0; i < inputs.length; i++)
	{
	   if (inputs[i].getAttribute('type') == 'text')
	   {
		   var reg = /^time([0-9_\-\.])+/;
		   if(reg.test(inputs[i].name)==true){
			   var reg2 = /^([0-1][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]/;
				if(reg2.test(inputs[i].value)==false){
					alert(inputs[i].name+" is not a valid time value");
					return false;
				}
		   }
	   }
	}
	return true;
}