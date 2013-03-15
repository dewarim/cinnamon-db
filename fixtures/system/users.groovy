import cinnamon.Acl
import cinnamon.AclEntry
import cinnamon.CmnGroup
import cinnamon.CmnGroupUser
import cinnamon.UserAccount
import cinnamon.global.Constants

include('system/groups')

def admin = Constants.USER_SUPERADMIN_NAME

load {

    adminUser(UserAccount, name: admin,
            fullname:'Administrator',
            description: 'Cinnamon Administrator', pwd: 'admin', sudoer: true
    )


}

fixture{
    def name = "_${adminUser.id}_${admin}"
    adminsOwnGroup(CmnGroup, name:name, groupOfOne:true)
    adminOwnGroupUser(CmnGroupUser, cmnGroup: adminsOwnGroup, userAccount: adminUser)
    adminSuperuser(CmnGroupUser, cmnGroup:superusers, userAccount:adminUser)
    adminUserUserGroup(CmnGroupUser, cmnGroup: usersGroup, userAccount: adminUser)
    adminAclDefaultEntry(AclEntry, group:adminsOwnGroup, acl:Acl.findByName(Constants.ACL_DEFAULT) )
}