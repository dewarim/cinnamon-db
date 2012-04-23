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
import server.exceptions.CinnamonConfigurationException;
import server.exceptions.CinnamonException;
import server.i18n.Language;
import utils.ParamParser;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;

/**
 * Concrete folder DAO implementation.
 * 
 * @see <a href="http://www.hibernate.org/328.html">Generic Data Access Objects</a>
 * @author Ingo Wiarda
 *
 */
public class LanguageDAOHibernate extends GenericHibernateDAO<Language, Long>
		implements LanguageDAO {

	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void delete(Long id) {
		Language l = get(id);
		makeTransient(l);
	}

	@Override
	public void update(Long id, Map<String, String> fields){
		// load object and change with given params:
		Language lang = get(id);
		if(lang == null){
			throw new CinnamonException("error.object.not.found");
		}
		if (fields.containsKey("iso_code")) {
			String isoCode = fields.get("iso_code");
			lang.setIsoCode(isoCode);
		}
	}


	@Override
	public Language get(Long id) {
		return getSession().find(Language.class, id);
	}

	@Override
	public Language findByIsoCode(String isoCode) {
		Query q = getSession().createNamedQuery("findLanguageByIsoCode");
		q.setParameter("isoCode", isoCode);
		return (Language) q.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Language> list() {
		Query q = getSession().createNamedQuery("selectAllLanguages");		
		return q.getResultList();
	}

	public Language makePersistent(Language lang){
		lang = super.makePersistent(lang);
		getSession().flush();
		if(lang.getId() == 0){
		    throw new CinnamonConfigurationException("Failure persisting Language.");
		}
		return lang;
	}
	
	@Override
	public Language get(String id){
		Long langId = ParamParser.parseLong(id, "error.get.language");
		return get(langId);
	}
}
