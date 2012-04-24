package cinnamon.interfaces;


import cinnamon.ObjectSystemData;
import cinnamon.relation.Relation;
import cinnamon.relation.resolver.RelationSide;

/**
 *
 */
public interface IRelationResolver {

    ObjectSystemData resolveVersion(Relation relation, ObjectSystemData changedOsd, String params, RelationSide relationSide);
 
}
