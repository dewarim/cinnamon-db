package cinnamon

import cinnamon.global.Constants
import org.dom4j.Element
import cinnamon.utils.ParamParser
import org.dom4j.DocumentHelper

class Metaset {

    static constraints = {
        content size: 1..Constants.METADATA_SIZE
    }

    static mapping = {
        table('metasets')
        version 'obj_version'
    }
    
    static hasMany = [osdMetasets:OsdMetaset, folderMetasets:FolderMetaset]

    String content
    MetasetType type
    
    Set<OsdMetaset> osdMetasets = []
    Set<FolderMetaset> folderMetasets = []
    
    public Metaset() {

    }

    /**
     * Create a new Metaset with the given content and type. This constructor does not
     * validate the parameters. A proper content doc should look like this:
     * <pre>
     * @code{
     *  <metaset id='id of metaset' type='id of metaset type'>
     *      ...data...
     *  </metaset>
     * }
     * <pre>
     * @param content the XML content
     * @param type the type of this metaset
     */
    public Metaset(String content, MetasetType type) {
        this.type = type
        setContent(content)
    }

    /**
     * Create a new Metaset with the parameters of the map. This constructor will
     * check if the XML content is well-formed and add the id and type to the XML node, if possible.  
     * @param cmd a Map containing the String parameters 'type' with the id of the metaset type and 'content'
     */
    public Metaset(Map<String, String> cmd) {
        type = MetasetType.findByName(cmd.get("type"));
        setContent(cmd.get("content"));
    }

    public void setContent(String content) {
        if(content == null){
            content = '<metaset />'
        }
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

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Metaset)) return false

        Metaset metaset = (Metaset) o

        if (content != metaset.content) return false
        if (type != metaset.type) return false

        return true
    }

    int hashCode() {
        int result
        result = (content != null ? content.hashCode() : 0)
        result = 31 * result + (type != null ? type.hashCode() : 0)
        return result
    }
}
