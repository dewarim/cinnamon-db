package cinnamon.index;

import cinnamon.index.Indexable;
import cinnamon.index.ResultValidator;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Scorer;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cinnamon.ObjectSystemData;
import cinnamon.interfaces.XmlConvertable;
import cinnamon.index.Indexable;
import cinnamon.index.ResultValidator;

import java.io.IOException;
import java.util.*;

public class ResultCollector extends Collector{

    transient Logger log = LoggerFactory.getLogger(this.getClass());

    LinkedHashSet<Integer> hits = new LinkedHashSet<Integer>();
    Set<Document> documents = new HashSet<Document>();
    IndexSearcher searcher;
    IndexReader reader;
    Integer docBase;


    public ResultCollector(IndexSearcher searcher){
        this.searcher = searcher;
    }

    public ResultCollector(){

    }

    @Override
    public void collect(int doc) {
//		log.debug("collect document: "+doc);
        hits.add(doc);
        try{
            Document d = searcher.doc(doc+docBase);
//			log.debug("about to add Document: "+d);
            documents.add(d);
        }
        catch (IOException e) {
            log.warn("ResultCollector.collect encountered an IOException:",e);
        }
    }

    public Collection<Document> getDocuments(){
        return documents;
    }

    public void setSearcher(IndexSearcher searcher){
        this.searcher = searcher;
    }

    /**
     * The ResultCollector stores all documents the Lucene search has returned.
     * You can retrieve them as an XML document by calling getSearchResultsAsXML.
     * This document only contains the minimum information required to retrieve
     * the objects found from the Cinnamon server. The client has to decide which
     * documents it really wants / needs to retrieve.
     * @return the search results as a dom4j-XML-Document.
     */
    public org.dom4j.Document getSearchResultsAsXML(){
        org.dom4j.Document resultDoc = DocumentHelper.createDocument();
        Element root = resultDoc.addElement("searchResults");
        root.addAttribute("total-results", String.valueOf(getDocuments().size()));

        for(Document doc : getDocuments()){
            Element item = root.addElement("item");
            item.addElement("javaClass").setText(doc.get("javaClass"));
            item.addElement("hibernateId").setText(doc.get("hibernateId"));
        }
        return resultDoc;
    }

    /**
     * For internal searches in Cinnamon-server, this retrieves and returns the
     * OSDs referenced by the search result, instead of building a result XML document.
     * @return a collection of OSDs found by the search.
     */
    public Collection<ObjectSystemData> getSearchResultsAsOSDs(){
        List<ObjectSystemData> osds = new ArrayList<ObjectSystemData>();
        for(Document doc : getDocuments()){
            if(doc.get("javaClass").equals("server.data.ObjectSystemData")){
                ObjectSystemData osd = ObjectSystemData.get(doc.get("hibernateId"));
                osds.add(osd);
            }
            else{
                log.debug("search result element is not an osd but: "+doc.get("javaClass"));
            }
        }
        return osds;
    }

    /**
     * Filter all results with a ResultValidator object. This is needed for example
     * for ACL based permission checking in Cinnamon.<br>
     * Side effect: store filtered docs in field documents.
     * @param val the ResultValidator which is used for validation / filtering.
     * @param filterClass a class which implements the Indexable interface and will be used to filter all
     * results whose class is not of filterClass. For example, if you set this parameter to server.Folder,
     * all server.data.OSD-results will be omitted from the final result.
     * @return Set of XmlConvertable objects.
     */
    public Set<XmlConvertable> filterResults(ResultValidator val, Class<? extends Indexable> filterClass){
        Set<Document> filteredDocs = new HashSet<Document>();
        Set<XmlConvertable> resultStore = new HashSet<XmlConvertable>();
        for(Document doc : documents){
            XmlConvertable convertable = val.validateAccessPermissions(doc, filterClass);
            if(convertable != null){
                resultStore.add( convertable );
                filteredDocs.add(doc);
            }
        }
        documents = filteredDocs;
        return resultStore;
    }



    @Override
    public boolean acceptsDocsOutOfOrder() {
        return true;
    }

    @Override
    public void setNextReader(IndexReader reader, int docBase)
    throws IOException {
        this.reader = reader; // ignore findBugs.warning: this field is included for possible future use.
        this.docBase = docBase;
    }

    @Override
    public void setScorer(Scorer scorer) throws IOException {
        // ignore scorer
    }

}
