package temp.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.index.IndexType;
import utils.ParamParser;

import javax.persistence.Query;
import java.util.List;

public class IndexTypeDAOHibernate  extends GenericHibernateDAO<IndexType, Long> implements IndexTypeDAO {
	
	transient Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public IndexType get(Long id) {
		return getSession().find(IndexType.class, id);
	}

    @Override
    public IndexType get(String id){
        Long lid = ParamParser.parseLong(id, "error.param.id");
        return get(lid);
    }

	/* (non-Javadoc)
	 * @see server.dao.PermissionDAO#findByName(java.lang.String)
	 */
	@Override
	public IndexType findByName(String name) {
		Query q =  getSession().createNamedQuery("findIndexTypeByName");
		q.setParameter("name", name);
		return (IndexType) q.getSingleResult();
	}

	/* (non-Javadoc)
	 * @see server.dao.PermissionDAO#list()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<IndexType> list() {
		Query q = getSession().createNamedQuery("selectAllIndexTypes");
		return q.getResultList();
	}

	/* (non-Javadoc)
	 * @see server.dao.GenericDAO#delete(java.lang.Long)
	 */
	@Override
	public void delete(Long id) {
		IndexType i = get(id);
		makeTransient(i);
	}
	
	
	
}
