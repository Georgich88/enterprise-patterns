package com.isaev.ee.pessimisticlock.application;

import com.isaev.ee.pessimisticlock.exceptions.PessimisticFileLockException;
import com.isaev.ee.pessimisticlock.fileprocessor.FileProcessor;
import com.isaev.ee.pessimisticlock.fileprocessor.TextFileProcessor;

import java.net.URI;
import java.nio.file.Path;

public class Application {

    public static final String DEMO_FILE_PATH = "/pg28885.txt";

    public static void main(String[] args) throws Exception {

        URI uri = Application.class.getResource(DEMO_FILE_PATH).toURI();
        String filePath = Path.of(uri).toString();
        FileProcessor firstFileProcessor = TextFileProcessor.of(filePath);

        try {
            FileProcessor secondFileProcessor = TextFileProcessor.of(filePath);
        } catch (PessimisticFileLockException exception) {
            System.out.println(exception.getMessage());
        }

        firstFileProcessor.process();

        FileProcessor secondFileProcessor = TextFileProcessor.of(filePath);
        secondFileProcessor.process();
    }
}
