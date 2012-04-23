package cinnamon

import cinnamon.index.Indexable
import cinnamon.interfaces.Ownable
import cinnamon.interfaces.XmlConvertable
import cinnamon.i18n.Language
import cinnamon.lifecycle.LifeCycleState
import cinnamon.global.Constants
import cinnamon.exceptions.CinnamonException
import cinnamon.utils.ParamParser
import org.dom4j.Document
import org.dom4j.DocumentHelper
import javax.persistence.EntityManager
import org.dom4j.Element
import cinnamon.global.Conf
import cinnamon.global.ConfThreadLocal
import cinnamon.utils.FileKeeper
import javax.persistence.NoResultException

class ObjectSystemData  implements Serializable, Ownable, Indexable, XmlConvertable{

    def folderService
    def userService

    static constraints = {
        contentPath(size: 0..255, nullable: true)
        contentSize(nullable: true)
        name(name:1..Constants.NAME_LENGTH)
        indexOk(nullable: true)
        predecessor(nullable: true)
        root(nullable: true) // TODO: can we switch to OSD.root:non-nullable?
        locker(nullable: true)
        format(nullable: true)
        appName(size: 0..255, blank: true)
        metadata(size: 1..Constants.METADATA_SIZE)
        procstate(size: 0..128, blank: true)
        version(size: 1..128)
        state(nullable: true)
    }

    static mapping = {
        table('objects')
    }

    String name
    String contentPath
    Long contentSize
    Boolean indexOk
    ObjectSystemData predecessor
    ObjectSystemData root
    UserAccount creator
    UserAccount modifier
    UserAccount owner
    UserAccount locker
    Date created = new Date()
    Date indexed = null
    Date modified = new Date()
    Language language
    Acl acl
    Folder parent
    Format format
    ObjectType type
    String appName = ''
    String metadata = '<meta />'
    String procstate = ''
    Boolean latestHead
    Boolean latestBranch = true
    String version = '1'
    LifeCycleState state


    public ObjectSystemData() {

    }

    /**
     * Create an OSD with all default settings and version 1.
     * Caller must only supply three things:
     * 1. the name;
     * 2. the user who is going to be owner, modifier and creator;
     * 3) the target folder wherein to create the object.
     * Everything else is set to default and can be modified after instantiation.
     * @param name  the name of the object.
     * @param user the user who is to be the creator, owner and modifier.
     * @param parentFolder the folder where the object will be created. The object will
     *                     inherit this folder's ACL by default.
     */
    public ObjectSystemData(String name, UserAccount user, Folder parentFolder){
        this.name = name;
        latestHead = true;

        log.debug("set root");
        setRoot(this);

        log.debug("set default objectType");
        type = ObjectType.findByName(Constants.OBJTYPE_DEFAULT);

        log.debug("set parentfolder");
        parent = parentFolder;

        log.debug("set acl to parent-folder's acl");
        setAcl(getParent().getAcl());
        procstate = Constants.PS_LABEL_CREATED;

        log.debug("set language to 'und' (undetermined)");
        Language lang = Language.findByIsoCode("und");
        setLanguage(lang);

        log.debug("set owner, modifier, creator to " + user.getName());
        setOwner(user);
        setModifier(user);
        setCreator(user);
    }

