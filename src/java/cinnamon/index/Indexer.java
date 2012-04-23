package cinnamon.index;

import org.apache.lucene.document.Document;
import temp.index.ContentContainer;

public interface Indexer {

	void indexObject(ContentContainer xml, Document doc, String fieldname, String searchString, Boolean multipleResults);
	
}
