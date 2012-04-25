package cinnamon

import cinnamon.global.Constants
import org.dom4j.Element
import cinnamon.utils.ParamParser
import org.dom4j.DocumentHelper

class Metaset {

    static constraints = {
        content size: 1..Constants.METADATA_SIZE
    }

    static hasMany = [osdMetasets:OsdMetaset, folderMetasets:FolderMetaset]

    String content
    MetasetType type

    public Metaset() {

    }

    public Metaset(String content, MetasetType type) {
        this.type = type;
        this.content = content;
    }

    public Metaset(Map<String, String> cmd) {
        type = MetasetType.findByName(cmd.get("type"));
        setContent(cmd.get("content"));
    }

    public void setContent(String content) {
        // fix id and type, in case the content is simply copied from some other metaset.
        Element c = (Element) ParamParser.parseXml(content, null);
        if( !c.hasContent() ){
            c.addAttribute("status","empty");
        }
        c.addAttribute("id", String.valueOf(id));
        if(type != null){
            c.addAttribute("type", type.getName());
        }
        this.content = c.asXML();
    }

    /**
     * Add the Metaset's fields as child-elements to a new element with the given name.
     * If the metaset is null, simply return an empty element.
     *
     * @param elementName name of the element
     * @param metaset the metaset which will be serialized
     * @return the new element.
     */
    public static Element asElement(String elementName, Metaset metaset) {
        if(metaset == null){
            return DocumentHelper.createElement(elementName);

        }
        Element content = (Element) ParamParser.parseXml(metaset.getContent(), null);
        content.addAttribute("id", String.valueOf(metaset.getId()));
        content.addAttribute("type", metaset.getType().getName());
        return content;
    }
}
