package cinnamon.interfaces;

import cinnamon.Acl;
import cinnamon.Metaset;
import cinnamon.MetasetType;
import cinnamon.index.Indexable;

import java.util.Set;

/**
 */
public interface IMetasetOwner extends Indexable {

    Set<Metaset> fetchMetasets();

    /**
     * Fetch a metaset by its given name. Returns null in case there is no such metaset
     * associated with this object.
     * @param name the name of the metaset
     * @return the metaset or null
     */
    Metaset fetchMetaset(String name);

    /**
     * Fetch a metaset by its given name. Returns null in case there is no such metaset
     * associated with this object.
     * @param name the name of the metaset
     * @param autocreate whether to create the metaset if it is missing or not 
     * @return the metaset or null if autocreate is false and the metaset was not found
     */
    Metaset fetchMetaset(String name, Boolean autocreate);
    
    void addMetaset(Metaset metaset);
    Acl getAcl();
    IMetasetJoin fetchMetasetJoin(MetasetType type);
    // getId() would be better, but runs into problems with dynamically generated GORM getId() method.
    Long myId();

}
