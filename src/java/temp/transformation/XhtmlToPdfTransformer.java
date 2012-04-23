package temp.transformation;

import org.xhtmlrenderer.simple.PDFRenderer;
import server.data.ContentStore;
import server.data.ObjectSystemData;
import server.exceptions.CinnamonException;

import java.io.File;

public class XhtmlToPdfTransformer implements ITransformer {

	Transformer transformer;
	
	@Override
	public void transformOSD(ObjectSystemData source, String params, String repositoryName) {
		try{
			File inFile = getInputFile(source, repositoryName);
			File pdfFile = File.createTempFile("cinnamon", ".pdf");
			PDFRenderer.renderToPDF(inFile, pdfFile.getAbsolutePath());
			String location = ContentStore.copyToContentStore(pdfFile.getAbsolutePath(), repositoryName);
			source.setContentPathAndFormat(location, transformer.getTargetFormat(), repositoryName);
		}
		catch (Exception e) {
			throw new CinnamonException("error.transforming.content",e);
		}
	}

	@Override
	public File transformToFile(ObjectSystemData source, String params, String repositoryName) {
		File pdfFile;
		try{
			pdfFile = File.createTempFile("cinnamon", ".pdf");
			File inFile = getInputFile(source, repositoryName);
			PDFRenderer.renderToPDF(inFile, pdfFile.getAbsolutePath());
		}
		catch (Exception e) {
			throw new CinnamonException("error.transforming.to.file",e);
		}
		return pdfFile;
	}

	File getInputFile(ObjectSystemData source, String repositoryName){
		String fullPath = source.getFullContentPath(repositoryName);
		if(fullPath == null){
			throw new CinnamonException("error.path.is.null");
		}
		File inFile = new File(fullPath);
		if(! inFile.exists()){
			throw new CinnamonException("error.io.file.not.found", fullPath, repositoryName);
		}
		return inFile;
	}
	
	public void setTransformer(Transformer transformer){
		this.transformer = transformer;
	}
		
}
