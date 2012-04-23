package temp.dao;

import server.dao.GenericDAO;
import server.lifecycle.LifeCycle;

import java.util.List;

public interface LifeCycleDAO extends GenericDAO<LifeCycle, Long> {

    /**
     * Find a LifeCycle by name.
     * @param name the name of the lifecycle you are looking for
     * @return the lifecycle or null if there is no lifecycle by this name in the database.
     */
    LifeCycle findByName(String name);

	List<LifeCycle> list();
    LifeCycle get(String id);
	void delete(Long id);
}