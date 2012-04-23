package temp.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.dao.GenericHibernateDAO;
import server.dao.MessageDAO;
import server.i18n.Language;
import server.i18n.Message;
import server.i18n.UiLanguage;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class MessageDAOHibernate  extends GenericHibernateDAO<Message, Long> implements MessageDAO {
	
	transient Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public Message get(Long id) {
		return getSession().find(Message.class, id);
	}


	/* (non-Javadoc)
	 * @see server.dao.MessageDAO#findByName(java.lang.String)
	 */
	@Override
	public Message findMessage(String message, UiLanguage language) {
		Query q =  getSession().createNamedQuery("findMessageByNameAndUiLanguage");
		q.setParameter("message", message);
		q.setParameter("language", language);
		Message m;
		try{
			m = (Message) q.getSingleResult();
		}
		catch (NoResultException e) {
			m = null;
		}
		return m;
	}

	/* (non-Javadoc)
	 * @see server.dao.MessageDAO#list()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Message> list() {
		Query q = getSession().createNamedQuery("selectAllMessages");
		return q.getResultList();
	}
	
	/* (non-Javadoc)
	 * @see server.dao.GenericDAO#delete(java.lang.Long)
	 */
	@Override
	public void delete(Long id) {
		Message i = get(id);
		makeTransient(i);
	}
	
	
	
}
