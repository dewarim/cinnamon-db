package temp.dao;

import server.dao.GenericDAO;
import server.lifecycle.LifeCycle;
import server.lifecycle.LifeCycleState;

import java.util.List;

public interface LifeCycleStateDAO extends GenericDAO<LifeCycleState, Long> {

    /**
     * Search for a LifeCycleState by name. Returns null if not found.
     * @param name Name of the LifeCycleState
     * @param lifeCycle the lifecycle which contains the wanted state.
     * @return the wanted state, or null.
     */
    LifeCycleState findByName(String name, LifeCycle lifeCycle);

	List<LifeCycleState> list();

    LifeCycleState get(String id);
	void delete(Long id);
}