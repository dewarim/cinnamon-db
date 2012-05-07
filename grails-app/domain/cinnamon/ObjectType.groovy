package cinnamon

import cinnamon.global.Constants
import org.dom4j.Element
import org.dom4j.DocumentHelper
import cinnamon.i18n.LocalMessage
import cinnamon.utils.ParamParser

class ObjectType implements Serializable  {

    static constraints = {
        description( size: 0..Constants.DESCRIPTION_SIZE, blank: true)
        name(size: 1..Constants.NAME_LENGTH, blank: false, unique: true)
        config size: 1..Constants.METADATA_SIZE, blank: false
    }

    static mapping = {
        cache true
        table 'objtypes'
        version 'obj_version'
    }

    String name
    String description
    String config = '<meta />'

    /**
     * Add the ObjectType's fields as child-elements to a new element with the given name.
     * If the type parameter is null, simply return an empty element.
     * @param elementName
     * @param type
     * @return the new element.
     */
    static Element asElement(String elementName, ObjectType type){
        Element e = DocumentHelper.createElement(elementName);
        // TODO: perhaps add cache-field xmlNode for XML serialized version.
        if(type != null){
//            if(type.xmlNode != null){
//                type.xmlNode.setName(elementName);
//                return (Element) ParamParser.parseXml(type.xmlNode.asXML(), null);
//            }
            e.addElement("id").addText(String.valueOf(type.getId()));
            e.addElement("name").addText( LocalMessage.loc(type.getName()));
            e.addElement("sysName").addText(type.getName());
            e.addElement("description").addText(  LocalMessage.loc(type.getDescription()));
//            type.xmlNode = e;
            e = (Element) ParamParser.parseXml(e.asXML(), null);
        }
        return e;
    }

    @Override
    boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ObjectType)) return false;

        ObjectType that = (ObjectType) o;

        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    String toString(){
        return "ObjectType #"+id+": name="+name+" description="+description;
    }
}
