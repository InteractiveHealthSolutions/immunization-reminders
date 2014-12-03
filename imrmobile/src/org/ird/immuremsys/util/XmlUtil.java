package org.ird.immuremsys.util;

import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Hashtable;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XmlUtil
{

	public static String createTag (String tagName, String tagData)
	{
		return createStartTag (tagName) + tagData + createEndTag (tagName);
	}

	public static String createStartTag (String tagName)
	{
		return "<" + tagName + ">";
	}

	public static String createEndTag (String tagName)
	{
		return "</" + tagName + ">";
	}

	public static Hashtable parseXmlResponse (InputStreamReader isr) throws IOException, XmlPullParserException
	{
		KXmlParser parser = new KXmlParser ();
		Hashtable model = null;

		try
		{
			parser.setInput (isr);

			// if its text, does'nt have a name, but a value
			parser.nextTag ();
			model = new Hashtable ();
			model = getNodeValuePairs (parser, model, "tbrresponse");

		}
		finally
		{
			System.out.println ("closing reader");
			isr.close ();
		}

		return model;
	}

	private static Hashtable getNodeValuePairs (KXmlParser parser, Hashtable model, String elementTag) throws XmlPullParserException, IOException
	{
		// Parse our XML file
		String name = "";
		String text = "";
		while (parser.nextTag () != XmlPullParser.END_TAG)
		{
			name = "";
			text = "";
			parser.require (XmlPullParser.START_TAG, null, null);
			name = parser.getName ();
			text = parser.nextText ();

			System.out.println (name + ":" + text);

			model.put (name, text);

			parser.require (XmlPullParser.END_TAG, null, null);
		}
		return model;
	}

}
