package cinnamon

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

    public void doDelete(){
        metaset.getFolderMetasets().remove(this);
        folder.getMetasets().remove(this);
        this.delete(flush: true)
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
