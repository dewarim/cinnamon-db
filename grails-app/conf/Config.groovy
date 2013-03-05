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
        debug 'stdout'
    }
}


// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'cinnamon.UserAccount'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'cinnamon.CmnGroupUser'
grails.plugins.springsecurity.authority.className = 'cinnamon.CmnGroup'
grails.plugins.springsecurity.authority.nameField = 'name'
grails.plugins.springsecurity.userLookup.usernamePropertyName='name'
grails.plugins.springsecurity.userLookup.passwordPropertyName='pwd'
grails.plugins.springsecurity.userLookup.enabledPropertyName='activated'
grails.plugins.springsecurity.userLookup.authoritiesPropertyName='groupUsers'
grails.plugins.springsecurity.successHandler.defaultTargetUrl='/login/dummyPage'
grails.plugins.springsecurity.accessDeniedHandler

grails.logging.jul.usebridge = false
/*
 The default page is responsible for connecting to the right database,
 so we always redirect the user there:
  */
grails.plugins.springsecurity.successHandler.alwaysUseDefault=true
grails.plugins.springsecurity.http.useExpressions=false

grails.views.javascript.library="jquery"
grails.json.legacy.builder=falsegrails.views.default.codec="none" // none, html, base64
grails.views.gsp.encoding="UTF-8"
