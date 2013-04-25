// cinnamon - the Open Enterprise CMS project
// Copyright (C) 2007-2013 Texolution GmbH (http://texolution.eu)
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
// (or visit: http://www.gnu.org/licenses/lgpl.html)

package cinnamon.global;

import cinnamon.exceptions.CinnamonConfigurationException;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.AccessControlException;
import java.util.*;

public class Conf{

	private transient Logger log = LoggerFactory.getLogger(getClass());
	private Document xml;
	private String configPath;
	private Boolean useSessionLogging = false;
	
	public Conf(String configName){
		String separator = File.separator;
		File configFile = null;
		try{
			String currentDir=(new File(".")).getAbsolutePath();
			configPath= currentDir.substring(0,currentDir.length()-1) + "webapps" + separator 
			+ "cinnamon" + separator + configName;
			configFile = new File(configPath);
			if(! configFile.exists()){
				throw new FileNotFoundException("Config file "+ configPath +" was not found.");
			}
		}
		catch (AccessControlException ace) {
			log.debug("AccessControlException: "+ace.getLocalizedMessage());			
		}
		catch (FileNotFoundException fnfe){
			log.debug("Config file not found in "+configPath);
			log.debug("Will try in CINNAMON_HOME_DIR='"+System.getenv().get("CINNAMON_HOME_DIR")+"'");
		}
		finally{
			if(configFile == null || ! configFile.exists()){
				// try do instantiate the configuration via environment var:
				String path = System.getenv().get("CINNAMON_HOME_DIR");
				setConfigPath(path + separator + configName);
				log.debug("ConfigPath: "+getConfigPath());	
				configFile = new File( getConfigPath() );
			}
		}
		
		log.debug("ConfigPath: "+configPath);
        SAXReader reader = new SAXReader();
       
		try{
			File cfg = new File(configPath);
			 xml = reader.read(cfg);
			 configureUseSessionLogging(xml);
		}
		catch(Exception ex) {
			log.debug("",ex);
			emergencyLog("Failure loading config file: " + ex.getMessage());
			throw new RuntimeException(ex); 
		}		
	}
	
	public Conf(){
		this("cinnamon_config.xml");
	}

