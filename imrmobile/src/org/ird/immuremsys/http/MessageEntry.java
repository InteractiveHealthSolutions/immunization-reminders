/* 
 * Copyright (c) 2008 Interactive Research & Development (IRD)
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:

 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package org.ird.immuremsys.http;

/**
 * 
 * @author Omar
 * 
 *         Use REQUEST_METHOD_HTTP_GET for any request that is simply querying
 *         the database use REQUEST_METHOD_HTTP_POST for all other request that
 *         may store or update data etc secure paramater is used to specify the
 *         use of HttpsConnection vs HttpConnection
 */
public class MessageEntry
{
	public static int	REQUEST_METHOD_HTTP_POST	= 0;
	public static int	REQUEST_METHOD_HTTP_GET		= 1;

	/**
	 * waitForResponseis only valid for REQUEST_METHOD_HTTP_POST as
	 * REQUEST_METHOD_HTTP_GET will alwasy wait for a response, hence get
	 */

	private String		url;
	private String		postParams;
	private boolean		waitForResponse;
	private int			requestMethod;
	private boolean		secure;

	public MessageEntry ()
	{
	}

	public MessageEntry (String url, String postParams, boolean waitForResponse, int requestMethod, boolean secure)
	{
		this.url = url;
		this.postParams = postParams;
		this.waitForResponse = waitForResponse;
		this.requestMethod = requestMethod;
		this.secure = secure;
	}

	public String getUrl ()
	{
		return url;
	}

	public void setUrl (String url)
	{
		this.url = url;
	}

	public String getPostParams ()
	{
		return postParams;
	}

	public void setPostParams (String postParams)
	{
		this.postParams = postParams;
	}

	public boolean getWaitForResponse ()
	{
		return waitForResponse;
	}

	public void setWaitForResponse (boolean waitForResponse)
	{
		this.waitForResponse = waitForResponse;
	}

	public int getRequestMethod ()
	{
		return requestMethod;
	}

	public void setRequestMethod (int requestMethod)
	{
		this.requestMethod = requestMethod;
	}

	public boolean isSecure ()
	{
		return secure;
	}

	public void setSecure (boolean secure)
	{
		this.secure = secure;
	}

	public String toString ()
	{
		return "URL:" + url + ";params:" + postParams;
	}
}
