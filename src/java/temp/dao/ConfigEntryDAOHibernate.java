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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.ConfigEntry;

import javax.persistence.Query;
import java.util.List;

public class ConfigEntryDAOHibernate extends
		GenericHibernateDAO<ConfigEntry, Long> implements ConfigEntryDAO {
	
	private transient Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void delete(Long id) {
		ConfigEntry moribundus = get(id);
		makeTransient(moribundus);
		flush();
	}

	@Override
	public ConfigEntry get(Long id) {
		return getSession().find(ConfigEntry.class, id);
	}


	@Override
	public ConfigEntry findByName(String name) {
		log.debug("ConfigEntry.findByName => "+name);
		Query q = getSession().createNamedQuery("selectConfigEntryByName");
		q.setParameter("name", name);
		if (q.getResultList().size() < 1){
			return null;
        }
		return (ConfigEntry) q.getSingleResult();
	}

	/* (non-Javadoc)
	 * @see server.dao.GenericDAO#list()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigEntry> list() {
		Query q = getSession().createNamedQuery("selectAllConfigEntries");
		return q.getResultList();
	}
	
}
