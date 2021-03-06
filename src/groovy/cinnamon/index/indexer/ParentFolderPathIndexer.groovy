package cinnamon.index.indexer

import cinnamon.Folder
import cinnamon.index.ContentContainer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.dom4j.Node
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Index the parent folder path of an object or folder.
 *
 */
public class ParentFolderPathIndexer extends DefaultIndexer{

	transient Logger log = LoggerFactory.getLogger(this.getClass());
	public ParentFolderPathIndexer() {
		fieldType.setTokenized(false)
		fieldType.setStored(false)
	}
	
	@SuppressWarnings("unchecked")
	public void indexObject(ContentContainer data, Document doc, String fieldname,
			String searchString, Boolean multipleResults) {

		org.dom4j.Document indexObject = data.asDocument();
		List<Node> hits = new ArrayList<Node>();
		
		if(multipleResults){
			hits = indexObject.selectNodes(searchString);
		}
		else{
			Node node = indexObject.selectSingleNode(searchString);
			if(node != null){
				hits.add(node);
			}
		}
		
		for(Node node : hits){
			String nodeValue = convertNodeToString(node);
			if(nodeValue != null){
				// fieldValue should be: osd.parent or folder.parent
				Folder folder = Folder.get(nodeValue);
                String path = folder.fetchPath();
				path = path.replaceFirst("/root","");
				log.debug("fieldname: "+fieldname+" value: "+ path);
				doc.add(new Field(fieldname, path.toLowerCase(), fieldType));
			}
			else{
				log.debug("nodeValue == null");
			}
		} 	
	}
		
}
