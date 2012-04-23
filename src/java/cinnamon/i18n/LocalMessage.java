package cinnamon.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.dao.MessageDAO;
import server.exceptions.CinnamonConfigurationException;
import server.i18n.Language;
import server.i18n.Message;
import server.i18n.UiLanguage;
import server.interfaces.ILocalizer;
import temp.i18n.Message;

/**
 * The LocalMessage stores a threadLocal MessageDAO. This way every class which needs
 * to localize a message can do so without having to instantiate a new MessageDAO and managing
 * the associated EntityManager.<br/>
 * <em>You must call initializeLocalMessage before using the loc-method!</em>
 */
public class LocalMessage implements ILocalizer{
	@SuppressWarnings("unused")
	private transient Logger log = LoggerFactory.getLogger(LocalMessage.class);
    private Boolean initialized = false;

	static ThreadLocal<LocalMessage> localMessage = new ThreadLocal<LocalMessage>(){
		@Override
		protected LocalMessage initialValue() {
			LocalMessage c;
			try {
				c = new LocalMessage();
			} catch (Exception e) {
				LoggerFactory.getLogger(LocalMessage.class).debug("", e);
				throw new CinnamonConfigurationException("Could not initialize LocalMessage: "+e.getLocalizedMessage());
			}
			return c;
		}
	};
	
	private MessageDAO messageDao;
	private temp.i18n.UiLanguage language;
	
	private LocalMessage(){}
	
	public static ThreadLocal<LocalMessage> getInstance() {
		return localMessage;
	}
	
	public static void setLocalDao(MessageDAO messageDao){
		LocalMessage.getInstance().get().messageDao = messageDao; 
	}
	
	public static void setLocalLanguage(temp.i18n.UiLanguage language){
		LocalMessage.getInstance().get().language = language;
	}
	
	public static void initializeLocalMessage(MessageDAO messageDao, temp.i18n.UiLanguage language){
		setLocalDao(messageDao);
		setLocalLanguage(language);
        getInstance().get().setInitialized(true);
	}

	public static MessageDAO getLocalDao(){
		return LocalMessage.getInstance().get().messageDao;
	}
	
	/**
	 * Localize a message string using the current user session's language. 
	 * @param message message id that has to be localized.
	 * @return the localized string, or - if no translation is available - the original string.
	 */
	public static String loc(String message){
//		Logger log = LoggerFactory.getLogger(LocalMessage.class);
		MessageDAO msgDao = getLocalDao();
//		log.debug("MessageDao: "+msgDao);
		ThreadLocal<LocalMessage> tl = getInstance();
//		log.debug("ThreadLocal: "+tl);
		LocalMessage lm = tl.get();
        if(lm == null || msgDao == null){
            // may not be properly initialized when used in Dandelion / admin tool.
            // it would be nice if there were a way to initialize the EntityManagers properly in Grails...
            return message;
        }
//		log.debug("LocalMessage: "+lm);
		temp.i18n.Message msg = msgDao.findMessage(message, lm.language);
		if(msg == null){
			return message;
		}
		else{
			return msg.getTranslation();
		}
	}
	
	public String localize(String message){
		return LocalMessage.loc(message);
	}
	
	/**
	 * Localize a message string
	 * @param message the message id that has to be localized
	 * @param lang the UI language
	 * @return the localized string, or - if no translation is available - the original string.
	 */
	public static String loc(String message, temp.i18n.UiLanguage lang){
		Message msg = getLocalDao().findMessage(message, lang);
		if(msg == null){
			return message;
		}
		else{
			return msg.getMessage();
		}
	}

    public Boolean getInitialized() {
        return initialized;
    }

    public void setInitialized(Boolean initialized) {
        this.initialized = initialized;
    }

    public static Boolean wasInitialized(){
        return getInstance().get().getInitialized();
    }
}
