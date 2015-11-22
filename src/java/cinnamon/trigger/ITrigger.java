package cinnamon.trigger;

import cinnamon.PoBox;

public interface ITrigger {

	PoBox executePreCommand(PoBox poBox, ChangeTrigger changeTrigger);
	PoBox executePostCommand(PoBox poBox, ChangeTrigger changeTrigger);
	
}
