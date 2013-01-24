package cinnamon.relation

import cinnamon.interfaces.IXmlDumper
import cinnamon.global.Constants
import cinnamon.interfaces.IRelationResolver
import cinnamon.exceptions.CinnamonException
import cinnamon.utils.ParamParser
import org.dom4j.Element
import cinnamon.ObjectSystemData
import cinnamon.relation.resolver.RelationSide

class RelationResolver implements Serializable, IXmlDumper {

    static constraints = {
        config(size: 0..Constants.METADATA_SIZE, blank: false)
        name(size: 1..Constants.NAME_LENGTH, blank: false, unique: true)
    }

    static mapping = {
        cache true
        table 'relation_resolvers'
        version 'obj_version'
        resolverClass column: 'class_name'
    }

    String name = ""
    String config = "<config />"
    Class<? extends IRelationResolver> resolverClass

    RelationResolver() { }
    
//    RelationResolver(Map fields) {
//        this(fields.get("config"), fields.get("class_name"), fields.get("name"));
//    }

    RelationResolver(String config, String className, String name) {
        if (className == null) {
            throw new CinnamonException("error.param.classname");
        }
        setConfig(config);
        this.name = name;
        try {
            resolverClass = (Class<? extends IRelationResolver>) Class.forName(className);
            // test if we can instantiate the resolver class:
            //noinspection GroovyUnusedAssignment
            IRelationResolver resolverClassTest = resolverClass.newInstance();
        } catch (InstantiationException e) {
            throw new CinnamonException("error.instantiating.class", className);
        } catch (IllegalAccessException e) {
            throw new CinnamonException("error.accessing.class", className);
        } catch (ClassNotFoundException e) {
            throw new CinnamonException("error.loading.class", className);
        }
    }

    void setConfig(String config) {
        if (config == null || config.trim().length() == 0) {
            this.config = "<config />";
        }
        else {
            ParamParser.parseXmlToDocument(config, "error.param.config");
            this.config = config;
        }
    }

    void toXmlElement(Element root) {
        Element relation = root.addElement("relation");
        relation.addElement("id").addText(String.valueOf(getId()));
        relation.addElement("configuration").addText(config);
        relation.addElement("resolverClass").addText(resolverClass.getName());
        relation.addElement("name").addText(name);
    }

    ObjectSystemData resolveOsdVersion(Relation relation, ObjectSystemData changedOsd, RelationSide relationSide) {
        try {
            IRelationResolver resolver = resolverClass.newInstance();
            return resolver.resolveVersion(relation, changedOsd, config, relationSide);
        } catch (IllegalAccessException e) {
            throw new CinnamonException("error.instantiating.class", e, resolverClass.getName());
        } catch (InstantiationException e) {
            throw new CinnamonException("error.instantiating.class", e, resolverClass.getName());
        }
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof RelationResolver)) return false

        RelationResolver that = (RelationResolver) o

        if (config != that.config) return false
        if (name != that.name) return false
        if (resolverClass != that.resolverClass) return false

        return true
    }

    int hashCode() {
        int result
        result = (name != null ? name.hashCode() : 0)
        result = 31 * result + (config != null ? config.hashCode() : 0)
        result = 31 * result + (resolverClass != null ? resolverClass.hashCode() : 0)
        return result
    }
}
