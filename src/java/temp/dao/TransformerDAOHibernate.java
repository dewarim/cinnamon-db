package temp.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.transformation.Transformer;
import utils.ParamParser;

import javax.persistence.Query;
import java.util.List;


public class TransformerDAOHibernate  extends GenericHibernateDAO<Transformer, Long> implements TransformerDAO {
	
	transient Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public Transformer get(Long id) {
		return getSession().find(Transformer.class, id);
	}
	
	@Override
	public Transformer get(String id) {
		Long transformerId = ParamParser.parseLong(id, "error.param.id");
		return getSession().find(Transformer.class, transformerId);
	}
	

	/* (non-Javadoc)
	 * @see server.dao.GenericDAO#list()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Transformer> list() {
		Query q = getSession().createNamedQuery("selectAllTransformers");
		return q.getResultList();
	}

	public void delete(Long id){
		Transformer t = get(id);
		makeTransient(t);
	}
	
}
