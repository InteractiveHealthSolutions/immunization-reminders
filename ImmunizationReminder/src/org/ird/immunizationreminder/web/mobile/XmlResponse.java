package org.ird.immunizationreminder.web.mobile;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class XmlResponse {
	private Document document;
	private Element root;
	
	private XmlResponse(String responseStatus) throws Exception{
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        document = docBuilder.newDocument();

        //create the root element 
        root = document.createElement(ResponseParam.XML_RESPONSE_ROOT_TAG);
        document.appendChild(root);
        
        //can be modified to set some attributes and values.....
        addElement(ResponseParam.RESPONSE_STATUS, responseStatus);
	}
	public static XmlResponse getErrorResponse(String errorMsg){
		XmlResponse xmlresp;
		try {
			xmlresp = new XmlResponse(ResponseParam.RESPONSE_STATUS_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		xmlresp.addElement(ResponseParam.RESPONSE_MESSAGE, errorMsg);
		return xmlresp;
	}
	public static XmlResponse getSuccessResponse(){
		XmlResponse xmlresp;
		try {
			xmlresp = new XmlResponse(ResponseParam.RESPONSE_STATUS_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return xmlresp;
	}
	public void addElement(String name , String value){
		//create child element, add an attribute, and add to root
        Element child = document.createElement(name);
        //child.setAttribute("name", "value");
        root.appendChild(child);
        
        //add a text element to the child
        Text text = document.createTextNode(value);
        text.setNodeValue(value);
        child.appendChild(text);
	}
	
	public String docToString() throws TransformerException{
        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.INDENT, "no");

        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(document);
        trans.transform(source, result);
        return sw.toString();
    }
	
	@Override
	public String toString() {
		try {
			return docToString();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return "";
	}
}
