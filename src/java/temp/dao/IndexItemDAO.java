package temp.dao;

import server.index.IndexItem;

public interface IndexItemDAO extends GenericDAO<IndexItem, Long> {
	
	IndexItem findByName(String name); 
}