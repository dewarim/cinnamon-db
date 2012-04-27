package cinnamon

import cinnamon.i18n.UiLanguage
import cinnamon.global.ConfThreadLocal
import cinnamon.exceptions.CinnamonException
import cinnamon.global.Constants

/*
 * Design Note: it may be easier in the long run to use session cookies or ids,
 * but then we may lose the feature to have multiple sessions from one user.
 */
class Session  implements Serializable {

    static constraints = {
        ticket(size: 1..255)
        username(size: 1..Constants.NAME_LENGTH)
        machinename(size: 1..Constants.NAME_LENGTH)
        message size: 0..65000, blank: true
    }

    static mapping = {
        table 'sessions'
        version 'obj_version'
    }
    
    String ticket
    Date expires = new Date()
    Long lifetime
    String username
    String machinename
    String message
    UiLanguage language
    UserAccount user

    public Session() {
    }

    public Session(String repository, UserAccount user, String machinename, UiLanguage language) {
        long expirationTime = ConfThreadLocal.getConf().getSessionExpirationTime(repository);
        ticket = UUID.randomUUID().toString() + "@" + repository;
        this.user = user;
        this.machinename = machinename;
        this.language = language;
        username = user.getName(); // while we still have direct SQL queries.
        expires.setTime(expires.getTime() + expirationTime); // for testing
    }

    /**
     * Copy a session, but assign a new UUID.
     * @param repository This session's repository's name.
     * @return the copy of the Session, with new UUID and extended expiration time.
     */
    public Session copy(String repository){
        // TODO: is this used anywhere?
        Session session = new Session();
        long expirationTime = ConfThreadLocal.getConf().getSessionExpirationTime(repository);
        session.ticket = UUID.randomUUID().toString()+"@"+repository;
        session.user = user;
        session.machinename = getMachinename();
        session.language = session.getLanguage();
        session.expires.setTime(expires.getTime() + expirationTime );
        return session;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Session)) return false;

        Session session = (Session) o;

        if (lifetime != session.lifetime) return false;
        if (expires != null ? !expires.equals(session.expires) : session.expires != null) return false;
        if (language != null ? !language.equals(session.language) : session.language != null) return false;
        if (machinename != null ? !machinename.equals(session.machinename) : session.machinename != null) return false;
        if (message != null ? !message.equals(session.message) : session.message != null) return false;
        if (ticket != null ? !ticket.equals(session.ticket) : session.ticket != null) return false;
        if (user != null ? !user.equals(session.user) : session.user != null) return false;
        if (username != null ? !username.equals(session.username) : session.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return ticket != null ? ticket.hashCode() : 0;
    }

    public String toString(){
        return "Session#"+id+" "+username +" "+ticket+" "+expires.toString();
    }
}
