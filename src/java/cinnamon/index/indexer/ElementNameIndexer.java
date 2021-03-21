package cinnamon.index.indexer;

import cinnamon.exceptions.CinnamonException;
import cinnamon.index.ContentContainer;
import cinnamon.index.Indexer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Index the names of XML elements
 */
public class ElementNameIndexer implements Indexer {

    public static final String DOCTYPE_ENTITY = "(<!(?:DOCTYPE|ENTITY)[^>]*>)";
    private static final Pattern DOCTYPE_OR_ENTITY_PATTERN = Pattern.compile(DOCTYPE_ENTITY);

    protected FieldType fieldType;
    boolean stored = true;

    public ElementNameIndexer() {
        fieldType = new FieldType();
        fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
        fieldType.setStored(false);
        fieldType.setTokenized(false);
    }

    transient Logger log = LoggerFactory.getLogger(getClass());

    @SuppressWarnings("unchecked")
    @Override
    public void indexObject(ContentContainer data, Document doc, String fieldname,
                            String searchString, Boolean multipleResults) {
        try {
            SAXParserFactory   factory        = SAXParserFactory.newInstance();
            SAXParser          saxParser      = factory.newSAXParser();
            ElementNameHandler nameHandler    = new ElementNameHandler();
            String             withoutDoctype = DOCTYPE_OR_ENTITY_PATTERN.matcher(data.asString()).replaceAll("");
            saxParser.parse(new ByteArrayInputStream(withoutDoctype.getBytes(StandardCharsets.UTF_8)), nameHandler);
            Set<String> elementNames = nameHandler.getNames();
            elementNames.forEach(name -> {
                log.debug("fieldname: " + fieldname + " value: " + name + " stored:" + fieldType.stored());
                doc.add(new Field(fieldname, name, fieldType));
            });
        } catch (Exception e) {
            throw new CinnamonException("Could not parse document.", e);
        }
    }

    public void setStored(boolean stored) {
        this.stored = stored;
        fieldType.setStored(stored);
    }

    public boolean isStored() {
        return stored;
    }

    static class ElementNameHandler extends DefaultHandler {
        Set<String> names = new HashSet<>();

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            names.add(qName);
            for (int x = 0; x < attributes.getLength(); x++) {
                String attributeQname = attributes.getQName(x);
                names.add(String.format("%s/@%s", qName, attributeQname));
                names.add(String.format("%s/@%s=\"%s\"", qName, attributeQname, attributes.getValue(x)));
            }

            super.startElement(uri, localName, qName, attributes);
        }

        public Set<String> getNames() {
            return names;
        }
    }

}
