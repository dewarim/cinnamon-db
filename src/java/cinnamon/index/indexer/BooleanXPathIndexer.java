package cinnamon.index.indexer;

import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>The BooleanXPathIndexer expects an XPath parameter as searchString and will stored
 * the results of this search in the Lucene document. Currently, Boolean fields need to consist
 * of a string "true" or "false".</p>
 */
public class BooleanXPathIndexer extends DefaultIndexer {

	public String convertNodeToString(Node node){
		return node.getText().trim().toLowerCase();
	}

}
