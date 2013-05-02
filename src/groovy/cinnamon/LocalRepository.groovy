package cinnamon

import cinnamon.index.IndexAction
import cinnamon.index.Indexable
import cinnamon.interfaces.Repository

/**
 * A thread local class to collect all change requests to indexable objects. 
 */
class LocalRepository {
    
    private static ThreadLocal<Repository> currentRepository = new ThreadLocal<Repository>();

//    /*
//    * A map of all repositories, initialized by LuceneService.initialize().
//    */
//    private static public Map<String,Repository> repositories = new HashMap<>()

    static public Repository getRepository(){
        return currentRepository.get();
    }

    static public void setRepository(Repository repository){
        currentRepository.set(repository);
    }

    private static ThreadLocal<HashMap<Indexable, IndexAction>> updatedObjects = new ThreadLocal<HashMap<Indexable, IndexAction>>(){
        @Override
        protected HashMap<Indexable,IndexAction> initialValue() {
            return new HashMap<>();
        }
    };

    public static void addIndexable(Indexable indexable, IndexAction action){
        updatedObjects.get().put(indexable, action);
    }

    public static Map<Indexable, IndexAction> getUpdatedObjects(){
        return updatedObjects.get();
    }

    public static void cleanUp(){
        getUpdatedObjects().clear();
    }
    
//    public static void addRepository(Repository repository){
//        repositories.put(repository.name, repository)
//    }
//    
//    public static Repository getRepository(String name){
//        return repositories.get(name)
//    }
}
