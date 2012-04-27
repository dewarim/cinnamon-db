package cinnamon

import cinnamon.global.Constants

class CmnGroup implements Serializable  {

    static mapping = {
        table 'groups'
        version 'obj_version'
        parent column: 'parent_id'
    }

    static hasMany = [groupUsers:CmnGroupUser, aclEntries:AclEntry]

    static constraints = {
        name unique: true , size: 1..Constants.NAME_LENGTH
        description size:  0..Constants.DESCRIPTION_SIZE, blank: true
        parent nullable: true
    }

    public static final String ALIAS_OWNER 		= "_owner";
    public static final String ALIAS_EVERYONE 	= "_everyone";
    public static final String[] defaultGroups = [ALIAS_EVERYONE, ALIAS_OWNER];

    String name
    String description
    Boolean groupOfOne = false // formerly known as is_user
    CmnGroup parent

    public CmnGroup(){

    }

    public CmnGroup(Map<String, String> cmd){
        name = cmd.get("name");
        description = cmd.get("description");
        groupOfOne = Boolean.parseBoolean(cmd.get("is_user"));
        parent = null; // TODO: extend API to set parent here.
    }

    public CmnGroup(String name, String description, Boolean is_user, CmnGroup parent){
        this.name = name;
        this.description = description;
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

        if (description != cmnGroup.description) return false
        if (groupOfOne != cmnGroup.groupOfOne) return false
        if (name != cmnGroup.name) return false
        if (parent != cmnGroup.parent) return false

        return true
    }

    int hashCode() {
        int result
        result = (name != null ? name.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (groupOfOne != null ? groupOfOne.hashCode() : 0)
        result = 31 * result + (parent != null ? parent.hashCode() : 0)
        return result
    }
}
