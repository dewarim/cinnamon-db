package temp.dao;

import server.CustomTable;

import javax.persistence.Query;
import java.util.List;

public class CustomTableDAOHibernate extends GenericHibernateDAO<CustomTable, Long>
		implements CustomTableDAO {

	@Override
	public void delete(Long id) {
		CustomTable moribundus = get(id);
		makeTransient(moribundus);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomTable> list() {
		Query q = getSession().createNamedQuery("selectAllCustomTables");
		return q.getResultList();
	}

	/* (non-Javadoc)
	 * @see server.dao.GenericDAO#get(java.lang.Long)
	 */
	@Override
	public CustomTable get(Long id) {
		return getSession().find(CustomTable.class, id);
	}
	
	
}
