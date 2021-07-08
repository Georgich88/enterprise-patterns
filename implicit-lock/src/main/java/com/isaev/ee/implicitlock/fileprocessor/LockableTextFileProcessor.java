package com.isaev.ee.implicitlock.fileprocessor;

import com.isaev.ee.implicitlock.fileprocessor.exceptions.OptimisticFileLockException;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Adds implicit lock functionality to the text file processor.
 * @author Georgy Isaev
 */
public class LockableTextFileProcessor extends TextFileProcessor implements Lockable {

    private FileVersion currentVersion;
    public static final String MESSAGE_ERROR_FILE_HAS_BEEN_CHANGED = "File has been changed. Editing is prohibited";

    protected LockableTextFileProcessor(Path path) throws IOException {
        super(path);
        this.currentVersion = FileVersion.of(path);
    }

    public static LockableTextFileProcessor of(String filePath) throws IOException {
        Path path = createPath(filePath);
        return new LockableTextFileProcessor(path);
    }

    @Override
    public synchronized void process() throws IOException, OptimisticFileLockException {
        tryLock();
        super.process();
    }

    public void tryLock() throws IOException, OptimisticFileLockException {
        FileVersion fileVersion = FileVersion.of(this.getPath());
        if (!fileVersion.equals(this.currentVersion)) {
            throw new OptimisticFileLockException(MESSAGE_ERROR_FILE_HAS_BEEN_CHANGED);
        }
    }


}
