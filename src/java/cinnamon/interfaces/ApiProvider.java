package cinnamon.interfaces;


import cinnamon.UserAccount;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

public interface ApiProvider {
	
	void setEm(EntityManager em);
	EntityManager getEm();
	void setRes(HttpServletResponse res);
	HttpServletResponse getRes();
	void setUser(UserAccount user);
	UserAccount getUser();
	void setRepository(Repository repository);
	Repository getRepository();
	
}
