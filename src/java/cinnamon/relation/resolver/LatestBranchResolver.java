package cinnamon.relation.resolver;

import cinnamon.ObjectSystemData;
import cinnamon.interfaces.IRelationResolver;
import cinnamon.relation.Relation;

public class LatestBranchResolver implements IRelationResolver {

    public ObjectSystemData resolveVersion(Relation relation, ObjectSystemData changedOsd, String params, RelationSide relationSide){
        return changedOsd.findLatestBranch();
    }
    
}