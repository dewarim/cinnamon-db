package cinnamon.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;

public class ParamParser {

	public static Long parseLong(String param, String message){
		try {
			return Long.parseLong(param);
		} catch (NumberFormatException e) {
			throw new RuntimeException(message);
		}
	}
	public static Integer parseInt(String param, String message){
		try {
			return Integer.parseInt(param);
		} catch (NumberFormatException e) {
			throw new RuntimeException(message);
		}
	}

    /**
     * Parses a string to a boolean value. The parameter is converted to lower case before
     * parsing.
     * @param param the string to parse
     * @param message the message in case something goes wrong.
     * @return the boolean result.
     */
    public static Boolean parseBoolean(String param, String message){
        
        try{
            String p = param.trim().toLowerCase();
			if(p.equals("true")){
                return true;
            }
            else if(p.equals("false")){
                return false;
            }
            else{
                throw new RuntimeException("error.param");
            }
		} catch (RuntimeException e) {
			throw new RuntimeException(message);
		}
    }

	public static Node parseXml(String xml, String message){
		return ParamParser.parseXmlToDocument(xml, message).getRootElement().detach();	
	}

    public static Document parseXmlToDocument(String xml){
        return parseXmlToDocument(xml, null);
    }

	public static Document parseXmlToDocument(String xml, String message){
		if(message == null){
			 message = "error.parse.xml";
		}
		try{
			xml = xml.replaceAll("^(?:\\xEF\\xBB\\xBF|\uFEFF)", ""); // remove BOM on UTF-8 Strings.
			SAXReader reader = new SAXReader();
			// ignore dtd-declarations
			reader.setIncludeExternalDTDDeclarations(false);
			// do not validate - we are only interested in receiving a doc.
			reader.setValidation(false);
			return reader.read(new StringReader(xml));
		}
		catch (DocumentException e) {
            Logger log = LoggerFactory.getLogger(ParamParser.class);
            log.debug("ParamParser::DocumentException::", e);
			throw new RuntimeException(message);
		}
	}
	
	public static String dateToIsoString(Date date){
		return String.format("%1$tFT%1$tT", date);
	}
	
	// Could be placed in a more specific XML class.
	/**
	 * Turn a raw XML string and return it in an indented
	 * and more human readable form.
	 * @param xml an XML document as string.
	 * @return an indented version of the input XML.  
	 */
	public static String prettyPrint(String xml){
		String prettyXML;
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			Document doc = DocumentHelper.parseText(xml);
			StringWriter sw = new StringWriter();
			XMLWriter xw = new XMLWriter(sw, format);
			xw.write(doc);
			prettyXML = sw.toString();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return prettyXML;
	}
}
