package temp.trigger;

import server.User;
import server.helpers.PoBox;
import server.interfaces.Repository;
import server.interfaces.Response;

public interface ITrigger {

	PoBox executePreCommand(PoBox poBox, String config);
	PoBox executePostCommand(PoBox poBox, String config);
	
	
}
