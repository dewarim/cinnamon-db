package temp.dao;

import server.index.IndexGroup;

public interface IndexGroupDAO extends GenericDAO<IndexGroup, Long> {
	
	IndexGroup findByName(String name);

    IndexGroup get(String id);
}