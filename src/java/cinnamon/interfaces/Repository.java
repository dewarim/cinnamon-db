package cinnamon.interfaces;

import cinnamon.SqlCustomConn;

import java.util.List;
import java.util.Map;

/**
 * Interface for Cinnamon repositories, which are a combination of
 * tables in the database (managed by Hibernate) and a file system data store.
 * Repositories may have custom SQL connections to other databases and
 * tables, as well as their own specific API which can be called
 * via the CommandRegistry.
 *
 */
public interface Repository {
	
	String getName();
	void setName(String name);
	
	Map<String, SqlCustomConn> getSqlCustomConns();
	void setSqlCustomConns(Map<String, SqlCustomConn> connections);
	
	CommandRegistry getCommandRegistry();
	void setCommandRegistry(CommandRegistry cmdReg);
	
	void loadApiClasses(List<String> apiClasses);
	
	void startIndexServer();
	void startWorkflowServer();
	
}
