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
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Index the names of XML elements
 */
public class ElementNameIndexer implements Indexer {

    public static final  String  DOCTYPE_ENTITY            = "(<!(?:DOCTYPE|ENTITY)[^>]*>)";
    private static final Pattern DOCTYPE_OR_ENTITY_PATTERN = Pattern.compile(DOCTYPE_ENTITY);

    protected FieldType fieldType;
    boolean stored = true;

    public ElementNameIndexer() {
        fieldType = new FieldType();
        fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
        fieldType.setStored(false);
        fieldType.setTokenized(false);
    }

    static Logger log = LoggerFactory.getLogger(ElementNameIndexer.class);

    @SuppressWarnings("unchecked")
    @Override
    public void indexObject(ContentContainer data, Document doc, String fieldname,
                            String searchString, Boolean multipleResults) {
        try {
            SAXParserFactory factory    = SAXParserFactory.newInstance();
            SAXParser        saxParser  = factory.newSAXParser();
            LexHandler       lexHandler = new LexHandler();
            saxParser.setProperty("http://xml.org/sax/properties/lexical-handler", lexHandler);
            ElementNameHandler nameHandler    = new ElementNameHandler();
            String             withoutDoctype = DOCTYPE_OR_ENTITY_PATTERN.matcher(data.asString()).replaceAll("");
            saxParser.parse(new ByteArrayInputStream(withoutDoctype.getBytes(StandardCharsets.UTF_8)), nameHandler);
            Set<String> elementNames = nameHandler.getNames();
            elementNames.forEach(name -> {
                log.debug("fieldname: " + fieldname + " value: " + name + " stored:" + fieldType.stored());
                doc.add(new Field(fieldname, name, fieldType));
            });
            List<String> comments = lexHandler.getComments();
            comments.forEach(comment -> {
                log.debug("fieldname: " + "xml.comment" + " value: " + comment + " stored:" + fieldType.stored());
                doc.add(new Field("xml.comment", comment, fieldType));
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

    static class LexHandler implements LexicalHandler {
        static Logger log = LoggerFactory.getLogger(LexHandler.class);

        List<String> comments = new ArrayList<>();

        @Override
        public void startDTD(String name, String publicId, String systemId) throws SAXException {

        }

        @Override
        public void endDTD() throws SAXException {

        }

        @Override
        public void startEntity(String name) throws SAXException {

        }

        @Override
        public void endEntity(String name) throws SAXException {

        }

        @Override
        public void startCDATA() throws SAXException {

        }

        @Override
        public void endCDATA() throws SAXException {

        }

        @Override
        public void comment(char[] ch, int start, int length) throws SAXException {
            log.debug("comment: " + new String(ch, start, length));
            comments.add(new String(ch,start,length).trim());
        }

        public List<String> getComments() {
            return comments;
        }
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
