grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.7
grails.project.source.level = 1.7
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.repos.default = "myRepo"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }

    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'

    repositories {
        mavenLocal()
        mavenRepo name:'myRepo'
        grailsRepo "http://grails.org/plugins"
        mavenCentral()
        grailsCentral()
        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.5'
        runtime('org.apache.commons:commons-compress:1.4')
        runtime('org.apache.lucene:lucene-core:3.6.2')
        runtime('org.apache.lucene:lucene-xml-query-parser:3.6.2')
        runtime('org.apache.lucene:lucene-queries:3.6.2')
        runtime 'postgresql:postgresql:9.1-901.jdbc4'
        runtime 'org.mindrot:jbcrypt:0.3m'
        test "org.spockframework:spock-grails-support:0.7-groovy-2.0"
        runtime 'dom4j:dom4j:1.6.1'
        runtime 'jaxen:jaxen:1.1.4'
        compile 'org.grails.plugins:cinnamon-humulus:0.2.5'
    }

    plugins {
        build(":tomcat:$grailsVersion",
              ":release:2.2.1") {
            export = false
        }
        runtime(":hibernate:$grailsVersion")
        compile(':webxml:1.4.1')
        compile(':resources:1.1.6')
        test (':spock:0.7'){
            exclude "spock-grails-support"
        }
        compile(':fixtures:1.2'){
            export = false
        }
    }
}
