package cinnamon.relation

import cinnamon.global.Constants
import cinnamon.interfaces.IXmlDumper
import cinnamon.utils.ParamParser
import cinnamon.exceptions.CinnamonException
import org.dom4j.Element
import cinnamon.i18n.LocalMessage
import cinnamon.ObjectSystemData
import cinnamon.relation.resolver.RelationSide

class RelationType implements IXmlDumper {

    static constraints = {
        description( size: 0..Constants.DESCRIPTION_SIZE, blank: true)
        name(size: 1..Constants.NAME_LENGTH, blank: false, unique: true)
    }

    static mapping = {
        cache true
        table 'relationtypes'
        version 'obj_version'
    }

    String name
    String description
    Boolean leftobjectprotected
    Boolean rightobjectprotected
    Boolean cloneOnRightCopy = false
    Boolean cloneOnLeftCopy = false
    RelationResolver leftResolver
    RelationResolver rightResolver

    public RelationType(){}

    public RelationType(Map<String, String> cmd) {
        name = cmd.get("name");
        description = cmd.get("description");
        leftobjectprotected = cmd.get("leftobjectprotected").equals("true");
        rightobjectprotected = cmd.get("rightobjectprotected").equals("true");
        cloneOnLeftCopy = cmd.get("cloneOnLeftCopy").equals("true");
        cloneOnRightCopy = cmd.get("cloneOnRightCopy").equals("true");

        /*
         * Design note: resolve by name is intended to make it easier for testing
         * as you can create a test relation type without having to look up the id of the
         * default (or to-be-tested) relation resolver.
         */
        if (cmd.containsKey("right_resolver")) {
            rightResolver = RelationResolver.findByName(cmd.get("right_resolver"));
        } else if (cmd.containsKey("right_resolver_id")) {
            rightResolver = RelationResolver.get(ParamParser.parseLong(cmd.get("right_resolver_id"), "error.param.right_resolver_id"));
        } else {
            rightResolver = RelationResolver.findByName(Constants.RELATION_RESOLVER_FIXED);
        }
        if (cmd.containsKey("left_resolver")) {
            leftResolver = RelationResolver.findByName(cmd.get("left_resolver"));
        } else if (cmd.containsKey("left_resolver_id")) {
            rightResolver = RelationResolver.get(ParamParser.parseLong(cmd.get("left_resolver_id"), "error.param.left_resolver_id"));
        } else {
            leftResolver = RelationResolver.findByName(Constants.RELATION_RESOLVER_FIXED);
        }

        if (rightResolver == null) {
            throw new CinnamonException("error.param.right_resolver_id");
        }
        if (leftResolver == null){
            throw new CinnamonException("error.param.left_resolver_id");
        }

        log.debug("leftResolver: " + leftResolver);
        log.debug("rightResolver: " + rightResolver);
    }


    public void toXmlElement(Element root) {
        Element rt = root.addElement("relationType");
        rt.addElement("id").addText(String.valueOf(getId()));
        rt.addElement("name").addText(LocalMessage.loc(getName()));
        rt.addElement("sysName").addText(getName());
        rt.addElement("description").addText(LocalMessage.loc(getDescription()));
        rt.addElement("rightobjectprotected").addText(rightobjectprotected.toString());
        rt.addElement("leftobjectprotected").addText(leftobjectprotected.toString());
        rt.addElement("cloneOnLeftCopy").addText(cloneOnLeftCopy.toString());
        rt.addElement("cloneOnRightCopy").addText(cloneOnRightCopy.toString());
        rt.addElement("leftResolver").addText(leftResolver.getName());
        rt.addElement("rightResolver").addText(rightResolver.getName());
    }

    public String toString() {
        return "RelationResolver::name:" + getName();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RelationType)) return false;

        RelationType that = (RelationType) o;

        if (id != that.id) return false;
        if (cloneOnLeftCopy != null ? !cloneOnLeftCopy.equals(that.cloneOnLeftCopy) : that.cloneOnLeftCopy != null)
            return false;
        if (cloneOnRightCopy != null ? !cloneOnRightCopy.equals(that.cloneOnRightCopy) : that.cloneOnRightCopy != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (leftResolver != null ? !leftResolver.equals(that.leftResolver) : that.leftResolver != null) return false;
        if (leftobjectprotected != null ? !leftobjectprotected.equals(that.leftobjectprotected) : that.leftobjectprotected != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (rightResolver != null ? !rightResolver.equals(that.rightResolver) : that.rightResolver != null)
            return false;
        if (rightobjectprotected != null ? !rightobjectprotected.equals(that.rightobjectprotected) : that.rightobjectprotected != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
