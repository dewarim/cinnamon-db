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

import server.ObjectType;
import server.dao.GenericHibernateDAO;
import server.dao.ObjectTypeDAO;

import javax.persistence.Query;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ObjectTypeDAOHibernate extends
        GenericHibernateDAO<ObjectType, Long> implements ObjectTypeDAO {

	@Override
	public void delete(Long id) {
		ObjectType moribundus = get(id);
		makeTransient(moribundus);
		flush();
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public List<ObjectType> findAllByName(String name) {
//		List<ObjectType> ret;
//		Query q = getSession().createNamedQuery("findObjectTypeByName");
//		q.setParameter("name", name);
//		ret = (List<ObjectType>) q.getResultList();
//		return ret;
//	}

	@Override
	public ObjectType get(Long id) {
		return getSession().find(ObjectType.class, id);
	}


	@Override
	public ObjectType findByName(String name) {
		Query q = getSession().createNamedQuery("findObjectTypeByName");
		q.setParameter("name", name);
		if (q.getResultList().size() < 1){
			return null;
        }
		return (ObjectType) q.getSingleResult();
	}

    @Override
    public Set<ObjectType> findAllByNameList(List<String> names) {
        // this is probably not the fastest possible implementation - but it's the easiest.
        Set<ObjectType> oTypes = new HashSet<ObjectType>();
        for(String name : names){
            ObjectType ot = findByName(name);
            if(ot != null){
                oTypes.add(ot);
            }
        }
        return oTypes;
    }

    /* (non-Javadoc)
      * @see server.dao.GenericDAO#list()
      */
	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectType> list() {
		Query q = getSession().createNamedQuery("selectAllObjectTypes");		
		return q.getResultList();
	}
	
}