    public ObjectSystemData(Map<String, Object> cmd, UserAccount user, boolean initFromPredecessor) {
        log.debug("predecessor-init");
        if (cmd.containsKey("preid")) {
            Long preId = ParamParser.parseLong((String) cmd.get("preid"), "error.param.pre_id");
            ObjectSystemData pred = ObjectSystemData.get(preId);
            if (pred == null) {
                throw new CinnamonException("error.predecessor.not_found");
            }

            if (initFromPredecessor) {
                name = pred.getName();
                parent = pred.getParent();
                metadata = pred.getMetadata();
                appName = pred.getAppName();
                language = pred.getLanguage();
                if(pred.getState() != null){
                    state = pred.getState().getLifeCycleStateForCopy();
                }
            }
            this.predecessor = pred;
            /*
                * because sys is now the latest obj in the branch,
                * its predecessor has to loose the latestBranch flag.
                */
            predecessor.setLatestBranch(false);
            predecessor.setLatestHead(false);
//			log.debug("flushing after setting predecessor.branches");

        }

        log.debug("set version label");
        version = createNewVersionLabel();
        log.debug("new version: " + version);
        latestHead = !version.contains(".");

        log.debug("set root");
        if (predecessor == null) {
            setRoot(this);
        }
        else {
            setRoot(predecessor.getRoot());
        }

        log.debug("set name,appname,metadata");
        if (cmd.containsKey("name")) {
            setName((String) cmd.get("name"));
        }
        if (cmd.containsKey("appname")) {
            setAppName((String) cmd.get("appname"));
        }
        if (cmd.containsKey("metadata")) {
            setMetadata((String) cmd.get("metadata"));
        }

        /*
           * Set ObjectType:
           * 1. by objtype_id
           * 2. by objtype string
           * 3. by predecessor
           * 4. default_object_type.
           */
        log.debug("set objectType");
        if (cmd.containsKey("objtype_id")) {

            Long otId = ParamParser.parseLong((String) cmd.get("objtype_id"), "error.param.objtype_id");
            this.type = ObjectType.get(otId);
        }
        else if (cmd.containsKey("objtype")) {
            ObjectType objectType = ObjectType.findByName((String) cmd.get("objtype"));
            if (objectType == null) {
                throw new CinnamonException("error.param.objtype");
            }
            else {
                this.type = objectType;
            }
        }
        else if (predecessor != null) {
            this.type = predecessor.getType();
        }
        else {
            this.type = ObjectType.findByName(Constants.OBJTYPE_DEFAULT);
        }

        log.debug("set parentfolder");
        if (cmd.containsKey("parentid")) {
            Long parent_id = Long.parseLong((String) cmd.get("parentid"));
            if (parent_id != 0) {
                parent = Folder.get(parent_id);
                if (parent != null) {
                    setParent(parent);
                }
                else {
                    throw new CinnamonException("error.parent_folder.not_found");
                }

            }
            else { // parent_id == 0
                Folder rootFolder = folderService.findRootFolder();
                setParent(rootFolder);
            }
        }
        else if (parent == null) {
            // note: parent may be set from predecessor.
            throw new CinnamonException("error.parent_folder.not_found");
        }

        log.debug("set format");
        if (cmd.containsKey("format_id")) {
            Long formatId = ParamParser.parseLong((String) cmd.get("format_id"), "error.param.format_id");
            Format format = Format.get(formatId);
            setFormat(format);
        }
        else if (cmd.containsKey("format")) {
            Format format = Format.findByName((String) cmd.get("format"));
            setFormat(format);
        }


        log.debug("set acl");
        if (cmd.containsKey("acl_id")) {
            Long aclId = ParamParser.parseLong((String) cmd.get("acl_id"), "error.param.acl_id");
            Acl acl = Acl.get(aclId);
            setAcl(acl);
        }
        else {
            // if no sepcific acl is given, use the parent folder's acl
            log.debug("set acl to parent-folder's acl");
            setAcl(getParent().getAcl());
        }

        procstate = Constants.PS_LABEL_CREATED;

        /*
           * Set language to language_id or to 'und' if language is null.
           */
        log.debug("set language");
        if (cmd.containsKey("language_id")) {
            Long langId = ParamParser.parseLong((String) cmd.get("language_id"),
                    "error.param.language_id");
            Language lang = Language.get(langId);
            if (lang == null) {
                throw new CinnamonException("error.param.language_id");
            }
            setLanguage(lang);
        }
        else if (getLanguage() == null) {
            Language lang = Language.findByIsoCode("und");
            setLanguage(lang);
        }

        log.debug("set owner, modifier, creator to " + user.getName());
        setOwner(user);
        setModifier(user);
        setCreator(user);
    }

