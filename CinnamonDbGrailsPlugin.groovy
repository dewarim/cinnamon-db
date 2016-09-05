import cinnamon.Session

class CinnamonDbGrailsPlugin {

    def version = "3.7.5"
    def grailsVersion = "2.4 > *"
    def dependsOn = [:]
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def title = "Cinnamon Db Plugin" 
    def author = "Ingo Wiarda"
    def authorEmail = "ingo_wiarda@dewarim.de"
    def description = "Domain and base classes for the Cinnamon CMS."

    def documentation = "http://cinnamon-cms.com"
    def license = "LGPL 2.1"
    def developers = [
            [name:'Ingo Wiarda', email: "ingo_wiarda@dewarim.de"]
    ]

    def issueManagement = [ system: "Github", url: "https://github.com/dewarim/cinnamon-db/issues" ]
    def scm = [ url: "https://github.com/dewarim/cinnamon-db" ]

    def doWithWebDescriptor = { xml ->
        
    }

    def doWithSpring = {

    }

    def doWithDynamicMethods = { ctx ->
    
    }

    def doWithApplicationContext = { applicationContext ->
        Session.infoService = applicationContext.infoService
    }

    def onChange = { event ->
    
    }

    def onConfigChange = { event ->
    
    }

    def onShutdown = { event ->
    
    }
}
