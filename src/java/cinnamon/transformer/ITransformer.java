package cinnamon.transformer;

import cinnamon.ObjectSystemData;
import cinnamon.transformation.Transformer;

import java.io.File;

/**
 * <h1>Interface class for OSD transformation</h1>
 * A Transformer class can be called to transform the content of an OSD itself or to
 * receive a file containing the transformed content.
 * <br/>
 * The workflow should be: copy the OSD you wish to transform. Then call 
 * the transformer with the appropriate parameters.
 *
 */
public interface ITransformer {

	/**
	 * Transform the content of an OSD.
	 * @param source the source object
	 * @param params additional parameters, for example output dimensions in XML format for
	 * an image transformer.
	 * @param repositoryName the name of the OSD's repository. Needed to find the contentPath.
	 */
	void transformOSD(ObjectSystemData source, String params, String repositoryName);
	
	/**
	 * Transform the content of an OSD and send it back as a temporary file,
	 * either for a HTTP-response to the client or to be used as content for another OSD.
	 * @param source the source OSD that contains the content from which the transformed output file will be generated
	 * @param params additional parameters, for example output dimensions in XML format for
	 * an image transformer.
	 * @param repositoryName the name of the OSD's repository. Needed to find the contentPath.
	 * @return the transformed file 
	 */
	File transformToFile(ObjectSystemData source, String params, String repositoryName);
	
	/**
	 * 
	 * @param transformer for most transformations, this is needed to safely determine the output 
	 * format.
	 */
	void setTransformer(Transformer transformer);
}
