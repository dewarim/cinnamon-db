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
import server.Acl;
import server.CopyResult;
import server.Folder;
import server.FolderType;
import server.User;
import server.dao.AclDAO;
import server.dao.DAOFactory;
import server.dao.FolderTypeDAO;
import server.dao.GenericHibernateDAO;
import server.dao.ObjectSystemDataDAO;
import server.dao.UserDAO;
import server.data.ObjectSystemData;
import server.data.Validator;
import server.exceptions.CinnamonConfigurationException;
import server.exceptions.CinnamonException;
import server.global.Constants;
import utils.ParamParser;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.*;

/**
 * Concrete folder DAO implementation.
 * 
 * @see <a href="http://www.hibernate.org/328.html">Generic Data Access Objects</a>
 *
 */
public class FolderDAOHibernate extends GenericHibernateDAO<Folder, Long>
		implements server.dao.FolderDAO {

	private Logger log = LoggerFactory.getLogger("server.dao.FolderDAOHibernate");
	static DAOFactory daoFactory = DAOFactory.instance(DAOFactory.HIBERNATE);
	
	@SuppressWarnings({"unchecked", "JpaQueryApiInspection"})
	@Override
	public List<Folder> findAllByPath(String path){
	    return findAllByPath(path, false, null);
	}

    @SuppressWarnings({"unchecked", "JpaQueryApiInspection"})
	@Override
	public List<Folder> findAllByPath(String path, Boolean autoCreate, Validator validator){
		String segs[] = path.split("/");

		Folder parent = findRootFolder();

		List<Folder> ret = new ArrayList<Folder>();
		ret.add(parent);
        for (String seg : segs) {
            if (seg.length() > 0) {
                Query q = getSession()
                        .createNamedQuery("selectFolderByParentAndName");
                q.setParameter("parent", parent);
                q.setParameter("name", seg);
                List<Folder> results = q.getResultList();

                if (results.isEmpty()) {
                    if(autoCreate){
                        if(validator != null){
                            validator.validateCreateFolder(parent);
                        }
                        Folder newFolder = new Folder(seg,"<meta />", parent.getAcl(), parent, parent.getOwner(), parent.getType() );
                        newFolder.setIndexOk(null); // so the IndexServer will index it.
                        makePersistent(newFolder);
                        ret.add(newFolder);
                        parent = newFolder;
                    }
                    else{
                        throw new CinnamonException("error.path.invalid", path);
                    }
                }
                else {
                    Folder folder = results.get(0);
                    parent = folder;
                    ret.add(folder);
                }
            }
        }
		return ret;
	}
	
	public Folder findByPath(String path){
		List<Folder> folders = findAllByPath(path);
		if(folders.isEmpty()){
			return null;
		}
		else{
			return folders.get(folders.size()-1);
		}
	}

	@SuppressWarnings({"unchecked", "JpaQueryApiInspection"})
	@Override
	public List<Folder> getSubfolders(Long parentID) {
		Folder parent;
		EntityManager em = getSession();
		if (parentID == 0L) {
			parent = findRootFolder();
		} else {
			parent = em.find(Folder.class, parentID);
		}
        return getSubfolders(parent);

	}
    
    @SuppressWarnings("unchecked")
    public List<Folder> getSubfolders(Folder parentFolder){
        EntityManager em = getSession();
        Query q = em.createNamedQuery("selectSubfolders");
		q.setParameter("parent", parentFolder);
		return q.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Folder> getSubfolders(Folder parentFolder, Boolean recursive){
        EntityManager em = getSession();
        Query q = em.createNamedQuery("selectSubfolders");
		q.setParameter("parent", parentFolder);        
        List<Folder> folders = q.getResultList();
        List<Folder> newFolders = new ArrayList<Folder>();
        if(recursive){
            for(Folder folder : folders){
                newFolders.addAll(getSubfolders(folder, true));
            }
        }
        folders.addAll(newFolders);
		return folders; 
    }

	@Override
	public void delete(Long id) {
		log.debug("before loading folder");
		Folder folder;
		if (id == 0L) {
			folder = findRootFolder();
		} else {
			folder = get(id);
		}

		if (folder == null) {
			throw new CinnamonException("error.folder.not_found");
		}

		// check for subfolders:
		List<Folder> list = findAllByParentID(id);
		if ( ! list.isEmpty()) {
			throw new CinnamonException("error.subfolders.exist");
		}

		// check for objects inside folder
	    ObjectSystemDataDAO osdDAO = daoFactory.getObjectSystemDataDAO(getSession());
	
		if ( ! osdDAO.findAllByParent(id).isEmpty()) {
			throw new CinnamonException("error.folder.has_content");
		}

		makeTransient(folder);
	}

	@Override
	public Folder update(Long id, Map<String, String> fields){
		// load object and change with given params:
		Folder folder = get(id);
		return update(folder, fields);
	}
	
	@Override
	public Folder update(Folder folder, Map<String,String> fields){
		if(folder == null){
			throw new CinnamonException("error.object.not.found");
		}
		if (fields.containsKey("parentid")) {		
			Long folderId = ParamParser.parseLong(fields.get("parentid"), "error.param.parent_id");
			Folder newParentFolder = get(folderId);
			if(newParentFolder == null){
				throw new CinnamonException("error.param.parent_id");
			}
            else if(newParentFolder.equals(folder)){
                // prevent a folder from being its own parent.
                throw new CinnamonException("error.illegal_parent_id");
            }
            else if(getParentFolders(newParentFolder).contains(folder)){
                // prevent a folder from being moved into one of its child folders.
                throw new CinnamonException("error.illegal_parent_id");
            }
			else{
				folder.setParent(newParentFolder);
                resetIndexOnFolderContent(folder);
			}
		}
		if(fields.containsKey("ownerid")){
			Long ownerId = ParamParser.parseLong(fields.get("ownerid"), "error.param.owner_id");
			UserDAO userDao = daoFactory.getUserDAO(getSession());
			User owner = userDao.get(ownerId);
			if(owner == null){
				throw new CinnamonException("error.user.not_found");
			}
            folder.setOwner(owner);	
		}
		if( fields.containsKey("name")) {
			folder.setName(fields.get("name"));
            resetIndexOnFolderContent(folder);
		}
		if( fields.containsKey("metadata")){
			folder.setMetadata(fields.get("metadata"));
		}
		if( fields.containsKey("aclid")){
			Long aclId = ParamParser.parseLong(fields.get("aclid"), "error.param.acl_id");
			AclDAO aclDAo = daoFactory.getAclDAO(getSession());
			Acl acl = aclDAo.get(aclId);
			if(acl == null){
				throw new CinnamonException("error.param.acl_id");
			}
			folder.setAcl(acl);

		}
        if(fields.containsKey("typeid")){
            Long typeId = ParamParser.parseLong(fields.get("typeid"), "error.param.type_id");
            FolderTypeDAO fDao = daoFactory.getFolderTypeDAO(getSession());
            FolderType ft = fDao.get(typeId);
            if(ft == null){
                throw new CinnamonException("error.param.type_id");
            }
            folder.setType(ft);
        }
		return folder;
	}

    /**
     * After a folder was moved, the folderPath index is invalid for all its content.
     * To correct this, the index_ok column is set to null so the IndexServer will
     * index each object anew.
     * @param folder a folder who will be re-indexed along with its content (recursively).
     */
    void resetIndexOnFolderContent(Folder folder){
        folder.setIndexOk(null);
        for(ObjectSystemData osd : getFolderContent(folder, false)){
            osd.setIndexOk(null);
        }
        for(Folder childFolder : getSubfolders(folder)){
            resetIndexOnFolderContent(childFolder);
        }
    }
	
	@Override
	public Folder get(Long id) {
		return getSession().find(Folder.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Folder> findAllByParentID(Long parentID) {
		Query q = getSession().createQuery("select f from Folder f where f.parent.id=:id")
		.setParameter("id", parentID);
		return q.getResultList();
	}
	
	@Override
	public Folder findByNameAndParentID(String name, Long id) {
		Query q = getSession().createQuery("select f from Folder f where name=:name and f.parent.id=:id");
		q.setParameter("name", name);
		q.setParameter("id", id);
		try{
			return (Folder) q.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Folder> findAllByNameAndParentID(String name, Folder parent) {
		Query q = getSession().createQuery("select f from Folder f where name=:name and parent=:parent");
		q.setParameter("name", name);
		q.setParameter("parent", parent);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Folder> list() {
		Query q = getSession().createNamedQuery("selectAllFolders");		
		return q.getResultList();
	}

	public Folder makePersistent(Folder f){
		f = super.makePersistent(f);
		getSession().flush();
		if(f.getId() == 0){
		    throw new CinnamonException("error.folder.persist");
		}
		return f;
	}
	
	/**
	 * Convenience method: given a String which represents a folder-Id, this
	 * method returns the Folder.
	 * @param id parsable with Long.parseLong
	 * @return Folder
	 * @throws CinnamonException if String is not a valid Long.
	 */
	public Folder get(String id){
		Long folderId = ParamParser.parseLong(id, "error.get.folder");
		return get(folderId);
	}
	
	/**
	 * Installation-Hint<br>
	 * Create a Folder whose parent equals it's own id and whose name is equals the ROOT_FOLDER_NAME.
	 * This is the default folder in which objects and folders are created if no parent_id is given.
	 * 
	 * @return	Folder rootFolder
	 */
	@SuppressWarnings({"JpaQueryApiInspection"})
    public Folder findRootFolder(){
		Query q = getSession().createNamedQuery("findRootFolder");
		q.setParameter("name", Constants.ROOT_FOLDER_NAME);
		Folder f;
		try{
			f = (Folder) q.getSingleResult();
		}
		catch (NoResultException e) {
			log.error("RootFolder is missing!");
			throw new CinnamonConfigurationException("Could not find the root folder. Please create a folder called "+Constants.ROOT_FOLDER_NAME
					+ " with parent_id == its own id.");
		}
		
		return f;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Folder> findIndexTargets(Integer maxResults){
		Query q = getSession().createNamedQuery("findFolderIndexTargets");
//		q.setParameter("date", getAncientCalendar());
		q.setMaxResults(maxResults);
		return q.getResultList();
	}
	
	@Override
	public Integer prepareReIndex() {
		Query q = getSession().createNamedQuery("prepareFolderReIndex");
		return q.executeUpdate();
	}

	
	@SuppressWarnings({"unchecked", "JpaQueryApiInspection"})
	public List<ObjectSystemData> getFolderContent(Folder folder, Boolean recursive){
        return getFolderContent(folder, recursive, null, null);
	}
    
    @SuppressWarnings({"unchecked", "JpaQueryApiInspection"})
	public List<ObjectSystemData> getFolderContent(Folder folder, Boolean recursive, Boolean latestHead, Boolean latestBranch){
		Query q;
        if(latestHead != null && latestBranch != null){
            q = getSession().createNamedQuery("findOsdsByParentAndLatestHeadOrLatestBranch");
            q.setParameter("latestHead", true);
            q.setParameter("latestBranch", true);
        }
        else if(latestHead != null){
            q = getSession().createNamedQuery("findOsdsByParentAndLatestHead");
            q.setParameter("latestHead", true);
        }
        else if(latestBranch != null){
            q = getSession().createNamedQuery("findOsdsByParentAndLatestBranch");
            q.setParameter("latestBranch", true);
        }
        else{
            q = getSession().createNamedQuery("findOsdsByParent");
        }
  	    q.setParameter("parent", folder);
        List<ObjectSystemData> osds = q.getResultList();
    	if(recursive){    		
    		List<Folder> subFolders = getSubfolders(folder);
    		for(Folder f : subFolders){
    			osds.addAll(getFolderContent(f, true, latestHead, latestBranch));
    		}
    	}
    	return osds;
	}

	public List<Folder> getParentFolders(Folder folder){
		List<Folder> folders = new ArrayList<Folder>();
		Folder root = findRootFolder();
		folder = folder.getParent();
		while(folder != null && folder !=  root ){
			folders.add(folder);
			folder = folder.getParent();
		}
		return folders;
	}

}
