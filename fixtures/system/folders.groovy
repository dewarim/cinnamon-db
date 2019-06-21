import cinnamon.Acl
import cinnamon.Folder
import cinnamon.FolderType
import cinnamon.UserAccount
import cinnamon.global.Constants

def admin = UserAccount.findByName(Constants.USER_SUPERADMIN_NAME)
def acl = Acl.findByName(Constants.ACL_DEFAULT)
def type = FolderType.findByName(Constants.FOLDER_TYPE_DEFAULT)

fixture{
    
    root(Folder, name:Constants.ROOT_FOLDER_NAME, owner:admin, parent:ref('root'), 
            acl:acl, type:type)

    templates(Folder, name:'templates', owner:admin, acl:acl, type:type, parent:root)
    system(Folder, name:'system', owner:admin, acl:acl, type:type, parent:root)
    apps(Folder, name:'applications', owner:admin, acl:acl, type:type, parent:system)
    custom(Folder, name:'custom', owner:admin, acl:acl, type:type, parent:system)
    transientFolder(Folder, name:'transient', owner:admin, acl:acl, type:type, parent:system)
    test(Folder, name:'test', owner:admin, acl:acl, type:type, parent:system)
    config(Folder, name:'config', owner:admin, acl:acl, type:type, parent:system)
    users(Folder, name:'users', owner:admin, acl:acl, type:type, parent:system)
    renderTasks(Folder, name:'render_tasks', owner:admin, acl:acl, type:type, parent:transientFolder)
    transTasks(Folder, name:'translation_tasks', owner:admin, acl:acl, type:type, parent:transientFolder)
    adminFolder(Folder, name:'admin', owner:admin, acl:acl, type:type, parent:users)
    userConfig(Folder, name:'config', owner:admin, acl:acl, type:type, parent:adminFolder)
    home(Folder, name:'home', owner:admin, acl:acl, type:type, parent:adminFolder)
    carts(Folder, name:'carts', owner:admin, acl:acl, type:type, parent:adminFolder)
    searches(Folder, name:'searches', owner:admin, acl:acl, type:type, parent:adminFolder)
    
}