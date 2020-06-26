package com.isaev.ee.implicitlock.fileprocessor;

import com.isaev.ee.implicitlock.fileprocessor.exceptions.OptimisticFileLockException;

import java.io.IOException;

public interface FileProcessor {

    void process() throws IOException, OptimisticFileLockException;

}
