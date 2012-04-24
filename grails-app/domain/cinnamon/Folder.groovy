package cinnamon

import cinnamon.interfaces.Ownable
import cinnamon.index.Indexable
import cinnamon.interfaces.XmlConvertable
import cinnamon.global.Constants
import cinnamon.utils.ParamParser
import cinnamon.exceptions.CinnamonException
import org.dom4j.Element
import org.dom4j.Document
import org.dom4j.DocumentHelper
import cinnamon.global.ConfThreadLocal
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream
import org.apache.commons.compress.archivers.ArchiveStreamFactory
import org.apache.commons.compress.utils.IOUtils
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import cinnamon.exceptions.CinnamonConfigurationException

class Folder implements Ownable, Indexable, XmlConvertable {

    transient def folderService
    transient def userService

    static constraints = {
        name unique:['parent'], size: Constants.NAME_LENGTH
        metadata( size: 1..Constants.METADATA_SIZE)
        parent nullable: true
    }

    static mapping = {
        table 'folders'
    }

    String name
    String metadata = "<meta />"
    UserAccount owner
    Folder parent
    Boolean indexOk = true
    FolderType type
    Acl acl
    Date indexed = new Date()

    public Folder(){

    }

    public Folder(Folder that){
        name = that.name;
        owner = that.owner;
        parent = that.parent;
        type = that.type;
        acl = that.acl;
        indexed = null;
        indexOk = null;
        metadata = that.metadata;
    }

    // TODO: determine which constructors are really needed.
    public Folder(String name, String metadata, Acl acl, Folder parent, UserAccount owner, FolderType type){
        this.name = name;
        setMetadata(metadata);
        this.acl = acl;
        this.parent = parent;
        this.owner = owner;
        this.type = type;
    }

    public Folder(Map<String, String> cmd){
        log.debug("Entering Folder-Constructor");

        this.name = cmd.get("name");
        this.setMetadata(cmd.get("metadata"));
        Long parentId= ParamParser.parseLong(cmd.get("parentid"), "error.param.parent_id");

        log.debug("trying to find parent folder with id "+parentId);

        if(parentId == 0){
            this.parent = folderService.findRootFolder();
            log.debug("got root folder:  "+parent);
        }
        else{
            Folder myParent = Folder.get(parentId);
            if(myParent == null ){ // parent not found
                throw new CinnamonException("error.parent_folder.not_found");
            }
            else{
                this.parent = myParent;
            }
        }

        // typeid is optional
        if(! cmd.containsKey("typeid")){
            cmd.put("typeid", "0");
        }
        Long folderTypeId = ParamParser.parseLong(cmd.get("typeid"), "error.param.type_id");

        if(folderTypeId == 0){
            this.type = FolderType.findByName(Constants.FOLDER_TYPE_DEFAULT);
        }
        else{
            this.type = FolderType.get(folderTypeId);
        }
        log.debug("check if folder already exists.");

        // check whether folder exists in parent folder
        Folder existingFolder = Folder.findByNameAndParent(name, parent);
        if(existingFolder != null){
            throw new CinnamonException("error.folder.exists");
        }

        log.debug("set acl for this folder");
        // Set ACL for this folder:
        if(cmd.containsKey("aclid")){
            Long acl_id = ParamParser.parseLong(cmd.get("aclid"), "error.param.acl_id");
            this.acl = Acl.get(acl_id);
        }
        else{
            // a folder normally inherits the parent folder's acl.
            this.acl = parent.acl;
        }

        Long ownerId = ParamParser.parseLong(cmd.get("ownerid"), "error.param.owner_id");
        UserAccount myOwner = UserAccount.get(ownerId);
        if(myOwner == null){
            throw new CinnamonException("error.user.not_found");
        }
        owner = myOwner;
    }

    Set<Folder> fetchChildren(){
        return Folder.findAll("from Folder f where f.parent=:parent", [parent:this])
    }

    /**
     * Set the Metadata on this folder. Tries to parse the submitted string
     * and throws an exception if it is not valid XML.
     * @param metadata an XML metadata string
     *
     */
    void setMetadata(String metadata) {
        if(metadata == null || metadata.trim().length() == 0){
            this.metadata = "<meta/>";
        }
        else{
            ParamParser.parseXmlToDocument(metadata, "error.param.metadata");
            this.metadata = metadata;
        }
    }

