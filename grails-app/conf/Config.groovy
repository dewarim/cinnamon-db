// configuration for plugin testing - will not be included in the plugin zip

grails.config.locations = ["classpath:${appName}-config.properties",
        "classpath:${appName}-config.groovy",
        "file:${userHome}/.grails/${appName}-config.properties",
        "file:${userHome}/.grails/${appName}-config.groovy",
        "file:${System.env.CINNAMON_HOME_DIR}/${appName}-config.groovy"
]

log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    appenders {
        console name:'stdout', layout:pattern(conversionPattern: '%c %m%n')
    }
    
    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate',
            'org.apache',
            'net.sf.ehcache',
            'org.grails.plugin.resource'

    warn   'org.mortbay.log'
    root {
        info 'stdout'
    }
}


// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'cinnamon.UserAccount'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'cinnamon.CmnGroupUser'
grails.plugin.springsecurity.authority.className = 'cinnamon.CmnGroup'
grails.plugin.springsecurity.authority.nameField = 'name'
grails.plugin.springsecurity.userLookup.usernamePropertyName='name'
grails.plugin.springsecurity.userLookup.passwordPropertyName='pwd'
grails.plugin.springsecurity.userLookup.enabledPropertyName='activated'
grails.plugin.springsecurity.userLookup.authoritiesPropertyName='groupUsers'
grails.plugin.springsecurity.successHandler.defaultTargetUrl='/login/dummyPage'
grails.plugin.springsecurity.accessDeniedHandler

grails.logging.jul.usebridge = false
/*
 The default page is responsible for connecting to the right database,
 so we always redirect the user there:
  */
grails.plugin.springsecurity.successHandler.alwaysUseDefault=true
grails.plugin.springsecurity.http.useExpressions=false

grails.views.javascript.library="jquery"
grails.json.legacy.builder=falsegrails.views.default.codec="none" // none, html, base64
grails.views.gsp.encoding="UTF-8"

// Uncomment and edit the following lines to start using Grails encoding & escaping improvements

/* remove this line 
// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside null
                scriptlet = 'none' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        filteringCodecForContentType {
            //'text/html' = 'html'
        }
    }
}
remove this line */
