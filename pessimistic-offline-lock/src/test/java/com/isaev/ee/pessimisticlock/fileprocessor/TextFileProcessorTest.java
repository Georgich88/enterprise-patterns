package com.isaev.ee.pessimisticlock.fileprocessor;

import com.isaev.ee.pessimisticlock.exceptions.PessimisticFileLockException;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TextFileProcessorTest {

    public static final String DEMO_FILE_PATH = "/pg28885.txt";

    @Test
    void shouldLockFile() {

        URI uri = assertDoesNotThrow(() -> TextFileProcessor.class.getResource(DEMO_FILE_PATH).toURI());
        String filePath = Path.of(uri).toString();
        FileProcessor firstFileProcessor = assertDoesNotThrow(() -> TextFileProcessor.of(filePath));

        assertThrows(PessimisticFileLockException.class, () -> TextFileProcessor.of(filePath));
        assertDoesNotThrow(() -> firstFileProcessor.process());
        FileProcessor secondFileProcessor = assertDoesNotThrow(() -> TextFileProcessor.of(filePath));
        assertDoesNotThrow(() -> secondFileProcessor.process());
    }

}