package temp.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Session;
import server.User;
import server.dao.GenericHibernateDAO;
import server.dao.SessionDAO;

import javax.persistence.Query;
import java.util.List;

public class SessionDAOHibernate
            extends GenericHibernateDAO<Session, Long>
            implements SessionDAO {
	private Logger log = LoggerFactory.getLogger("server.dao.SessionDAOHibernate");

	@Override
    @SuppressWarnings("unchecked")
	public Session findByTicket(String ticket) {
		Query q	= getSession().createQuery("select s from Session s where ticket=:ticket");
		q.setParameter("ticket",ticket);
        List<Session> tickets = q.getResultList();
        return tickets.isEmpty() ? null : tickets.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Session> findByUser(User user){
		Query q = getSession().createQuery("select s from Session s where user=:user");
		q.setParameter("user", user);
		return q.getResultList();
	}
	

	public void setMessage(String ticket, String message){
		Session s = findByTicket(ticket);
		s.setMessage(message);

		flush();
	}
	
	public void setMessage(String ticket, String className, String message){		
		String msg = message + " " + className;
		Session session = findByTicket(ticket);
		session.setMessage(msg);
		log.debug("setMessage: "+session.getMessage());
	}
	
	@Override
	public void delete(String ticket) {
		log.debug("delete ticket: '"+ticket+"'");
		Session s = findByTicket(ticket);
		makeTransient(s);
    	flush();
	}

	@Override
	public Session get(Long id) {
		return getSession().find(Session.class, id);
	}
	
	@Override
	public void delete(Session session) {
		makeTransient(session);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Session> list() {
		Query q = getSession().createNamedQuery("selectAllSessions");
		return q.getResultList();
	}

	@Override
	public void delete(Long id) {
		Session s = get(id);
		makeTransient(s);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void deleteAll(){
		Query q = getSession().createNamedQuery("selectAllSessions");
		for(Session s : (List<Session>) q.getResultList()){
			makeTransient(s);
		}
	}
}
