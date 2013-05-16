package cinnamon

import humulus.EnvironmentHolder

public class InfoService {

    def grailsApplication

    static def transactional = false

    String getRepositoryName() {
        EnvironmentHolder.environment.dbName
    }

    ConfigObject getConfig() {
        return grailsApplication.config
    }
    
}
