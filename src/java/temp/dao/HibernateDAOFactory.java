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
import server.dao.AclDAO;
import server.dao.AclEntryDAO;
import server.dao.AclEntryDAOHibernate;
import server.dao.AclEntryPermissionDAO;
import server.dao.AclEntryPermissionDAOHibernate;
import server.dao.ChangeTriggerDAO;
import server.dao.ChangeTriggerDAOHibernate;
import server.dao.ChangeTriggerTypeDAO;
import server.dao.ChangeTriggerTypeDAOHibernate;
import server.dao.CustomTableDAO;
import server.dao.CustomTableDAOHibernate;
import server.dao.DAOFactory;
import server.dao.FolderDAO;
import server.dao.FolderDAOHibernate;
import server.dao.FolderTypeDAO;
import server.dao.FolderTypeDAOHibernate;
import server.dao.FormatDAO;
import server.dao.FormatDAOHibernate;
import server.dao.GenericHibernateDAO;
import server.dao.GroupDAO;
import server.dao.GroupDAOHibernate;
import server.dao.IndexGroupDAO;
import server.dao.IndexGroupDAOHibernate;
import server.dao.IndexItemDAO;
import server.dao.IndexItemDAOHibernate;
import server.dao.IndexTypeDAO;
import server.dao.IndexTypeDAOHibernate;
import server.dao.LanguageDAO;
import server.dao.LanguageDAOHibernate;
import server.dao.LifeCycleDAO;
import server.dao.LifeCycleDAOHibernate;
import server.dao.LifeCycleStateDAO;
import server.dao.LifeCycleStateDAOHibernate;
import server.dao.MessageDAO;
import server.dao.MessageDAOHibernate;
import server.dao.ObjectSystemDataDAO;
import server.dao.ObjectSystemDataDAOHibernate;
import server.dao.ObjectTypeDAO;
import server.dao.ObjectTypeDAOHibernate;
import server.dao.PermissionDAO;
import server.dao.PermissionDAOHibernate;
import server.dao.RelationDAO;
import server.dao.RelationDAOHibernate;
import server.dao.RelationTypeDAO;
import server.dao.RelationTypeDAOHibernate;
import server.dao.SessionDAO;
import server.dao.SessionDAOHibernate;
import server.dao.TransformerDAO;
import server.dao.TransformerDAOHibernate;
import server.dao.UiLanguageDAO;
import server.dao.UiLanguageDAOHibernate;
import server.dao.UserDAO;
import server.dao.UserDAOHibernate;
import server.exceptions.CinnamonConfigurationException;

import javax.persistence.EntityManager;
import server.dao.RelationResolverDAO;
import server.dao.RelationResolverDAOHibernate;

/**
 * Factory for DAO instances.
 * 
 * @see <a href="http://www.hibernate.org/328.html">Generic Data Access Objects</a>
 * @author Stefan Rother
 *
 */
public class HibernateDAOFactory extends DAOFactory {

	@Override
	public FormatDAO getFormatDAO(EntityManager session) {
		return (FormatDAO) instantiateDAO(FormatDAOHibernate.class, session);
	}
	
	@Override
	public AclDAO getAclDAO(EntityManager session) {
		return (AclDAO) instantiateDAO(server.dao.AclDAOHibernate.class, session);
	}

	@Override
	public CustomTableDAO getCustomTableDAO(EntityManager session) {
		return (CustomTableDAO) instantiateDAO(CustomTableDAOHibernate.class, session);
	}
	
	@Override
	public GroupDAO getGroupDAO(EntityManager session) {
		return (GroupDAO) instantiateDAO(GroupDAOHibernate.class, session);
	}

	@Override
	public SessionDAO getSessionDAO(EntityManager session) {
		return (SessionDAO) instantiateDAO(SessionDAOHibernate.class, session);
	}

	@Override
	public UserDAO getUserDAO(EntityManager session) {
		return (UserDAO) instantiateDAO(UserDAOHibernate.class, session);
	}

	@Override
	public RelationTypeDAO getRelationTypeDAO(EntityManager session) {
		return (RelationTypeDAO) instantiateDAO(RelationTypeDAOHibernate.class, session);
	}

	@Override
	public RelationDAO getRelationDAO(EntityManager session) {
		return (RelationDAO) instantiateDAO(RelationDAOHibernate.class, session);
	}

	@Override
	public ObjectSystemDataDAO getObjectSystemDataDAO(EntityManager session) {
		return (ObjectSystemDataDAO) instantiateDAO(ObjectSystemDataDAOHibernate.class, session);
	}

