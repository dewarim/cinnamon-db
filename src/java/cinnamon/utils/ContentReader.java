package cinnamon.utils;

import cinnamon.exceptions.CinnamonException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ContentReader {
    static Logger log = LoggerFactory.getLogger(ContentReader.class);

    public static String readFileAsString(String filePath)
            throws IOException {
        return readFileAsString(filePath, null);
    }

    public static String readFileAsString(String filePath, String encoding) throws IOException, FileNotFoundException {
        if(filePath == null){
            throw new CinnamonException("error.path.is.null");
        }
        log.debug("reading from file: " + filePath);
        File f = new File(filePath);
        checkExistence(f);
        checkDirectory(f);
        return FileUtils.readFileToString(f, encoding);
    }

    public static byte[] readFileAsBytes(String filePath) throws IOException {
        if(filePath == null){
            throw new CinnamonException("error.path.is.null");
        }
        log.debug("reading from file: " + filePath);
        File f = new File(filePath);
        checkExistence(f);
        checkDirectory(f);
        return FileUtils.readFileToByteArray(f);
    }

    static void checkExistence(File file) throws FileNotFoundException {
        if (!file.exists()) {
            log.warn("File not found: " + file.getAbsolutePath());
            throw new FileNotFoundException("error.file_not_found");
        }
    }

    static void checkDirectory(File file) throws IOException {
        if (file.isDirectory()) {
            throw new IOException("error.filename.is_directory");
        }
    }

}
