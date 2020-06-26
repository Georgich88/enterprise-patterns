package com.isaev.ee.implicitlock.fileprocessor;

import com.isaev.ee.implicitlock.fileprocessor.exceptions.OptimisticFileLockException;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LockableTextFileProcessorTest {
    public static final String DEMO_FILE_PATH = "/pg28885.txt";

    @Test
    void shouldThrowOptimisticLockException() {
        URI uri = assertDoesNotThrow(() -> LockableTextFileProcessor.class.getResource(DEMO_FILE_PATH).toURI());
        String filePath = Path.of(uri).toString();
        FileProcessor firstFileProcessor = assertDoesNotThrow(() -> LockableTextFileProcessor.of(filePath));
        FileProcessor secondFileProcessor = assertDoesNotThrow(() -> LockableTextFileProcessor.of(filePath));
        assertDoesNotThrow(() -> secondFileProcessor.process());
        assertThrows(OptimisticFileLockException.class, () -> firstFileProcessor.process());
    }

}