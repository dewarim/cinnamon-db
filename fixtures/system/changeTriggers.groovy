import cinnamon.trigger.ChangeTrigger
import cinnamon.trigger.ChangeTriggerType
import cinnamon.trigger.impl.RelationChangeTrigger

def cmdList = ["setcontent", "setmeta", "setsysmeta", "version", "delete"]

fixture {
    
    relationChangeTrigger(ChangeTriggerType, name: 'RelationChangeTriggerType',
            triggerClass: RelationChangeTrigger
    )
    
    for (String command : cmdList) {
        "${command}CmdBean"(ChangeTrigger, controller:'cinnamon', action:command,
                triggerType:relationChangeTrigger, ranking:100,
                active: true, preTrigger: true, postTrigger: true, config:'<meta />');
    }
    
}