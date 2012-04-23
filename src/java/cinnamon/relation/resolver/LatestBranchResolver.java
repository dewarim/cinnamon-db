package cinnamon.relation.resolver;

import server.Relation;
import server.dao.DAOFactory;
import server.dao.ObjectSystemDataDAO;
import server.data.ObjectSystemData;
import server.interfaces.IRelationResolver;
import server.resolver.RelationSide;
import utils.HibernateSession;

import javax.persistence.EntityManager;

public class LatestBranchResolver implements IRelationResolver{

    public ObjectSystemData resolveVersion(Relation relation, ObjectSystemData changedOsd, String params, RelationSide relationSide){
        EntityManager em = HibernateSession.getLocalEntityManager();
        DAOFactory daoFactory = DAOFactory.instance(DAOFactory.HIBERNATE);
        ObjectSystemDataDAO osdDao = daoFactory.getObjectSystemDataDAO(em);
        return osdDao.findLatestBranch(changedOsd);
    }
    
}