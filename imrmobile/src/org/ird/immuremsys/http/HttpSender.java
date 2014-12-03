package org.ird.immuremsys.http;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Hashtable;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import org.ird.immuremsys.util.XmlUtil;

public class HttpSender
{
	public MessageEntry	entry;
	public String		baseUrl;
	public Hashtable	model;

	public Hashtable doPost (String baseUrl, MessageEntry entry)
	{
		HttpConnection hc = null;
		OutputStream os = null;
		String url = null;
		int responseCode;
		boolean waitForResponse;
		model = null;

		url = entry.getUrl ();
		waitForResponse = entry.getWaitForResponse ();

		try
		{
			hc = (HttpConnection) Connector.open (url, Connector.READ_WRITE, true);
			hc.setRequestMethod (HttpConnection.POST);
			hc.setRequestProperty ("Content-Type", "application/x-www-form-urlencoded");
			hc.setRequestProperty ("User-Agent", "Profile/MIDP-2.0 Configuration/CLDC-1.0");
			hc.setRequestProperty ("Content-Language", "en-US");

			System.out.println ("URL + Post Paramters :" + url+ entry.getPostParams ());

			os = hc.openOutputStream ();
			os.write (entry.getPostParams ().getBytes ());
			os.flush ();

			responseCode = hc.getResponseCode ();
			if (responseCode != HttpConnection.HTTP_OK)
			{
				throw new IOException ("Http response code: " + responseCode);
			}

			if (waitForResponse)
			{
				model = XmlUtil.parseXmlResponse (new InputStreamReader (hc.openInputStream ()));
			}
		}
		catch (ClassCastException e)
		{
			throw new IllegalArgumentException ("Not an HTTP URL");
		}
		catch (IOException e)
		{
			e.printStackTrace ();
		}
		catch (SecurityException e)
		{
			e.printStackTrace ();
		} 
		catch (Exception e)
		{
			e.printStackTrace ();
		}
		finally
		{
			if (os != null)
			{
				try
				{
					os.close ();
				}
				catch (IOException e)
				{
					e.printStackTrace ();
				}
			}

			if (hc != null)
			{
				try
				{
					hc.close ();
				}
				catch (IOException e)
				{
					e.printStackTrace ();
				}
			}
		}
		return model;
	}
}
