package cinnamon

import cinnamon.global.Constants
import org.dom4j.Document
import org.dom4j.DocumentHelper
import org.dom4j.Element
import cinnamon.exceptions.CinnamonException

import java.util.concurrent.ConcurrentLinkedQueue

class FolderService {

    def osdService

    /**
     * Check if a folder has content objects (meaning OSD, not sub-folders)
     * @param folder the folder to check
     * @return true if there is at least one OSD which has this folder as parent, false otherwise.
     */

    Boolean hasContent(Folder folder) {
        return ObjectSystemData.findWhere(parent:folder) != null
    }

    /**
     * @return the root folder of the repository to which the user is logged in.
     */
    Folder findRootFolder(){
        return Folder.find("from Folder as f where name=:name and f=f.parent", [name : Constants.ROOT_FOLDER_NAME])
    }

    public List<Folder> getSubfolders(Folder parent) {
        if (parent == null) {
            return [findRootFolder()];
        } else {
            return Folder.findAll("select f from Folder f where f.parent=:parent and f.parent != f order by f.name",[parent:parent])
        }
    }

    public List<Folder> getSubfolders(Folder parentFolder, Boolean recursive){
        List<Folder> folders = Folder.findAll("select f from Folder f where f.parent=:parent and f.parent != f order by f.name",[parent:parentFolder])
        List<Folder> newFolders = new ArrayList<Folder>();
        if(recursive){
            for(Folder folder : folders){
                newFolders.addAll(getSubfolders(folder, true));
            }
        }
        folders.addAll(newFolders);
        return folders;
    }


    /**
     * Turn a collection of folders into an XML document. Any exceptions encountered during
     * serialization are turned into error-Elements which contain the exception's message.
     * @param results
     * @return Document
     */
    Document generateQueryFolderResultDocument(Collection<Folder> results){
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("folders");

        for (Folder	folder : results) {
            Long id= folder.getId();
            log.debug("working on object: "+id);
            try {
                folder.toXmlElement(root);
            }
            catch(CinnamonException ex) {
                log.error("Error serializing folder: " + id + " - " + ex.getMessage());
                Element error = DocumentHelper.createElement("error").addText(ex.getLocalizedMessage());
                error.addElement("id").addText(id.toString());
                root.add(error);
            }
        }
        return doc;
    }

    /**
     *
     * @param recursive set to true if subfolders must be included in the list of OSDs.
     * @return a List of OSDs in this folder
     */
    public List<ObjectSystemData> getOSDList(Folder folder, Boolean recursive){
        return getFolderContent(folder, recursive);
    }

    public List<ObjectSystemData> getFolderContent(Folder folder, Boolean recursive){
        return getFolderContent(folder, recursive, null, null);
    }

