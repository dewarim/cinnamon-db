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
import server.dao.GenericDAO;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

/**
 * Implementation of GenericDAO with Hibernate.
 * 
 * @see <a href="http://www.hibernate.org/328.html">Generic Data Access Objects</a>
 * @author Stefan Rother
 *
 */
public abstract class GenericHibernateDAO<T, ID extends Serializable>
		implements GenericDAO<T, ID> {
	private Class<T> persistentClass;
	private EntityManager session;
    Logger log = LoggerFactory.getLogger(this.getClass());
    
	@SuppressWarnings("unchecked")
	public GenericHibernateDAO() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public void setSession(EntityManager s) {
		this.session = s;
	}

    public Boolean sessionIsClosed(){
        return ! session.isOpen();
    }
    
	protected EntityManager getSession() {
		if (session == null){
			throw new IllegalStateException("Session has not been set on DAO before usage");
        }
        if(sessionIsClosed()){
            log.warn("EntityManager is closed.");
        }
        return session;
    }

	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	public T makePersistent(T entity) {
		getSession().persist(entity);
		return entity;
	}

	public void makeTransient(T entity) {
		if(entity != null){
			getSession().remove(entity);
		}
	}

	public void flush() {
		getSession().flush();
	}

	public void clear() {
		getSession().clear();
	}

	public void delete(T entity){
		makeTransient(entity);
	}
	
}