    /**
     * Create a new OSD based upon "that". The lifecycle state (if not null) is set to the default lifecycle state.
     * Root, predecessor, format and locked_by are set to null,
     * version is also 0. You MUST set those to the correct values.
     *
     * @param that the source OSD
     * @param user the active user who will be set as creator / modifier.
     */
    public ObjectSystemData(ObjectSystemData that, UserAccount user) {
        acl = that.getAcl();
        appName = that.getAppName();
        created = Calendar.getInstance().getTime();
        creator = user;
        owner = user;
        modified = Calendar.getInstance().getTime();
        modifier = user;
//        format = that.getFormat();
        language = that.getLanguage();
        latestHead = that.getLatestHead();
        latestBranch = that.getLatestBranch();
        locker = null;
        metadata = that.getMetadata();
        name = that.getName();
        parent = that.getParent();
        predecessor = null;
        procstate = that.getProcstate();
        type = that.getType();
        version = "0";

        if (that.getState() != null) {
            state = that.getState().getLifeCycleStateForCopy();
        }
    }

    /**
     * Set the Metadata on this object. Tries to parse the submitted string
     * and throws an exception if it is not valid XML.
     *
     * @param metadata the custom metadata
     */
    public void setMetadata(String metadata) {
        if (metadata == null || metadata.trim().length() == 0) {
            metadata = "<meta/>";
        }
        else {
            ParamParser.parseXmlToDocument(metadata, "error.param.metadata");
        }
        this.metadata = metadata;
    }
    /**
     * createClone: create a copy with created and modified set to current date, <br>
     * and without an Id (which should be set by the persistence layer when inserting into  the db).
     * @return the cloned OSD, not yet persisted to the database. Note: this clone lacks the relations
     * of the original. If you need the relations, you should use original.copyRelations(clone).
     */
    public ObjectSystemData createClone() {
        ObjectSystemData twin = new ObjectSystemData();

        twin.setAcl(acl);
        twin.setAppName(appName);
        twin.setContentPath(contentPath);
        twin.setContentSize(contentSize);
        twin.setCreated(Calendar.getInstance().getTime());
        twin.setCreator(creator);
        twin.setFormat(format);
        //		twin.setIndexed(null); // must be "now()", or IndexServer will do bad things.
        twin.setLanguage(language);
        twin.setLatestBranch(latestBranch);
        twin.setLatestHead(latestHead);
        twin.setLocker(locker)
        twin.setMetadata(metadata)
        twin.setModified(Calendar.getInstance().getTime())
        twin.setModifier(modifier)
        twin.setName(name)
        twin.setParent(parent)
        twin.setPredecessor(predecessor)
        twin.setProcstate(procstate)
        twin.setRoot(root)
        twin.setType(type)
        twin.setVersion(version)
        if (state != null) {
            twin.setState(state.lifeCycleStateForCopy);
        }
        return twin;
    }

    public void updateAccess(UserAccount user) {
        setModifier(user);
        setModified(Calendar.getInstance().getTime());
    }

    public Document toXML() {
        Document doc = DocumentHelper.createDocument();
        doc.add(convert2domElement());

        return doc;
    }

    /**
     * Delete the file content of this Data Object.
     * @param repository the name of the repository that contains the file.
     */
    public void deleteContent(String repository) {
        Conf conf = ConfThreadLocal.getConf();
        String contentPath = getContentPath();
        if (contentPath != null && contentPath.length() > 0) {
            File contentFile = new File(conf.getDataRoot() + File.separator +
                    repository + File.separator + contentPath);
            log.debug("deleteContent: " + contentFile.getAbsolutePath());

            if (contentFile.exists()) {
                log.debug("content exists, setting file up for later deletion.");
                FileKeeper fileKeeper = FileKeeper.getInstance();
                fileKeeper.addFileForDeletion(contentFile);
            }
            else {
                log.warn("content file " + contentFile.getAbsolutePath() + "does not exist.");
            }
            setContentSize(null);
            setContentPath(null);
            setFormat(null);
        }
    }

