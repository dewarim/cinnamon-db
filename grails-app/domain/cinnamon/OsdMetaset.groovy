package cinnamon

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
        osd.addToMetasets(this);
        metaset.addToOsdMetasets(this);
    }

    public String toString(){
        return "OsdMetaset: "+id+" OSD: "+osd.getId()+" Metaset: "+metaset.getId();
    }

    /*
     * Note: this method will cause Hibernate to fail with  
     * "deleted object would be re-saved by cascade (remove deleted object from associations)"
     * if the osd or the metaset have changed during this session 
     * (which changes the hashCode and thus confuses the HashSet-Implementation)
     */
    public void doDelete(){
        metaset.osdMetasets.remove(this)
        osd.metasets.remove(this)
        this.delete(flush: true)
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
