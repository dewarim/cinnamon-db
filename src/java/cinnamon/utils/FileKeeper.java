package cinnamon.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * This class prevents API-methods and ChangeTriggers from corrupting files
 * if an exception is thrown after a file has been scheduled for deletion.
 *
 * Instead of deleting a file directly, a method should always use the ThreadLocal
 * FileKeeper instance to add the target file to a list of files which will be
 * deleted at the end of the request.
 * If the database is rolled back due to an exception, the files will not be
 * deleted. This prevents an OSD from reaching a state where (after a rollback)
 * osd.content_path points to a file which does no longer exist.
 */
public class FileKeeper {

    private transient Logger log = LoggerFactory.getLogger(this.getClass());

    static ThreadLocal<FileKeeper> localFileKeeper = new ThreadLocal<FileKeeper>(){
            protected FileKeeper initialValue(){
                return new FileKeeper();
            }
    };

    /**
     * Retrieve a ThreadLocal FileKeeper instance.
     * @return a local FileKeeper
     */
    static public FileKeeper getInstance(){
        return localFileKeeper.get();
    }

    private Set<File> files = new HashSet<File>();

    /**
     * Add a file which will be deleted later on if no
     * exception occurs which causes the current operation
     * to return an error message to the client.
     * @param file the file to be deleted.
     */
    public void addFileForDeletion(File file){
        files.add(file);
    }

    /**
     * Delete all files of this thread which have been
     * scheduled for deletion.
     */
    // TODO: finishDeleteFiles is not used anywhere. Refactor.
    public void finishDeleteFiles() {
        for (File file : files) {
            if (!file.exists()) {
                log.debug("File " + file.getAbsolutePath() + " no longer exists and cannot be deleted.");
                continue;
            }
            if (!file.delete()) {
                log.debug("Could not delete file '" + file.getAbsolutePath() + "'");
            }
        }
    }

}
