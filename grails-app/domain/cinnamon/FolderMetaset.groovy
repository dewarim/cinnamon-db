package cinnamon

import cinnamon.index.IndexAction
import cinnamon.interfaces.IMetasetJoin

class FolderMetaset implements IMetasetJoin {

    static constraints = {
    }

    static mapping = {
        table('folder_metasets')
        version 'obj_version'
    }

    Folder folder
    Metaset metaset


    public FolderMetaset() {
    }

    public FolderMetaset(Folder folder, Metaset metaset) {
        this.folder = folder;
        this.metaset = metaset;
    }

    public String toString() {
        return "FolderMetaset: Folder: " + folder.getId() + " Metaset: " + metaset.getId();
    }

    void doDelete() {
        this.delete(flush: true)
        // update folder because metadata has changed:
        LocalRepository.addIndexable(folder, IndexAction.UPDATE)
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof FolderMetaset)) return false

        FolderMetaset that = (FolderMetaset) o

        if (folder != that.folder) return false
        if (metaset != that.metaset) return false

        return true
    }

    int hashCode() {
        int result
        result = (folder != null ? folder.hashCode() : 0)
        result = 31 * result + (metaset != null ? metaset.hashCode() : 0)
        return result
    }
}
