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

import server.FolderType;

/**
 * The DAO interface for a particular entity extends the generic interface and provides the type arguments.
 * 
 * @see <a href="http://www.hibernate.org/328.html">Generic Data Access Objects</a>
 * @author Stefan Rother
 *
 */
public interface FolderTypeDAO extends GenericDAO<FolderType, Long> {
	/**
	 * Delete an empty folder specified by the id-parameter.
	 * @param id folder id
	 */
	void delete(Long id);

	/**
	 * Returns the FolderType that matches the name or null.
	 * @param name of the FolderType object you are looking for.
	 * @return FolderType
	 */
	FolderType findByName(String name);

}
