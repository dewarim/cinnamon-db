package temp.dao;

import server.dao.GenericHibernateDAO;
import server.dao.LifeCycleDAO;
import server.lifecycle.LifeCycle;
import utils.ParamParser;

import javax.persistence.Query;
import java.util.List;

public class LifeCycleDAOHibernate extends GenericHibernateDAO<LifeCycle, Long>
		implements LifeCycleDAO {

	@Override
	public void delete(Long id) {
		LifeCycle moribundus = get(id);
		makeTransient(moribundus);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LifeCycle> list() {
		Query q = getSession().createNamedQuery("selectAllLifeCycles");
		return q.getResultList();
	}

	/* (non-Javadoc)
	 * @see server.dao.GenericDAO#get(java.lang.Long)
	 */
	@Override
	public LifeCycle get(Long id) {
		return getSession().find(LifeCycle.class, id);
	}

    @Override
    public LifeCycle get(String id){
        Long myId = ParamParser.parseLong(id, "error.get.lifecycle_id");
        return get(myId);
    }

    @Override
    public LifeCycle findByName(String name) {
        Query q = getSession().createNamedQuery("findLifeCycleByName");
        q.setParameter("name", name);
        List<LifeCycle> lcList = q.getResultList();
        if(lcList.isEmpty()){
            return null;
        }
        else{
            return lcList.get(0);
        }
    }
}