package cinnamon.response;


import cinnamon.interfaces.Response;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

@Deprecated
public class HtmlResponse implements Response {

	HttpServletResponse res;
	StringBuilder content = new StringBuilder();
	
	public HtmlResponse(){		

	}
	
	public HtmlResponse(HttpServletResponse res){
		this.res = res;
	}
	
	public HtmlResponse(HttpServletResponse res, String content){
		this.res = res;
		this.content.append(content);
	}
	
	public HtmlResponse(HttpServletResponse res, StringBuilder content){
		this.res = res;
		this.content = content;
	}
	
	@Override
	public HttpServletResponse getServletResponse() {
		return res;
	}

	@Override
	public void setServletResponse(HttpServletResponse res) {
		this.res = res;		
	}

	@Override
	public void write() throws IOException, UnsupportedEncodingException {
		res.setContentType("text/html; charset=utf-8");
        String c = content.toString();
        res.setContentLength(c.getBytes("UTF8").length);
        PrintWriter toClient= res.getWriter();
		toClient.print(c);
		toClient.close();
	}

	/**
	 * @return the content as String
	 */
	public String getContent() {
		return content.toString();
	}

	public StringBuilder getContentBuilder(){
		return content;
	}
	
	/**
	 * @param content the content to set
	 */
	public void setContent(StringBuilder content) {
		this.content = content;
	}
	
	public void setContent(String content){
		this.content = new StringBuilder(content);
	}
}
