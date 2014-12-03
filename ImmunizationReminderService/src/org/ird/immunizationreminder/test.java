package org.ird.immunizationreminder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.LoggedInUser;

public class test {
public static void main(String[] args) {
	String regx="";
	if(System.getProperty("os.name").toLowerCase().indexOf("linux")!=-1){
		regx="[a-zA-Z]:(\\\\[\\w-]+)+";
	}
	Pattern p=Pattern.compile(regx);
	Matcher m=p.matcher("c:\\jdksds-sds\\cxzjsbc");
	System.out.println(m.matches());
	
/*	System.out.println(Context.getIRSetting("child.child-id.min-length", "6"));
	try{
	Context.updateIrSetting("child.child-id.min-length", "5", null);;
	}catch (Exception e) {
		e.printStackTrace();
	}
	System.out.println(Context.getIRSetting("child.child-id.min-length", "7"));*/
}
}
