package temp.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.trigger.ChangeTriggerType;
import utils.ParamParser;

import javax.persistence.Query;
import java.util.List;

public class ChangeTriggerTypeDAOHibernate  extends GenericHibernateDAO<ChangeTriggerType, Long> implements ChangeTriggerTypeDAO {
	
	transient Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public ChangeTriggerType get(Long id) {
		return getSession().find(ChangeTriggerType.class, id);
	}

	/* (non-Javadoc)
	 * @see server.dao.GenericDAO#list()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ChangeTriggerType> list() {
		Query q = getSession().createNamedQuery("selectAllChangeTriggerTypes");
		return q.getResultList();
	}

	public void delete(Long id){
		ChangeTriggerType ctt = get(id);
		makeTransient(ctt);
	}

    @Override
    public ChangeTriggerType get(String id){
        Long lid = ParamParser.parseLong(id, "error.get.changeTriggerType");
        return get(lid);
    }
	
}
