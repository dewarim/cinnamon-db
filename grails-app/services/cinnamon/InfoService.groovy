package cinnamon


public class InfoService {

    def grailsApplication

    static def transactional = false

    String getRepositoryName() {
        grailsApplication.config.dbName
    }

    ConfigObject getConfig() {
        return grailsApplication.config
    }

    private static final ThreadLocal<Long> lastInsertId = new ThreadLocal<Long>() {
        @Override
        protected Long initialValue() {
            return null;
        }
    }

    Long getLastInsertId(){
        return lastInsertId.get();
    }

    void setLastInsertId(Long id){
        lastInsertId.set(id);
    }

}
