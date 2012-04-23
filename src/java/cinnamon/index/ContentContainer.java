package cinnamon.index;

import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cinnamon.exceptions.CinnamonException;
import cinnamon.utils.ParamParser;

/**
 * A content container class which will load the content when needed - unless it was either supplied
 * at instantiation.
 *
 */
public class ContentContainer {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private Boolean contentLoaded = false;
    private byte[] content = new byte[0];
    private String contentAsString;
    private Document contentAsDoc;
    private Indexable indexable;
    private String repository;

    /**
     * Instantiate a new ContentContainer object and set the content with a byte[] array.
     * @param content a byte array which holds the content
     */
    public ContentContainer(byte[] content) {
        this.content = content;
        contentLoaded = true;
    }

    /**
     * Instantiate a new ContentContainer object which loads the data dynamically from the specified
     * object and repository.
     * @param indexable An object which implements the Indexable interface and which supplies its content upon request.
     * @param repository the name of the repository where the Indexable is located. This is required because the actual
     * path to the content file in the file system is dependent on the repository - and a normal OSD object by itself
     * does not really know to which repository it belongs.<br>
     * (path to content is: $data_root + $repositoryName + $osd.contentPath)
     */
    public ContentContainer(Indexable indexable, String repository){
        this.indexable = indexable;
        this.repository = repository;
    }

    /**
     * Parse the content as an XML-document. Will always return a document. In case of invalid content, it
     * returns "&lt;empty /&gt;" as a document.
     * @return a dom4j Document which is either a representation of the content as XML, or an empty document which
     * contains only an "empty" element.
     */
    public Document asDocument(){
        if(contentAsDoc == null){
            try{
                contentAsDoc = ParamParser.parseXmlToDocument(asString());
                        // "error.parse.indexable_object"); // error message is not needed as the exception is not
                        // propagated to the end user.
            }
            catch (Exception e){
                log.debug("Failed to parse content. Will create <empty/> content.");
                contentAsDoc = ParamParser.parseXmlToDocument("<empty />");
            }
        }
        return contentAsDoc;
    }

    /**
     * Return the content as a String. This method always returns a String, which may be empty in case
     * the content is not defined.
     * @throws CinnamonException is thrown if the content cannot be loaded.
     * @return the content bytes converted to a String. Note: this has not been tested with conflicting locales,
     * so if you upload a string data in binary form in a different encoding than the server system locale, this method
     * might not prove 100% reliable.
     */
    public String asString() throws CinnamonException{
        if(contentAsString == null){
            contentAsString = new String(asBytes());
        }
        return contentAsString;
    }

    public byte[] asBytes(){
        if(contentLoaded){
            return content;
        }
        else{
            content = indexable.getContentAsBytes(repository);
            contentLoaded = true;
            return content;
        }
    }
}
