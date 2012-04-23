package cinnamon.index;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cinnamon.interfaces.XmlConvertable;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SearchResult {

    Logger log = LoggerFactory.getLogger(this.getClass());

    Float maxScore;
    Integer totalResults;
    Integer startingResults;
    Integer pageSize;

    Map<Document, Float> docScoreMap = new HashMap<Document, Float>();
    Map<XmlConvertable, Float> xmlScoreMap = new HashMap<XmlConvertable, Float>();

    public SearchResult(TopDocs hits, IndexSearcher searcher, Integer startingResult, Integer pageSize) throws IOException {
        this.startingResults = startingResult;
        this.pageSize = pageSize;
        maxScore = hits.getMaxScore();
        totalResults = hits.totalHits;
        ScoreDoc[] scoreDocs = hits.scoreDocs;
        for (int x = startingResult; (x < totalResults) && (x < scoreDocs.length); x++) {
            // The searcher will be closed before result filtering, so it's a good idea to extract the documents now.
            Document doc = searcher.doc(scoreDocs[x].doc);
            Float score = scoreDocs[x].score;
            addDocument(doc, score);
        }
    }

    public Integer getRelevantResultsCount() {
        return xmlScoreMap.size();
    }

    public void addDocument(Document document, Float score) {
        Float s = docScoreMap.put(document, score);
        if(s != null){
            log.debug("docScoreMap already contained document "+document.toString());
        }
    }

    public Float getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Float maxScore) {
        this.maxScore = maxScore;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Map<Document, Float> getDocScoreMap() {
        return docScoreMap;
    }

    public void setDocScoreMap(Map<Document, Float> docScoreMap) {
        this.docScoreMap = docScoreMap;
    }

    public SearchResult filterDocuments(ResultValidator validator) {
        Set<Document> filteredDocs = new HashSet<Document>();
        for (Map.Entry<Document, Float> entry : docScoreMap.entrySet()) {
            Document doc = entry.getKey();
            Float score = entry.getValue();
            XmlConvertable convertable = validator.validateAccessPermissions(doc, null);
            if (convertable != null) {
                xmlScoreMap.put(convertable, score);
                filteredDocs.add(doc);
                if (xmlScoreMap.size() >= pageSize) {
                    // we need only $pageSize elements to display.
                    log.debug("reached pageSize.");
                    break;
                }
            }
            else{
                log.debug("Could not validate access to "+doc.toString());
            }
        }
        for (Document doc : filteredDocs) {
            docScoreMap.remove(doc);
        }
        return this;
    }

    public org.dom4j.Document getSearchResultsAsXML() {
        org.dom4j.Document resultDoc = DocumentHelper.createDocument();
        Element root = resultDoc.addElement("searchResults");
        root.addAttribute("total-results", String.valueOf(totalResults));
        root.addAttribute("relevant-results", String.valueOf(getRelevantResultsCount()));

        for (Map.Entry<XmlConvertable, Float> entry : xmlScoreMap.entrySet()) {
            XmlConvertable xml = entry.getKey();
            Float score = entry.getValue();
            Element item = root.addElement("item");
            item.addElement("score").setText(String.valueOf(score));// TODO: turn score into score/maxScore %
            xml.toXmlElement(item);
        }
        return resultDoc;
    }
}
