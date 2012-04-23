package temp.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.AclEntry;
import server.dao.AclEntryDAO;
import server.dao.GenericHibernateDAO;

import javax.persistence.Query;
import java.util.List;

public class AclEntryDAOHibernate  extends GenericHibernateDAO<AclEntry, Long> implements AclEntryDAO {
	
	transient Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public AclEntry get(Long id) {
		return getSession().find(AclEntry.class, id);
	}

	/* (non-Javadoc)
	 * @see server.dao.GenericDAO#list()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AclEntry> list() {
		Query q = getSession().createNamedQuery("selectAllAclEntries");
		return q.getResultList();
	}

	public void delete(Long id){
		AclEntry ae = get(id);
		makeTransient(ae);
	}
	
}
