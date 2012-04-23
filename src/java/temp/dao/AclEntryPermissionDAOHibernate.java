package temp.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.AclEntryPermission;

import javax.persistence.Query;
import java.util.List;

public class AclEntryPermissionDAOHibernate  extends GenericHibernateDAO<AclEntryPermission, Long> implements AclEntryPermissionDAO {
	
	transient Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public AclEntryPermission get(Long id) {
		return getSession().find(AclEntryPermission.class, id);
	}

	/* (non-Javadoc)
	 * @see server.dao.GenericDAO#list()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AclEntryPermission> list() {
		Query q = getSession().createNamedQuery("selectAllAclEntryPermissions");
		return q.getResultList();
	}

	public void delete(Long id){
		AclEntryPermission aep = get(id);
		makeTransient(aep);
	}
	
	public void delete(AclEntryPermission aep){
		makeTransient(aep);
	}
	
}
