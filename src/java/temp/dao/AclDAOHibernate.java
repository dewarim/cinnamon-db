package temp.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Acl;
import server.exceptions.CinnamonException;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;

public class AclDAOHibernate extends GenericHibernateDAO<Acl, Long> implements
		AclDAO {

	final transient Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public Acl findByName(String name) {
		Query q = getSession().createNamedQuery("findAclByName");
		q.setParameter("name", name);
        List<Acl> acls = q.getResultList();
        if(acls.isEmpty()){
            return null;
        }
		return acls.get(0);
	}

	@Override
	public void delete(Long id) {
		Acl moribundus = get(id);
		makeTransient(moribundus);
		flush();
	}

	@Override
	public Acl get(Long id) {
		return getSession().find(Acl.class, id);
	}

	@Override
	public void update(Long id, Map<String, String> cmd) {
		Acl acl = get(id);
		if(acl == null){
			throw new CinnamonException("error.acl.not_found", id.toString());
		}
		if (cmd.containsKey("name")) {
			acl.setName(cmd.get("name"));
		}
		if (cmd.containsKey("description")) {
			acl.setDescription(cmd.get("description"));
		}
					
		// save object
		log.debug("Trying to save ACL " + id);
//		em.persist(acl);
		makePersistent(acl);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Acl> list() {
		Query q = getSession().createNamedQuery("selectAllAcls");
		return q.getResultList();
	}

}
