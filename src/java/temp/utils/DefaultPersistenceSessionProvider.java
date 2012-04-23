package temp.utils;


public class DefaultPersistenceSessionProvider implements
        PersistenceSessionProvider {

	String name;
	String persistence_unit;
	String url;
	
	public DefaultPersistenceSessionProvider(String name, String persistence_unit, String url){
		this.name = name;
		this.persistence_unit = persistence_unit;
		this.url = url;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getPersistenceUnit() {
		return persistence_unit;
	}

	@Override
	public String getUrl() {
		return url;
	}

}
