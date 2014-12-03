package org.ird.immunizationreminder.web.mobile;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;

import com.mysql.jdbc.StringUtils;

public class IMRSMobileServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException
	{
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		try
		{
			int version = Integer.parseInt (req.getParameter (RequestParam.APP_VER.getParamName()));

			if (version != IMRMobileGlobals.IMR_VERSION)
			{
				sendResponse(resp, XmlResponse.getErrorResponse("Aap ne jis application say login kia hay uska version (" + version + ") purana hay. New version (" + IMRMobileGlobals.IMR_VERSION
						+ ") ki Updated Application download karain aur dobara koshish karain").docToString());
			}
		}
		catch (Exception e)
		{
			try {
				sendResponse(resp, XmlResponse.getErrorResponse("Error occurred while finding version from request. Baraey meherbani Tbreach2 team say rujoo karain").docToString());
			} catch (TransformerException e1) {
				e1.printStackTrace();
			}
		}
		
		String service = req.getParameter(RequestParam.REQ_TYPE.getParamName());

		if (StringUtils.isEmptyOrWhitespaceOnly(service))
		{
			try
			{
				sendResponse(resp, XmlResponse.getErrorResponse("Request Type missing from Request. Contact Program Vendor").docToString());
			}
			catch (TransformerException e) {
				e.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if (service.equalsIgnoreCase(RequestParam.REQ_TYPE_FETCH.getParamName())) {
			try
			{
				FetchHandler.handleFetch(req, resp);
			}
			catch (TransformerException e) {
				e.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if (service.equalsIgnoreCase(RequestParam.REQ_TYPE_SUBMIT.getParamName())) {
			try
			{
				SubmitHandler.handleSubmit(req, resp);
			}
			catch (TransformerException e) {
				e.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void sendResponse(HttpServletResponse response , String responseToSend) throws IOException
	{
		System.out.println(responseToSend);
		response.getOutputStream().println(responseToSend);
	}
}
