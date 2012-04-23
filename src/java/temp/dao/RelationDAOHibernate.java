package temp.dao;

import server.Relation;
import server.RelationType;
import server.dao.GenericHibernateDAO;
import server.dao.RelationDAO;
import server.data.ObjectSystemData;

import javax.persistence.Query;
import java.util.List;

public class RelationDAOHibernate extends GenericHibernateDAO<Relation, Long>
		implements RelationDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<Relation> findAllByLeftID(Long leftID) {
    	Query q = getSession().createQuery("select r from Relation r where r.leftOSD.id=:id");
		q.setParameter("id", leftID);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Relation> findAllByLeftOrRightID(Long leftID, Long rightID) {
    	Query q = getSession().createQuery("select r from Relation r where r.leftOSD.id=:leftid or r.rightOSD.id=:rightid");
    	q.setParameter("leftid", leftID);
    	q.setParameter("rightid", rightID);
    	return q.getResultList();
	}

    @SuppressWarnings("unchecked")
	@Override
	public List<Relation> findAllByLeftOrRight(ObjectSystemData left, ObjectSystemData right) {
    	Query q = getSession().createQuery("select r from Relation r where r.leftOSD=:left or r.rightOSD=:right");
    	q.setParameter("left", left);
    	q.setParameter("right", right);
    	return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Relation findOrCreateRelation(RelationType type,
			ObjectSystemData left, ObjectSystemData right, String metadata) {
		Query q = getSession().createNamedQuery("findRelationsByLeftAndRightAndType");
		q.setParameter("left", left);
		q.setParameter("right", right);
		q.setParameter("type", type);
		List<Relation> relations = q.getResultList(); // workaround for broken API with "NoResultException"
		if(relations.isEmpty()){
			Relation newRelation = new Relation(type,left,right, metadata);
			return makePersistent(newRelation);
		}
		else{
			return relations.get(0);
		}
	}

	/* (non-Javadoc)
	 * @see server.dao.RelationDAO#list()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Relation> list() {
		Query q = getSession().createNamedQuery("selectAllRelations");
		return q.getResultList();
	}

	/* (non-Javadoc)
	 * @see server.dao.GenericDAO#get(java.lang.Long)
	 */
	@Override
	public Relation get(Long id) {
		return getSession().find(Relation.class, id);
	}

	@Override
	public void delete(Long id) {
		Relation r = get(id);
		makeTransient(r);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Relation> findAllByLeft(ObjectSystemData left) {
		Query q = getSession().createNamedQuery("findRelationsByLeft");
		q.setParameter("left", left);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Relation> findAllByLeftAndType(ObjectSystemData left, RelationType type) {
		Query q = getSession().createNamedQuery("findRelationsByLeftAndType");
		q.setParameter("left", left);
		q.setParameter("type", type);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Relation> findAllByLeftAndRight(ObjectSystemData left, ObjectSystemData right) {
		Query q = getSession().createNamedQuery("findRelationsByLeftAndRight");
		q.setParameter("left", left);
		q.setParameter("right", right);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Relation> findAllByLeftAndRightAndType(ObjectSystemData left,
			ObjectSystemData right, RelationType type) {
		Query q = getSession().createNamedQuery("findRelationsByLeftAndRightAndType");
		q.setParameter("left", left);
		q.setParameter("right", right);
		q.setParameter("type", type);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Relation> findAllByRight(ObjectSystemData right) {
		Query q = getSession().createNamedQuery("findRelationsByRight");
		q.setParameter("right", right);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Relation> findAllByRightAndType(ObjectSystemData right, RelationType type) {
		Query q = getSession().createNamedQuery("findRelationsByRightAndType");
		q.setParameter("right", right);
		q.setParameter("type", type);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Relation> findAllByType(RelationType type) {
		Query q = getSession().createNamedQuery("findRelationByType");
		q.setParameter("type", type);
		return q.getResultList();
	}
	
	
	
}
