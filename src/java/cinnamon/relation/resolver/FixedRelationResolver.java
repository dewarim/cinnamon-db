package cinnamon.relation.resolver;

import server.interfaces.IRelationResolver;
import server.Relation;
import server.data.ObjectSystemData;
import server.resolver.RelationSide;

public class FixedRelationResolver implements IRelationResolver{

    public ObjectSystemData resolveVersion(Relation relation, ObjectSystemData changedOsd, String params, RelationSide relationSide){
	    return changedOsd;
    }  
    
}