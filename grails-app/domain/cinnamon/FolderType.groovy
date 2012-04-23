package cinnamon

import org.dom4j.Element
import org.dom4j.DocumentHelper
import cinnamon.i18n.LocalMessage
import cinnamon.global.Constants

class FolderType {

    static constraints = {
        description( size: 0..Constants.DESCRIPTION_SIZE, blank: true)
        name(size: 1..Constants.NAME_LENGTH, blank: false, unique: true)
    }

    static mapping = {
        table 'folder_types'
    }

    String name
    String description


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
            e.addElement("id").addText(String.valueOf(type.getId()));
            e.addElement("name").addText( LocalMessage.loc(type.getName()));
            e.addElement("sysName").addText(type.getName());
            e.addElement("description").addText( LocalMessage.loc(type.getDescription()));
        }
        return e;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FolderType)) return false;

        FolderType that = (FolderType) o;

        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public String toString(){
        return "FolderType #"+id+" "+name+" ("+description+")";
    }
}
