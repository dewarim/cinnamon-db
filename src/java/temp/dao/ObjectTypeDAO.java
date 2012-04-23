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
import server.dao.GenericDAO;

import java.util.List;
import java.util.Set;

/**
 * The DAO interface for a particular entity extends the generic interface and provides the type arguments.
 * 
 * @see <a href="http://www.hibernate.org/328.html">Generic Data Access Objects</a>
 * @author Stefan Rother
 *
 */
public interface ObjectTypeDAO extends GenericDAO<ObjectType, Long> {
	/**
	 * Delete an empty folder specified by the id-parameter.
	 * @param id folder id</li>
	 */
	void delete(Long id);

	/**
	 * Resolves an object type name to an ObjectType instance
	 * @param name name of the object type
	 * @return List<ObjectType>
	 */
//	List<ObjectType> findAllByName(String name);

	/**
	 * Returns the ObjectType that matches the name or null.
	 * @param name name of the object type
	 * @return ObjectType
	 */
	ObjectType findByName(String name);

    /**
     * Find all ObjectTypes by the given list of names. This method is useful
     * if you want to check if an OSD's object type matches an entry from a list of
     * object type names.
     * @param names list of names
     * @return set of object types
     */
    Set<ObjectType> findAllByNameList(List<String> names);

}
