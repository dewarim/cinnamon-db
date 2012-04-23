package cinnamon

class AclEntryPermission {

    static constraints = {
        aclEntry unique: ['permission']
    }

    static mapping = {
        table('aclentry_permissions')
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AclEntryPermission)) return false;

        AclEntryPermission that = (AclEntryPermission) o;

        if (aclEntry != null ? !aclEntry.equals(that.aclEntry) : that.aclentry != null) return false;
        if (permission != null ? !permission.equals(that.permission) : that.permission != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = aclEntry != null ? aclEntry.hashCode() : 0;
        result = 31 * result + (permission != null ? permission.hashCode() : 0);
        return result;
    }
}
