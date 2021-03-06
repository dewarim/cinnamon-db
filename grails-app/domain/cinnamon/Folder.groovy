package cinnamon

import cinnamon.exceptions.CinnamonConfigurationException
import cinnamon.global.ConfThreadLocal
import cinnamon.index.IndexAction
import cinnamon.index.IndexJob
import cinnamon.interfaces.Accessible
import cinnamon.interfaces.Ownable
import cinnamon.index.Indexable
import cinnamon.interfaces.XmlConvertable
import cinnamon.global.Constants
import cinnamon.references.Link
import cinnamon.references.LinkType
import cinnamon.utils.ParamParser
import cinnamon.exceptions.CinnamonException
import org.dom4j.Element
import org.dom4j.Document
import org.dom4j.DocumentHelper
import cinnamon.interfaces.IMetasetOwner
import org.dom4j.Node

import cinnamon.interfaces.IMetasetJoin
import org.hibernate.Hibernate

class Folder implements Ownable, Indexable, XmlConvertable, Serializable, IMetasetOwner, Accessible {

    static def folderService
    static def metasetService
    static def linkService

    static constraints = {
        name unique: ['parent'], size: 1..Constants.NAME_LENGTH
        parent nullable: true
    }

    static mapping = {
        table 'folders'
        version 'obj_version'
    }

    String name
    UserAccount owner
    Folder parent
    FolderType type
    Acl acl
    Boolean metadataChanged = false
    String summary = '<summary />'

    def grailsApplication

    public Folder() {

    }

    /**
     * Create a new Folder, based on another.
     * Notes:
     * <ul><li>
     * The new folder has the same name and parent, so you must at least change
     * one of them as folder names must be unique inside a given folder (like in a
     * hierarchical file system).
     * </li>
     * <li>
     * This constructor does not copy the custom metadata, as that requires a Hibernate id,
     * which is generated on calling newFolder.save(). So if you want more than an empty
     * custom metadata field, you should set the metadata after saving the new instance. 
     * </li>
     * </ul>
     * @param that the folder upon which the copy is based.
     */
    public Folder(Folder that) {
        name = that.name;
        owner = that.owner;
        parent = that.parent;
        type = that.type;
        acl = that.acl;
    }

    public Folder(String name, Acl acl, Folder parent, UserAccount owner, FolderType type) {
        this.name = name;
        this.acl = acl;
        this.parent = parent;
        this.owner = owner;
        this.type = type;
    }

    public Folder(Map<String, String> cmd) {
        log.debug("Entering Folder-Constructor");

        this.name = cmd.get("name");
        this.storeMetadata(cmd.get("metadata"));
        Long parentId = ParamParser.parseLong(cmd.get("parentid"), "error.param.parent_id");

        log.debug("trying to find parent folder with id " + parentId);

        if (parentId == 0L) {
            this.parent = folderService.findRootFolder();
            log.debug("got root folder:  " + parent);
        }
        else {
            Folder myParent = Folder.get(parentId);
            if (myParent == null) { // parent not found
                throw new CinnamonException("error.parent_folder.not_found");
            }
            else {
                this.parent = myParent;
            }
        }

        // typeid is optional
        if (!cmd.containsKey("typeid")) {
            cmd.put("typeid", "0");
        }
        Long folderTypeId = ParamParser.parseLong(cmd.get("typeid"), "error.param.type_id");

        if (folderTypeId == 0L) {
            this.type = FolderType.findByName(Constants.FOLDER_TYPE_DEFAULT);
        }
        else {
            this.type = FolderType.get(folderTypeId);
        }
        log.debug("check if folder already exists.");

        // check whether folder exists in parent folder
        Folder existingFolder = Folder.findByNameAndParent(name, parent);
        if (existingFolder != null) {
            throw new CinnamonException("error.folder.exists");
        }

        log.debug("set acl for this folder");
        // Set ACL for this folder:
        if (cmd.containsKey("aclid")) {
            Long acl_id = ParamParser.parseLong(cmd.get("aclid"), "error.param.acl_id");
            this.acl = Acl.get(acl_id);
        }
        else {
            // a folder normally inherits the parent folder's acl.
            this.acl = parent.acl;
        }

        Long ownerId = ParamParser.parseLong(cmd.get("ownerid"), "error.param.owner_id");
        UserAccount myOwner = UserAccount.get(ownerId);
        if (myOwner == null) {
            throw new CinnamonException("error.user.not_found");
        }
        owner = myOwner;
    }

