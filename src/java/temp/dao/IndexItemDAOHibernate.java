package temp.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.index.IndexItem;

import javax.persistence.Query;
import java.util.List;

public class IndexItemDAOHibernate  extends GenericHibernateDAO<IndexItem, Long> implements IndexItemDAO {
	
	transient Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public IndexItem get(Long id) {
		return getSession().find(IndexItem.class, id);
	}


	/* (non-Javadoc)
	 * @see server.dao.PermissionDAO#findByName(java.lang.String)
	 */
	@Override
	public IndexItem findByName(String name) {
		Query q =  getSession().createNamedQuery("findIndexItemByName");
		q.setParameter("name", name);
		return (IndexItem) q.getSingleResult();
	}

	/* (non-Javadoc)
	 * @see server.dao.PermissionDAO#list()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<IndexItem> list() {
		Query q = getSession().createNamedQuery("selectAllIndexItems");
		log.debug("created namedQuery:"+q.toString());
		return q.getResultList();
	}

	/* (non-Javadoc)
	 * @see server.dao.GenericDAO#delete(java.lang.Long)
	 */
	@Override
	public void delete(Long id) {
		IndexItem i = get(id);
		makeTransient(i);
	}
	
	
	
}
