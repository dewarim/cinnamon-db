package cinnamon.utils

import org.dom4j.Document
import org.dom4j.Element
import org.dom4j.Node
import cinnamon.Folder
import cinnamon.ObjectSystemData

/**
 * Store information about the files contained in a zipped folder.
 */
class ZippedFolder {
    
    Folder rootFolder;
    File zipFile;
    Set<Folder> folders = new HashSet<Folder>();
    Set<ObjectSystemData> osds =new HashSet<ObjectSystemData>();

    public ZippedFolder() {
    }

    public ZippedFolder(Folder rootFolder) {
        this.rootFolder = rootFolder;
        addToFolders(rootFolder);
    }

    public ZippedFolder(File zipFile) {
        this.zipFile = zipFile;
    }

    public ZippedFolder(File zipFile, Folder rootFolder) {
        this.rootFolder = rootFolder;
        addToFolders(rootFolder);
        this.zipFile = zipFile;
    }

    public Boolean addToFolders(Folder folder){
        return folders.add(folder);
    }

    public Boolean addToObjects(ObjectSystemData osd){
        return osds.add(osd);
    }

    public File getZipFile() {
        return zipFile;
    }

    public void setZipFile(File zipFile) {
        this.zipFile = zipFile;
    }

    public Set<Folder> getFolders() {
        return folders;
    }

    public void setFolders(Set<Folder> folders) {
        this.folders = folders;
    }

    public Set<ObjectSystemData> getOsds() {
        return osds;
    }

    public void setOsds(Set<ObjectSystemData> osds) {
        this.osds = osds;
    }

    /**
     * Generate a detailed content list of this zip file as a XML-String.
     * @return a String which contains the serialized OSDs which were added to this zip file.
     */
    public String generateContentList(){
        return generateContentListAsDocument().asXML();
    }

    /**
     * Generate a detailed content list of this document as an XML document.
     * @return a dom4j-Document which contains the XML serializations of the OSDs inside this zip file. The format is:
     * <pre>
     * {@code
     *  <zipContent>
     *      <object><id>1</id>...</object>
     *      <object><id>2</id>...</object>
     *  </zipContent>
     * }
     * </pre>
     */
    public Document generateContentListAsDocument(){
        Document doc = ParamParser.parseXmlToDocument("<zipContent/>");
        Element root = doc.getRootElement();
        for(ObjectSystemData osd : osds){
            osd.toXmlElement(root);
        }
        return doc;
    }

    /**
     * Add the XML content list of this zip file to an OSD. This is added as a metaset with type "zipContent".
     * If a metaset of this type already exists, it will be replaced. The metaset contains a list of XML-serialized
     * OSDs (which represents the OSD's metadata, not the content).
     * <pre>
     *     {@code
     *     <metaset type="zipContent">
     *      <object><id>1</id>...</object>
     *      ...
     *      <object><id>999</id>...</object>
     *     </metaset>
     *     }
     * </pre>
     * @param osd the osd which will store the new metaset.
     */
    public void addContentListToMetadata(ObjectSystemData osd){
        Document meta = ParamParser.parseXmlToDocument(osd.getMetadata());
        Node zipContentNode = meta.selectSingleNode("/metaset[@type='zipContent']");
        Element zipMetaset;
        if(zipContentNode != null){
            zipContentNode.detach(); // remove old contentSet
        }
        zipMetaset = meta.getRootElement().addElement("metaset");
        zipMetaset.addAttribute("type","zipContent");
        for(ObjectSystemData contentOsd : osds){
            contentOsd.toXmlElement(zipMetaset);
        }
        osd.setMetadata(meta.asXML());
    }
}
