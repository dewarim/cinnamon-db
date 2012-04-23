package cinnamon.index

import cinnamon.global.Constants
import org.dom4j.Element
import org.apache.lucene.document.Document
import cinnamon.exceptions.CinnamonException
import cinnamon.i18n.LocalMessage

class IndexType {
    public enum DataType {
        STRING, BOOLEAN, DATE_TIME, INTEGER, DECIMAL, TIME, TEXT
    }

    static constraints = {
        name unique:true, size: 1..Constants.NAME_LENGTH
    }

    String name
    Class<? extends Indexer> indexerClass
    Class<? extends ValueAssistanceProvider> vaProviderClass
    DataType dataType

    public IndexType() {

    }

    public IndexType(Map<String, String> fields) {
        try {
            name = fields.get("name");
            indexerClass = (Class<Indexer>) Class.forName(fields.get("indexer_class"));
            vaProviderClass = (Class<ValueAssistanceProvider>) Class.forName(fields.get("va_provider_class"));
            dataType = DataType.valueOf(fields.get("data_type"));
        } catch (ClassNotFoundException e) {
            throw new CinnamonException("error.class.not.found", e.getMessage());
        }
    }

    public IndexType(String name, Class<? extends Indexer> indexerClass,
                     Class<? extends ValueAssistanceProvider> vaProviderClass,
                     DataType dataType) {
        this.name = name;
        this.indexerClass = indexerClass;
        this.dataType = dataType;
        this.vaProviderClass = vaProviderClass;
    }

    public void toXmlElement(Element root) {
        Element type = root.addElement("indexType");
        type.addElement("id").addText(String.valueOf(id));
        type.addElement("name").addText(LocalMessage.loc(name));
        type.addElement("indexerClass").addText(indexerClass.getName());
        type.addElement("dataType").addText(dataType.toString());
        type.addElement("vaProviderClass").addText(vaProviderClass.getName());
    }

    public Indexer getIndexer() {
        Indexer indexer;
        try {
            indexer = indexerClass.newInstance();
        }
        catch (InstantiationException e) {
            throw new CinnamonException("error.instantiating.class", e, indexerClass.getName());
        } catch (IllegalAccessException e) {
            throw new CinnamonException("error.accessing.class", e, indexerClass.getName());
        }
        return indexer;
    }


    public void indexContent(ContentContainer content, Document doc, String fieldname, String searchString, Boolean multipleResults) {
        getIndexer().indexObject(content, doc, fieldname, searchString, multipleResults);
    }

    public void indexSysMeta(ContentContainer sysMeta, Document doc, String fieldname, String searchString, Boolean multipleResults) {
        getIndexer().indexObject(sysMeta, doc, fieldname, searchString, multipleResults);
    }

    public void indexMetadata(ContentContainer metadata, Document doc, String fieldname, String searchString, Boolean multipleResults) {
        getIndexer().indexObject(metadata, doc, fieldname, searchString, multipleResults);
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof IndexType)) return false

        IndexType indexType = (IndexType) o

        if (dataType != indexType.dataType) return false
        if (indexerClass != indexType.indexerClass) return false
        if (name != indexType.name) return false
        if (vaProviderClass != indexType.vaProviderClass) return false

        return true
    }

    int hashCode() {
        int result
        result = (name != null ? name.hashCode() : 0)
        result = 31 * result + (indexerClass != null ? indexerClass.hashCode() : 0)
        result = 31 * result + (vaProviderClass != null ? vaProviderClass.hashCode() : 0)
        result = 31 * result + (dataType != null ? dataType.hashCode() : 0)
        return result
    }
}
