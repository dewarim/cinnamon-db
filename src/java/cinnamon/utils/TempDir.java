package cinnamon.utils;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.UUID;

/**
 * Create a directory in java.io.tempdir.
 * @author ingo
 *
 */
public class TempDir {
	
	public static File createTempDir(){
		String uuid = UUID.randomUUID().toString();
		File tempDir = new File(System.getProperty("java.io.tmpdir")+ 
				File.separator + "cinnamonTempDir_"+uuid);
		if(tempDir.exists()){
			/*
			 *  In the extremely unlikely event that we win the lottery and find
			 *  an existing tempdir, try again:
			 */
			return createTempDir();
		}
		if(! tempDir.mkdir()){
			LoggerFactory.getLogger(TempDir.class).warn("tempDir.mkdir() returned false");
		}
		return tempDir;
	}

}
