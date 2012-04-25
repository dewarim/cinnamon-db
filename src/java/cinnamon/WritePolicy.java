package cinnamon;

/**
 *
 * Write policy for metadata.
 *
 */
public enum WritePolicy {

    /**
     * If the metaset exists, overwrite its content.
     */
    WRITE,

    /**
     * If the metaset exists, keep its content and ignore the new set.
     */
    IGNORE,

    /**
     * If the metaset already exists and is referenced by other items,
     * create a new, separate metaset for the current item and set the new content on that one.
     */
    BRANCH

}
