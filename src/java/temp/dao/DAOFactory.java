// cinnamon - the Open Enterprise CMS project
// Copyright (C) 2007 Dr.-Ing. Boris Horner
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
package temp.dao;

import server.ConfigEntry;
import server.dao.*;
// the following imports were added because IntelliJ does not find them otherwise:
import server.dao.ObjectSystemDataDAO;
import server.dao.AclDAO;
import server.dao.AclEntryDAO;
import server.dao.AclEntryPermissionDAO;
import server.dao.ChangeTriggerDAO;
import server.dao.ChangeTriggerTypeDAO;
import server.dao.CustomTableDAO;
import server.dao.FolderDAO;
import server.dao.FolderTypeDAO;
import server.dao.FormatDAO;
import server.dao.GroupDAO;
import server.dao.IndexGroupDAO;
import server.dao.IndexItemDAO;
import server.dao.IndexTypeDAO;
import server.dao.LanguageDAO;
import server.dao.LifeCycleDAO;
import server.dao.LifeCycleStateDAO;
import server.dao.MessageDAO;
import server.dao.ObjectTypeDAO;
import server.dao.PermissionDAO;
import server.dao.RelationDAO;
import server.dao.RelationResolverDAO;
import server.dao.RelationTypeDAO;
import server.dao.SessionDAO;
import server.dao.TransformerDAO;
import server.dao.UiLanguageDAO;
import server.dao.UserDAO;
import server.exceptions.CinnamonConfigurationException;

import javax.persistence.EntityManager;

/**
 * @see <a href="http://www.hibernate.org/328.html">Generic Data Access Objects</a>
 * @author Stefan Rother
 *
 */
public abstract class DAOFactory {
	/**
	 * Creates a standalone DAOFactory that returns unmanaged DAO beans for use
	 * in any environment Hibernate has been configured for. Uses
	 * HibernateUtil/SessionFactory and Hibernate context propagation
	 * (CurrentSessionContext), thread-bound or transaction-bound, and
	 * transaction scoped.
	 */
	@SuppressWarnings("unchecked")
	public static final Class HIBERNATE = server.dao.HibernateDAOFactory.class;

	/**
	 * Factory method for instantiation of concrete factories.
     * @param factory the Class factory which defines the kind of DAO class returned by this method..
     * @return a DAOFactory which will produce DAOs of the specified type.
     */
	@SuppressWarnings("unchecked")
	public static DAOFactory instance(Class factory) {
		try {
			return (DAOFactory) factory.newInstance();
		} catch (Exception ex) {
			throw new CinnamonConfigurationException("Couldn't create DAOFactory: " + factory, ex);
		}
	}

	// Add your DAO interfaces here
	// public abstract FolderDAO getFolderDAO();
	public abstract FolderDAO getFolderDAO(EntityManager session);
	public abstract FolderTypeDAO getFolderTypeDAO(EntityManager session);
	public abstract ObjectTypeDAO getObjectTypeDAO(EntityManager session);
	public abstract ObjectSystemDataDAO getObjectSystemDataDAO(EntityManager session);
	public abstract RelationDAO getRelationDAO(EntityManager session);
	public abstract RelationResolverDAO getRelationResolverDAO(EntityManager session);
	public abstract RelationTypeDAO getRelationTypeDAO(EntityManager session);
	public abstract UserDAO getUserDAO(EntityManager session);
	public abstract SessionDAO getSessionDAO(EntityManager session);
	public abstract GroupDAO getGroupDAO(EntityManager session);
	public abstract AclDAO getAclDAO(EntityManager session);
	public abstract FormatDAO getFormatDAO(EntityManager session);
	public abstract CustomTableDAO getCustomTableDAO(EntityManager session);
	public abstract AclEntryDAO getAclEntryDAO(EntityManager session);
	public abstract AclEntryPermissionDAO getAclEntryPermissionDAO(EntityManager session);
	public abstract PermissionDAO getPermissionDAO(EntityManager session);
	public abstract LanguageDAO getLanguageDAO(EntityManager session);
    public abstract LifeCycleDAO getLifeCycleDAO(EntityManager session);
    public abstract LifeCycleStateDAO getLifeCycleStateDAO(EntityManager session);
	public abstract IndexTypeDAO getIndexTypeDAO(EntityManager session);
	public abstract IndexItemDAO getIndexItemDAO(EntityManager session);
	public abstract IndexGroupDAO getIndexGroupDAO(EntityManager session);
	public abstract MessageDAO getMessageDAO(EntityManager session);
	public abstract ChangeTriggerDAO getChangeTriggerDAO(EntityManager session);
	public abstract ChangeTriggerTypeDAO getChangeTriggerTypeDAO(EntityManager session);
	public abstract TransformerDAO getTransformerDAO(EntityManager session);
    public abstract UiLanguageDAO getUiLanguageDAO(EntityManager session);
    public abstract ConfigEntryDAO getConfigEntryDAO(EntityManager session);

}
