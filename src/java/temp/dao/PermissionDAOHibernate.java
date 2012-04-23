package temp.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.AclEntry;
import server.AclEntryPermission;
import server.Permission;

import javax.persistence.Query;
import java.util.List;

public class PermissionDAOHibernate  extends GenericHibernateDAO<Permission, Long> implements PermissionDAO {
	
	transient Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public Permission get(Long id) {
		return getSession().find(Permission.class, id);
	}

	@Override
	public AclEntryPermission addToAclEntry(Permission permission, AclEntry aclEntry) {
		AclEntryPermission aep = new AclEntryPermission(aclEntry, permission);
		getSession().persist(aep);
		getSession().flush();
		return aep;
	}

	/* (non-Javadoc)
	 * @see server.dao.PermissionDAO#findByName(java.lang.String)
	 */
	@Override
	public Permission findByName(String name) {
		Query q =  getSession().createNamedQuery("findPermissionByName");
		q.setParameter("name", name);
        List<Permission> permissionList = q.getResultList();
		return permissionList.size() > 0 ? permissionList.get(0) : null;
	}

	/* (non-Javadoc)
	 * @see server.dao.PermissionDAO#list()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Permission> list() {
		Query q = getSession().createNamedQuery("selectAllPermissions");
		return q.getResultList();
	}

	/* (non-Javadoc)
	 * @see server.dao.GenericDAO#delete(java.lang.Long)
	 */
	@Override
	public void delete(Long id) {
		Permission p = get(id);
		makeTransient(p);
	}
	
	
	
}
