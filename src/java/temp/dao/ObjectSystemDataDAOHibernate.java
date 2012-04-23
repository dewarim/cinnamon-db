// cinnamon - the Open Enterprise CMS project
// Copyright (C) 2007 Dr.-Ing. Boris Horner
// 
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA

package temp.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Folder;
import server.ObjectType;
import server.Relation;
import server.User;
import server.dao.DAOFactory;
import server.dao.GenericHibernateDAO;
import server.dao.ObjectSystemDataDAO;
import server.dao.RelationDAO;
import server.data.ContentStore;
import server.data.ObjectSystemData;
import server.exceptions.CinnamonException;
import server.helpers.ObjectTreeCopier;
import server.interfaces.Repository;
import utils.ParamParser;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.*;

public class ObjectSystemDataDAOHibernate extends
        GenericHibernateDAO<ObjectSystemData, Long> implements
        ObjectSystemDataDAO {
	private final Logger log = LoggerFactory.getLogger("server.dao.ObjectSystemDataDAOHibernate");
	static final DAOFactory factory = DAOFactory.instance(DAOFactory.HIBERNATE);

    private Map<ObjectSystemData, ObjectSystemData> emptyCopies	= new HashMap<ObjectSystemData,ObjectSystemData>();

	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectSystemData> findAllByParent(Long parentID) {
		Query q = getSession().createQuery(
		"select o from ObjectSystemData o where o.parent.id=:parentid")
		.setParameter("parentid", parentID);
		return q.getResultList();
	}

    @SuppressWarnings("unchecked")
	@Override
	public List<ObjectSystemData> findAllByParent(Folder parent) {
		Query q = getSession().createQuery(
		"select o from ObjectSystemData o where o.parent=:parent")
		.setParameter("parent", parent);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectSystemData> findAllByPredecessorID(ObjectSystemData predecessor) {
		Query q = getSession().createNamedQuery("findOsdsByPredecessor");
		q.setParameter("predecessor", predecessor);
		return q.getResultList();
	}

	@Override
	public ObjectSystemData get(Long id) {
		return getSession().find(ObjectSystemData.class, id);
	}

    @Override
    public void delete(ObjectSystemData osd){
        delete(osd, false, false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void delete(ObjectSystemData osd, Boolean killDescendants, Boolean removeLeftRelations){
        log.debug("Found osd");
        ObjectSystemData predecessor = osd.getPredecessor();

        log.debug("checking for descendants ");
        boolean hasDescendants = findAllByPredecessorID(osd).size() > 0;
        if (killDescendants && hasDescendants) {
            Query q = getSession().createNamedQuery("findOsdsByPredecessorOrderByIdDesc");
            for(ObjectSystemData pre : (List<ObjectSystemData>) q.getResultList() ){
                delete(pre, killDescendants, removeLeftRelations);
            }
        }
        else if (hasDescendants){
            throw new CinnamonException("error.delete.has_descendants");
        }


        // check for protected relations
    	Long osd_id = osd.getId();
	    RelationDAO relationDao = factory.getRelationDAO(getSession());
	    List<Relation> relations = relationDao.findAllByLeftOrRightID(osd_id, osd_id);
        for(Relation rel : relations){
            server.RelationType rt = rel.getType();
            /*
             * if an object is protected by the relation type, it
             * must not be deleted.
             */
            if( ( rt.isRightobjectprotected() && rel.getRight().equals(osd)) ||
                ( rt.isLeftobjectprotected() && rel.getLeft().equals(osd) && ! removeLeftRelations)
                    ){
                throw new CinnamonException("error.protected_relations");
            }
        }

        // delete relations
    	for (Relation rel : relations) {
    		relationDao.makeTransient(rel);
    	}

    	makeTransient(osd);
    	log.debug("object deleted.");

    	/*
    	 * An object is latestBranch, if it has no descendants.
    	 * You can only delete an object without descendants.
    	 * So, the predecessor's only child has been deleted and this
    	 * makes the predecessor latestBranch.
    	 *
    	 * An object is latestHead, if it is not of part of a branch and has no
    	 * descendants. As we already said, this predecessor cannot have any
    	 * descendants and so we can set latestHead to true if it is part of
    	 * the main branch (no . in version).
    	 */
    	if(predecessor != null){
    		predecessor.setLatestBranch(true);
    		if(! predecessor.getVersion().contains(".")){
    			predecessor.setLatestHead(true);
    		}
    	}

    	ContentStore.deleteObjectFile(osd);
    }

	@Override
	public void delete(Long id) {
		log.debug("before loading osd");	
	    ObjectSystemData osd = get(id);
	    if(osd == null) {
    		throw new CinnamonException("error.object.not.found");
    	}
        delete(osd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectSystemData> findAllByRootAndNotSelf(ObjectSystemData osd) {
		Query q = getSession().createNamedQuery("findOSDByRootAndNotSelf");
		q.setParameter("root", osd.getRoot());
		q.setParameter("self", osd);
		return q.getResultList();
	}

	/* (non-Javadoc)
	 * @see server.dao.ObjectSystemDataDAO#list()
	 */
	@SuppressWarnings("unchecked")
	@Override	
	public List<ObjectSystemData> list() {
		Query q = getSession().createNamedQuery("selectAllOSDs");
		return q.getResultList();
	}
	
	@Override
	public ObjectSystemData get(String id){
		Long osdId = ParamParser.parseLong(id, "error.param.id"); 
		return get(osdId);
	}

    @Override
    public ObjectSystemData getOsdNotNull(String id) {
        ObjectSystemData osd = get(id);
        if(osd == null){
            throw new CinnamonException("error.get.osd");
        }
        return osd;
    }

    @SuppressWarnings("unchecked")
	public List<ObjectSystemData> findIndexTargets(Integer maxResults){
		Query q = getSession().createNamedQuery("findObjectIndexTargets");
		q.setMaxResults(maxResults);
		return q.getResultList();
	}

	@Override
	public Integer prepareReIndex() {
		Query q = getSession().createNamedQuery("prepareOSD_ReIndex");
		return q.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectSystemData> findAllVersions(ObjectSystemData osd) {
		if(! osd.getVersion().equals("1")){
			osd = osd.getRoot();
		}
		Query q = getSession().createNamedQuery("findAllVersionsOfOsdOrderByIdDesc");
		q.setParameter("root", osd);
		return q.getResultList();
	}

	@Override
	public ObjectSystemData findByRootAndVersion(ObjectSystemData osd,
			String version) {
		Query q = getSession().createNamedQuery("findOsdByRootAndVersion");
		q.setParameter("root", osd.getRoot());
		q.setParameter("version", version);
		ObjectSystemData result;
		try{
			result = (ObjectSystemData) q.getSingleResult();
		}
		catch (NoResultException e) {
			result = null;
		}
		return result; 
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectSystemData> findAllVersionsOrderLastToFirst(ObjectSystemData osd) {
		Query q = getSession().createNamedQuery("findOSDAllVersionsLastToFirst");
		q.setParameter("root", osd.getRoot());
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectSystemData> findAllByTypeAndProcstate(ObjectType type,
			String procstate) {
		Query q = getSession().createNamedQuery("findAllOsdByTypeAndProcstate");
		q.setParameter("type", type);
		q.setParameter("procstate", procstate);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectSystemData> findAllByTypeAndProcstateAndOwner(
			ObjectType type, String procstate, User owner) {
		Query q = getSession().createNamedQuery("findAllOsdByTypeAndProcstateAndOwner");
		q.setParameter("type", type);
		q.setParameter("procstate", procstate);
		q.setParameter("owner", owner);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectSystemData> findAllByTypeAndProcstateAndOwnerAndRelationToLeftOsd(
			ObjectType type, String procstate, User owner,
			ObjectSystemData related) {
		Query q = getSession().createNamedQuery("findAllOsdByTypeAndProcstateAndOwnerAndRelationToLeftOsd");
		q.setParameter("type", type);
		q.setParameter("procstate", procstate);
		q.setParameter("owner", owner);
		q.setParameter("leftOSD", related);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectSystemData> findAllByTypeAndProcstateAndRelationToLeftOsd(
			ObjectType type, String procstate, ObjectSystemData related) {
		Query q = getSession().createNamedQuery("findAllOsdByTypeAndProcstateAndRelationToLeftOsd");
		q.setParameter("type", type);
		q.setParameter("procstate", procstate);
		q.setParameter("leftOSD", related);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectSystemData> findAllByParentAndName(Folder parent,
			String name) {
		Query q = getSession().createNamedQuery("findOsdsByParentAndName");
		q.setParameter("parent", parent);
		q.setParameter("name", name);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectSystemData> findAllByNameAndType(String name, ObjectType type) {
		Query q = getSession().createNamedQuery("findOsdsByNameAndType");
		q.setParameter("name", name);
		q.setParameter("type", type);
		return q.getResultList();
	}

    // copy OSD:
    @Override
    public List<ObjectSystemData> copyObjectTree(ObjectSystemData source, Folder targetFolder, User activeUser, Repository repository){
//        RelationDAO relDao = factory.getRelationDAO( getSession() );
		ObjectSystemDataDAO osdDao = factory.getObjectSystemDataDAO( getSession() );
		List<ObjectSystemData> allVersions = osdDao.findAllVersions(source);
		List<ObjectSystemData> newTree = new ArrayList<ObjectSystemData>();

		// create copies of all versions:
//		clearEmptyCopies();
        ObjectTreeCopier objectTreeCopier = new ObjectTreeCopier(activeUser, targetFolder);
		log.debug("create empty copies of all versions");
		for(ObjectSystemData osd : allVersions){
			log.debug("create empty copy of: "+osd.getId());
			ObjectSystemData copy = objectTreeCopier.createEmptyCopy(osd);
			log.debug(String.format("Empty copy of %d is %d", osd.getId(), copy.getId()));
			copy.setMetadata(osd.getMetadata());
			objectTreeCopier.getCopyCache().put(osd, copy);

            osd.copyContent(repository.getName(), copy);
			newTree.add(copy);
		}

        return newTree;
    }

    @Override
    public List<ObjectSystemData> getObjectsInFolder(String versions, String id){
		versions = versions != null ? versions : "head";
		versions = versions.length() == 0 ? "head" : versions; // check that we didn't get an empty string.

		Long parentId = ParamParser.parseLong(id, "error.param.parent_id");
	  	String versionPred = ObjectSystemData.fetchVersionPredicate(versions);
        DAOFactory daoFactory = DAOFactory.instance(DAOFactory.HIBERNATE);
		server.dao.FolderDAO folderDAO = daoFactory.getFolderDAO(getSession());
		Folder parent = folderDAO.get(parentId);

		// TODO: use DAO
		Query q = getSession().createQuery("select o from ObjectSystemData o where parent=:parent "+versionPred);
    	q.setParameter("parent", parent);
    	@SuppressWarnings("unchecked")
    	List<ObjectSystemData> results = q.getResultList();
      	log.debug("size of unfiltered result-list: "+results.size());
    	return results;
	}

    @Override
    public ObjectSystemData findLatestHead(ObjectSystemData osd){
        Query q = getSession().createNamedQuery("findLatestHead");
        q.setParameter("root", osd.getRoot());
        // this should always return a result, because if we have an object, at least version 1 exists.
        return (ObjectSystemData) q.getSingleResult();
    }

    @Override
    public ObjectSystemData findLatestBranch(ObjectSystemData osd) {
        Query q = getSession().createNamedQuery("findLatestBranchOrderByModified");
        q.setParameter("root", osd.getRoot());
        // there should always be one object for which latestBranch is true.
        return (ObjectSystemData) q.getResultList().get(0);
    }

   public List<ObjectSystemData> findAllByLockOwner(User user){
       Query q = getSession().createNamedQuery("findAllByLockOwner");
       q.setParameter("lockOwner", user);
       return q.getResultList();
   }
}