    public static void emergencyLog(String line) {
        try {
            String path;
            if(System.getProperty("os.name").contains("Windows") ){
                path ="c:\\cinnamon_emergency.log";
            }
            else{
                path = System.getenv().get("CINNAMON_HOME_DIR")+File.separator+"cinnamon_emergency.log";
            }
            FileWriter resultsFile = new FileWriter(path, true);
            PrintWriter toFile = new PrintWriter(resultsFile);
            toFile.println(line);
            toFile.close();
            resultsFile.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /* Extract info exactly once from config file for efficiency reasons.
      * The value useSessionLogging is queried by almost every request, so we will
      * store the result in Conf.useSessionLogging.
      */
	public void configureUseSessionLogging(Document xml){
		Boolean logging =  false;
		try {
			 String setting = xml.selectSingleNode("/cinnamon_config/use_session_logging").getText();
			 logging = setting.equalsIgnoreCase("true");
		} catch (Exception e) {
			log.debug("Could not extract a valid value for use_session_logging from in cinnamon config."+
					" I will use the default (use_session_logging == false)");
		}
		setUseSessionLogging(logging);
	}
	
	/**
	 * @param configPath the configPath to set
	 */
	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

	public String getConfigPath() {
		return configPath;
	}

	public String getDbType(){
		return xml.selectSingleNode("/cinnamon_config/db_type").getText();
	}
	
	public String getJdbcDriver(){
		return xml.selectSingleNode("/cinnamon_config/jdbc_driver").getText();
	}
	
	public String getJdbcProtocol(){
		return xml.selectSingleNode("/cinnamon_config/jdbc_protocol").getText();
	}
	
	public String getSqlUser() {
		return xml.selectSingleNode("/cinnamon_config/sql_user").getText();
	}

	public String getSqlPwd() {
		return xml.selectSingleNode("/cinnamon_config/sql_pwd").getText();
	}

	public String getSqlHost() {
		return xml.selectSingleNode("/cinnamon_config/sql_host").getText();
	}

	public String getSystemRoot() {
		return xml.selectSingleNode("/cinnamon_config/system_root").getText();
	}

	/**
	 * 
	 * @return the path to where the repositories are stored.
	 */
	public String getDataRoot() {
		return xml.selectSingleNode("/cinnamon_config/data_root").getText();
	}

    public static String createDatabaseConnectionURL(String repository, String dbType, String protocol, String host, String user, String password){
        String connString;
        switch (dbType) {
            case "mysql":
                connString = "jdbc:" + protocol + "://" + host +
                        "/" + repository +
                        "?user=" + user +
                        "&password=" + password;
                break;
            case "mssql":
                connString = "jdbc:" + protocol + "://" + host +
                        ";databaseName=" + repository +
                        ";user=" + user +
                        ";password=" + password;
                break;
            case "mssql2000":
                connString = "jdbc:jtds:" + protocol + "://" + host +
                        "/" + repository +
                        ";user=" + user +
                        ";password=" + password +
                        ";TDS=8.0";
                break;
            case "postgresql":
                connString = "jdbc:" + protocol + "://" + host +
                        "/" + repository +
                        "?user=" + user +
                        "&password=" + password;
                //"&ssl=true";
                break;
            default:
                throw new CinnamonConfigurationException("unknown db_type in config found!" +
                        " Currently, Cinnamon only connects to MySQL, PostgreSQL or MS-SQL dbs");
        }
        return connString;
    }

	public String getDatabaseConnectionURL(String repository){
		log.debug("Building database connection URL for repository '"+repository+"'");
		String connString = createDatabaseConnectionURL(repository, getDbType(), getJdbcProtocol(), getSqlHost(), getSqlUser(), getSqlPwd());

		log.debug("connectionURL == "+connString);
		return connString;
	}
	
	public String getSep() {
		return File.separator;
	}
	
	@SuppressWarnings("unchecked")
	public Collection<String> getRepositoryList() {						
		List<Node> nodeList = xml.selectNodes("/cinnamon_config/repositories/repository");
		log.debug("number of repositories found:"+nodeList.size());
		Collection<String> list = new LinkedList<>();
		for(Node n : nodeList){
			String name = n.selectSingleNode("name").getText();
			log.debug("found repository in config: '" + name + "'");
			list.add(name);
		}
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Node> getRepositories(){
		List<Node> nodeList = xml.selectNodes("/cinnamon_config/repositories/repository");
		log.debug("number of repositories found:"+nodeList.size());
		return nodeList;
	}
	
	public Node getRepositoriesNode(){
		return xml.selectSingleNode("/cinnamon_config/repositories");
	}
	
	/**
	 * 
	 * @return The name of the Cinnamon class to be used as a container for storing the 
	 * parts of a workflow that are specific to the Cinnamon CMS.  
	 */
	public String getWorkflowServerUserName() {
		String name=  null;
		try {
			 name = xml.selectSingleNode("//workflow_server_username")
				.getText();
		} catch (Exception e) {
			log.debug("error.workflow_username.not_found.");
		}

		if (name == null) {
			name = "WorkflowServer";
		}
		return name;
	}

	/**
	 * <p>Getter for the configuration file element "logback_configuration_path". If the element is
	 * absent, the method returns null and Logback will use the default settings.</p>
	 * <p>Example: <code>&lt;logback_configuration_path&gt;/home/zimt/logback.xml&lt;/logback_configuration_path&gt;</code>
	 * </p>
	 * <p>This allows the server administrator to use different log settings for production and
	 * debugging scenarios.</p>
	 * @return the absolute path to the logback configuration file or null if none is found.
	 */
	public String getLogbackConfigurationPath(){
		String logback = null;
		try {
			 logback = xml.selectSingleNode("//logback_configuration_path")
				.getText();
		} catch (Exception e) {
			log.warn("Could not find logback_configuration_path in cinnamon config - using default.");			
		}
//		log.debug("logback_configuration_path: "+logback);
		return logback;
	}
	

	/**
	 * @return the xml
	 */
	public Document getXml() {
		return xml;
	}

	/**
	 * @param xml the xml to set
	 */
	public void setXml(Document xml) {
		this.xml = xml;
	}

	/**
	 * @return the useSessionLogging
	 */
	public Boolean getUseSessionLogging() {
		return useSessionLogging;
	}

	/**
	 * @param useSessionLogging the useSessionLogging to set
	 */
	private void setUseSessionLogging(Boolean useSessionLogging) {
		this.useSessionLogging = useSessionLogging;
	}
	
	public String getPersistenceUnit(String repository){
		Node repNode = xml.selectSingleNode("/cinnamon_config/repositories/repository[name='"+repository+"']");
		//		log.debug("found repNode: "+repNode.asXML());
		if(repNode == null){
			throw new CinnamonConfigurationException("Could not find repository "+
					repository + " in config file.");
					
		}
		String pu = repNode.selectSingleNode("persistence_unit").getText();
		
		log.debug(String.format("persistence-unit %s found for repository %s",pu,repository));
		return pu;
	}
	
	/**
	 * Load custom properties files from the config dir.
	 * @param name - the name of the Properties file.
	 * @return a Properties object, which may be empty if no file was found.
	 */
	public Properties findProperties(String name){
		Properties props = new Properties();
		
		BufferedInputStream bis = null;
		try {
			File path = new File(getConfigPath()).getParentFile();
			String propPath = path.getAbsolutePath() + getSep()+ name;
			FileInputStream fis = new FileInputStream(propPath);
			bis = new BufferedInputStream(fis);
			props.load(bis);
			
		} catch (Exception ex) {
			log.debug("failed to load properties.", ex);
		}
		finally{
			if(bis != null){
				try {
					bis.close();
				} catch (IOException e) {
					log.error("failed to close FileInputStream.", e);
				}
			}
		}
		return props;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getApiClasses(String repository){
		Node repo = xml.selectSingleNode("//repository[name='"+repository+"']/apiClasses");
		List<Node> apiNodes = repo.selectNodes("apiClass");
		List<String> apiClasses = new ArrayList<>();
		for(Node n : apiNodes){
			apiClasses.add(n.getText());
		}
		return apiClasses;
	}

	/**
	 *
	 * Retrieve any single element from the configuration file by tag name / xpath.
	 * In case the element cannot be found, getField() returns the default string. 
	 * @param defaultString the value to be used if the fieldName-param results  in an invalid / null value
	 * @param fieldName which will be prefixed with '//' before using it in doc.selectSingleNode(xpath)
	 * @return String the value of the selected field (or the defaultString)
	 */
	public String getField(String fieldName, String defaultString){		
		String field = null;
		
		try {
			field = getXml().selectSingleNode("//"+fieldName).getText();
		} catch (Exception e) {
			log.debug("Did not find //" + fieldName + " in config - using default: "+defaultString);
		}
		
		if (field == null || field.length() == 0) {
			field = defaultString;
		}
		
		return field;
	}



}
