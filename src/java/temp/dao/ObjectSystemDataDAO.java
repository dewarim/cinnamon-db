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

import server.Folder;
import server.ObjectType;
import server.User;
import server.dao.GenericDAO;
import server.data.ObjectSystemData;
import server.exceptions.CinnamonException;
import server.interfaces.Repository;

import java.util.List;

public interface ObjectSystemDataDAO extends GenericDAO<ObjectSystemData, Long> {

	/**
	 * Return all OSDs with a given predecessor
	 * @param predecessor predecessor osd
	 * @return List<ObjectSystemData>
	 */
	List<ObjectSystemData> findAllByPredecessorID(ObjectSystemData predecessor);
	
	/**
	 * Return all OSDs in a specific parent folder.
	 * @param parentFolderId id of the parent folder
	 * @return Content of parent folder (OSDs)
	 */
	List<ObjectSystemData> findAllByParent(Long parentFolderId);


    /**
     * Find all OSDs in a specific folder.
     * @param parent the parent folder
     * @return Content of parent folder (OSDs)
     */
    List<ObjectSystemData> findAllByParent(Folder parent);

	/**
	 * Find all OSDs whose indexed-time is "0000-00-00 00:00:00". Those are the ones
	 * whose index is not current.
	 * @param maxResults maximum number of results
	 * @return a List of all IndexTargets (limited by maxResults).
	 */
	List<ObjectSystemData> findIndexTargets(Integer maxResults);
	
	/**
	 * Set the indexed-column to 0 and trigger a re-indexing by the IndexServer.
     * @return the number of affected rows.
	 */
	Integer prepareReIndex();
	
//	ObjectSystemData findByName(String name);

	List<ObjectSystemData> findAllByNameAndType(String name, ObjectType type);
	
	List<ObjectSystemData> findAllByRootAndNotSelf(ObjectSystemData osd);
	
	/**
	 * Convenience method: given a String which represents a OSD-Id, this
	 * method returns the OSD.
	 * @param id - String parsable with Long.parseLong
	 * @return ObjectSystemData
	 * @throws CinnamonException if String is not a valid Long.
	 */
	ObjectSystemData get(String id);

    /**
     * Convenience method: given a String which represents an OSD-id,
     * return the OSD specified - or an exception if the object was not found.
     * @param id
     * @return the requested object or null, if allowed.
     */
    ObjectSystemData getOsdNotNull(String id);

	/**
	 * Find all versions of a given object. Sort them in descending order by id.
	 * @param osd
	 * @return list of all objects of this object tree.
	 */
	List<ObjectSystemData> findAllVersions(ObjectSystemData osd);
	

	/**
	 * Find a specific version of an object, given the root object of this object tree.
	 * @param osd the root object.
     * @param version the specific version that is needed.
	 * @return the requested version of null.
	 */
	ObjectSystemData findByRootAndVersion(ObjectSystemData osd, String version);

//    List<ObjectSystemData> findAllByParentAnd
	List<ObjectSystemData> findAllVersionsOrderLastToFirst(ObjectSystemData osd);
	List<ObjectSystemData> findAllByTypeAndProcstateAndOwnerAndRelationToLeftOsd(ObjectType type, String procstate, User owner, ObjectSystemData related);
	List<ObjectSystemData> findAllByTypeAndProcstateAndRelationToLeftOsd(ObjectType type, String procstate, ObjectSystemData related);
	List<ObjectSystemData> findAllByTypeAndProcstateAndOwner(ObjectType type, String procstate, User owner);
	List<ObjectSystemData> findAllByTypeAndProcstate(ObjectType type, String procstate);
	List<ObjectSystemData> findAllByParentAndName(Folder parent, String name);

    List<ObjectSystemData> copyObjectTree(ObjectSystemData osd, Folder targetFolder, User activeUser, Repository repository);

    /**
     * Find all objects in a folder, filtered by version.
     * @param versions String which defines the version - may be "head", "branch" or "all". If null, "head" is assumed.
     * @param id Folder.id
     * @return a list of OSDs.
     */
    List<ObjectSystemData> getObjectsInFolder(String versions, String id);

    /**
     * Delete a given OSD. Will throw an exception if there are any descendants or protected relations.
     * @param osd the OSD to be deleted.
     */
    void delete(ObjectSystemData osd);

    /**
     * Eradicate an OSD.
     * @param osd the osd to be deleted.
     * @param killDescendants if set to true, delete descendants. if set to false, throw an exception upon
     * encountering child objects.
     * @param removeLeftRelations if set to true, delete Relations where this OSD is on the left side
     * (ie, outgoing relations) even though they may be protected. This may be necessary for a soft rollback, where we
     * have to delete an object tree including content and relations and where a database rollback is not an option.
     */
    void delete(ObjectSystemData osd, Boolean killDescendants, Boolean removeLeftRelations);

    /**
     * Find the latest head version of this OSD. The latest version may or may not exist in the same folder
     * as the parameter OSD or its root. 
     * @param osd an OSD which head you are searching for.
     * @return the head OSD (meaning the OSD with the highest version number which is not a branch.)
     */
    ObjectSystemData findLatestHead(ObjectSystemData osd);

    /**
     * Find the newest object of an object tree, no matter on which branch it is located.
     * @param osd the osd from whose object tree the newest branch is retrieved.
     * @return the newest branch object.
     */
    ObjectSystemData findLatestBranch(ObjectSystemData osd);

    /**
     * Find all objects that a user has a lock on.
     * @param user the user whose objects you want to list
     * @return a list of Objects locked by this user.
     */
    List<ObjectSystemData> findAllByLockOwner(User user);
}
