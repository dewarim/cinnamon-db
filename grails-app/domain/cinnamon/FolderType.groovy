package cinnamon

import org.dom4j.Element
import org.dom4j.DocumentHelper
import cinnamon.i18n.LocalMessage
import cinnamon.global.Constants

class FolderType {

    static constraints = {
        name(size: 1..Constants.NAME_LENGTH, blank: false, unique: true)
        config size: 1..Constants.METADATA_SIZE, blank: false
    }

    static mapping = {
        cache true
        table 'folder_types'
        version 'obj_version'
    }

    FolderType() {
    }

    FolderType(String name, String config) {
        this.name = name
        if(config){
            this.config = config
        }
    }

    String name
    String config = '<meta />'


    /**
     * Add the FolderType's fields as child-elements to a new element with the given name.
     * If the type parameter is null, simply return an empty element.
     * @param elementName the parent element name for the folder type's elements.
     * @param type the folder type that is going to be serialized to XML.
     * @return the new element.
     */
    public static Element asElement(String elementName, FolderType type){
        Element e = DocumentHelper.createElement(elementName);
        if(type != null){
            e.addElement("id").addText(type.id.toString())
            e.addElement("name").addText( LocalMessage.loc(type.name))
            e.addElement("sysName").addText(type.name)
            e.addElement("config").addText(type.config)
        }
        return e;
    }  

    public String toString(){
        return "FolderType #"+id+" "+name
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof FolderType)) return false

        FolderType that = (FolderType) o

        if (config != that.config) return false
        if (name != that.name) return false

        return true
    }

    int hashCode() {
        return (name != null ? name.hashCode() : 0)
    }
}
