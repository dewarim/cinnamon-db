package cinnamon;

import cinnamon.interfaces.IXmlDumper;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Dump information about an object whose class implements IXmlDumper.
 * This class uses the "toXmlElement" method of the class have toString() print
 * meaningful output (even though its in XML format).
 * Note: due to problems with Grails and initialization of LocalMessage DAO, this
 * class is currently not in use.
 */
public class XmlDumper {

    static private Logger log = LoggerFactory.getLogger(XmlDumper.class);

    static public String dumpPrettyXml(IXmlDumper dumpling){
        if(dumpling == null){
            return "XmlDumper received null object.";
        }
        try{
            Document doc = DocumentHelper.createDocument(
                    DocumentHelper.createElement("RelationResolver"));
            dumpling.toXmlElement(doc.getRootElement());
            OutputFormat format = OutputFormat.createPrettyPrint();
            StringWriter sw = new StringWriter();
            XMLWriter xw = new XMLWriter(sw, format);
            xw.write(doc);
            return sw.toString();
        }
        catch (IOException iox){
            log.debug("failed on toString()",iox);
            return dumpling.getClass()+"#"+dumpling.hashCode();
        }
    }

}
