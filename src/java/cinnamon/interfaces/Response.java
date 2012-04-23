package cinnamon.interfaces;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Response {
	void setServletResponse(HttpServletResponse resp);
	HttpServletResponse getServletResponse();
	
	/**
	 * Send the content of the Response to the client which requested it.
	 * @throws java.io.IOException
	 */
	void write() throws IOException;
	
	/**
	 * For filters and triggers, this method should return the content
	 * of the Response object as a String, if possible. A XML-String is
	 * recommended.
	 * @return the content of the Response
	 */
	String getContent();
}