	@Override
	public ObjectTypeDAO getObjectTypeDAO(EntityManager session) {
		return (ObjectTypeDAO) instantiateDAO(ObjectTypeDAOHibernate.class, session);
	}

	@Override
	public FolderDAO getFolderDAO(EntityManager session) {
		return (FolderDAO) instantiateDAO(FolderDAOHibernate.class, session);
	}
	
	@Override
	public AclEntryDAO getAclEntryDAO(EntityManager session) {
		return (AclEntryDAO) instantiateDAO(AclEntryDAOHibernate.class, session);
	}
	
	@Override
	public AclEntryPermissionDAO getAclEntryPermissionDAO(EntityManager session) {
		return (AclEntryPermissionDAO) instantiateDAO(AclEntryPermissionDAOHibernate.class, session);
	}

	/* (non-Javadoc)
	 * @see server.dao.DAOFactory#getPermissionDAO(javax.persistence.EntityManager)
	 */
	@Override
	public PermissionDAO getPermissionDAO(EntityManager session) {
		return (PermissionDAO) instantiateDAO(PermissionDAOHibernate.class, session);
	}
	
	/* (non-Javadoc)
	 * @see server.dao.DAOFactory#getLanguageDAO(javax.persistence.EntityManager)
	 */
	@Override
	public LanguageDAO getLanguageDAO(EntityManager session) {
		return (LanguageDAO) instantiateDAO(LanguageDAOHibernate.class, session);
	}

    @Override
    public LifeCycleDAO getLifeCycleDAO(EntityManager session) {
        return (LifeCycleDAO) instantiateDAO(LifeCycleDAOHibernate.class, session);
    }

    @Override
    public LifeCycleStateDAO getLifeCycleStateDAO(EntityManager session) {
        return (LifeCycleStateDAO) instantiateDAO(LifeCycleStateDAOHibernate.class, session);
    }

    @Override
    public UiLanguageDAO getUiLanguageDAO(EntityManager session) {
        return (UiLanguageDAO) instantiateDAO(UiLanguageDAOHibernate.class, session);
    }

    @SuppressWarnings("unchecked")
	private GenericHibernateDAO<?, ?> instantiateDAO(Class daoClass,
			EntityManager session) {
		try {
			GenericHibernateDAO<?, ?> dao = (GenericHibernateDAO<?, ?>) daoClass
					.newInstance();
			dao.setSession(session);
			return dao;
		} catch (Exception ex) {
			throw new CinnamonConfigurationException("Can not instantiate DAO: " + daoClass,
					ex);
		}
	}

	@Override
	public IndexItemDAO getIndexItemDAO(EntityManager session) {
		return (IndexItemDAO) instantiateDAO(IndexItemDAOHibernate.class, session);
	}

	@Override
	public IndexTypeDAO getIndexTypeDAO(EntityManager session) {
		return (IndexTypeDAO) instantiateDAO(IndexTypeDAOHibernate.class, session);
	}
	
	@Override
	public IndexGroupDAO getIndexGroupDAO(EntityManager session) {
		return (IndexGroupDAO) instantiateDAO(IndexGroupDAOHibernate.class, session);
	}
	
	@Override
	public FolderTypeDAO getFolderTypeDAO(EntityManager session) {
		return (FolderTypeDAO) instantiateDAO(FolderTypeDAOHibernate.class, session);
	}
	
	@Override
	public MessageDAO getMessageDAO(EntityManager session) {
		return (MessageDAO) instantiateDAO(MessageDAOHibernate.class, session);
	}

	@Override
	public ChangeTriggerDAO getChangeTriggerDAO(EntityManager session) {
		return (ChangeTriggerDAO) instantiateDAO(ChangeTriggerDAOHibernate.class, session);
	}

	@Override
	public ChangeTriggerTypeDAO getChangeTriggerTypeDAO(EntityManager session) {
		return (ChangeTriggerTypeDAO) instantiateDAO(ChangeTriggerTypeDAOHibernate.class, session);
	}

	@Override
	public TransformerDAO getTransformerDAO(EntityManager session) {
		return (TransformerDAO) instantiateDAO(TransformerDAOHibernate.class, session);
	}

    @Override
	public RelationResolverDAO getRelationResolverDAO(EntityManager session) {
		return (RelationResolverDAO) instantiateDAO(RelationResolverDAOHibernate.class, session);
	}

    @Override
    public ConfigEntryDAO getConfigEntryDAO(EntityManager session) {
        return (ConfigEntryDAO) instantiateDAO(ConfigEntryDAOHibernate.class, session);
    }
}
