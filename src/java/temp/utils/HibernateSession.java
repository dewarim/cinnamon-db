// cinnamon - the Open Enterprise CMS project
// Copyright (C) 2007-2009 Horner GmbH (http://www.horner-project.eu)
// 
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
// (or visit: http://www.gnu.org/licenses/lgpl.html)

package temp.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.exceptions.CinnamonConfigurationException;
import utils.DatabaseConfig;
import utils.PersistenceSessionProvider;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/**
 * HibernateSessions is a utility class for both Cinnamon and the workflow-server.
 * It stores database connections in ThreadLocal-vars so that EntityManagers are available
 * for persistence operations in all classes.<br>
 * The use of a mechanism that amounts to global vars is appropriate as EntityManagerFactories are
 * singletons and we only use one EntityManager per thread of each kind. (The use of two EMs with
 * connections to the same database is not recommended - seems buggy.)<br>
 * 
 * TODO: explain EntityTransaction-Handling (=> rollback etc)
 * @author ingo
 *
 */

public class HibernateSession {
	
	private transient Logger log = LoggerFactory.getLogger(this.getClass());
	
	EntityManagerFactory emf;
	SessionFactory factory;
	
	/**
	 * The static localEntityManager holds the current EntityManager for the main application. This
	 * can be either the workflow engine or the Cinnamon server, depending on who's running the show.
	 * You need to make sure that mechanisms which bridge the gap between the workflow server and the
	 * CMS use the correct EM. (Example: the method Folder.findWorkflowFolder needs no EM-param when used
	 * by the CMS, but it needs one when used by the workflow-engine - otherwise it starts looking
	 * for this folder in the workflow-db.) 
	 */
	public static ThreadLocal<EntityManager> localEntityManager = new ThreadLocal<EntityManager>();

    public static ThreadLocal<String> localRepositoryName = new ThreadLocal<String>();

	/**
	 * The static repositories HashMap contains several EntityManagers for the workflow-engine,
	 * which may have to retrieve business objects from several databases.<br>
	 * Given the information in which repository an object resides, this object can use its own EM to 
	 * fetch and edit further items in the same database. Example: the workflow server (using 
	 * the localEntityManager) loads a finished workflow task object, whose metadata is stored
	 * in a business object in a Cinnamon db. If we want to retrieve or change the metadata, we need an
	 * EM for the Cinnamon db, which is stored in the corresponding repository.
	 * 
	 */
	public static ThreadLocal< HashMap<String, EntityManager>> repositories = new ThreadLocal<HashMap<String,EntityManager>>(){
		protected HashMap<String, EntityManager> initialValue() {
			return new HashMap<String, EntityManager>();
		}
	};
	
	/**
	 * Map <connection_url, EMF> of factories
	 */
	public static HashMap<String, EntityManagerFactory> emf_hash = new HashMap<String, EntityManagerFactory>();


	/**
	 *
	 * @param conf - a DatabaseConfig object which generates the JDBC-connection-URL
	 * @param repository - a param for the DatabaseConfig (db-name)
	 * @param persistence_unit - the name of the persistence unit from META-INF/persistence.xml
	 */

	public HibernateSession(DatabaseConfig conf, String repository, String persistence_unit){
		this(conf.getDatabaseConnectionURL(repository) , repository, persistence_unit);
	}
	
	public HibernateSession(String url, String repository, String persistence_unit){
		// choose repository database:
		Map<String,String> myProperties = new HashMap<String,String>();
		
		log.debug("Using hibernate.connection.url "+ url);		
		myProperties.put("hibernate.connection.url", url);
		
		if (emf_hash.containsKey(url)) {
			log.debug("re-using old EntityManagerFactory");
			emf = emf_hash.get(url);
		} else {
			log.debug("creating new EntityManagerFactory");
			emf = Persistence.createEntityManagerFactory(persistence_unit, myProperties);
			emf_hash.put(url, emf);
		}
	}
	
