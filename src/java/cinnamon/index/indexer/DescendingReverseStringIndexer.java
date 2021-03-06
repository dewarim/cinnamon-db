package cinnamon.index.indexer;

import org.dom4j.Node;

/**
 * <p>The ReverseStringIndexer expects an XPath parameter as searchString and will stored
 * the results of this search in the Lucene document.</p>
 * <p>Example: name="index.name", searchString="//name" will find all name-elements.
 * and stored the <i>analyzed</i> results of node.getText()()</p>
 * <p>Note: this may create strange Unicode results.</p>
 */
public class DescendingReverseStringIndexer extends ReverseStringIndexer{
	
	public String convertNodeToString(Node node){
		StringBuilder builder = descendIntoNodes(node);		
		return builder.reverse().toString().trim();		
	}
}
