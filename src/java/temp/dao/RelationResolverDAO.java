package temp.dao;

import server.Relation;
import server.RelationType;
import server.data.ObjectSystemData;
import server.RelationResolver;
import java.util.List;

public interface RelationResolverDAO extends server.dao.GenericDAO<RelationResolver, Long> {

	List<RelationResolver> list();
    RelationResolver findByName(String name);


}