    public void setContentPathAndFormat(String contentPath, String formatName, String repositoryName) {
        Format newFormat = Format.findByName(formatName);
        setFormat(newFormat);
        setContentPathAndFormat(contentPath, newFormat, repositoryName);
    }

    public void setContentPathAndFormat(String contentPath, Format newFormat, String repositoryName) {
        setContentPath(contentPath, repositoryName); // side effect: will set contentSize, if valid.
        setFormat(newFormat);
    }

    public void toXmlElement(Element root) {
        root.add(convert2domElement());
    }

    public Element convert2domElement() {
        Element data = DocumentHelper.createElement("object");
        data.addElement("id").addText(String.valueOf(getId()));
        data.addElement("name").addText(getName());
        data.addElement("version").addText(getVersion());
        data.addElement("created").addText(ParamParser.dateToIsoString(getCreated()));
        data.addElement("modified").addText(ParamParser.dateToIsoString(getModified()));
        data.addElement("procstate").addText(getProcstate());
        data.addElement("aclId").addText(String.valueOf(getAcl().getId()));

        data.addElement("appName").addText(getAppName());
        data.addElement("latestHead").addText(getLatestHead().toString());
        data.addElement("latestBranch").addText(getLatestBranch().toString());

        log.debug("UserAsElementSection");
        data.add(userService.asElement("lockedBy", locker));
        data.add(userService.asElement("owner", owner));
        data.add(userService.asElement("creator", creator));
        data.add(userService.asElement("modifier", modifier));

        log.debug("FormatAsElement");
        data.add(Format.asElement("format", getFormat()));
        log.debug("ObjectTypeAsElement");
        data.add(ObjectType.asElement("objectType", getType()));

        log.debug("nullChecks");
        if (getContentSize() != null) {
            data.addElement("contentsize").addText(String.valueOf(getContentSize()));
        }
        else {
            data.addElement("contentsize");
        }

        if (getParent() != null) {
            data.addElement("parentId").addText(String.valueOf(getParent().getId()));
        }
        else {
            data.addElement("parentId");
        }

        if (getPredecessor() != null) {
            data.addElement("predecessorId").addText(String.valueOf(getPredecessor().getId()));
        }
        else {
            data.addElement("predecessorId");
        }

        if (getRoot() != null) {
            data.addElement("rootId").addText(String.valueOf(getRoot().getId()));
        }
        else {
            data.addElement("rootId"); // TODO: prevent rootId==null on the db level.
        }

        log.debug("languageSection");
        Element lang = data.addElement("language");
        lang.addElement("id").addText(String.valueOf(getLanguage().getId()));
        lang.addElement("isoCode").addText(getLanguage().getIsoCode());

        log.debug("lifecycleSection");
        if (getState() == null) {
            data.addElement("lifeCycleState");
        }
        else {
            data.addElement("lifeCycleState").addText(String.valueOf(state.getId()));
        }

        return data;
    }

    /**
     * Read the content of the OSD and return it as XML for indexing.
     * @see cinnamon.index.Indexable
     * @param repository The repository where the indexable object is located.
     * @return A string containing the XML version of this object's content.
     */
    public String getContent(String repository) {
        return getContent(repository, null);
    }

    /**
     * @see temp.data.ObjectSystemData#getContent(String)
     * @param repository the name of the repository where this object is stored.
     * @param encoding the encoding of the content. May be null.
     * @return a string containing the content, in XML format if possible. If the contentSize is null,
     * an empty content-element is returned.
     */
    public String getContent(String repository, String encoding) {
        if (contentSize == null) {
            return "<content/>";
        }
        String fileContent;
        try {
            fileContent = ContentReader.readFileAsString(
                    getFullContentPath(repository), encoding
            );
        } catch (Exception e) {
            throw new CinnamonException(e);
        }
        return fileContent;
    }

