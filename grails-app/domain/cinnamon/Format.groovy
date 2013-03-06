package cinnamon

import cinnamon.global.Constants
import org.dom4j.Element
import org.dom4j.DocumentHelper
import cinnamon.utils.ParamParser
import cinnamon.i18n.LocalMessage

class Format implements Serializable  {

    static constraints = {
        description( size: 0..Constants.DESCRIPTION_SIZE, blank: true)
        name(size: 1..Constants.NAME_LENGTH, blank: false, unique: true)
        extension(size: 1..64, blank: false)
        contenttype(size: 1..128, blank: false)
        defaultObjectType nullable: true
    }

    static mapping = {
        cache true
        table 'formats'
        version 'obj_version'
    }

    String name
    String extension
    String contenttype
    ObjectType defaultObjectType

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
            e.addElement("contentType").addText(format.getContenttype());
            e.addElement("extension").addText(format.getExtension());            
            e.addElement("defaultObjectType").addText(format.defaultObjectType?.name ?: '')
//            format.xmlNode = e;
            e = (Element) ParamParser.parseXml(e.asXML(), null);
        }
        return e;
    }

    /**
     * Utility method: update Format data via map params (for example, from HTTP requests)
     * @param cmd
     */
    public void update(Map<String, String> cmd) {
        String name = cmd.get("name");
        if(name != null){
            setName(name);
        }

        String extension = cmd.get("extension");
        if(extension != null){
            setExtension(extension);
        }

        String contenttype = cmd.get("contenttype");
        if(contenttype != null){
            setContenttype(contenttype);
        }

        if (cmd.containsKey('default_object_type_id')){
            ObjectType objectType = ObjectType.get(cmd.get('default_object_type_id'))
            setDefaultObjectType(objectType)
        }
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Format)) return false

        Format format = (Format) o

        if (contenttype != format.contenttype) return false
        if (defaultObjectType != format.defaultObjectType) return false
        if (extension != format.extension) return false
        if (name != format.name) return false

        return true
    }

    int hashCode() {
        int result
        result = (name != null ? name.hashCode() : 0)
        result = 31 * result + (extension != null ? extension.hashCode() : 0)
        return result
    }
}