    /**
     * Add the folder as XML Element "folder" to the parameter Element.
     * @param root
     */
    public void toXmlElement(Element root){
        Element folder = root.addElement("folder");
        folder.addElement("id").addText(String.valueOf(getId()) );
        folder.addElement("name").addText( getName());
        folder.add(userService.asElement("owner", getOwner()));
        folder.addElement("aclId").addText(String.valueOf(getAcl().getId()));
        folder.addElement("typeId").addText(String.valueOf(type.getId()));
        if(folder.getParent() != null){
            folder.addElement("parentId").addText(String.valueOf(getParent().getId()));
        }
        else{
            folder.addElement("parentId");
        }
        if(folderService.getSubfolders(this).isEmpty()){
            folder.addElement("hasChildren").addText("false");
        }
        else{
            folder.addElement("hasChildren").addText("true");
        }
    }

    @Override
    /*
      * Implements Comparable interface based on id.
      * This is used to generate search results with a consistent ordering (where the
      * results are not ordered by any other parameter).
      *
      */
    public int compareTo(XmlConvertable o) {
        if(getId() > o.getId()){
            return 1;
        }
        else if(getId() < o.getId()){
            return -1;
        }
        return 0;
    }

    /**
     * Create a list of folders right up to one step below the root folder (so the
     * root folder is not returned).
     * @return List of parent folders
     */
    List<Folder> getAncestors(){
        List<Folder> folderList = new ArrayList<Folder>();
        Folder folder = this;
        while(folder.getParent() != null && folder.getParent() != folder){
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
    public byte[] getContentAsBytes(String repository){
        return "<content/>".getBytes();
    }

    @Override
    public String getSystemMetadata() {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("sysMeta");
        String className = getClass().getName();
        root.addAttribute("javaClass", className);
        root.addAttribute("hibernateId", String.valueOf(getId()));
        root.addAttribute("id", className + "@" + getId());
        toXmlElement(root);
        return doc.asXML();
    }

    /**
     * Compute this folder's path, which is the combination of all parent folder's names,
     * for example: /system/workflows/templates.
     * @return the path of this folder.
     */
    public String fetchPath(){
        /*
         * getParentFolders returns: "c/b/a" for folders /a/b/c
        */
        List<Folder> folders = ancestors;
        StringBuilder path = new StringBuilder();
        Collections.reverse(folders);
        folders.add(this);
        for(Folder f : folders){
            path.append('/');
            path.append(f.getName());
        }
        return path.toString();
    }

    /**
     * Create a zipped folder containing those OSDs and subfolders (recursively) which the
     * validator allows. <br/>
     * Zip file encoding compatibility is difficult to achieve.<br/>
     * Using Cp437 as encoding will generate zip archives which can be unpacked with MS Windows XP
     * system utilities and also with the Linux unzip tool v6.0 (although the unzip tool will list them
     * as corrupted filenames with "?" in place for the special characters, it should unpack them
     * correctly). In tests, 7zip was unable to unpack those archives without messing up the filenames
     * - it requires UTF8 as encoding, as far as I can tell.<br/>
     * see: http://commons.apache.org/compress/zip.html#encoding<br/>
     * to manually test this, use: https://github.com/dewarim/GrailsBasedTesting
     * @param folderDao data access object for Folder objects
     * @param latestHead if set to true, only add objects with latestHead=true, if set to false include only
     *                   objects with latestHead=false, if set to null: include everything regardless of
     *                   latestHead status.
     * @param latestBranch if set to true, only add objects with latestBranch=true, if set to false include only
     *                   objects with latestBranch=false, if set to null: include everything regardless of
     *                     latestBranch status.
     * @param validator a Validator object which should be configured for the current user to check if access
     *                  to objects and folders inside the given folder is allowed. The content of this folder
     *                  will be filtered before it is added to the archive.
     * @return the zip archive of the given folder
     */
    public File createZippedFolder(session, Boolean latestHead, Boolean latestBranch,
                                   Validator validator){
        String repositoryName = session.repositoryName;
        final File sysTempDir = new File(System.getProperty("java.io.tmpdir"));
        File tempFolder = new File(sysTempDir, UUID.randomUUID().toString());
        if (!tempFolder.mkdirs()) {
            throw new CinnamonException(("error.create.tempFolder.fail"));
        }

        List<Folder> folders = new ArrayList<Folder>();
        folders.add(this);
        folders.addAll(folderService.getSubfolders(this, true));
        folders = validator.filterUnbrowsableFolders(folders);
        log.debug("# of folders found: "+folders.size());
        // create zip archive:
        File zipFile = null;
        try {
            zipFile = File.createTempFile("cinnamonArchive", "zip");
            final OutputStream out = new FileOutputStream(zipFile);
            ZipArchiveOutputStream zos = (ZipArchiveOutputStream) new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            String encoding = ConfThreadLocal.getConf().getField("zipFileEncoding", "Cp437");

            log.debug("current file.encoding: "+System.getProperty("file.encoding"));
            log.debug("current Encoding for ZipArchive: "+zos.getEncoding()+"; will now set: "+encoding);
            zos.setEncoding(encoding);
            zos.setFallbackToUTF8(true);
            zos.setCreateUnicodeExtraFields(ZipArchiveOutputStream.UnicodeExtraFieldPolicy.ALWAYS);

            for (Folder folder : folders) {
                String path =  folder.fetchPath().replace(fetchPath(), name); // do not include the parent folders up to root.
                log.debug("zipFolderPath: "+path);
                File currentFolder = new File(tempFolder, path);
                if (!currentFolder.mkdirs()) {
                    // log.warn("failed  to create folder for: "+currentFolder.getAbsolutePath());
                }
                List<ObjectSystemData> osds = validator.filterUnbrowsableObjects(
                       folderService.getFolderContent(folder, false, latestHead, latestBranch));
                for (ObjectSystemData osd : osds) {
                    if (osd.getContentSize() == null) {
                        continue;
                    }
                    File outFile = osd.createFilenameFromName(currentFolder);
                    // the name in the archive should be the path without the temp folder part prepended.
                    String zipEntryPath = outFile.getAbsolutePath().replace(tempFolder.getAbsolutePath(), "");
                    if(zipEntryPath.startsWith(File.separator)){
                        zipEntryPath = zipEntryPath.substring(1);
                    }
                    log.debug("zipEntryPath: "+zipEntryPath);

                    zipEntryPath = zipEntryPath.replaceAll("\\\\","/");
                    zos.putArchiveEntry(new ZipArchiveEntry(zipEntryPath));
                    IOUtils.copy(new FileInputStream(osd.getFullContentPath(repositoryName)), zos);
                    zos.closeArchiveEntry();
                }
            }
            zos.close();
        } catch (Exception e) {
            log.debug("Failed to create zipFolder:", e);
            throw new CinnamonException("error.zipFolder.fail", e.getLocalizedMessage());
        }
        return zipFile;
    }

    // TODO: rename method
    public boolean is_rootfolder(){
        if(getParent() == null){
            return false;
        }

        return getParent().getId() == getId();
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Folder)) return false

        Folder folder = (Folder) o

        if (acl != folder.acl) return false
        if (indexOk != folder.indexOk) return false
        if (indexed != folder.indexed) return false
        if (metadata != folder.metadata) return false
        if (name != folder.name) return false
        if (owner != folder.owner) return false
        if (parent != folder.parent) return false
        if (type != folder.type) return false

        return true
    }

