package temp.dao;

import server.index.IndexType;

public interface IndexTypeDAO extends GenericDAO<IndexType, Long> {
	
	IndexType findByName(String name);

    IndexType get(String id);
	
}