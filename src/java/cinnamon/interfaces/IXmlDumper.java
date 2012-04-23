package cinnamon.interfaces;

import org.dom4j.Element;

/**
 * An object of a class which implements IXmlDumper is guaranteed to be convertable
 * to an XML-representation. This is used in server.helpers.XmlDumper to create
 * nice toString() output.
 */
public interface IXmlDumper {

    public void toXmlElement(Element root);

}
