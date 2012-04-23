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

import java.io.Serializable;
import java.util.List;

/**
 * Super interface for common CRUD functionality.
 * 
 * @see <a href="http://www.hibernate.org/328.html">Generic Data Access Objects</a>
 * @author Stefan Rother
 *
 */
public interface GenericDAO<T, ID extends Serializable> {
	/**
	 * Save an entity to the database.
	 * @param entity
	 * @return the entity which was saved to the database. 
	 */
	T makePersistent(T entity);

	/**
	 * Remove an entity from the database. Do not call from outside
	 * a DAO-class.
	 * @param entity
	 */
	void makeTransient(T entity);
	
	
	// CRUD default operations:
	
	/**
	 * @return T - the specified object, or null.
	 */
	T get(Long id);
	
	/**
	 * Remove an entity from the database (T entity).
	 * <br/>
	 * Note: outside of DAOs, you should call delete(id) instead of 
	 * makeTransient(), because the delete-Action may perform further
	 * cleanup steps.
	 * 
	 * @param id the id of the entity to delete.
	 */
	void delete(Long id); // synonym with makeTransient(T entity).
	
	/**
	 * 
	 * @return a List of all objects of this type. Be careful and do not
	 * try to load the entire database in one go.
	 */
	List<T> list();

    /**
     * Check if the EntityManager of this DAO object is still open.
     * This is useful to debug IllegalStateExceptions when some code updates some but not all EMs after
     * encountering an exception.
     * @return true if the EntityManager of this DAO is closed.
     */
    Boolean sessionIsClosed();
}
