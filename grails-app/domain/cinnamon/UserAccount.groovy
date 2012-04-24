package cinnamon

import org.dom4j.Element
import cinnamon.global.Constants
import cinnamon.i18n.UiLanguage

class UserAccount {

    static hasMany = [groupUsers:CmnGroupUser]
    static mapping = {
        table 'users'
    }
    static constraints = {
        name unique: true, size: 1..Constants.NAME_LENGTH, validator: {val, obj ->
            val.trim().length() != 0
        }
        pwd size: 4..255
        description size: 0..Constants.DESCRIPTION_SIZE, blank: true
        email nullable: true
        language nullable: true
    }

    private transient Element xmlNode = null;
    private transient Boolean userIsSuperuser = null;
    
    String name
    String pwd
    String fullname
    String description
    Boolean activated = true
    Boolean sudoer = false
    Boolean sudoable = false
    
    Boolean accountExpired = false
    Boolean accountLocked = false
    Boolean passwordExpired = false
    
    Date tokenAge = new Date(0L)
    Integer tokensToday = 0
    String token = UUID.randomUUID().toString()
    
    String email
    UiLanguage language

    public UserAccount(){
    }

    public UserAccount(Map<String, String> cmd){
        setName(cmd.get("name"));
        setPwd(cmd.get("pwd"));
        fullname = cmd.get("fullname");
        description = cmd.get("description");
        email = cmd.get("email");
        language = UiLanguage.getDefaultLanguage();
        if(cmd.containsKey("sudoable")){
            sudoable = cmd.get("sudoable").equals("true");
        }
        if(cmd.containsKey("sudoer")){
            sudoer = cmd.get("sudoer").equals("true");
        }
    }

    public UserAccount(String name, String pwd, String fullname, String description){
        setName(name);
        setPwd(pwd);
        this.fullname = fullname;
        this.description = description;
    }

    public Boolean verifySuperuserStatus(){
        if(userIsSuperuser != null){
            return userIsSuperuser;
        }
        CmnGroup adminGroup = CmnGroup.findByName(Constants.GROUP_SUPERUSERS);
        if(adminGroup == null){
            log.debug("Superuser-CmnGroup was not found.");
            return false;
        }
        Set<CmnGroupUser> adminGroupUsers = new HashSet<CmnGroupUser>();
        adminGroupUsers.addAll(adminGroup.getGroupUsers());
        // debug code:
//		log.debug("groupUsers for admin-group: "+adminGroupUsers);
//		log.debug("groupUsers for user: "+getGroupUsers());
//		for(CmnGroupUser gu : adminGroupUsers){
//            if(getGroupUsers().contains(gu)){
//                log.debug(getGroupUsers() + " contains "+gu);
//            }
//            else{
//                log.debug(getGroupUsers() + " does not contain "+gu);
//            }
//        }
        userIsSuperuser = adminGroupUsers.removeAll(getGroupUsers());
        log.debug("superuserStatus: "+getName()+" == "+userIsSuperuser);
        log.debug("adminGroupUsers: "+adminGroupUsers);
        return userIsSuperuser;
    }



    Set<CmnGroup> findAllGroups(){
        Set<CmnGroup> groups = new HashSet<CmnGroup>();
        for (CmnGroupUser gu : getGroupUsers()) {
            groups.add(gu.cmnGroup);
            groups.addAll(gu.cmnGroup.findAncestors());

        }
        return groups;
    }



    public String toString(){
        return "UserAccount #"+id+" "+name;
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof UserAccount)) return false

        UserAccount user = (UserAccount) o

        if (accountExpired != user.accountExpired) return false
        if (accountLocked != user.accountLocked) return false
        if (activated != user.activated) return false
        if (description != user.description) return false
        if (email != user.email) return false
        if (fullname != user.fullname) return false
        if (id != user.id) return false
        if (language != user.language) return false
        if (name != user.name) return false
        if (passwordExpired != user.passwordExpired) return false
        if (pwd != user.pwd) return false
        if (sudoable != user.sudoable) return false
        if (sudoer != user.sudoer) return false
        if (token != user.token) return false
        if (tokenAge != user.tokenAge) return false
        if (tokensToday != user.tokensToday) return false
        if (userIsSuperuser != user.userIsSuperuser) return false
        if (xmlNode != user.xmlNode) return false

        return true
    }

    int hashCode() {
        int result
        result = (xmlNode != null ? xmlNode.hashCode() : 0)
        result = 31 * result + (userIsSuperuser != null ? userIsSuperuser.hashCode() : 0)
        result = 31 * result + (name != null ? name.hashCode() : 0)
        result = 31 * result + (pwd != null ? pwd.hashCode() : 0)
        result = 31 * result + (fullname != null ? fullname.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (activated != null ? activated.hashCode() : 0)
        result = 31 * result + (sudoer != null ? sudoer.hashCode() : 0)
        result = 31 * result + (sudoable != null ? sudoable.hashCode() : 0)
        result = 31 * result + (accountExpired != null ? accountExpired.hashCode() : 0)
        result = 31 * result + (accountLocked != null ? accountLocked.hashCode() : 0)
        result = 31 * result + (passwordExpired != null ? passwordExpired.hashCode() : 0)
        result = 31 * result + (tokenAge != null ? tokenAge.hashCode() : 0)
        result = 31 * result + (tokensToday != null ? tokensToday.hashCode() : 0)
        result = 31 * result + (token != null ? token.hashCode() : 0)
        result = 31 * result + (email != null ? email.hashCode() : 0)
        result = 31 * result + (language != null ? language.hashCode() : 0)
        result = 31 * result + (id != null ? id.hashCode() : 0)
        return result
    }
}
