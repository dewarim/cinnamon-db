package temp.dao;

import server.RelationType;

public interface RelationTypeDAO extends GenericDAO<RelationType, Long> {

    /**
     * @param name String containing the name of the wanted relation type
     * @return the requested relation type or null if it was not found.
     */
	RelationType findByName(String name);
	
	void delete(Long relationTypeID);
	
	RelationType get(String id);
}
