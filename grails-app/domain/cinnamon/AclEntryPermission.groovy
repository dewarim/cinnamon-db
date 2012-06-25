package cinnamon

class AclEntryPermission implements Serializable  {

    static constraints = {
        aclEntry unique: ['permission']
    }

    static mapping = {
        table('aclentry_permissions')
        version 'obj_version'
        aclEntry column: 'aclentry_id'
    }

    AclEntry aclEntry
    Permission permission

    public AclEntryPermission(){}

    public AclEntryPermission(AclEntry aclEntry, Permission permission) {
        this.aclEntry = aclEntry;
        this.permission = permission;

        aclEntry.getAePermissions().add(this);
        permission.getAePermissions().add(this);
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof AclEntryPermission)) return false

        AclEntryPermission that = (AclEntryPermission) o

        if (aclEntry != that.aclEntry) return false
        if (permission != that.permission) return false

        return true
    }

    int hashCode() {
        int result
        result = (aclEntry != null ? aclEntry.hashCode() : 0)
        result = 31 * result + (permission != null ? permission.hashCode() : 0)
        return result
    }
}
