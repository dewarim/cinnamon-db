package temp.dao;

import server.AclEntry;
import server.AclEntryPermission;
import server.Permission;

public interface PermissionDAO extends GenericDAO<Permission, Long> {
	
	AclEntryPermission addToAclEntry(Permission permission, AclEntry aclEntry);

    /**
     * Fetch the permission with the given name. Returns the Permission or null.
     * @param name the name of the permission object you want.
     * @return the permission of null if it is not in the database.
     */
	Permission findByName(String name); 
}