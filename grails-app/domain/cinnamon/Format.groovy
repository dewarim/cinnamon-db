package cinnamon

import cinnamon.global.Constants
import org.dom4j.Element
import org.dom4j.DocumentHelper
import cinnamon.utils.ParamParser
import cinnamon.i18n.LocalMessage

class Format {

    static constraints = {
        description( size: 0..Constants.DESCRIPTION_SIZE, blank: true)
        name(size: 1..Constants.NAME_LENGTH, blank: false, unique: true)
        extension(size: 1..64, blank: false)
        contenttype(size: 1..128, blank: false)
    }

    static mapping = {
        table 'formats'
    }

    String name
    String description
    String extension
    String contenttype

    /**
     * Add the Format's fields as child-elements to a new element with the given name.
     * If the format is null, simply return an empty element.
     * @param elementName
     * @param format
     * @return the new element.
     */
    public static Element asElement(String elementName, Format format){
        // TODO: if necessary, cache the result of the xml transform.
        // With Hibernate/Grails perhaps use an onUpdate-Filter to
        // add the XML-serialization to a cache field.
        Element e = DocumentHelper.createElement(elementName);
        if(format != null){
//            if(xmlNode != null){
//                xmlNode.setName(elementName);
//                return (Element) ParamParser.parseXml(format.xmlNode.asXML(), null);
//            }
            e.addElement("id").addText(String.valueOf(format.getId()));
            e.addElement("name").addText( LocalMessage.loc(format.getName()));
            e.addElement("sysName").addText(format.getName());
            e.addElement("description").addText(  LocalMessage.loc(format.getDescription()));
            e.addElement("contentType").addText(format.getContenttype());
            e.addElement("extension").addText(format.getExtension());
//            format.xmlNode = e;
            e = (Element) ParamParser.parseXml(e.asXML(), null);
        }
        return e;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Format)) return false;

        Format format = (Format) o;

        if (contenttype != null ? !contenttype.equals(format.contenttype) : format.contenttype != null) return false;
        if (description != null ? !description.equals(format.description) : format.description != null) return false;
        if (extension != null ? !extension.equals(format.extension) : format.extension != null) return false;
        if (name != null ? !name.equals(format.name) : format.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (extension != null ? extension.hashCode() : 0);
        result = 31 * result + (contenttype != null ? contenttype.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
