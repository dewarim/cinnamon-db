package cinnamon

import org.dom4j.Element
import cinnamon.i18n.LocalMessage
import cinnamon.global.PermissionName
import cinnamon.global.Constants

class Permission  implements Serializable {

    public static final String[] defaultPermissions = [
        // object:
        PermissionName.WRITE_OBJECT_CONTENT, PermissionName.READ_OBJECT_CONTENT,
        PermissionName.WRITE_OBJECT_CUSTOM_METADATA, PermissionName.READ_OBJECT_CUSTOM_METADATA,
        PermissionName.WRITE_OBJECT_SYS_METADATA, PermissionName.READ_OBJECT_SYS_METADATA,
        PermissionName.VERSION_OBJECT, PermissionName.DELETE_OBJECT,
        PermissionName.BROWSE_OBJECT, PermissionName.LOCK,

        // folder:
        PermissionName.CREATE_FOLDER, PermissionName.DELETE_FOLDER, PermissionName.BROWSE_FOLDER,
        PermissionName.EDIT_FOLDER, PermissionName.CREATE_OBJECT,

        // misc:
        PermissionName.MOVE, PermissionName.SET_ACL,
        PermissionName.QUERY_CUSTOM_TABLE,
        PermissionName.CREATE_INSTANCE,
    ]


    static constraints = {
        name(size: 1..Constants.NAME_LENGTH, blank: false)
    }
    
    static mapping = {
        cache true
        table "permissions"
        version 'obj_version'
    }

    static hasMany = [aePermissions:AclEntryPermission]

    String name

    void toXmlElement(Element root){
        Element permission = root.addElement("permission");
        permission.addElement("id").addText(String.valueOf(getId()) );
        permission.addElement("name").addText(  LocalMessage.loc(getName()));
        permission.addElement("sysName").addText(getName());
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Permission)) return false

        Permission that = (Permission) o
        if (name != that.name) return false

        return true
    }

    int hashCode() {
        return (name != null ? name.hashCode() : 0)
    }
}
