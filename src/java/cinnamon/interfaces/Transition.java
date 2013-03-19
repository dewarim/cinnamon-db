package cinnamon.interfaces;

import cinnamon.ObjectSystemData;
import org.dom4j.Node;
import java.util.List;

/**
 * <h1>Workflow transition</h1>
 * When a workflow task is done, the client selects and executes a transition 
 * to go from one state (task) to another (or to the end of the workflow). 
 *
 */
public interface Transition {

	/**
	 * Execute a transition and return a list of new tasks.
	 * @param task	the current task which is left via this transition
	 * @param transitionNode an XML node which contains further parameters for the transition.
	 * @param repository the repository in which the task lies. Needed to copy content of new tasks.
	 * @return a list of new tasks (or an empty list if now new tasks were created).
	 */
	List<ObjectSystemData> execute(ObjectSystemData task, Node transitionNode, Repository repository);
	
}
