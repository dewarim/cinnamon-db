package cinnamon

import cinnamon.global.Constants
import org.dom4j.Node
import cinnamon.utils.ParamParser
import org.dom4j.Element
import org.dom4j.DocumentHelper
import cinnamon.i18n.LocalMessage

class ConfigEntry  implements Serializable {

    static constraints = {
        name size: 1..Constants.NAME_LENGTH, unique: true
        config size: 1..Constants.METADATA_SIZE
    }

    static mapping = {
        cache true
        table('config_entries')
        version 'obj_version'
    }

    String name
    String config = "<config />"

    public Node parseConfig(){
        try{
            return ParamParser.parseXml(config, null);
        }
        catch (Exception e){
            log.debug("Failed to parse config: ",e);
            return null;
        }
    }

    public void setConfig(String config) {
        if(config == null || config.trim().length() == 0){
            this.config = "<config />";
        }
        else{
            ParamParser.parseXmlToDocument(config, "error.param.config");
            this.config = config;
        }
    }

    /**
     * Add the ConfigEntry's fields as child-elements to a new element with the given name.
     * If the entry parameter is null, simply return an empty element.
     * @param elementName the name of the XML element into which the data is stored.
     * @param entry the ConfigEntry to be converted to an Element
     * @return the new element.
     */
    public static Element asElement(String elementName, ConfigEntry entry){
        Element e = DocumentHelper.createElement(elementName);
        if(entry != null){
            e.addElement("id").addText(String.valueOf(entry.getId()));
            e.addElement("name").addText( LocalMessage.loc(entry.getName()));
            e.addElement("config").addText(entry.getConfig());
        }
        return e;
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof ConfigEntry)) return false

        ConfigEntry that = (ConfigEntry) o

        if (config != that.config) return false
        if (name != that.name) return false

        return true
    }

    int hashCode() {
        int result
        result = (name != null ? name.hashCode() : 0)
        result = 31 * result + (config != null ? config.hashCode() : 0)
        return result
    }
}
