package temp.dao;

import server.Relation;
import server.RelationType;
import server.data.ObjectSystemData;
import server.dao.GenericHibernateDAO;
import server.RelationResolver;
import server.dao.RelationResolverDAO;
import javax.persistence.Query;
import java.util.List;

public class RelationResolverDAOHibernate extends GenericHibernateDAO<RelationResolver, Long>
		implements RelationResolverDAO {

	/* (non-Javadoc)
	 * @see server.dao.RelationDAO#list()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RelationResolver> list() {
		Query q = getSession().createNamedQuery("selectAllRelationResolvers");
		return q.getResultList();
	}

	/* (non-Javadoc)
	 * @see server.dao.GenericDAO#get(java.lang.Long)
	 */
	@Override
	public RelationResolver get(Long id) {
		return getSession().find(RelationResolver.class, id);
	}

	@Override
	public void delete(Long id) {
		RelationResolver r = get(id);
		makeTransient(r);
	}

    @Override
    public RelationResolver findByName(String name) {
        Query q = getSession().createNamedQuery("findRelationResolverByName");
        q.setParameter("name", name);
        return (RelationResolver) q.getSingleResult();
    }
}
