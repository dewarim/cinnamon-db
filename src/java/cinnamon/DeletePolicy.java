package cinnamon;

/**
 * Delete policy for metasets.
 */
public enum DeletePolicy {

    /**
     * If other items reference this metaset, delete all of those references and then
     * delete the metaset. Throw an exception if the action is forbidden by ACL access rules.
     */
    COMPLETE,

    /**
     * If other items reference this metaset, delete all of those references as long
     * as the ACL on those items allows it. Only delete the metaset if it's no longer
     * referenced.
     */
    ALLOWED

}
