package cinnamon.interfaces;

import server.Relation;
import server.data.ObjectSystemData;
import server.resolver.RelationSide;

/**
 *
 */
public interface IRelationResolver {

    ObjectSystemData resolveVersion(Relation relation, ObjectSystemData changedOsd, String params, RelationSide relationSide);
 
}
