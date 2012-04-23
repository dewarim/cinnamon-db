package temp.dao;

import server.Relation;
import server.RelationType;
import server.dao.GenericDAO;
import server.data.ObjectSystemData;

import java.util.List;

public interface RelationDAO extends GenericDAO<Relation, Long> {
	List<Relation> findAllByLeftID(Long leftID);
	
	List<Relation> findAllByLeftOrRightID(Long leftID, Long rightID);
	List<Relation> findAllByLeftOrRight(ObjectSystemData left, ObjectSystemData right);

    /**
     * Check whether a relation already exists. Create it if not.
     * @param type the type of Relation
     * @param left the left (or: the outgoing) OSD
     * @param right the right (or: the receiving) OSD
     * @param metadata XML metadata string, defaults to an empty &gt;meta&lt; element.
     * @return a Relation which matches the supplied parameters.
     */
	Relation findOrCreateRelation(RelationType type, ObjectSystemData left, ObjectSystemData right, String metadata);
	
	List<Relation> findAllByLeft(ObjectSystemData left);
	List<Relation> findAllByRight(ObjectSystemData right);
	List<Relation> findAllByLeftAndRight(ObjectSystemData left, ObjectSystemData right);
	
	List<Relation> findAllByLeftAndType(ObjectSystemData left, RelationType type);
	List<Relation> findAllByLeftAndRightAndType(ObjectSystemData left, ObjectSystemData right, RelationType type);
	List<Relation> findAllByRightAndType(ObjectSystemData right, RelationType type);
	
	List<Relation> findAllByType(RelationType type);
}
