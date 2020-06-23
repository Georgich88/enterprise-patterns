package com.isaev.ee.optimisticlock.fileprocessor;

import com.isaev.ee.optimisticlock.fileprocessor.exceptions.OptimisticFileLockException;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TextFileProcessorTest {
    public static final String DEMO_FILE_PATH = "/pg28885.txt";

    @Test
    void shouldThrowOptimisticLockException() throws URISyntaxException {
        URI uri = assertDoesNotThrow(() -> TextFileProcessor.class.getResource(DEMO_FILE_PATH).toURI());
        String filePath = Path.of(uri).toString();
        FileProcessor firstFileProcessor = assertDoesNotThrow(() -> TextFileProcessor.of(filePath));
        FileProcessor secondFileProcessor = assertDoesNotThrow(() -> TextFileProcessor.of(filePath));
        assertDoesNotThrow(() -> secondFileProcessor.process());
        assertThrows(OptimisticFileLockException.class, () -> firstFileProcessor.process());
    }

}