package temp.dao;

import server.Format;

import java.util.Map;

public interface FormatDAO extends GenericDAO<Format, Long> {
	// invalid: Name is unique!
//	List<Format> findAllByName(String name);
	
	/**
	 *  @return the Format with the given name, or null. 
	 */
	Format findByName(String name);
	
	void delete(Long id);
	
	void update(Long id, Map<String, String> cmd);
	
	Format get(String id);
	
}
