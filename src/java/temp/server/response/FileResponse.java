package temp.server.response;

import server.interfaces.Response;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileResponse implements Response {

	HttpServletResponse res;
	String contentPath;
	Long contentSize = 0L;
	String filename;
	
	public FileResponse(){		
	
	}
	
	public FileResponse(HttpServletResponse res){
		this.res = res;
	}
	
	public FileResponse(HttpServletResponse res, 
			String contentPath, Long contentSize,
			String filename){
		this.res = res;
		this.contentPath = contentPath;
		this.contentSize = contentSize;
		this.filename = filename;
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
	public void write() throws IOException {
		File file = new File(contentPath);
		FileInputStream fin = new FileInputStream(file);

		byte b[] = new byte[2048];
		res.setContentLength(contentSize.intValue());
		res.setContentType("binary/octet-stream");
		res.setHeader("Content-Disposition", "attachment; filename="
			      + filename);

		ServletOutputStream oout = res.getOutputStream();
		int len;
		while ((len = fin.read(b)) > 0) {
		    oout.write(b, 0, len);
		}
		oout.close();
		fin.close();
	}
		


	/**
	 * @return the contentPath
	 */
	public String getContentPath() {
		return contentPath;
	}

	/**
	 * @param contentPath the contentPath to set
	 */
	public void setContentPath(String contentPath) {
		this.contentPath = contentPath;
	}

	/**
	 * @return the contentSize
	 */
	public Long getContentSize() {
		return contentSize;
	}

	/**
	 * @param contentSize the contentSize to set
	 */
	public void setContentSize(Long contentSize) {
		this.contentSize = contentSize;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getContent(){
		return String.format("<response><file><filename>%s</filename>"+
					"<size>%d</size><path>%s</path></file></response>",
					getFilename(), getContentSize(), getContentPath());
	}
	
}
