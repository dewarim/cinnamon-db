package cinnamon.exceptions;

/**
 * An Exception that can be safely ignored. Used to return from deep inside
 * HTTP response processing without triggering the Grails Error handler (which would
 * override the already rendered response).<br>
 * Why this class exists: While a controller action examines a request, it may
 * encounter several situations, where it is necessary to stop, render an error message
 * and return it to the user. For example:
 * <pre>
 *  UserAccount user = loadUserFromDatabase(id);
 *  if( user == null){
 *    render_error_message();
 *    return;
 *  }
 *  Folder homeFolder = loadUserHomeFolder(user);
 *  if(homeFolder == null){
 *    // ...
 * }
 * </pre>
 * A user is an object which needs to be loaded often. This may result in several
 * redundant lines of code just for error checking for each affected method. By using
 * IgnorableException, the amount of boilerplate code can be reduced:
 * <pre>
 * try{
 *   UserAccount user = loadAndCheckUserFromDatabase(id)
 *   Folder homeFolder = loadAndCheckUserHomeFolder(user)
 * }
 * catch(IgnorableException ie){
 *  return
 *  }
 * </pre>
 * In this example, the loadAndCheck-methods would contain the code necessary for
 * error handling - and they would render the default error message, like "user not found"
 * or "home folder not found". So you can use loadAndCheckUserFromDatabase(id) every time
 * you need to retrieve a user object safely from the database.<br>
 * This approach is obviously most useful in a setting where default responses may be rendered
 * to the client which do not much additional state like in AJAX requests. If your request
 * needs to contain numerous other domain objects, it may make more sense to use the traditional
 * "bloated controller" approach. 
 */
public class IgnorableException extends RuntimeException{

    public IgnorableException(String s) {
        super(s);
    }

    public IgnorableException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public IgnorableException(Throwable throwable) {
        super(throwable);
    }

    public IgnorableException() {
    }
}
