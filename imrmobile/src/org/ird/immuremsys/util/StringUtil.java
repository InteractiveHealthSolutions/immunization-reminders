package org.ird.immuremsys.util;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

public class StringUtil
{
	public static String replaceAll (String oldStr, String newStr, String inString)
	{
		while (inString.indexOf (oldStr) != -1)
			inString = replace (oldStr, newStr, inString);
		return inString;
	}

	public static String replace (String oldStr, String newStr, String inString)
	{
		int start = inString.indexOf (oldStr);
		if (start == -1)
		{
			return inString;
		}
		StringBuffer sb = new StringBuffer ();
		sb.append (inString.substring (0, start));
		sb.append (newStr);
		sb.append (inString.substring (start + oldStr.length ()));
		return sb.toString ();
	}

	/**
	 * Returns the current string value in an item field
	 * 
	 * @param item
	 * @return
	 */
	public static String getString (Item item)
	{
		if (item instanceof StringItem)
			return ((StringItem) item).getText ();
		if (item instanceof TextField)
			return ((TextField) item).getString ();
		if (item instanceof ChoiceGroup)
			return ((ChoiceGroup) item).getString (((ChoiceGroup) item).getSelectedIndex ());
		else
			return null;
	}

}
