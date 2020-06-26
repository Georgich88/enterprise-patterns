package com.isaev.ee.implicitlock.application;

import com.isaev.ee.implicitlock.fileprocessor.FileProcessor;
import com.isaev.ee.implicitlock.fileprocessor.LockableTextFileProcessor;
import com.isaev.ee.implicitlock.fileprocessor.exceptions.OptimisticFileLockException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class Application {

    public static final String DEMO_FILE_PATH = "/pg28885.txt";

    public static void main(String[] args) throws IOException, OptimisticFileLockException, URISyntaxException {
        URI uri = Application.class.getResource(DEMO_FILE_PATH).toURI();
        String filePath = Path.of(uri).toString();
        FileProcessor firstFileProcessor = LockableTextFileProcessor.of(filePath);
        FileProcessor secondFileProcessor = LockableTextFileProcessor.of(filePath);
        secondFileProcessor.process();
        try {
            firstFileProcessor.process();
        } catch (OptimisticFileLockException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
