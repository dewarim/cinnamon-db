package cinnamon.relation.resolver;

import server.dao.DAOFactory;
import server.dao.ObjectSystemDataDAO;
import server.interfaces.IRelationResolver;
import server.Relation;
import server.data.ObjectSystemData;
import server.resolver.RelationSide;
import utils.HibernateSession;

import javax.persistence.EntityManager;

public class LatestHeadResolver implements IRelationResolver{

    public ObjectSystemData resolveVersion(Relation relation, ObjectSystemData changedOsd, String params, RelationSide relationSide){
        EntityManager em = HibernateSession.getLocalEntityManager();
        DAOFactory daoFactory = DAOFactory.instance(DAOFactory.HIBERNATE);
        ObjectSystemDataDAO osdDao = daoFactory.getObjectSystemDataDAO(em);
        return osdDao.findLatestHead(changedOsd);
    }
    
}