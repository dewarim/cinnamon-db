package temp.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Format;
import server.exceptions.CinnamonException;
import utils.ParamParser;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

public class FormatDAOHibernate extends GenericHibernateDAO<Format, Long>
		implements FormatDAO {

	private transient Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void delete(Long id) {
		Format moribundus = get(id);
		makeTransient(moribundus);
	}

	// invalid: Name is unique!
//	@Override
//	public List<Format> findAllByName(String name) {
//		Query q = getSession().createNamedQuery("findFormatByName");
//		q.setParameter("name", name);
//		return q.getResultList();
//	}
	
	@Override
	public Format findByName(String name) {
		Query q = getSession().createNamedQuery("findFormatByName");
		q.setParameter("name", name);
		try{
			return (Format) q.getSingleResult();
		}
		catch (NoResultException e) {
			log.debug("FormatDAO.findByName: no Format by name of '" + name + "'");
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see server.dao.GenericDAO#get(java.lang.Long)
	 */
	@Override
	public Format get(Long id) {
		return getSession().find(Format.class, id);
	}

	/* (non-Javadoc)
	 * @see server.dao.GenericDAO#list()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Format> list() {
		Query q = getSession().createNamedQuery("selectAllFormats");
		return q.getResultList();
	}

	public void update(Long id, Map<String, String> cmd) {
		Format format = get(id);
		if(format == null){
			throw new CinnamonException("error.format.not_found", String.valueOf(id));
		}	

		String name = cmd.get("name");
		if(name != null){
			format.setName(name);
		}
		
		String extension = cmd.get("extension");
		if(extension != null){
			format.setExtension(extension);
		}
		
		String contenttype = cmd.get("contenttype");
		if(contenttype != null){
			format.setContenttype(contenttype);
		}
		
		String description = cmd.get("description");
		if(description != null){
			format.setDescription(description);
		}
	}

	@Override
	public Format get(String id) {
		Long formatId = ParamParser.parseLong(id, "error.param.id");
		return get(formatId);
	}
	
}
