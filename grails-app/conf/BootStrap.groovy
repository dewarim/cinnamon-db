import cinnamon.UserAccount

class BootStrap {

    def fixtureLoader
    
    def init = { servletContext ->

        grails.util.Environment.executeForCurrentEnvironment {
            /*
             * To initialize an empty database, use:
             * grails -Dgrails.env=initialize run-app
             */
            initialize{
                log.debug("run: initializeDatabase()")
                initializeDatabase()
            }
        }

    }

    def destroy = { servletContext ->

    }

    def initializeDatabase() {
        try {
            log.debug("Starting initialization.")
            def userCount = UserAccount.count()
            if (userCount){
                throw new RuntimeException(
                        'Failed to initialize database: it already contains user accounts.')
            }
            
            fixtureLoader.load('system/metasetTypes')
            fixtureLoader.load('system/objectTypes')
            fixtureLoader.load('system/acls')
            fixtureLoader.load('system/folderTypes')
            
        }
        catch (Exception e) {
            log.error("Failed to initialize repository", e)
        }
        log.info("Initialization finished.")
    }

}