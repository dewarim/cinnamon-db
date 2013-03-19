package cinnamon.response;

import cinnamon.interfaces.Response;
import cinnamon.utils.ParamParser;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Deprecated
public class XmlResponse implements Response {
	
    Logger log = LoggerFactory.getLogger(this.getClass());
    
	HttpServletResponse res;
	Document doc = DocumentHelper.createDocument();
	
	public XmlResponse(){		
	
	}
	
	public XmlResponse(HttpServletResponse res){
		this.res = res;
	}
	
	public XmlResponse(HttpServletResponse res, Document doc){
		this.res = res;
		this.doc = doc;
	}
	
	public XmlResponse(HttpServletResponse res, String xml){
		this.res = res;		
		this.doc = ParamParser.parseXmlToDocument(xml, "failed to parse XmlResponse:\n" + xml);
	}
	
	@Override
	public HttpServletResponse getServletResponse() {
		return res;
	}

	@Override
	public void setServletResponse(HttpServletResponse res) {
		this.res = res;		
	}
	
	@Override
	public void write() throws IOException {
        //log.debug("Writing XML Response");
        int contentLength = doc.asXML().getBytes("UTF8").length;
        //log.debug("Length: "+contentLength);
        //log.debug("Response: ==|"+doc.asXML()+"|==");
        
		res.setContentType("application/xml; charset=UTF-8");
		XMLWriter writer = new XMLWriter(res.getWriter());
        res.setContentLength(contentLength);
		writer.write(doc);
		writer.close();
	}

	/**
	 * @return the doc
	 */
	public Document getDoc() {
		return doc;
	}

	/**
	 * @param doc the doc to set
	 */
	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public String getContent(){
		return doc.asXML();
	}
	
	/**
	 * Add a text node to the document root. This is useful for methods which
	 * only return one field like {@code <success>it works</success>}.
	 * @param nodeName name of the text node
	 * @param text text content of the new node
	 */
	public void addTextNode(String nodeName, String text){
		doc.addElement(nodeName).addText(text);
	}

    /**
     * Create or extend warnings node in response document.<br>
     * Result will be something like:<br>
     * <pre>
     * {@code
     *  <cinnamon>
     *   <otherNodes>...</otherNodes>
     *   <warnings>
     *     <warning>
     *          <message>warning.translation.incomplete</message>
     *          <content>untranslated text</content>
     *      </warning>
     *   </warnings>
     *  </cinnamon>
     * }
     * </pre>
     * @param warningMessage the warning message
     * @param content a String containing additional information like an object id or name.
     */
    public void addWarning(String warningMessage, String content){
        Node warningsNode = doc.selectSingleNode("/cinnamon/warnings");
        if(warningsNode == null){
            warningsNode = doc.getRootElement().addElement("warnings");
        }
        Element warning = ((Element) warningsNode).addElement("warning");
        warning.addElement("message").addText(warningMessage);
        warning.addElement("content").addText(content);
    }
}
