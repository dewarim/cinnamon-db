class CinnamonDbGrailsPlugin {

    def version = "3.0.0.26"
    def grailsVersion = "2.1 > *"
    def dependsOn = [:]
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def title = "Cinnamon Db Plugin" // Headline display name of the plugin
    def author = "Ingo Wiarda"
    def description = '''\
Domain and base classes for the Cinnamon CMS.
'''

    def documentation = "http://cinnamon-cms.de"
    def license = "LGPL 2.1"
    def organization = [ name: "Texolution GmbH", url: "http://texolution.eu/" ]
    def developers = [
            [name:'Ingo Wiarda', email: "ingo.wiarda@texolution.eu"]
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
    
    }

    def onChange = { event ->
    
    }

    def onConfigChange = { event ->
    
    }

    def onShutdown = { event ->
    
    }
}