    public byte[] getContentAsBytes(String repository){
        byte[] fileContent;
        try {
            String path = getFullContentPath(repository);
            if(path == null){
                return "<empty />".getBytes();
            }
            log.debug("path to file: "+path);
            fileContent = utils.ContentReader.readFileAsBytes(path);
        } catch (Exception e) {
            throw new CinnamonException(e);
        }
        return fileContent;
    }

    @Override
    public String getSystemMetadata() {
        log.debug("getsystemMeta");
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("sysMeta");
        String className = getClass().getName();
        root.addAttribute("javaClass", className);
        root.addAttribute("hibernateId", String.valueOf(getId()));
        root.addAttribute("id", className + "@" + getId()); // for a given repository, it's unique.
        log.debug("convert2domElement");
        root.add(convert2domElement());
        return doc.asXML();
    }

    /**
     * @param repositoryName the name of the repository where the object is stored.
     * @return the complete path to this OSD's content - or null, if no content exists.
     */
    public String getFullContentPath(String repositoryName) {
        Conf conf = ConfThreadLocal.getConf();
        if (contentPath == null) {
//            log.debug("ContentPath is null");
            return null;
        }
        String fullContentPath = conf.getDataRoot() + File.separator +
                repositoryName + File.separator + getContentPath();
//        log.debug("fullContentPath: "+fullContentPath);
        return fullContentPath;
    }

    /**
     * Set the index status, true means indexing was completed successfully, false means
     * indexing has failed. If indexOk is null, this object has not been indexed yet (or
     * it is scheduled to be indexed as soon as possible, provided the IndexServer is running,
     * which can be configured by setting {@code <startIndexServer>true</startIndexServer>}
     * in the cinnamon_config.xml).
     *
     * @param indexOk the index status
     */
    public void setIndexOk(Boolean indexOk) {
        this.indexOk = indexOk;
    }


