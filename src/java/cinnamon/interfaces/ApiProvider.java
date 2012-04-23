package cinnamon.interfaces;

import server.User;
import server.interfaces.Repository;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

public interface ApiProvider {
	
	void setEm(EntityManager em);
	EntityManager getEm();
	void setRes(HttpServletResponse res);
	HttpServletResponse getRes();
	void setUser(User user);
	User getUser();
	void setRepository(Repository repository);
	Repository getRepository();
	
}