    Set<Folder> fetchChildren() {
        return Folder.findAll("from Folder f where f.parent=:parent", [parent: this])
    }

    /**
     * // TODO: move XML serialization to a service class? 
     * Add the folder as XML Element "folder" to the parameter Element.
     * @param root
     */
    @Override
    public Element toXmlElement(Element root, Boolean includeSummary) {
        return toXmlElement(root, [], includeSummary)
    }
    
    /**
     * // TODO: move XML serialization to a service class? 
     * Add the folder as XML Element "folder" to the parameter Element.
     * @param root
     */
    @Override
    public Element toXmlElement(Element root, List metasets, Boolean includeSummary) {
        Element folder = root.addElement("folder")
        folder.addElement("id").addText(String.valueOf(id))
        folder.addElement("name").addText(name)
        folder.add(UserAccount.asElement("owner", owner))
        folder.addElement("aclId").addText(String.valueOf(acl.id))
        folder.addElement("typeId").addText(String.valueOf(type.id))
        if (folder.getParent() != null) {
            folder.addElement("parentId").addText(String.valueOf(parent.id))
        }
        else {
            folder.addElement("parentId")
        }
        if (folderService.getSubfolders(this).isEmpty() && linkService.findLinksIn(this, LinkType.FOLDER).isEmpty()) {
            folder.addElement("hasChildren").addText("false")
        }
        else {
            folder.addElement("hasChildren").addText("true")
        }
        folder.addElement("metadataChanged").addText(String.valueOf(metadataChanged))
        def metaElement = folder.addElement('meta')
        if(metasets.size() > 0){
            metasets.each{ type ->
                def metaset = fetchMetaset(type)
                if(metaset){
                    metaElement.add(Metaset.asElement('metaset', metaset))
                }
            }
        }
        if(includeSummary){
            folder.add(ParamParser.parseXml(summary, "Failed to parse summary"))
        }
        return folder
    }

    @Override
    /*
      * Implements Comparable interface based on id.
      * This is used to generate search results with a consistent ordering (where the
      * results are not ordered by any other parameter).
      *
      */ public int compareTo(XmlConvertable o) {
        if (getId() > o.myId()) {
            return 1;
        }
        else if (getId() < o.myId()) {
            return -1;
        }
        return 0;
    }

    /**
     * Create a list of folders right up to one step below the root folder (so the
     * root folder is not returned).
     * @return List of parent folders
     */
    List<Folder> getAncestors() {
        List<Folder> folderList = new ArrayList<Folder>();
        Folder folder = this;
        while (folder.getParent() != null && folder.getParent() != folder) {
            folderList.add(folder.getParent());
            folder = folder.getParent();
        }
        return folderList;
    }

    @Override
    public String getContent(String repository) {
        /*
           * Folder.getContent could return a list of children and objects.
           * But what would be the purpose of this in a search context?
           * Is there a use case for "give me all folders which contain foo"
           * instead of searching for all objects/folders named "foo"?
           */
        return "<content/>";
    }

    @Override
    public byte[] getContentAsBytes(String repository) {
        return "<content/>".getBytes();
    }

    @Override
    public String getSystemMetadata(Boolean withRelations, Boolean withSummary, Boolean withLinks) {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("sysMeta");
        String className = getClass().getName();
        root.addAttribute("javaClass", className);
        root.addAttribute("hibernateId", String.valueOf(getId()));
        root.addAttribute("id", className + "@" + getId());
        def sysMeta = toXmlElement(root, withSummary);
        // note: Folders currently do not have relations, so the parameter is set to an empty node if required.
        if (withRelations) {
            ((Element) root.selectSingleNode('folder')).addElement('relations')
        }
        if(withLinks){
            addLinksToElement(sysMeta)
        }
        return doc.asXML();
    }

    public Element addLinksToElement(Element root) {
        def linkEl = root.addElement("links")
        Link.findAllByFolder(this).each { link ->
            linkEl.addElement("link")
                    .addAttribute("type", link.type.name())
                    .addText(link.folder.id.toString())
        }
        return root;
    }

    /**
     * Compute this folder's path, which is the combination of all parent folder's names,
     * for example: /system/workflows/templates.
     * @return the path of this folder.
     */
    public String fetchPath() {
        /*
         * getParentFolders returns: "c/b/a" for folders /a/b/c
        */
        List<Folder> folders = ancestors;
        StringBuilder path = new StringBuilder();
        Collections.reverse(folders);
        folders.add(this);
        for (Folder f : folders) {
            path.append('/');
            path.append(f.getName());
        }
        return path.toString();
    }

