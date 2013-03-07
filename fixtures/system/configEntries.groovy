import cinnamon.ConfigEntry
import cinnamon.global.Constants

fixture{
    
    translationConfig(ConfigEntry, name:'translation.config',
            config:"<config><aclForTranslatedObjects>" +
                    Constants.ACL_DEFAULT + "</aclForTranslatedObjects></config>"
    )
    
}