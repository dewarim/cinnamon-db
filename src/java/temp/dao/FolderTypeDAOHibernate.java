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
import server.FolderType;

import javax.persistence.Query;
import java.util.List;

public class FolderTypeDAOHibernate extends
		GenericHibernateDAO<FolderType, Long> implements FolderTypeDAO {
	
	private transient Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void delete(Long id) {
		FolderType moribundus = get(id);
		makeTransient(moribundus);
		flush();
	}

	@Override
	public FolderType get(Long id) {
		return getSession().find(FolderType.class, id);
	}


	@Override
	public FolderType findByName(String name) {
		log.debug("FolderType.findByName => "+name);
		Query q = getSession().createNamedQuery("selectFolderTypeByName");
		q.setParameter("name", name);
		if (q.getResultList().size() < 1)
			return null;
		return (FolderType) q.getSingleResult();
	}

	/* (non-Javadoc)
	 * @see server.dao.GenericDAO#list()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FolderType> list() {
		Query q = getSession().createNamedQuery("selectAllFolderTypes");		
		return q.getResultList();
	}
	
}