    /**
     * Determine if this folder is the root folder of the repository.
     * The root folder has itself as its parent.
     * @return true if this is the repository's root folder, false otherwise.
     */
    public boolean rootFolderCheck() {
        if (getParent() == null) {
            return false;
        }

        return getParent().getId() == getId();
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Folder)) return false

        Folder folder = (Folder) o

        if (acl != folder.acl) return false
        if (name != folder.name) return false
        if (owner != folder.owner) return false
        if (parent != folder.parent) return false
        if (type != folder.type) return false
        if (summary != folder.summary) return false
        if (metadataChanged != folder.metadataChanged) return false

        return true
    }

    int hashCode() {
        int result
        result = (name != null ? name.hashCode() : 0)
        result = 31 * result + (owner != null ? owner.hashCode() : 0)
        result = 31 * result + (type != null ? type.hashCode() : 0)
        result = 31 * result + (acl != null ? acl.hashCode() : 0)
        return result
    }

    /**
     * Update the fields of this folder objects from a parameter map 
     * (build from HTTP requests in FolderController.updateFolder)
     * @param fields map of the fields to be set
     * @return the updated folder object
     */
    public Folder update(Map fields) {
        if (fields.parentid) {
            Folder newParentFolder = get(fields.parentid);
            if (newParentFolder == null) {
                throw new CinnamonException("error.param.parent_id");
            }
            else if (newParentFolder.equals(this)) {
                // prevent a folder from being its own parent.
                throw new CinnamonException("error.illegal_parent_id");
            }
            else if (getParentFolders(newParentFolder).contains(this)) {
                // prevent a folder from being moved into one of its child folders.
                throw new CinnamonException("error.illegal_parent_id");
            }
            else {
                setParent(newParentFolder);
                resetIndexOnFolderContent();
            }
        }
        if (fields.ownerid) {
            UserAccount owner = UserAccount.get(fields.ownerid);
            if (owner == null) {
                throw new CinnamonException("error.user.not_found");
            }
            setOwner(owner);
        }
        if (fields.name) {
            setName(fields.name);
            resetIndexOnFolderContent();
        }
        if (fields.metadata) {
            storeMetadata(fields.metadata);
        }
        if (fields.aclid) {
            Acl acl = Acl.get(fields.aclid);
            if (acl == null) {
                throw new CinnamonException("error.param.acl_id");
            }
            setAcl(acl);

        }
        if (fields.typeid) {
            FolderType ft = FolderType.get(fields.typeid);
            if (ft == null) {
                throw new CinnamonException("error.param.type_id");
            }
            setType(ft);
        }
        return this;
    }

    /**
     * After a folder was moved, the folderPath index is invalid for all its content.
     * To correct this, all contained objects and folders must be scheduled for re-indexing. 
     * @param folder a folder who will be re-indexed along with its content (recursively).
     */
    void resetIndexOnFolderContent() {
        updateIndex()
        for (ObjectSystemData osd : fetchFolderContent(false)) {
            osd.updateIndex()
        }
        for (Folder childFolder : fetchSubfolders(true)) {
            childFolder.resetIndexOnFolderContent();
        }
    }

    /**
     * Returns the subfolders of this folder
     * @return List of folders
     */
    public List<Folder> getSubfolders() {
        return Folder.findAllByParent(this);
    }

    /**
     * Returns the subfolders of this folder.
     * @param recursive if true, descend into sub folders
     * @return List of folders or an empty list.
     */
    public List<Folder> fetchSubfolders(Boolean recursive) {
        def folders = Folder.findAllByParent(this)
        List<Folder> newFolders = new ArrayList<Folder>();
        if (recursive) {
            for (Folder folder : folders) {
                newFolders.addAll(folder.fetchSubfolders(true));
            }
        }
        folders.addAll(newFolders);
        return folders;
    }

    /**
     * Return all ancestor-Folders
     * up to but not including the root folder.
     * @param folder the folder whose parent folders you want.
     * @return List of parent folders excluding root folder.
     */
    public List<Folder> getParentFolders(Folder folder) {
        List<Folder> folders = new ArrayList<Folder>()
        folder = folder.parent
        while (folder != null && folder != folder.parent) {
            folders.add(folder)
            folder = folder.parent
        }
        return folders
    }

    /**
     * Find all objects in a folder. This method can descend into sub folders and also
     * select only the newest versions of the objects it finds.
     * @param folder the folder whose content you need.
     * @param recursive if true, descend into sub folders recursively and get their content, too.
     * @param latestHead if != null, return objects which have latestHead=true. If both latestHead and latestBranch are null,
     *                   return all objects. If latestHead and latestBranch are both set, return objects which satisfy one
     *                   of the criteria (where latestHead _or_ latestBranch matches the parameter).
     * @param latestBranch if != null, return objects which have latestBranch=true. If both latestHead and latestBranch are null,
     *                     return all objects. If latestHead and latestBranch are both set, return objects which satisfy one
     *                     of the criteria (where latestHead _or_ latestBranch matches the parameter).
     * @return a List of the objects found in this folder, as filtered by the parameters set.
     */
    public List<ObjectSystemData> fetchFolderContent(Boolean recursive, Boolean latestHead, Boolean latestBranch) {
        def osds
        if (latestHead != null && latestBranch != null) {
            osds = ObjectSystemData.findAll("from ObjectSystemData o where o.parent=:parent and (o.latestHead=:latestHead or o.latestBranch=:latestBranch)",
                    [parent: this, latestHead: true, latestBranch: true])
        }
        else if (latestHead != null) {
            osds = ObjectSystemData.findAll("from ObjectSystemData o where o.parent=:parent and o.latestHead=:latestHead",
                    [parent: this, latestHead: true]
            )
        }
        else if (latestBranch != null) {
            osds = ObjectSystemData.findAll("from ObjectSystemData o where o.parent=:parent and o.latestBranch=:latestBranch",
                    [parent: this, latestBranch: true]
            )
        }
        else {
            osds = ObjectSystemData.findAllByParent(this)
        }
        if (recursive) {
            List<Folder> subFolders = getSubfolders();
            for (Folder f : subFolders) {
                log.debug("recurse into: ${f.name}")
                osds.addAll(f.fetchFolderContent(true, latestHead, latestBranch));
            }
        }
        log.debug("folder content found: ${osds.size()}")
        return osds;
    }


    public List<ObjectSystemData> fetchFolderContent(Boolean recursive) {
        return fetchFolderContent(recursive, null, null);
    }

    /**
     * @return the compiled metadata of this element (all metasets collected under one meta root element).
     */
    public String getMetadata() {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("meta");
        for (Metaset m : metasets.collect { it.metaset }) {
            root.add(Metaset.asElement("metaset", m));
        }
        return doc.asXML();
    }

    /**
     * Set the Metadata on this folder. Tries to parse the submitted string
     * and throws an exception if it is not valid XML.<br/>
     * Note: this parses the meta-xml into metasets and stores them as such.
     * @param metadata the custom metadata
     */
    public void storeMetadata(String metadata) {
        storeMetadata(metadata, WritePolicy.BRANCH);
    }

    /**
     * Set the Metadata on this item. Tries to parse the submitted string
     * and throws an exception if it is not valid XML.<br/>
     * Note: this parses the meta-xml into metasets and stores them as such.
     * This method does not unlink existing metasets unless you submit metadata without any
     * metasets inside.
     * @param metadata the custom metadata
     * @param writePolicy the WritePolicy - how to treat existing metasets.
     */
    public void storeMetadata(String metadata, WritePolicy writePolicy) {
        if (metadata == null || metadata.trim().length() < 9) {
            log.debug("delete obsolete metasets")
            metasets.collect { it.metaset }.each { Metaset metaset ->
                metasetService.unlinkMetaset(this, metaset)
            }
        }
        else {
            Document doc = ParamParser.parseXmlToDocument(metadata, "error.param.metadata");
            List<Node> sets = doc.selectNodes("//metaset");
            if (sets.size() == 0) {
                if (metasets.size() > 0) {
                    // delete obsolete metasets:
                    for (Metaset m : fetchMetasets()) {
                        metasetService.unlinkMetaset(this, m);
                    }
                }
                return;
            }

            Set<MetasetType> currentMetasetMap = new HashSet<MetasetType>();
            fetchMetasets().each {
                // create a set of the currently existing metasets.
                currentMetasetMap.add(it.type);
            }
            for (Node metasetNode : sets) {
                String content = metasetNode.detach().asXML();
                String metasetTypeName = metasetNode.selectSingleNode("@type").getText();
                log.debug("metasetType: " + metasetTypeName);
                MetasetType metasetType = MetasetType.findByName(metasetTypeName);
                if (metasetType == null) {
                    throw new CinnamonException("error.unknown.metasetType", metasetTypeName);
                }
                metasetService.createOrUpdateMetaset(this, metasetType, content, writePolicy);
                currentMetasetMap.remove(metasetType);
            }

            currentMetasetMap.each {
                // any metaset that was not found in the metadata parameter has to be deleted.                    
                metasetService.unlinkMetaset(this, this.fetchMetaset(it.name)); // somewhat convoluted.
            }
        }
    }

    public Set fetchMetasets() {
        FolderMetaset.findAll("from FolderMetaset fm where fm.folder=:folder",
                [folder: this]).collect { it.metaset }
    }

    public Metaset fetchMetaset(String name) {
        return fetchMetaset(name, false)
    }

    public Metaset fetchMetaset(String name, Boolean autocreate) {
        Metaset metaset = null;
        MetasetType type = MetasetType.findByName(name)
        for (Metaset m : fetchMetasets()) {
            if (m.getType() == type) {
                metaset = m;
                break;
            }
        }
        if (metaset == null && autocreate) {
            metaset = metasetService.createMetaset(type, null)
        }
        return metaset;
    }

    public IMetasetJoin fetchMetasetJoin(MetasetType type) {
        // TODO: ensure that no duplicate metasets are created, then refactor this code.
        List<FolderMetaset> metasetList =FolderMetaset.findAll("from FolderMetaset o where o.metaset.type=:metasetType and o.folder=:folder",
                [folder: this, metasetType: type]);
        log.debug("query for: " + type.name + " / osd: " + id + " returned #objects: " + metasetList.size());
        if (metasetList.size() == 0) {
            return null;
        }
        else if (metasetList.size() > 1) {
            throw new CinnamonConfigurationException("Found two metasets of the same type in folder #" + getId());
        }
        else {
            def msj = metasetList[0]
            if(msj.isDirty()) {
                // removing a metaset with unpersisted changes is problematic.
                msj.save(flush:true)
            }
            return msj
        }
    }

    public void addMetaset(Metaset metaset) {
        // make sure that we do not add a second metaset of the same type:
        MetasetType metasetType = metaset.getType();
        IMetasetJoin metasetJoin = fetchMetasetJoin(metasetType);
        if (metasetJoin != null) {
//            log.debug("found existing metasetJoin: "+metasetJoin.getId());
            throw new CinnamonException("you tried to add a second metaset of type " + metasetType.getName() + " to " + getId());
        }
        FolderMetaset folderMetaset = new FolderMetaset(this, metaset);
        folderMetaset.save()
    }

    Long myId() { return id }

    public Boolean hasXmlContent() {
        return false;
    }

    public void updateIndex(){
        IndexJob indexJob = new IndexJob(this)
        indexJob.save()
    }

    def afterInsert(){
        LocalRepository.addIndexable(this, IndexAction.ADD)
        def user = ConfThreadLocal.conf.currentUser
        if (user && user.changeTracking) {
            metadataChanged = true
        }
        else {
            log.debug("no user found for changeTracking updates or user does not track changes")
        }
    }

    def afterUpdate(){
        LocalRepository.addIndexable(this, IndexAction.UPDATE)
    }

    def afterDelete(){
        LocalRepository.addIndexable(this, IndexAction.REMOVE)
    }
    
    def beforeUpdate(){
        def

                user = ConfThreadLocal.conf.currentUser
        if (user && user.changeTracking) {
            def dirtyProperties = this.dirtyPropertyNames
            if( dirtyProperties.size == 1 && dirtyProperties.contains('summary')){
                // just setting the summary does not set metadataChanged.
                return true
            }
            metadataChanged = true
        }
        else {
            log.debug("no user found for changeTracking updates or user does not track changes")
        }
    }

    @Override
    public String uniqueId() {
        String className = Hibernate.getClass(this).getName();
        return className + "@" + getId();
    }

    @Override
    public Indexable reload(){
        def folder = Folder.get(this.getId());
        log.debug("Folder.get for "+id+" returned: "+folder);
        return folder;
    }

    def Set<FolderMetaset> getMetasets(){
        FolderMetaset.findAllByFolder(this)
    }
}  
