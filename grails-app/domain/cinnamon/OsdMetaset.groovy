package cinnamon

class OsdMetaset {

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

    public void doDelete(){
        metaset.removeFromOsdMetasets(this);
        osd.removeFromMetasets(this);
        this.delete()
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
