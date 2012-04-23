package temp.dao;

import server.lifecycle.LifeCycle;
import server.lifecycle.LifeCycleState;
import utils.ParamParser;

import javax.persistence.Query;
import java.util.List;

public class LifeCycleStateDAOHibernate extends GenericHibernateDAO<LifeCycleState, Long>
		implements LifeCycleStateDAO {

	@Override
	public void delete(Long id) {
		LifeCycleState moribundus = get(id);
		makeTransient(moribundus);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LifeCycleState> list() {
		Query q = getSession().createNamedQuery("selectAllLifeCycleStates");
		return q.getResultList();
	}

    @Override
    public LifeCycleState get(String id) {
        Long myId = ParamParser.parseLong(id, "error.get.lifecycle_state_id");
        return get(myId);
    }

    /* (non-Javadoc)
      * @see server.dao.GenericDAO#get(java.lang.Long)
      */
	@Override
	public LifeCycleState get(Long id) {
		return getSession().find(LifeCycleState.class, id);
	}

    @Override
    public LifeCycleState findByName(String name, LifeCycle lifeCycle) {
        Query q = getSession().createNamedQuery("findLifeCycleStateByName");
        q.setParameter("lifeCycle", lifeCycle);
        q.setParameter("name", name);
        List<LifeCycleState> results = q.getResultList();
        if(results.isEmpty()){
            return null;
        }
        else{
            return results.get(0);
        }
    }
}