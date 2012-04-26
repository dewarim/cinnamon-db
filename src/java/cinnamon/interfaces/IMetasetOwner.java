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
    Metaset fetchMetaset(String name);
    void addMetaset(Metaset metaset);
    Acl getAcl();
    IMetasetJoin fetchMetasetJoin(MetasetType type);
    // getId() would be better, but runs into problems with dynamically generated GORM getId() method.
    Long myId();

}
