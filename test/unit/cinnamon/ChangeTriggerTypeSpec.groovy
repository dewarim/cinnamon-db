package cinnamon

import cinnamon.trigger.ChangeTriggerType
import cinnamon.trigger.impl.TestTriggerImpl
import grails.plugin.spock.UnitSpec
import grails.test.mixin.Mock

/**
 * 
 */
@Mock([ChangeTriggerType])
class ChangeTriggerTypeSpec extends UnitSpec {
    
    def 'changeTriggerType CRUD'(){
        setup:
        def ct = new ChangeTriggerType('LSAT', TestTriggerImpl)
        
        when:
        ct.save(flush: true)
        
        then:
        ct.id
        
        when:
        ct.name = 'LSAT-New'
        ct.save(flush: true)
        
        then:
        ChangeTriggerType.findByName('LSAT-New')
        
        when:
        ct.delete()
        
        then:
        ! ChangeTriggerType.findByName('LSAT-New')
    }
    
}
