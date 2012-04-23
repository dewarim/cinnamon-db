package temp.dao;

import server.RelationType;
import utils.ParamParser;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class RelationTypeDAOHibernate extends
		GenericHibernateDAO<RelationType, Long> implements RelationTypeDAO {

	@Override
	public void delete(Long relationTypeID) {
		EntityManager em = getSession();
		Query q = em.createQuery("select r from RelationType r where id=:id").setParameter("id", relationTypeID);
		RelationType moribundus = (RelationType) q.getSingleResult();
		makeTransient(moribundus);
	}

	@Override
    @SuppressWarnings("unchecked")
	public RelationType findByName(String name) {
		Query q = getSession().createQuery("select rt from RelationType rt where name=:name");
		q.setParameter("name", name);
        List<RelationType> rts = q.getResultList();
        if(rts.size() > 0){
            return rts.get(0);
        }
        else{
            return null;
        }
	}

	@Override
	public RelationType get(Long id) {
		return getSession().find(RelationType.class, id);
	}
	
	@Override
	public RelationType get(String id) {
		Long relTypeId = ParamParser.parseLong(id, "error.param.id"); 
		return getSession().find(RelationType.class, relTypeId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RelationType> list() {
		Query q = getSession().createNamedQuery("selectAllRelationTypes");
		return q.getResultList();
	}

}
