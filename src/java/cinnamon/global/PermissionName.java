package cinnamon.global;

/**
 *
 */
public class PermissionName {

    /**
     * Permission to lock an object. Implies permission to unlock the user's own locks.
     */
    public static final String LOCK = "_lock";

    /**
     * Permission to write to an object, that is: change its content.
     */
    public static final String WRITE_OBJECT_CONTENT = "_write_object_content";

    /**
     * Permission to read to an object's content.
     */
    public static final String READ_OBJECT_CONTENT = "_read_object_content";

    /**
     * Permission to change an object's system metadata
     */
    public static final String WRITE_OBJECT_SYS_METADATA = "_write_object_sysmeta";

    /**
     * Permission to read an object's system metadata
     */
    public static final String READ_OBJECT_SYS_METADATA = "_read_object_sysmeta";

    /**
     * Permission to change an object's custom metadata
     */
    public static final String WRITE_OBJECT_CUSTOM_METADATA = "_write_object_custom_metadata";

    /**
     * Permission to read an object's custom metadata
     */
    public static final String READ_OBJECT_CUSTOM_METADATA = "_read_object_custom_metadata";

    /**
     * Permission to create a new version of an object.
     */
    public static final String VERSION_OBJECT = "_version";

    /**
     * Permission to delete an object.
     */
    public static final String DELETE_OBJECT = "_delete_object";

    /**
     * Permission to browse an object, that is: the server will list it when
     * queried for the content of a folder or when a search for turns up this
     * object.
     */
    public static final String BROWSE_OBJECT = "_browse";

    /**
     * Permission to create a folder inside the current one.
     */
    public static final String CREATE_FOLDER = "_create_folder";

    /**
     * Permission to delete a folder.
     */
    public static final String DELETE_FOLDER = "_delete_folder";

    /**
     * Permission to create an object inside a folder.
     */
    public static final String CREATE_OBJECT = "_create_inside_folder"; // create_object_inside_folder
    /**
     * Permission to edit a folder (change name, change metadata).
     */
    public static final String EDIT_FOLDER = "_edit_folder";

    /**
     * Permission to browse this folder, that is: the folder will be
     * displayed in a list of its parent's content or may turn up
     * during a search.
     */
    public static final String BROWSE_FOLDER = "_browse_folder";

    /**
     * Permission to move an object or folder, depending on which it is set.
     */
    public static final String MOVE = "_move";

    /**
     * Permission to change an object's or folder's ACL.
     */
    public static final String SET_ACL = "_set_acl";

    /**
     * Permission to execute queries against custom tables.
     */
    public static final String QUERY_CUSTOM_TABLE = "_query_custom_table";

    /**
     * Permission to execute customized queries against the OSD-table.
     * Currently not used.
     */
    public static final String EXECUTE_QUERY = "_execute_query";

}
