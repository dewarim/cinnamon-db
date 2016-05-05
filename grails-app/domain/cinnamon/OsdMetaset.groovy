package cinnamon

import cinnamon.index.IndexAction
import cinnamon.interfaces.IMetasetJoin

class OsdMetaset implements IMetasetJoin {

    static constraints = {
        osd unique: ['metaset']
    }

    static mapping = {
        table 'osd_metasets'
        version 'obj_version'
    }
    
    ObjectSystemData osd
    Metaset metaset

    public OsdMetaset(){
    }

    public OsdMetaset(ObjectSystemData osd, Metaset metaset) {
        this.osd = osd;
        this.metaset = metaset;
    }

    public String toString(){
        return "OsdMetaset: "+id+" OSD: "+osd.getId()+" Metaset: "+metaset.getId();
    }

    void doDelete(){
        this.delete(flush: true)
        LocalRepository.addIndexable(osd, IndexAction.UPDATE)
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof OsdMetaset)) return false

        OsdMetaset that = (OsdMetaset) o

        if (metaset != that.metaset) return false
        if (osd != that.osd) return false

        return true
    }

    int hashCode() {
        int result
        result = (osd != null ? osd.hashCode() : 0)
        result = 31 * result + (metaset != null ? metaset.hashCode() : 0)
        return result
    }
}
