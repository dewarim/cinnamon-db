package cinnamon

import org.dom4j.Element
import cinnamon.Permission as Permission

class AclEntry implements Serializable  {

    static mapping = {
        table "aclentries"
        version 'obj_version'
    }

    static constraints = {
        acl unique: ['group']
    }

    static hasMany = [aePermissions: AclEntryPermission]
    CmnGroup group
    Acl acl

    public AclEntry(){

    }

    public AclEntry(Acl acl, CmnGroup group){
        this.acl = acl;
        this.group = group;
    }
    
    /**
     * Check all Permissions of this AclEntry if one of them matches the required permission.
     * @param permission the permission whose applicability to this AclEntry you want to check
     * @return true if a matching Permission was found, false otherwise.
     */
    public Boolean findPermission(Permission permission){
        log.debug("# of permissions in this aep: "+getAePermissions().size());
        for(AclEntryPermission aep : getAePermissions()){
            Permission p = aep.getPermission();
            // log.debug("check Permission "+p +" against "+permission);
            if(aep.getPermission().equals(permission)){
                return true;
            }
        }
        return false;
    }

    public void toXmlElement(Element root){
        Element aclEntry = root.addElement("aclEntry");
        addEntryElements(aclEntry);
    }

    public void addEntryElements(Element aclEntry){
        aclEntry.addElement("id").addText(String.valueOf(getId()));
        aclEntry.addElement("aclId").addText(String.valueOf(getAcl().getId()));
        aclEntry.addElement("groupId").addText(String.valueOf(getGroup().getId()));
    }

    public void toXmlElementWithPermissions(Element root){
        Element aclEntry = root.addElement("aclEntry");
        addEntryElements(aclEntry);
        Element permissions = aclEntry.addElement("permissions");
        Set<AclEntryPermission> aepSet = getAePermissions();
        for(AclEntryPermission aep : aepSet){
            aep.getPermission().toXmlElement(permissions);
        }
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof AclEntry)) return false

        AclEntry aclEntry = (AclEntry) o

        if (acl != aclEntry.acl) return false
        if (group != aclEntry.group) return false

        return true
    }

    int hashCode() {
        int result
        result = (group != null ? group.hashCode() : 0)
        result = 31 * result + (acl != null ? acl.hashCode() : 0)
        return result
    }


}
