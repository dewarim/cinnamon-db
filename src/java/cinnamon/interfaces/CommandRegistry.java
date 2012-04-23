package cinnamon.interfaces;

import server.User;
import server.interfaces.Repository;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * A CommandRegistry stores MethodContainers and allows a CinnamonServer to invoke those methods. 
 *
 */
public interface CommandRegistry {

	void registerAPI(Map<String, MethodContainer> commands);
	Response invoke(String command, Map<String, Object> params,
                    HttpServletResponse res, User user, Repository repository);
	
}
