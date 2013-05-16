import cinnamon.trigger.ChangeTrigger
import cinnamon.trigger.ChangeTriggerType
import cinnamon.trigger.impl.RelationChangeTrigger

def xmlCmdList = ["setcontent", "setmeta", "setsysmeta", "version", "delete", "create"]
def webCmdList = ['saveContent', "saveMetadata", 'newVersion', 'iterate', 'saveField', "saveObject"]

fixture {
    
    relationChangeTrigger(ChangeTriggerType, name: 'RelationChangeTriggerType',
            triggerClass: RelationChangeTrigger
    )
    
    for (String command : xmlCmdList) {
        "${command}CmdBean"(ChangeTrigger, controller:'cinnamon', action:command,
                triggerType:relationChangeTrigger, ranking:100,
                active: true, preTrigger: true, postTrigger: true, config:'<meta />');
    } 
    
    for (String command : webCmdList) {
        "${command}CmdBean"(ChangeTrigger, controller:'osd', action:command,
                triggerType:relationChangeTrigger, ranking:100,
                active: true, preTrigger: true, postTrigger: true, config:'<meta />');
    }
    
}