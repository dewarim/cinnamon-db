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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Folder)) return false;

        Folder folder = (Folder) o;

        if (acl != null ? !acl.equals(folder.acl) : folder.acl != null) return false;
        if (metadata != null ? !metadata.equals(folder.metadata) : folder.metadata != null) return false;
        if (name != null ? !name.equals(folder.name) : folder.name != null) return false;
        if (owner != null ? !owner.equals(folder.owner) : folder.owner != null) return false;
        if (parent != null ? !parent.equals(folder.parent) : folder.parent != null) return false;
        if (type != null ? !type.equals(folder.type) : folder.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
    }
}
