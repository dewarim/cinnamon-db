package cinnamon.trigger.impl

import cinnamon.PoBox
import cinnamon.trigger.ITrigger

/**
 * A nop class for testing.
 */
class TestTriggerImpl implements ITrigger{
    
    @Override
    PoBox executePreCommand(PoBox poBox, String config) {
        return poBox
    }

    @Override
    PoBox executePostCommand(PoBox poBox, String config) {
        return poBox 
    }
    
}
