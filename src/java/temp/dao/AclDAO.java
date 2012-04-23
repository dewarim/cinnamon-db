package temp.dao;

import server.Acl;
import server.dao.GenericDAO;

import java.util.Map;

public interface AclDAO extends GenericDAO<Acl, Long> {

    /**
     * Find an ACL by its name. Will return null if no ACL was found.
     * @param name The name of the ACL you search for.
     * @return the requested ACL or null
     */
	Acl findByName(String name);

	void update(Long id, Map<String, String> cmd);
}
