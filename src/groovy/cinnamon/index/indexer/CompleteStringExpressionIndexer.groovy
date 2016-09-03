package cinnamon.index.indexer

import cinnamon.index.ContentContainer
import cinnamon.index.Indexer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.FieldType
import org.apache.lucene.index.IndexOptions
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Index the result string from an XPath expression.
 * This class ignores the multipleResults parameter for its IndexItems, because it expects
 * to receive a searchString which returns a single string value. 
 */
class CompleteStringExpressionIndexer implements Indexer {

    transient Logger log = LoggerFactory.getLogger(this.getClass());


    protected FieldType fieldType;
    boolean stored = false;

    public CompleteStringExpressionIndexer() {
        fieldType = new FieldType();
        fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
        fieldType.setStored(true);
        fieldType.setTokenized(false);
    }
    

    @SuppressWarnings("unchecked")
    @Override
    public void indexObject(ContentContainer data, Document doc, String fieldName,
                            String searchString, Boolean multipleResults) {

        org.dom4j.Document indexObject = data.asDocument();
        String expressionValue = indexObject.valueOf(searchString);
        if (expressionValue != null) {
            log.debug("fieldname: " + fieldName + " value: " + expressionValue);
            doc.add(new Field(fieldName, expressionValue, fieldType));
        }
    }

    Field.Store getStore() {
        return store
    }

    void setStore(Field.Store store) {
        this.store = store
    }
}
