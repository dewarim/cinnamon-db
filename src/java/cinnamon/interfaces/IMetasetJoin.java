package cinnamon.interfaces;

import cinnamon.Metaset;

/**
 * Interface for classes joining metasets to other items, currently:
 * <ul>
 * <li>OsdMetaset</li>
 * <li>FolderMetaset</li>
 * </ul>
 */
public interface IMetasetJoin {

//    long getId();
    void doDelete();
    Metaset getMetaset();

}
