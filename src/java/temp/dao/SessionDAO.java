package temp.dao;

import server.Session;
import server.User;

import java.util.List;

public interface SessionDAO extends GenericDAO<Session, Long> {

    /**
     * Find a session by its ticket.
     * @param ticket the ticket of the session
     * @return the requested session or null if the session was not found.
     */
	Session findByTicket(String ticket);

	List<Session> findByUser(User user);
	void delete(String ticket);
	void delete(Session session);
	/**
	 * Delete all Sessions from the session table. 
	 */
	void deleteAll();
	void setMessage(String ticket, String message);
	void setMessage(String ticket, String className, String message);
}
