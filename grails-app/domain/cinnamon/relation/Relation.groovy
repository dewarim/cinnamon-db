package cinnamon.relation

import cinnamon.ObjectSystemData
import cinnamon.exceptions.CinnamonException
import cinnamon.utils.ParamParser
import org.dom4j.Element
import org.dom4j.Node

class Relation {

    static constraints = {
        leftOSD unique: ['rightOSD', 'type']
    }

    static mapping = {
        table('relations')
        version 'obj_version'
        leftOSD column: 'left_id'
        rightOSD column: 'right_id'
    }

    RelationType type
    ObjectSystemData leftOSD
    ObjectSystemData rightOSD
    String metadata = "<meta />"

    public Relation(){

    }

    public Relation(RelationType type, ObjectSystemData left, ObjectSystemData right, String metadata){
        this.type = type;
        this.leftOSD = left;
        this.rightOSD = right;
        setMetadata(metadata);
    }

    public void setMetadata(String metadata) {
        if(metadata == null || metadata.trim().length() == 0){
            this.metadata = "<meta/>";
        }
        else{
            ParamParser.parseXmlToDocument(metadata, "error.param.metadata");
            this.metadata = metadata;
        }
    }

    public void toXmlElement(Element root, Boolean includeMetadata){
        Element relation = root.addElement("relation")
        relation.addElement("id").addText(String.valueOf(getId()) )
        relation.addElement("leftId").addText( String.valueOf(leftOSD.getId()))
        relation.addElement("rightId").addText( String.valueOf(rightOSD.getId()))
        relation.addElement("type").addText(type.getName())
        relation.addElement("typeId").addText(String.valueOf(type.id))
        if(includeMetadata){
            Node metadata = ParamParser.parseXml(getMetadata(), null)
            relation.add(metadata)
        }
    }

    public void toXmlElement(Element root){
        toXmlElement(root, false);
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Relation)) return false

        Relation relation = (Relation) o

        if (leftOSD != relation.leftOSD) return false
        if (metadata != relation.metadata) return false
        if (rightOSD != relation.rightOSD) return false
        if (type != relation.type) return false

        return true
    }

    int hashCode() {
        int result
        result = (type != null ? type.hashCode() : 0)
        result = 31 * result + (leftOSD != null ? leftOSD.hashCode() : 0)
        result = 31 * result + (rightOSD != null ? rightOSD.hashCode() : 0)
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0)
        return result
    }


}