    public List<ObjectSystemData> getFolderContent(Folder folder, Boolean recursive, Boolean latestHead, Boolean latestBranch){
        if(latestHead != null && latestBranch != null){
            q = ObjectSystemData.findAllByParent("findOsdsByParentAndLatestHeadOrLatestBranch");
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


    /**
     * Copy a source folder to a target folder. Copies recursively.
     * @param target the target folder in which the copy is created
     * @param croakOnError if true, do not continue past an error.
     * @param versions must be one of 'all', 'branch', 'head' - determines which OSDs inside the folder tree to copy.
     * @param user the user who issued the copyFolder command. This user is the new owner of the copy and he is also
     * used for permission checks.
     * @return a CopyResult object containing information about new folders and objects as well as error messages.
     */
    public CopyResult copyFolder(Folder source, Folder target,
                                 Boolean croakOnError, String versions,
                                 UserAccount user) {
        /*
         * validate read permissions on source folder
         * validate write permissions on target folder
         * create folder in target folder
         * create a CopyResult object
         * this folder has sub folders? Copy them. Add their CopyResult to the existing one.
         * this folder has OSDs? Copy them.
         *
         *
         */
        CopyResult copyResult = new CopyResult();
        Validator validator = new Validator(user);
        try{
            // we need permission to browse this folder.
            validator.validateGetFolder(source);
        }
        catch (CinnamonException ce){
            return copyResult.addFailure(source, ce);
        }

        try{
            // we need the permission to create a folder inside the target folder.
            validator.validateCreateFolder(target);

        }
        catch (CinnamonException ce){
            return copyResult.addFailure(target, ce);
        }

        Folder copy = new Folder(source);
        copy.owner = user;
        copy.parent = target;
        copy.save()
        copyResult.addFolder(copy);

        // copy child folders
        List<Folder> children = getSubfolders(source);
        for(Folder child : children){
            CopyResult cr = copyFolder(child, copy, croakOnError, versions, user);
            copyResult.addCopyResult(cr);
            if(copyResult.foundFailure() && croakOnError){
                return copyResult;
            }
        }

        // copy content
        Collection<ObjectSystemData> folderContent = ObjectSystemData.findAllByParent(source);
        log.debug("folderContent contains "+folderContent.size()+" objects.");

        if(versions.equals("all")){
            log.debug("copy all versions");
            // copy all versions
            ObjectTreeCopier otc = new ObjectTreeCopier( user, copy, validator, true);
            copyResult.addCopyResult( copyAllVersions(folderContent, otc, croakOnError));
        }
        else if(versions.equals("branch")){
            log.debug("copy newest branch objects");
            Set<ObjectSystemData> branches = new HashSet<ObjectSystemData>();
            for(ObjectSystemData osd: folderContent){
                branches.add( oDao.findLatestBranch(osd) );
            }
            copyResult.addCopyResult( createNewVersionCopies(branches, copy, validator, user, croakOnError));
        }
        else{
            log.debug("copy head of object tree");
            // the default: copy head
            Set<ObjectSystemData> headSet = new HashSet<ObjectSystemData>();
            for(ObjectSystemData head : folderContent){
                ObjectSystemData latestHead = oDao.findLatestHead(head);
                log.debug("latestHead found for "+head.getId());
                headSet.add( latestHead );
            }
            copyResult.addCopyResult( createNewVersionCopies(headSet, copy, validator, user, croakOnError));
        }
        log.debug("new folders: "+copyResult.newFolderCount());
        log.debug("new objects: "+copyResult.newObjectCount());
        return copyResult;
    }

    /**
     * Copy all versions of the objects found in a folder. This will create the complete object tree of
     * the objects, so if an object has ancestors or descendants in other folders, those will be copied, too.
     * @param folderContent the content of the folder which should be copied completely.
     * @param otc a ObjectTreeCopier which is configured with a validator and correct activeUser.
     * @param croakOnError if true, stop in case of an error and return a CopyResult which contains the events so far.
     * @return a copyResult containing a collection of all failed and successful attempts at copying the
     * folder's contents.
     */
    CopyResult copyAllVersions(Collection<ObjectSystemData> folderContent, ObjectTreeCopier otc, Boolean croakOnError){
        CopyResult copyResult = new CopyResult();

        ConcurrentLinkedQueue<ObjectSystemData> conQueue = new ConcurrentLinkedQueue<ObjectSystemData>();
        conQueue.addAll(folderContent);
        log.debug("starting to copy "+conQueue.size()+" objects");

        for (ObjectSystemData source : conQueue) {
//            otc.resetCopyResult();
            try {
                // create a full copy of the whole object tree:
                otc.createFullCopy(source);
                copyResult.addCopyResult(otc.getCopyResult());
            }
            catch (Exception ex) {
                log.debug("objectTreeCopy failed for id " + source.getId(), ex);
                // copy failed - now we have to cleanup and remove the already created copies:
                ObjectSystemData brokenCopy = otc.getCopyCache().get(source);
                if (brokenCopy != null) {
                    // we should nuke all other objects with the same root,
                    // as they won't be amendable to a copy operation either.
                    for (ObjectSystemData osd : conQueue) {
                        if (osd.getRoot().equals(brokenCopy.getRoot())) {
                            conQueue.remove(osd);
                        }
                    }

                    // recursively delete the broken object tree.
                    osdService.delete(brokenCopy.getRoot(), true, true);
                }

                log.debug("cleanup complete.");
                copyResult.addFailure(source, new CinnamonException(ex));
                if(croakOnError){
                    return copyResult;
                }
            }
        }
        return copyResult;
    }

    CopyResult createNewVersionCopies(Collection<ObjectSystemData> sources, Folder target, Validator validator,
                                      UserAccount user, Boolean croakOnError) {
        CopyResult copyResult = new CopyResult();

        for (ObjectSystemData osd : sources) {
            log.debug("trying to copy "+osd.getId());
            // check permissions:
            try {
                validator.validateCopy(osd, target);
            }
            catch (Exception ex) {
                copyResult.addFailure(osd, new CinnamonException(ex));
                if(croakOnError){
                    return copyResult;
                }
            }

            ObjectSystemData newCopy = new ObjectSystemData(osd, user);
            newCopy.setVersion("1");
            newCopy.setRoot(newCopy);
            newCopy.setParent(target);
            newCopy.save()
            osdService.copyContent(osd,newCopy);
            copyResult.addObject(newCopy);
        }
        return copyResult;
    }

     boolean folderExists(long id){
		Folder f = Folder.get(id);
		return f != null;
	}

    // TODO: looks weird. Where is this used?
    Boolean folderExists(Folder folder){
        if(folder == null){
            throw new CinnamonException("error.folder.not_found");
        }
        return folderExists(folder.id)
    }


}

