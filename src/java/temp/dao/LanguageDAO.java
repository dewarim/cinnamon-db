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

import server.i18n.Language;

import java.util.Map;

/**
 * The DAO interface for a particular entity extends the generic interface and provides the type arguments.
 * 
 * @see <a href="http://www.hibernate.org/328.html">Generic Data Access Objects</a>
 *
 */
public interface LanguageDAO extends GenericDAO<Language, Long> {
	
	/**
	 * Delete a language specified by the id-parameter.
	 * @param id folder id
	 */
	void delete(Long id);

	/**
	 * Set isoCode of a language via a Map.
	 * currently accepted fields
	 * <ul> 
	 * 		<li>[iso_code]= new ISO-code</li>
	 * </ul>
	 * 
	 * @param id	the id of the language (Long)
	 * @param fields map of the fields to be set
	 */
	void update(Long id, Map<String, String> fields);
	

	/**
	 * Return the language corresponding to the given isoCode. 
	 * @return	Language or a NoResultException if no Language was found.
	 */
	Language findByIsoCode(String isoCode);

	/**
	 * Convenience method: given a String which represents a Language-Id, this
	 * method returns the corresponding Language object and existing id.
	 * @param id - String representing a Long value.
	 * @return Language
	 */
	Language get(String id);
}
