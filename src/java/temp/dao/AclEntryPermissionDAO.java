package temp.dao;

import server.AclEntryPermission;

public interface AclEntryPermissionDAO extends GenericDAO<AclEntryPermission, Long> {
	AclEntryPermission get(Long id);
	void delete(Long id);
	void delete(AclEntryPermission aep);
}