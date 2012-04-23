package temp.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.index.IndexGroup;
import utils.ParamParser;

import javax.persistence.Query;
import java.util.List;

public class IndexGroupDAOHibernate  extends GenericHibernateDAO<IndexGroup, Long> implements IndexGroupDAO {
	
	transient Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public IndexGroup get(Long id) {
		return getSession().find(IndexGroup.class, id);
	}

    @Override
    public IndexGroup get(String id){
        Long lid = ParamParser.parseLong(id, "error.param.id");
        return get(lid);
    }

	/* (non-Javadoc)
	 * @see server.dao.PermissionDAO#findByName(java.lang.String)
	 */
	@Override
	public IndexGroup findByName(String name) {
		Query q =  getSession().createNamedQuery("findIndexGroupByName");
		q.setParameter("name", name);
		return (IndexGroup) q.getSingleResult();
	}

	/* (non-Javadoc)
	 * @see server.dao.PermissionDAO#list()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<IndexGroup> list() {
		Query q = getSession().createNamedQuery("selectAllIndexGroups");
		return q.getResultList();
	}

	/* (non-Javadoc)
	 * @see server.dao.GenericDAO#delete(java.lang.Long)
	 */
	@Override
	public void delete(Long id) {
		IndexGroup i = get(id);
		makeTransient(i);
	}
	
	
	
}
