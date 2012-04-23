package cinnamon.interfaces;

import java.lang.reflect.Method;

/**
 * A container for a class and a method, which can be called with a command from a CommandRegistry.  
 *
 */
public interface MethodContainer {
	
	@SuppressWarnings("unchecked")
	Class getMethodClass();
	Method getMethod();
	String getCommand();
	
	/**
	 * Check if the CinnamonMethod-Annotation of this API-method has the value "true". 
	 * @return true if the annotation was found, false otherwise.
	 */
	Boolean checkTrigger();
	
}
