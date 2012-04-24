import org.springframework.jdbc.datasource.DriverManagerDataSource
import humulus.Environment
import cinnamon.CinnamonUserDetailsService
import humulus.CinnamonPasswordEncoder
import humulus.RepositoryLoginFilter
import humulus.SwitchableDataSource

beans = {

    parentDataSource(DriverManagerDataSource) { bean ->
        bean.'abstract' = true;
        username = "sa"
//    	pooled = true
    }

    Environment.list().each { env ->
        "${env.prefix}DataSource"(DriverManagerDataSource) { bean ->
            bean.parent = parentDataSource
            bean.scope = 'prototype'
            url = env.dbConnectionUrl
            log.debug("url = '$url'")
            driverClassName = env.driverClassName
            if (env.username) {
                username = env.username
            }
            if (env.password) {
                password = env.password
            }
        }
    }

    def dataSources = [:]
    Environment.list().each {env ->
        dataSources[env.id] = ref(env.prefix + 'DataSource')
        log.debug("dataSource: ${dataSources[env.id]}")
    }

    dataSource(SwitchableDataSource) {
        targetDataSources = dataSources
    }

    userDetailsService(CinnamonUserDetailsService){
        // looks like this service is not injected automatically:
        repositoryService = ref('repositoryService')
    }
    passwordEncoder(CinnamonPasswordEncoder)

    authenticationProvider(org.springframework.security.authentication.dao.DaoAuthenticationProvider){
        userDetailsService = ref('userDetailsService')
    }

    authenticationManager(org.springframework.security.authentication.ProviderManager){
        providers = ref('authenticationProvider')
    }

    repositoryLoginFilter(RepositoryLoginFilter){
        authenticationManager = ref('authenticationManager')
    }
}