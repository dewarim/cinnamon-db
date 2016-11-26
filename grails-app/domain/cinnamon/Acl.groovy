package cinnamon

import cinnamon.global.Constants
import org.dom4j.Element
import cinnamon.i18n.LocalMessage

class Acl  implements Serializable {

    static mapping = {
        cache true
        table 'acls'
        version 'obj_version'
    }

    static constraints = {
        name unique: true, blank: false, size: 1..Constants.NAME_LENGTH
    }

    static hasMany = [customTables: CustomTable]
    private transient Map<UserAccount, List<AclEntry>> userEntries = new HashMap<UserAccount, List<AclEntry>>();

    String name

    public Acl() {
    }

    public Acl(Map<String, String> cmd) {
        name = cmd["name"];
    }

    public Acl(String name) {
        this.name = name;
    }

    List<AclEntry> getAclEntries(){
        return AclEntry.findAllByAcl(this)
    }
    
    /**
     * Add the folder as XML Element "acl" to the parameter Element.
     * @param root the root element to which the acl node will be added.
     */
    public void toXmlElement(Element root){
        Element acl = root.addElement("acl");
        acl.addElement("id").addText(String.valueOf(getId()) );
        acl.addElement("name").addText( LocalMessage.loc(getName()));
        acl.addElement("sysName").addText(getName());
    }

    /**
     * Cache the results of a query for the aclEntries a user has for this Acl.
     * TODO: check how this fits into recursively resolving Group.parent
     * @param user the user whose AclEntries are being queried.
     * @return a List of AclEntries to which the user belongs.
     */
    public List<AclEntry> getUserEntries(UserAccount user){
        if(! userEntries.containsKey(user)){
            def userGroups = CmnGroupUser.findAllByUserAccount(user).collect{it.cmnGroup}
            def entries = AclEntry.findAllByAclAndGroupInList(this, userGroups)
            userEntries[user] = entries
        }
        return userEntries[user]
    }

    /**
     * Update Acl via map params (from HTTP requests etc)
     * @param cmd
     */
    public void update(Map<String, String> cmd) {
        if (cmd.containsKey("name")) {
            setName(cmd.get("name"));
        }
    }


    public String toString(){
        return "Acl #"+id+": "+name
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Acl)) return false

        Acl acl = (Acl) o

        if (name != acl.name) return false

        return true
    }

    int hashCode() {
        int result = (name != null ? name.hashCode() : 0)
        return result
    }


}