    int hashCode() {
        int result
        result = (name != null ? name.hashCode() : 0)
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0)
        result = 31 * result + (owner != null ? owner.hashCode() : 0)
        result = 31 * result + (parent != null ? parent.hashCode() : 0)
        result = 31 * result + (indexOk != null ? indexOk.hashCode() : 0)
        result = 31 * result + (type != null ? type.hashCode() : 0)
        result = 31 * result + (acl != null ? acl.hashCode() : 0)
        result = 31 * result + (indexed != null ? indexed.hashCode() : 0)
        return result
    }



    /**
     * Update the fields of this folder objects from a parameter map (from HTTP requests)
     * @param fields map of the fields to be set
     * @return the updated folder object
     */
    public Folder update(Map<String,String> fields){
        if (fields.containsKey("parentid")) {
            Long folderId = ParamParser.parseLong(fields.get("parentid"), "error.param.parent_id");
            Folder newParentFolder = get(folderId);
            if(newParentFolder == null){
                throw new CinnamonException("error.param.parent_id");
            }
            else if(newParentFolder.equals(this)){
                // prevent a folder from being its own parent.
                throw new CinnamonException("error.illegal_parent_id");
            }
            else if(getParentFolders(newParentFolder).contains(this)){
                // prevent a folder from being moved into one of its child folders.
                throw new CinnamonException("error.illegal_parent_id");
            }
            else{
                setParent(newParentFolder);
                resetIndexOnFolderContent();
            }
        }
        if(fields.containsKey("ownerid")){
            Long ownerId = ParamParser.parseLong(fields.get("ownerid"), "error.param.owner_id");
            UserAccount owner = UserAccount.get(ownerId);
            if(owner == null){
                throw new CinnamonException("error.user.not_found");
            }
            setOwner(owner);
        }
        if( fields.containsKey("name")) {
            setName(fields.get("name"));
            resetIndexOnFolderContent();
        }
        if( fields.containsKey("metadata")){
            setMetadata(fields.get("metadata"));
        }
        if( fields.containsKey("aclid")){
            Long aclId = ParamParser.parseLong(fields.get("aclid"), "error.param.acl_id");
            Acl acl = Acl.get(aclId);
            if(acl == null){
                throw new CinnamonException("error.param.acl_id");
            }
            setAcl(acl);

        }
        if(fields.containsKey("typeid")){
            Long typeId = ParamParser.parseLong(fields.get("typeid"), "error.param.type_id");
            FolderType ft = FolderType.get(typeId);
            if(ft == null){
                throw new CinnamonException("error.param.type_id");
            }
            setType(ft);
        }
        return this;
    }

    /**
     * After a folder was moved, the folderPath index is invalid for all its content.
     * To correct this, the index_ok column is set to null so the IndexServer will
     * index each object anew.
     * @param folder a folder who will be re-indexed along with its content (recursively).
     */
    void resetIndexOnFolderContent(){
        setIndexOk(null);
        for(ObjectSystemData osd : fetchFolderContent(false)){
            osd.setIndexOk(null);
        }
        for(Folder childFolder : fetchSubfolders(true)){
            childFolder.resetIndexOnFolderContent();
        }
    }

    /**
     * Returns the subfolders of this folder
     * @return List of folders
     */
    public List<Folder> getSubfolders(){
        return Folder.findAllByParent(this);
    }

    /**
     * Returns the subfolders of this folder.
     * @param recursive if true, descend into sub folders
     * @return List of folders or an empty list.
     */
    public List<Folder> fetchSubfolders(Boolean recursive){
        def folders = Folder.findAllByParent(this)
        List<Folder> newFolders = new ArrayList<Folder>();
        if(recursive){
            for(Folder folder : folders){
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
    public List<ObjectSystemData> fetchFolderContent(Boolean recursive, Boolean latestHead, Boolean latestBranch){
        def osds
        if(latestHead != null && latestBranch != null){
            osds = ObjectSystemData.findAll("select o from ObjectSystemData o where o.parent=:parent and (o.latestHead=:latestHead or o.latestBranch=:latestBranch)",
                    [parent:this, latestHead:true,latestBranch:true])
        }
        else if(latestHead != null){
            osds = ObjectSystemData.findAll("select o from ObjectSystemData o where o.parent=:parent and o.latestHead=:latestHead",
                    [parent:this, latestHead: true]
            )
        }
        else if(latestBranch != null){
            osds = ObjectSystemData.findAll("select o from ObjectSystemData o where o.parent=:parent and o.latestBranch=:latestBranch",
                    [parent:this, latestBranch: true]
            )
        }
        else{
            osds = ObjectSystemData.findAllByParent(this)
        }
        if(recursive){
            List<Folder> subFolders = getSubfolders();
            for(Folder f : subFolders){
                osds.addAll(f.fetchFolderContent(true, latestHead, latestBranch));
            }
        }
        return osds;
    }


    public List<ObjectSystemData> fetchFolderContent(Boolean recursive){
        return fetchFolderContent(recursive, null, null);
    }

    /**
     * Installation-Hint<br>
     * Create a Folder whose parent equals it's own id and whose name is equals the ROOT_FOLDER_NAME.
     * This is the default folder in which objects and folders are created if no parent_id is given.
     *
     * @return	Folder rootFolder
     */
    public Folder findRootFolder(){
        def rootFolder = Folder.find("select f from Folder f where f.name=:name and f.parent.id=f.id",
            [name:Constants.ROOT_FOLDER_NAME]
        )
        if(! rootFolder){
            log.error("RootFolder is missing!");
            throw new CinnamonConfigurationException("Could not find the root folder. Please create a folder called "+Constants.ROOT_FOLDER_NAME
                    + " with parent_id == its own id.");
        }

        return rootFolder;
    }

    long getId(){
        return id
    }
}