	/**
	 * 
	 * @param cr
	 * @return the current EntityManager for this repository - or a new one if its null.<br>
	 *  
	 */
	public static EntityManager getRepositoryEntityManager(PersistenceSessionProvider cr){
		String url = cr.getUrl();
		EntityManager em = repositories.get().get(url);
		if (em == null) {
			// unregistered repository: create new EM:
			HibernateSession hs = new HibernateSession( url, cr.getName(), cr.getPersistenceUnit());
			em = hs.getEntityManager();
			repositories.get().put(url, em);
		}	
		return em;
	}

	/**
	 * Closes the old {@link javax.persistence.EntityManager} and sets the repository to use the new one.<br>
	 * You can use setRepositoryEntityManager(cr,null) followed by getRepositoryEntityManager(cr)
	 * to get a fresh EM. 
	 * @param cr
	 * @param em
	 */
	public static void setRepositoryEntityManager(PersistenceSessionProvider cr, EntityManager em){
		String url = cr.getUrl();
		EntityManager oldEm = repositories.get().get(url);
		if (oldEm != null) {
			oldEm.close();
			
		}
		repositories.get().put(url, em);
	}
	
	public static ThreadLocal<HashMap<String, EntityManager>> getRepositories() {
		return repositories;
	}

	public static void setRepositories(
			ThreadLocal<HashMap<String, EntityManager>> repositories) {
		HibernateSession.repositories = repositories;
	}

	/**
	 * Create a new EntityManager.
	 * @return a fresh EntityManager for this HibernateSession's database.
	 */
	public EntityManager getEntityManager(){
		return emf.createEntityManager();
	}
		
	public Session getDom4jSession(){
		return factory.openSession();
	}

	public static HashMap<String, EntityManagerFactory> getEmf_hash() {
		return emf_hash;
	}

	public static void setEmf_hash(HashMap<String, EntityManagerFactory> emf_hash) {
		HibernateSession.emf_hash = emf_hash;
	}

	public EntityManagerFactory getEmf() {
		return emf;
	}

	public void setEmf(EntityManagerFactory emf) {
		this.emf = emf;
	}

	public SessionFactory getFactory() {
		return factory;
	}

	public void setFactory(SessionFactory factory) {
		this.factory = factory;
	}

	public static EntityManager getLocalEntityManager() {
		EntityManager em = localEntityManager.get();
		Logger log = LoggerFactory.getLogger(utils.HibernateSession.class);
		if (em == null) {
			log.error("Empty EntityManager retrieved!");
			try{
				throw new RuntimeException("Empty EntityManager!");
			}
			catch (Exception e) {
				log.debug("Trying to find out which method dares to call the local EM out of place.\n",e);
			}
		}
		else if (! em.isOpen()) {
			log.error("EntityManager is not open!");
			throw new CinnamonConfigurationException("You tried to retrieve a closed EntityManager!");
		}
//		log.debug("using local EntityManager: "+em);
		return em;
	}
	
	public static Boolean hasLocalEntityManager(){
		return localEntityManager.get() != null;
	}

    public static void setLocalEntityManager(
            ThreadLocal<EntityManager> localEntityManager) {
        HibernateSession.localEntityManager = localEntityManager;
    }

	public static void setLocalEntityManager(
			EntityManager em) {
		HibernateSession.localEntityManager.set(em);		
	}

	/**
	 * Close all EntityManagers (that is, both localEntityManager and those in repositories hash). 
	 */
	static public void closeAll(){
		try{
			if (getLocalEntityManager().isOpen()){
				getLocalEntityManager().close();
			}
		}
		catch (Exception e) {
			Logger log = LoggerFactory.getLogger(utils.HibernateSession.class);
			log.debug("closeAll failed: ",e);
		}
		
		for (EntityManager em : repositories.get().values()) {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}

    public static ThreadLocal<String> getLocalRepositoryNameThread() {
        return localRepositoryName;
    }

    public static String getLocalRepositoryName(){
        return localRepositoryName.get();
    }

    public static void setLocalRepositoryName(ThreadLocal<String> localRepositoryName) {
        HibernateSession.localRepositoryName = localRepositoryName;
    }

    public static void setLocalRepositoryName(String localRepositoryName) {
        Logger log = LoggerFactory.getLogger(HibernateSession.class);
        log.debug("set repositoryName to:"+localRepositoryName);
        HibernateSession.localRepositoryName.set(localRepositoryName);
    }
}
