package cinnamon.trigger;

import cinnamon.PoBox;

public interface ITrigger {

	PoBox executePreCommand(PoBox poBox, String config);
	PoBox executePostCommand(PoBox poBox, String config);
	
	
}
