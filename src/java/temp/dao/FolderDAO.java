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

import server.CopyResult;
import server.Folder;
import server.dao.GenericDAO;
import server.data.ObjectSystemData;
import server.data.Validator;
import server.exceptions.CinnamonException;

import java.util.List;
import java.util.Map;

/**
 * The DAO interface for a particular entity extends the generic interface and provides the type arguments.
 * 
 * see <a href="http://www.hibernate.org/328.html">Generic Data Access Objects</a>
 * @author Stefan Rother
 *
 */
public interface FolderDAO extends GenericDAO<Folder, Long> {

	/**
	 * Returns all folders for the given parent id.
	 * @param parentId the id of the parent folder
     * @return a list of all direct child folders
	 */
	List<Folder> findAllByParentID(Long parentId);

	/**
	 * Returns the folder with the given path.
	 * @param path path in the form /folder1/folder2/...
	 * @return List of Folders in path (except root folder)
	 * @throws CinnamonException on error
	 */
	List<Folder> findAllByPath(String path);	// "getFolderByPath"

	/**
	 * Returns the folder with the given path.
	 * @param path path in the form /folder1/folder2/...
     * @param autoCreate automatically create missing folders with the parent folder's parameters.
     * @param validator A validator (may be null) to verify if a user is allowed to autocreate folders.
	 * @return List of Folders in path (except root folder)
	 * @throws CinnamonException on error
	 */
	List<Folder> findAllByPath(String path, Boolean autoCreate, Validator validator);	// "getFolderByPath"

	/**
	 * Returns the subfolders of the folder with the given id.
	 * @param parentId parent folder id
	 * @return List of folders
	 */
	List<Folder> getSubfolders(Long parentId);


   	/**
	 * Returns the subfolders of the folder with the given id.
	 * @param parentFolder - the folder whose sub-folders will be returned.
	 * @return List of folders or an empty list.
	 */
	List<Folder> getSubfolders(Folder parentFolder);
    
    /**
	 * Returns the subfolders of the folder with the given id.
	 * @param parentFolder - the folder whose sub-folders will be returned.
     * @param recursive if true, descend into sub folders
	 * @return List of folders or an empty list.
	 */
	List<Folder> getSubfolders(Folder parentFolder, Boolean recursive);

	/**
	 * Delete an empty folder specified by the id-parameter.
	 * @param id folder id
	 */
	void delete(Long id);

	/**
	 * Set name and or description of an folder.
	 * currently accepted fields
	 * <ul> 
	 * 		<li>[parentid] = new parent id</li>
	 * 		<li>[name]= new name</li>
	 * </ul>
	 * 
	 * @param id	the id of the folder (integer)
	 * @param fields map of the fields to be set
	 * @throws CinnamonException if the parent folder doesn't exist
     * @return the updated folder object
	 */
	Folder update(Long id, Map<String, String> fields);
	
	/**
	 * @see #update(Long, java.util.Map)
	 * @param folder the folder to update
	 * @param fields map of the fields to be set
     * @return the updated folder object
	 */
	Folder update(Folder folder, Map<String, String> fields);

	/**
	 * Installation-Hint<br>
	 * Create a Folder whose parent equals it's own id and whose name equals Constants.ROOT_FOLDER_NAME.
	 * This is the default folder in which objects and folders are created if no parent_id is given.
	 * @param name name of the folder
     * @param id parent id of the folder
	 * @return	rootFolder
	 */
	Folder findByNameAndParentID(String name, Long id);

	List<Folder> findAllByNameAndParentID(String name, Folder parent);
	
	/**
	 * Find all Folders where index_ok is NULL. Those are the ones
	 * whose index is not current.
	 * @param maxResults maximum number of results
	 * @return List of Folders to index (limited by maxResults).
	 */
	List<Folder> findIndexTargets(Integer maxResults);
	
	/**
	 * Set the indexed-column to 0 and trigger a re-indexing by the IndexServer.
     * @return the number of affected rows.
	 */
	Integer prepareReIndex();
	
	Folder get(String id);
	
	Folder findRootFolder();
	
	List<ObjectSystemData> getFolderContent(Folder folder, Boolean recursive);

    /**
     * Find all objects in a folder. This method can descend into sub folders and also
     * select only the newest versions of the objects it finds.
     * @param folder the folder whose content you need.
     * @param recursive if true, descend into sub folders recursively and get their content, too.
     * @param latestHead if true, return objects which have latestHead=true. If both latestHead and latestBranch are null,
     *                   return all objects. If latestHead and latestBranch are both set, return objects which satisfy one
     *                   of the criteria (where latestHead _or_ latestBranch matches the parameter).
     * @param latestBranch if true, return objects which have latestBranch=true. If both latestHead and latestBranch are null,
     *                     return all objects. If latestHead and latestBranch are both set, return objects which satisfy one
     *                     of the criteria (where latestHead _or_ latestBranch matches the parameter).
     * @return a List of the objects found in this folder, as filtered by the parameters set.
     */
    List<ObjectSystemData> getFolderContent(Folder folder, Boolean recursive, Boolean latestHead, Boolean latestBranch);

	/**
	 * Return all ancestor-Folders
	 * up to but not including the root folder.
     * @param folder the folder whose parent folders you want.
     * @return List of parent folders excluding root folder.
	 */
	List<Folder> getParentFolders(Folder folder);
	
	Folder findByPath(String path);

}
