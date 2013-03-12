package cinnamon

import cinnamon.global.Constants
import cinnamon.i18n.LocalMessage
import org.dom4j.Element

class CmnGroup implements Serializable  {

    static mapping = {
        cache true
        table 'groups'
        version 'obj_version'
        parent column: 'parent_id'
    }

    static hasMany = [groupUsers:CmnGroupUser, aclEntries:AclEntry]

    static constraints = {
        name unique: true , size: 1..Constants.NAME_LENGTH
        parent nullable: true
    }

    public static final String ALIAS_OWNER 		= "_owner";
    public static final String ALIAS_EVERYONE 	= "_everyone";
    public static final String[] defaultGroups = [ALIAS_EVERYONE, ALIAS_OWNER];

    String name
    Boolean groupOfOne = false // formerly known as is_user
    CmnGroup parent

    public CmnGroup(){

    }

    public CmnGroup(String name, Boolean is_user, CmnGroup parent){
        this.name = name;
        this.groupOfOne = is_user;
        this.parent = parent;
    }

    public Set<CmnGroup> findAncestors(){
        HashSet<CmnGroup> ancestors = new HashSet<CmnGroup>();
        while(getParent() != null && ! ancestors.contains(getParent())){
            ancestors.add(getParent());
        }
        return ancestors;
    }

    /**
     * Recursively find all Groups which are descendants of this group.
     * (this method returns when there are no more groups to be found).
     * @return Set<CmnGroup> a Set of all descendant groups.
     */
    @SuppressWarnings("unchecked")
    public Set<CmnGroup> findChildren(){
        def children = new HashSet<CmnGroup>();
        CmnGroup.findByParent(this).each{g ->
            children.add(g);
            children.addAll(g.findChildren());
        }
        return children;
    }

    public String toString(){
        return "CmnGroup #"+id+" "+name;
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof CmnGroup)) return false

        CmnGroup cmnGroup = (CmnGroup) o

        if (groupOfOne != cmnGroup.groupOfOne) return false
        if (name != cmnGroup.name) return false
        if (parent != cmnGroup.parent) return false

        return true
    }

    int hashCode() {
        int result
        result = (name != null ? name.hashCode() : 0)
        return result
    }

    public void toXmlElement(Element root) {
        Element group = root.addElement( "group" );
        group.addElement("id").addText(String.valueOf(getId()) );
        group.addElement( "name").addText(  LocalMessage.loc(getName()) );
        group.addElement( "sysName").addText(getName());
        group.addElement( "is_user").addText( groupOfOne.toString() );
        group.addElement( "parent").addText(getParent() == null ? "null" : String.valueOf(getParent().getId())  );
        Element subGroups= group.addElement("subGroups");
        for(CmnGroup g: CmnGroup.findAllByParent(this)){
            Element subGroup = subGroups.addElement("subGroup");
            subGroup.addElement("id").addText(String.valueOf(g.getId()));
            subGroup.addElement("name").addText(LocalMessage.loc(g.getName()));
            subGroup.addElement("sysName").addText(g.getName());
        }
        Element users = group.addElement("users");
        UserAccount.executeQuery("select ua from UserAccount ua where ua in (select cgu.userAccount from CmnGroupUser cgu where cgu.cmnGroup=:group)", [group:this]).each{ user ->
            users.add(UserAccount.asElement("user", user));
        }
    }
}
