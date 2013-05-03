package cinnamon

import cinnamon.i18n.UiLanguage
import cinnamon.exceptions.CinnamonException
import cinnamon.global.Constants
import humulus.EnvironmentHolder

/*
 * Design Note: it may be easier in the long run to use session cookies or ids,
 * but then we may lose the feature to have multiple sessions from one user.
 */
class Session implements Serializable {

    static constraints = {
        ticket(size: 1..255)
        username(size: 1..Constants.NAME_LENGTH)
        machinename(size: 1..Constants.NAME_LENGTH)
        message size: 0..65000, blank: true
    }

    static mapping = {
        table 'sessions'
        version 'obj_version'
        language column: 'ui_language_id'
    }
    
    String ticket
    Date expires = new Date()
    Long lifetime = new Date().time+3600000
    String username
    String machinename = 'unknown'
    String message = '-'
    UiLanguage language
    UserAccount user

    public Session() {
    }

    public Session(String repository, UserAccount user, String machinename, UiLanguage language) {
        long expirationTime = sessionExpirationTime(repository)
        ticket = UUID.randomUUID().toString() + "@" + repository;
        this.user = user;
        this.machinename = machinename;
        this.language = language;
        username = user.getName(); // while we still have direct SQL queries.
        expires.setTime(expires.getTime() + expirationTime); // for testing
    }

    void checkForExpiration() {
        if (getExpires().getTime() < new Date().getTime()) {
            log.debug("session is expired.");
            throw new CinnamonException("error.session.expired");
        }
    }

    void renewSession(Long expirationTime) {
        Long newTime = (new Date()).getTime() + expirationTime;
        setExpires(new Date(newTime));
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Session)) return false

        Session session = (Session) o

        if (expires != session.expires) return false
        if (language != session.language) return false
        if (lifetime != session.lifetime) return false
        if (machinename != session.machinename) return false
        if (message != session.message) return false
        if (ticket != session.ticket) return false
        if (user != session.user) return false
        if (username != session.username) return false

        return true
    }

    int hashCode() {
        int result
        result = (ticket != null ? ticket.hashCode() : 0)
        result = 31 * result + (expires != null ? expires.hashCode() : 0)
        result = 31 * result + (lifetime != null ? lifetime.hashCode() : 0)
        result = 31 * result + (username != null ? username.hashCode() : 0)
        result = 31 * result + (machinename != null ? machinename.hashCode() : 0)
        result = 31 * result + (message != null ? message.hashCode() : 0)
        result = 31 * result + (language != null ? language.hashCode() : 0)
        result = 31 * result + (user != null ? user.hashCode() : 0)
        return result
    }

    public String toString(){
        return "Session#"+id+" "+username +" "+ticket+" "+expires.toString();
    }

    Long sessionExpirationTime(String repositoryName){
        if(! repositoryName){
            repositoryName = EnvironmentHolder.environment.dbName
        }
        // cannot use "def grailsApplication", because this method is used in a constructor
        def grailsConfig = domainClass.grailsApplication.config
        def repo = grailsConfig.repositories.repository.find{
            it.key == repositoryName
        }
        Long expTime = repo?.sessionExpirationTime
        log.debug("sessionExpirationTime for ${repositoryName}: ${expTime}")
        return expTime ?: 3600000

    }

}
