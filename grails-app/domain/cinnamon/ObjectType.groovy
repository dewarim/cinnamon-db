package cinnamon

import cinnamon.global.Constants
import org.dom4j.Element
import org.dom4j.DocumentHelper
import cinnamon.i18n.LocalMessage
import cinnamon.utils.ParamParser

class ObjectType implements Serializable  {

    static constraints = {
        name(size: 1..Constants.NAME_LENGTH, blank: false, unique: true)
        config size: 1..Constants.METADATA_SIZE, blank: false
    }

    static mapping = {
        cache true
        table 'objtypes'
        version 'obj_version'
    }

    ObjectType() {
    }

    ObjectType(String name, String config) {
        this.name = name
        if(config){
            this.config = config
        }
    }

    String name
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
            e.addElement("id").addText(type.id.toString())
            e.addElement("name").addText( LocalMessage.loc(type.name))
            e.addElement("sysName").addText(type.name)
            e.addElement("config").addText(type.config)
            e = (Element) ParamParser.parseXml(e.asXML(), null)
        }
        return e;
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof ObjectType)) return false

        ObjectType that = (ObjectType) o

        if (config != that.config) return false
        if (name != that.name) return false

        return true
    }

    int hashCode() {
        int result
        result = (name != null ? name.hashCode() : 0)
        return result
    }

    String toString(){
        return "ObjectType #"+id+": name="+name;
    }
}
