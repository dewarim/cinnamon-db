package cinnamon

import cinnamon.global.Constants
import org.dom4j.Element
import cinnamon.i18n.LocalMessage

class Acl {

    static mapping = {
        table 'acls'
    }

    static constraints = {
        name unique: true, blank: false, size: 1..Constants.NAME_LENGTH
        description size: 0..Constants.DESCRIPTION_SIZE, blank: true
    }

    static hasMany = [aclEntries: AclEntry, customTables: CustomTable]
    private transient Map<UserAccount, List<AclEntry>> userEntries = new HashMap<UserAccount, List<AclEntry>>();

    String name
    String description

    public Acl() {
    }

    public Acl(Map<String, String> cmd) {
        name = cmd.get("name");
        description = cmd.get("description");
    }

    public Acl(String name, String description) {
        this.name = name;
        this.description = description;
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
        acl.addElement("description").addText( LocalMessage.loc(getDescription()));
    }

    /**
     * Cache the results of a query for the aclEntries a user has for this Acl.
     * TODO: check how this fits into recursively resolving Group.parent
     * @param user the user whose AclEntries are being queried.
     * @return a List of AclEntries to which the user belongs.
     */
    public List<AclEntry> getUserEntries(UserAccount user){
        if(! userEntries.containsKey(user)){
            def entries = AclEntry.findAll("from AclEntry ae where ae.acl=:acl and ae.group in (select g.cmnGroup from CmnGroupUser g where g.userAccount=:user)",
                    [aclEntries: this, user:user]);
            userEntries.put(user,entries);
        }
        return userEntries.get(user);
    }

    /**
     * Update Acl via map params (from HTTP requests etc)
     * @param cmd
     */
    public void update(Map<String, String> cmd) {
        if (cmd.containsKey("name")) {
            setName(cmd.get("name"));
        }
        if (cmd.containsKey("description")) {
            setDescription(cmd.get("description"));
        }
    }


    public String toString(){
        return "Acl #"+id+": "+name+" ("+description+")";
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Acl)) return false

        Acl acl = (Acl) o

        if (description != acl.description) return false
        if (name != acl.name) return false

        return true
    }

    int hashCode() {
        int result
        result = (name != null ? name.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        return result
    }


}
