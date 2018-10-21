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

}
