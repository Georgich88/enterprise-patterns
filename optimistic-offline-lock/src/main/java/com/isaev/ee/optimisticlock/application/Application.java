package com.isaev.ee.optimisticlock.application;

import com.isaev.ee.optimisticlock.fileprocessor.FileProcessor;
import com.isaev.ee.optimisticlock.fileprocessor.TextFileProcessor;
import com.isaev.ee.optimisticlock.fileprocessor.exceptions.OptimisticFileLockException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class Application {

    public static final String DEMO_FILE_PATH = "/pg28885.txt";

    public static void main(String[] args) throws IOException, OptimisticFileLockException, URISyntaxException {
        URI uri = Application.class.getResource(DEMO_FILE_PATH).toURI();
        String filePath = Path.of(uri).toString();
        FileProcessor firstFileProcessor = TextFileProcessor.of(filePath);
        FileProcessor secondFileProcessor = TextFileProcessor.of(filePath);
        secondFileProcessor.process();
        try {
            firstFileProcessor.process();
        } catch (OptimisticFileLockException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
