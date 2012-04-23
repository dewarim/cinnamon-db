package temp.dao;

import server.CustomTable;

import java.util.List;

public interface CustomTableDAO extends GenericDAO<CustomTable, Long> {
	List<CustomTable> list();

	void delete(Long id);
}