    @SuppressWarnings("unchecked")
    public String createNewVersionLabel() {

        if (getPredecessor() == null) {
            return "1";
        }

        String predecessorVersion = getPredecessor().getVersion();
        String[] branches = predecessorVersion.split("\\.");
        String lastSegment = branches[branches.length - 1];
        String[] lastBranch = lastSegment.split("-");

        String lastDescendantVersion;
        try {
            log.debug("query for predecessor " + getPredecessor().getId());
            List<ObjectSystemData> versions = ObjectSystemData.findAll("from ObjectSystemData o where o.predecessor=:pred order by id desc",[pred:predecessor])
            ObjectSystemData lastDescendant;
            if (versions.size() == 0) {
                throw new NoResultException()
            }
            else {
                lastDescendant = versions.get(0);
            }
            lastDescendantVersion = lastDescendant.getVersion();
        } catch (NoResultException e) {
            // no object with same predecessor
            log.debug("no result for last-descendant-query");
            String buffer = lastBranch.length == 2 ? lastBranch[1] : lastBranch[0];
            String stem = predecessorVersion.substring(0, predecessorVersion.length() - buffer.length());
            buffer = String.valueOf(Integer.parseInt(buffer) + 1);
            return stem + buffer;
        }
        log.debug("lastDescendant: " + lastDescendantVersion);
        String[] lastDescBranches = lastDescendantVersion.split("\\.");
        if (branches.length == lastDescBranches.length) {
            // last descendant is the only one so far: create first branch
            return predecessorVersion + ".1-1";
        }
        String buffer = lastDescBranches[lastDescBranches.length - 1].split("-")[0];
        buffer = String.valueOf(Integer.parseInt(buffer) + 1);
        return predecessorVersion + "." + buffer + "-1";
    }
    @Override
    /*
      * Implements Comparable interface based on id.
      * This is used to generate search results with a consistent ordering (where the
      * results are not ordered by any other parameter).
      */
    public int compareTo(XmlConvertable o) {
        if (getId() > o.getId()) {
            return 1;
        }
        else if (getId() < o.getId()) {
            return -1;
        }
        return 0;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ObjectSystemData)) return false;

        ObjectSystemData that = (ObjectSystemData) o;

        if (acl != null ? !acl.equals(that.acl) : that.acl != null) return false;
        if (appName != null ? !appName.equals(that.appName) : that.appName != null) return false;
        if (contentPath != null ? !contentPath.equals(that.contentPath) : that.contentPath != null) return false;
        if (contentSize != null ? !contentSize.equals(that.contentSize) : that.contentSize != null) return false;
        if (created != null ? !created.equals(that.created) : that.created != null) return false;
        if (creator != null ? !creator.equals(that.creator) : that.creator != null) return false;
        if (format != null ? !format.equals(that.format) : that.format != null) return false;
        if (language != null ? !language.equals(that.language) : that.language != null) return false;
        if (latestBranch != null ? !latestBranch.equals(that.latestBranch) : that.latestBranch != null)
            return false;
        if (latestHead != null ? !latestHead.equals(that.latestHead) : that.latestHead != null) return false;
        if (locker != null ? !locker.equals(that.locker) : that.locker != null) return false;
        if (metadata != null ? !metadata.equals(that.metadata) : that.metadata != null) return false;
        if (modified != null ? !modified.equals(that.modified) : that.modified != null) return false;
        if (modifier != null ? !modifier.equals(that.modifier) : that.modifier != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (owner != null ? !owner.equals(that.owner) : that.owner != null) return false;
        if (parent != null ? !parent.equals(that.parent) : that.parent != null) return false;
        if (predecessor != null ? !predecessor.equals(that.predecessor) : that.predecessor != null) return false;
        if (procstate != null ? !procstate.equals(that.procstate) : that.procstate != null) return false;
        if (root != null ? !root.equals(that.root) : that.root != null) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }

    /**
     * Get a new filename for the given osd.
     * First, it filters potentially harmful characters from the object's name. Then,
     * if a file with osd.name already exists, it will try
     * to create increasingly specific filenames, by using the version and then the id to differentiate this
     * filename from other existing files. This method is intended for use when yours is the only thread actually
     * working in this folder. It is <em>not</em> to be used for generic folders like java.io.tmpdir to construct
     * "unique" filenames (where you may encounter security problems due to race conditions).
     * If all fails, create a filename $name_$version_$id_$uuid.$format where uuid is a unique hex string.
     *
     * @param path the path where the new File will be created.
     * @return a filename which is unique inside this path.
     */
    public File createFilenameFromName(File path) {
        String extension = "";
        if(format != null){
            // the format *should* be set, but you never know. Perhaps someone created a 0-byte lock file without format.
            extension = "."+format.getExtension();
        }
        name = name.replaceAll("[^\\w]", "_");
        File file = new File(path, name + extension);
        if (file.exists()) {
            name = name + "_" + getVersion();
            file = new File(path, name + extension);
            if (file.exists()) {
                name = name + "_" + getId();
                file = new File(path, name + extension);
                if (file.exists()) {
                    // "Wir können auch anders."
                    return new File(path, name + "_" + UUID.randomUUID().toString() + extension);
                }
            }
        }
        return file;
    }

    String fetchVersionPredicate(String versions) {
        String versionPred;
        if (versions == null || versions.length() == 0 || versions.equals("head")) {
            versionPred = " and latesthead=true";
        }
        else if (versions.equals("all")) {
            versionPred = "";
        }
        else if (versions.equals("branch")) {
            versionPred = " and latestbranch=true";
        }
        else {
            throw new CinnamonException("error.param.version");
        }
        return versionPred;
    }

}
