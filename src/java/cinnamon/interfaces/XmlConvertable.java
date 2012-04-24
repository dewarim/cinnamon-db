package cinnamon.interfaces;

import org.dom4j.Element;

/**
 * Applies to all classes which can add themselves to an XML Element.
 * @author ingo
 *
 */
public interface XmlConvertable extends Comparable<XmlConvertable> {

	void toXmlElement(Element root);
//	Long getId();

}
