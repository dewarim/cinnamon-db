package temp.dao;

import server.AclEntry;

public interface AclEntryDAO extends GenericDAO<AclEntry, Long> {
	AclEntry get(Long id);
}