package cinnamon.references

import cinnamon.Acl
import cinnamon.Folder
import cinnamon.ObjectSystemData
import cinnamon.UserAccount
import org.dom4j.DocumentHelper
import org.dom4j.Element

class Link {

    static constraints = {
        osd nullable: true
        folder nullable: true
    }

    static mapping = {
        table('links')
    }
    
    UserAccount owner
    Folder parent
    Folder folder
    ObjectSystemData osd
    Acl acl
    LinkType type
    LinkResolver resolver = LinkResolver.FIXED
    
    public Link(){}
    
    public Link(LinkType type, LinkResolver resolver, UserAccount owner, Folder parent, Folder folder, ObjectSystemData osd, Acl acl) {
        this.type = type;
        this.resolver = resolver;
        this.owner = owner;
        this.parent = parent;
        this.folder = folder;
        this.osd = osd;
        this.acl = acl;
    }

    public static Element asElement(String rootName, Link link){
        Element e = DocumentHelper.createElement(rootName);
        if(link != null){
            e.addElement("linkId").addText(link.id.toString());
            e.addElement("type").addText(link.type.name());
            e.addElement("aclId").addText(link.acl.getId().toString());
            e.addElement("ownerId").addText(link.owner.getId().toString());
            e.addElement("parentId").addText(link.parent.getId().toString());
            e.addElement("resolver").addText(link.resolver.name());
            if(link.type == LinkType.FOLDER){
                e.addElement("id").addText(link.folder.getId()?.toString());
            }
            else{
                e.addElement("id").addText(link.osd.getId()?.toString());
            }
        }
        return e;
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Link)) return false

        Link link = (Link) o

        if (acl != link.acl) return false
        if (folder != link.folder) return false
        if (osd != link.osd) return false
        if (owner != link.owner) return false
        if (parent != link.parent) return false
        if (resolver != link.resolver) return false
        if (type != link.type) return false

        return true
    }

    int hashCode() {
        int result
        result = parent.hashCode()
        result = 31 * result + (folder != null ? folder.hashCode() : 0)
        result = 31 * result + (osd != null ? osd.hashCode() : 0)
        return result
    }
}
