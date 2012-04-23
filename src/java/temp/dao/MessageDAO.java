package temp.dao;

import server.dao.GenericDAO;
import server.i18n.Language;
import server.i18n.Message;
import server.i18n.UiLanguage;

public interface MessageDAO extends GenericDAO<Message, Long> {
	
	/**
	 * Find the translation of a specific message string.
	 * @param message
	 * @param language
	 * @return the translation or the English message string if nothing was found.
	 */
	Message findMessage(String message, UiLanguage language);
	
}