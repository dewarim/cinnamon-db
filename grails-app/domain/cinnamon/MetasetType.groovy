package cinnamon

import cinnamon.global.Constants
import org.dom4j.Element
import org.dom4j.DocumentHelper
import cinnamon.i18n.LocalMessage

class MetasetType {

    static constraints = {
        name unique: true, size: 1..Constants.NAME_LENGTH
        config size: 1..Constants.XML_PARAMS
    }

    static mapping = {
        cache true
        table('metaset_types')
        version 'obj_version'
    }
    
    String name
    String config = '<metaset />'

    public MetasetType(){

    }

    public MetasetType(String name, String config){
        this.name = name;
        if(config != null){
            this.config = config;
        }
    }

    /**
     * Add the MetasetType's fields as child-elements to a new element with the given name.
     * If the type parameter is null, simply return an empty element.
     * @param elementName the parent element name for the folder type's elements.
     * @param type the metaset type that is going to be serialized to XML.
     * @return the new element.
     */
    public static Element asElement(String elementName, MetasetType type){
        Element e = DocumentHelper.createElement(elementName);
        if(type != null){
            e.addElement("id").addText(String.valueOf(type.getId()));
            e.addElement("name").addText( LocalMessage.loc(type.getName()));
            e.addElement("sysName").addText(type.getName());
            e.addElement("config").addText(type.getConfig());
        }
        return e;
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof MetasetType)) return false

        MetasetType that = (MetasetType) o

        if (config != that.config) return false
        if (name != that.name) return false

        return true
    }

    int hashCode() {
        int result
        result = (name != null ? name.hashCode() : 0)
        return result
    }
}
