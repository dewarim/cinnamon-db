package cinnamon.relation

import cinnamon.global.Constants
import cinnamon.interfaces.IXmlDumper
import org.dom4j.Element
import cinnamon.i18n.LocalMessage
import cinnamon.ObjectSystemData
import cinnamon.relation.resolver.RelationSide

class RelationType implements IXmlDumper {

    static constraints = {
        name(size: 1..Constants.NAME_LENGTH, blank: false, unique: true)
    }

    static mapping = {
        cache true
        table 'relationtypes'
        version 'obj_version'
    }

    String name
    Boolean leftobjectprotected
    Boolean rightobjectprotected
    Boolean cloneOnRightCopy = false
    Boolean cloneOnRightVersion = false
    Boolean cloneOnLeftCopy = false
    Boolean cloneOnLeftVersion = false
    RelationResolver leftResolver
    RelationResolver rightResolver

    public RelationType(){}
    
    public RelationType(name,
                        leftProtected, rightProtected,
                        leftResolver, rightResolver,
                        cloneLeftCopy, cloneRightCopy){
        this.name = name
        this.leftobjectprotected = leftProtected
        this.rightobjectprotected = rightProtected
        this.leftResolver = leftResolver
        this.rightResolver = rightResolver
        this.cloneOnLeftCopy = cloneLeftCopy
        this.cloneOnRightCopy = cloneRightCopy
    } 
    
    public RelationType(name,
                        leftProtected, rightProtected,
                        leftResolver, rightResolver,
                        cloneLeftCopy, cloneRightCopy,
            cloneLeftVersion, cloneRightVersion
    ){
        this(name, leftProtected,rightProtected, leftResolver, rightResolver, cloneLeftCopy, cloneRightCopy)
        this.cloneOnLeftVersion = cloneLeftVersion
        this.cloneOnRightVersion = cloneRightVersion
    }
    
    public void toXmlElement(Element root) {
        Element rt = root.addElement("relationType");
        rt.addElement("id").addText(String.valueOf(getId()));
        rt.addElement("name").addText(LocalMessage.loc(getName()));
        rt.addElement("sysName").addText(getName());
        rt.addElement("rightobjectprotected").addText(rightobjectprotected.toString());
        rt.addElement("leftobjectprotected").addText(leftobjectprotected.toString());
        rt.addElement("cloneOnLeftCopy").addText(cloneOnLeftCopy.toString());
        rt.addElement("cloneOnLeftVersion").addText(cloneOnLeftVersion.toString());
        rt.addElement("cloneOnRightCopy").addText(cloneOnRightCopy.toString());
        rt.addElement("cloneOnRightVersion").addText(cloneOnRightVersion.toString());
        rt.addElement("leftResolver").addText(leftResolver.getName());
        rt.addElement("rightResolver").addText(rightResolver.getName());
    }

    public String toString() {
        return "RelationType::name:" + getName();
    }

    /**
     * Find the correct OSD version for a given relation side.
     *
     * @param relation     The relation for which the correct osd version is needed.
     * @param osd          the OSD whose version needs to be determined.
     * @param relationSide the side of the relation for which you need the OSD version to be resolved.
     * @return the version as found by the Resolver class.
     */
    ObjectSystemData findOsdVersion(Relation relation, ObjectSystemData osd, RelationSide relationSide) {
        switch (relationSide) {
            case RelationSide.LEFT:
                return leftResolver.resolveOsdVersion(relation, osd, relationSide);
            case RelationSide.RIGHT:
                return rightResolver.resolveOsdVersion(relation, osd, relationSide);
        }
        return null; // is never reached unless RelationSide is null. And then you are up a certain creek without a paddle anyway.
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof RelationType)) return false

        RelationType that = (RelationType) o

        if (cloneOnLeftCopy != that.cloneOnLeftCopy) return false
        if (cloneOnLeftVersion != that.cloneOnLeftVersion) return false
        if (cloneOnRightCopy != that.cloneOnRightCopy) return false
        if (cloneOnRightVersion != that.cloneOnRightVersion) return false
        if (leftResolver != that.leftResolver) return false
        if (leftobjectprotected != that.leftobjectprotected) return false
        if (name != that.name) return false
        if (rightResolver != that.rightResolver) return false
        if (rightobjectprotected != that.rightobjectprotected) return false

        return true
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
