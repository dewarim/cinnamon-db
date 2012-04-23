package temp.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.dao.ChangeTriggerDAO;
import server.dao.GenericHibernateDAO;
import server.trigger.ChangeTrigger;

import javax.persistence.Query;
import java.util.List;

public class ChangeTriggerDAOHibernate  extends GenericHibernateDAO<ChangeTrigger, Long> implements ChangeTriggerDAO {
	
	transient Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public ChangeTrigger get(Long id) {
		return getSession().find(ChangeTrigger.class, id);
	}

	/* (non-Javadoc)
	 * @see server.dao.GenericDAO#list()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ChangeTrigger> list() {
		Query q = getSession().createNamedQuery("selectAllChangeTriggers");
		return q.getResultList();
	}

	public void delete(Long id){
		ChangeTrigger ae = get(id);
		makeTransient(ae);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChangeTrigger> findAllByCommandAndPostAndActiveOrderByRanking(String command) {
		Query q = getSession().createNamedQuery("findAllChangeTriggersByCommandAndPostAndActiveOrderByRanking");
		q.setParameter("command", command);
		return q.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChangeTrigger> findAllByCommandAndPreAndActiveOrderByRanking(String command) {
		Query q = getSession().createNamedQuery("findAllChangeTriggersByCommandAndPreAndActiveOrderByRanking");
		q.setParameter("command", command);
		return q.getResultList();
	}
	
}
