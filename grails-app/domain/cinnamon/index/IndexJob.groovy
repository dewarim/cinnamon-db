package cinnamon.index

class IndexJob {

    static constraints = {
    }
    
    static mapping = {
        table 'index_jobs'
        version false
    }
    
    Class indexableClass
    Long indexableId
    Boolean failed = false

    public IndexJob() {

    }

    public IndexJob(Indexable indexable){
        this.indexableClass = indexable.getClass();
        this.indexableId = indexable.myId();
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof IndexJob)) return false

        IndexJob indexJob = (IndexJob) o

        if (failed != indexJob.failed) return false
        if (indexableClass != indexJob.indexableClass) return false
        if (indexableId != indexJob.indexableId) return false

        return true
    }

    int hashCode() {
        return (indexableId != null ? indexableId.hashCode() : 0)
    }
}
