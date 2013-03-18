import cinnamon.Acl
import cinnamon.global.Constants

def names = [Constants.ACL_DEFAULT, 'authoring.acl', 'review.acl', 'released.acl']

fixture{
    
    names.each{name ->
        "$name"(Acl, name:name)
    }
    
}
