package com.isaev.ee.optimisticlock.fileprocessor;

import com.isaev.ee.optimisticlock.fileprocessor.exceptions.OptimisticFileLockException;

import java.io.IOException;

public interface FileProcessor {

    void process() throws IOException, OptimisticFileLockException;

}
