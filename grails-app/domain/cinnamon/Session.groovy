package cinnamon

import cinnamon.i18n.UiLanguage
import cinnamon.exceptions.CinnamonException
import cinnamon.global.Constants

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
    
    static infoService
    
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
        return result
    }

    public String toString(){
        return "Session#"+id+" "+username +" "+ticket+" "+expires.toString();
    }

    Long sessionExpirationTime(String repositoryName){
        def repo = infoService?.config?.repositories?.values()?.find{it.get('name') == repositoryName}
        Long expTime = repo?.sessionExpirationTime?.value ?: 3600000
        log.debug("sessionExpirationTime for ${repositoryName}: ${expTime}")
        return expTime
    }
    
    /** Copy a session, but assign a new UUID.
     * @param repository This session's repository's name.
     * @return the copy of the Session, with new UUID and extended expiration time.
     */
    public Session copy(String repository){
        Session session = new Session();
        def repo = infoService?.config?.repositories?.values()?.find{it.get('name') == repository}
        long expirationTime = repo?.sessionExpirationTime?.value ?: 3600000
        session.ticket = UUID.randomUUID().toString()+"@"+repository;
        session.user = user;
        session.username = username;
        session.machinename = getMachinename();
        session.language = session.getLanguage();
        session.expires.setTime(expires.getTime() + expirationTime );
        session.language = language
        return session;
    }
  
}
