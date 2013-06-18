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
        metaset.addToFolderMetasets(this);
        folder.addToMetasets(this);
    }

    public String toString() {
        return "FolderMetaset: Folder: " + folder.getId() + " Metaset: " + metaset.getId();
    }

    public void doDelete() {
        /*
         * this rather convoluted method of removing the FolderMetaset from the collection
         * is due to the fact that if the folder or metaset object isDirty(),
         * the folderMetasets/metasets set may not find the FM due to changed hashCode().
         */
        metaset.folderMetasets.remove(
                metaset.folderMetasets.find { it.id == this.id } ?: this 
                // ?:this: to prevent "remove null"
        )
        folder.metasets.remove(folder.metasets.find { it.id == this.id } ?: this)
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
