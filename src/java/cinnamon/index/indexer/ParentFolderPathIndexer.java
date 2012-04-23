package cinnamon.index.indexer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Folder;
import server.dao.DAOFactory;
import server.dao.FolderDAO;
import server.index.ContentContainer;
import utils.HibernateSession;
import utils.ParamParser;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Index the parent folder path of an object or folder.
 *
 */
public class ParentFolderPathIndexer extends DefaultIndexer{

	transient Logger log = LoggerFactory.getLogger(this.getClass());
	static DAOFactory daoFactory = DAOFactory.instance(DAOFactory.HIBERNATE);
	public ParentFolderPathIndexer() {
		index = Index.NOT_ANALYZED;
		store = Store.NO;
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
				EntityManager em = HibernateSession.getLocalEntityManager();								
				FolderDAO folderDao = daoFactory.getFolderDAO(em);
				Folder folder = folderDao.get(nodeValue);
                String path = folder.fetchPath();
				log.debug("fieldname: "+fieldname+" value: "+ path);
				doc.add(new Field(fieldname, path.toLowerCase(), store, index));
			}
			else{
				log.debug("nodeValue == null");
			}
		} 	
	}
		
}
