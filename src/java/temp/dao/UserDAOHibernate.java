package temp.dao;

import cinnamon.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Group;
import server.GroupUser;
import server.Session;
import server.User;
import server.dao.DAOFactory; // unnecessary imports included because IntelliJ does not find them otherwise.
import server.dao.GenericHibernateDAO;
import server.dao.GroupDAO;
import server.dao.UserDAO;
import utils.ParamParser;

import javax.persistence.Query;
import java.util.List;

public class UserDAOHibernate extends GenericHibernateDAO<UserAccount, Long> implements
        UserDAO {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	static DAOFactory daoFactory = DAOFactory.instance(DAOFactory.HIBERNATE);
	
	@Override
	public UserAccount get(Long id) {
		return getSession().find(UserAccount.class, id);
	}
	
	@Override
	public UserAccount findByName(String name) {
		Query q = getSession().createNamedQuery("findUserByName");
		q.setParameter("name", name);
		return (UserAccount) q.getSingleResult();
	}

	@Override
	public UserAccount findByNameAndPassword(String name, String password) {
//		log.debug(String.format("findByNameAndPassword: '%s' '%s'", name, password));
		
		Query q = getSession().createNamedQuery("findUserByNameAndPassword");
		q.setParameter("name", name);
		q.setParameter("pwd", password);
		
//		log.debug("Starting Query");
		return (UserAccount) q.getSingleResult();
	}

	@Override
	public void delete(Long id) {
	    UserAccount moribundus = get(id);
	    
	    // remove possible open sessions (for example, those left by loss of connection)
	    server.dao.SessionDAO sessionDAO = daoFactory.getSessionDAO(getSession());
	    List<Session> sessionList = sessionDAO.findByUser(moribundus);
	    for(Session s : sessionList){
	    	sessionDAO.makeTransient(s);
	    }
	    
	    // do not delete the user's group in case he is reactivated.
//	    GroupDAO groupDAO = factory.getGroupDAO(getSession());	    
//	    CmnGroup myGroup = groupDAO.findByUser(moribundus);
//	    groupDAO.makeTransient(myGroup);

	    /*
	     *  Users usually cannot be deleted (because of the Objects they
	     *  own inside the db). They are set to inactive instad.
	     */	    
	    moribundus.setActivated(false);
		flush(); // really necessary? 
	}

	@Override
	public Long addToGroup(Long userID, Long groupID) {
		UserAccount user = get(userID);
	    GroupDAO groupDAO = daoFactory.getGroupDAO(getSession());
		Group group = groupDAO.get(groupID);
		
		GroupUser gu = new GroupUser(user, group);
		getSession().persist(gu);
		return gu.getId();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<UserAccount> list() {
		Query q = getSession().createNamedQuery("selectAllUsers");
		return q.getResultList();
	}

	@Override
	public UserAccount get(String id) {
		Long userId = ParamParser.parseLong(id, "error.param.id"); 
		return get(userId);
	}
}
