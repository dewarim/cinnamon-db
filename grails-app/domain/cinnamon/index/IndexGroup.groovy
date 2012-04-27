package cinnamon.index

import cinnamon.global.Constants
import org.dom4j.Element
import cinnamon.i18n.LocalMessage

class IndexGroup implements Serializable{

    static constraints = {
        name unique:true, size: 1..Constants.NAME_LENGTH
    }

    static mapping = {
        version 'obj_version'
    }
    
    static hasMany = [items:IndexItem]

    String name


    public IndexGroup(){

    }

    public IndexGroup(String name){
        this.name = name;
    }

    public IndexGroup(Map<String,String> fields){
        this.name = fields.get("name");
    }

    public void toXmlElement(Element root, Boolean includeIndexItems){
        Element group = root.addElement("indexGroup");
        group.addElement("id").addText(String.valueOf(id));
        group.addElement("name").addText(LocalMessage.loc(name));
        group.addElement("sysName").addText(name);
        Element itemList = group.addElement("indexItems");
        if(includeIndexItems){
            for(IndexItem item : items){
                item.toXmlElement(itemList);
            }
        }
    }

    public void toXmlElement(Element root){
        toXmlElement(root, false);
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof IndexGroup)) return false

        IndexGroup that = (IndexGroup) o

        if (name != that.name) return false

        return true
    }

    int hashCode() {
        return (name != null ? name.hashCode() : 0)
    }
}
