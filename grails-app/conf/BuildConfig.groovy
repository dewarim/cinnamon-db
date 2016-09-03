grails.servlet.version = "3.0"
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.8
grails.project.source.level = 1.8
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.repos.default = "myRepo"
grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }

    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'

    repositories {
        mavenCentral()
        grailsCentral()
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        runtime('org.apache.commons:commons-compress:1.10')
        runtime 'postgresql:postgresql:9.1-901.jdbc4'
        runtime('org.apache.lucene:lucene-core:6.2.0')
        runtime('org.apache.lucene:lucene-queries:6.2.0')
        runtime 'org.mindrot:jbcrypt:0.3m'
        test "org.spockframework:spock-grails-support:0.7-groovy-2.0"
        runtime 'dom4j:dom4j:1.6.1'
        runtime 'jaxen:jaxen:1.1.4'
        runtime 'org.apache.httpcomponents:httpclient:4.5.1'
//        compile 'org.grails.plugins:cinnamon-humulus:0.3'
    }

    plugins {
        compile(':spring-security-core:2.0-RC5') // needed for CinnamonPasswordEncoder
        compile ":rest-client-builder:2.1.1"
        build(":tomcat:7.0.42"){
            export=false
        }
        build(":release:3.1.1"){
            export=false
        }
//        runtime(":hibernate:3.6.10.19")
        runtime(":hibernate4:4.3.8.1")
        compile(':webxml:1.4.1')
//        compile(':resources:1.2.RC2')
        test (':spock:0.7'){
            exclude "spock-grails-support"
        }
        compile(':fixtures:1.3'){
            export = false
        }
    }
}
