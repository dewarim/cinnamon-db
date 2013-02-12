package cinnamon.index

import cinnamon.global.Constants
import org.apache.lucene.document.Field
import org.dom4j.Element
import org.apache.lucene.document.Document
import cinnamon.i18n.LocalMessage
import cinnamon.utils.ParamParser

class IndexItem implements Serializable  {

    static constraints = {
        name unique:true, size: 1..Constants.NAME_LENGTH
        searchString blank: true, size:0..Constants.XPATH_LENGTH
        searchCondition size:1..Constants.XPATH_LENGTH
        fieldname size: 1..255
        vaProviderParams(size: 1..Constants.XML_PARAMS)
    }

    static mapping = {
        cache true
        version 'obj_version'
        table 'index_items'
        vaProviderParams column: 'va_params'
    }
    
    static belongsTo = [indexType:IndexType, indexGroup:IndexGroup]

    String name
    String searchString = ''

    /**
     * searchCondition is an XPath expression that evaluates to true
     * if the IndexItem should try to index the given document.<br/>
     * The XPath will be applied successively to the metadata, system
     * metadata and content (depending on forContent, forMetadata and
     * forSysMetadata)
     * and if any of those returns true, the item
     * will be indexed by using the search_string.
     */
    String searchCondition = 'true()'
    Boolean systemic = false
    String fieldname = 'content'
    Boolean forContent = false
    Boolean forMetadata = false
    Boolean forSysMeta = false
    Boolean storeField = false

    /**
     * If an indexItem can generate multiple distinct results, you should set this
     * flag to true. For example, an XML structure containing a list of "filename"
     * elements which is queries by an xpath statement of //filename will generate a
     * list of nodes, which should all be indexed. On the other hand, a UUID search
     * field will only contain 1 result.
     */
    Boolean multipleResults = false

    /**
     * Each IndexType is connected to a specific ValueAssistanceProvider, which is called
     * upon to provide a list of possible values for a search field on the client.<br/>
     * For example, if you index a field "birthday", the acceptable values represent
     * to a certain date in the past - the client should not submit search requests for
     * values like "birthday=John" but rather tell the user to correct the input.
     */
    String vaProviderParams = "<vaParams />"

    public IndexItem(){

    }

    public IndexItem(Map<String, String> fields){
        name = fields.get("name");
        searchString = fields.get("search_string");
        searchCondition = fields.get("search_condition");
        fieldname = fields.get("fieldname");
        vaProviderParams = fields.get("va_provider_params");
        indexGroup = IndexGroup.get(Long.parseLong(fields.get("index_group_id")));
        indexType = IndexType.get(Long.parseLong(fields.get("index_type_id")));
        multipleResults = fields.get("multiple_results").equals("true");
        systemic = fields.get("systemic").equals("true");
        forContent = fields.get("for_content").equals("true");
        forMetadata = fields.get("for_metadata").equals("true");
        forSysMeta = fields.get("for_sys_meta").equals("true");
        storeField = fields.get('store_field').equals('true')
    }

    public IndexItem(String name, String xpath, String searchCondition, String fieldname, IndexType indexType,
                     Boolean multipleResults, String vaParams, Boolean systemic, IndexGroup indexGroup,
                     Boolean forContent, Boolean forMetadata, Boolean forSysMeta, Boolean storeField){
        this.name = name;
        this.searchString = xpath;
        this.searchCondition = searchCondition;
        this.fieldname = fieldname;
        this.indexType = indexType;
        this.multipleResults = multipleResults;
        this.vaProviderParams = vaParams;
        this.systemic = systemic;
        this.indexGroup = indexGroup;
        this.forContent = forContent;
        this.forMetadata = forMetadata;
        this.forSysMeta = forSysMeta
        this.storeField = storeField
    }
    public void toXmlElement(Element root){
        Element item = root.addElement("indexItem");
        item.addElement("id").addText(String.valueOf(id));
        item.addElement("name").addText(name);
        item.addElement("searchString").addText(searchString);
        item.addElement("fieldname").addText(fieldname);
        item.addElement("fieldDisplayName").addText(LocalMessage.loc(fieldname));
        indexType.toXmlElement(item);
        item.addElement("multipleResults").addText(multipleResults.toString());
        item.add(ParamParser.parseXml(vaProviderParams, "error.param.vaParams"));
        item.addElement("systemic").addText(systemic.toString());
        item.addElement("forMetadata").addText(forMetadata.toString());
        item.addElement("forContent").addText(forContent.toString());
        item.addElement("forSysMeta").addText(forSysMeta.toString());
        item.addElement("storeField").addText(storeField.toString());
        indexGroup.toXmlElement(item);
    }

    public void indexObject(ContentContainer content, ContentContainer metadata, ContentContainer systemMetadata, Document doc){
        ContentContainer[] params = [content, metadata, systemMetadata];
        if(! checkCondition(params)){
            log.debug("checkCondition returned false");
            return;
        }
        log.debug("checkCondition returned true");
        log.debug("searchString: "+searchString);
        log.debug("fieldname: "+fieldname);
        if(forContent){
            indexType.indexContent(content, doc, fieldname, searchString, multipleResults);
        }
        if(forSysMeta){
//            log.debug("sysMeta:\n "+systemMetadata.asString());
            try{
            indexType.indexSysMeta(systemMetadata, doc, fieldname, searchString, multipleResults);
            }
            catch (Exception e){
                log.debug("failed to index sysmeta:",e)
            }
        }
        if(forMetadata){
            indexType.indexMetadata(metadata, doc, fieldname, searchString, multipleResults);
        }
    }

    /**
     * Check if one of the given parameter Strings has a positive result for the
     * xpath expression in searchCondition.
     * @param params an array of strings which contain XML documents.
     * @return true if one of the strings resulted in a positive match for searchCondition.
     */
    public Boolean checkCondition(ContentContainer[] params){
        Boolean result = false;
        for(ContentContainer xml : params){
            try{
                // TODO: possibly define size limits.
                org.dom4j.Document indexObject = xml.asDocument();
                log.debug("checkCondition "+searchCondition+": "+indexObject.valueOf(searchCondition)) ;
                if(indexObject.valueOf(searchCondition).equals("true")){
                    result = true;
                    break;
                }
            }
            catch (Exception e) {
                log.debug("checkCondition: "+e.message);
            }
        }
        return result;
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof IndexItem)) return false

        IndexItem indexItem = (IndexItem) o

        if (fieldname != indexItem.fieldname) return false
        if (forContent != indexItem.forContent) return false
        if (forMetadata != indexItem.forMetadata) return false
        if (forSysMeta != indexItem.forSysMeta) return false
        if (indexGroup != indexItem.indexGroup) return false
        if (indexType != indexItem.indexType) return false
        if (multipleResults != indexItem.multipleResults) return false
        if (name != indexItem.name) return false
        if (searchCondition != indexItem.searchCondition) return false
        if (searchString != indexItem.searchString) return false
        if (storeField != indexItem.storeField) return false
        if (systemic != indexItem.systemic) return false
        if (vaProviderParams != indexItem.vaProviderParams) return false

        return true
    }

    int hashCode() {
        int result
        result = (name != null ? name.hashCode() : 0)
        result = 31 * result + (fieldname != null ? fieldname.hashCode() : 0)
        return result
    }
}
