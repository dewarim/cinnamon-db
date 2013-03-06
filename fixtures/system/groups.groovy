import cinnamon.CmnGroup
import cinnamon.global.Constants

fixture{
 
    superusers(CmnGroup, name:Constants.GROUP_SUPERUSERS, groupOfOne:false)
    aliasEveryone(CmnGroup, name:Constants.ALIAS_EVERYONE, groupOfOne: false)
    aliasOwner(CmnGroup, name:Constants.ALIAS_OWNER, groupOfOne: false)
    
}