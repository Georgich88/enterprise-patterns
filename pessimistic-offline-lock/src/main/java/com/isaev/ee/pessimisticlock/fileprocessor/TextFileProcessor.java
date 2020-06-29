package com.isaev.ee.pessimisticlock.fileprocessor;

import com.isaev.ee.pessimisticlock.exceptions.PessimisticFileLockException;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Appends a text file with a demo licence text.
 *
 * @author Georgy Isaev
 */
public class TextFileProcessor implements FileProcessor {

    public static final String MESSAGE_ERROR_PATH_IS_NULL = "Path to file should not be null";
    public static final String MESSAGE_ERROR_NOT_A_FILE = "Path should be a path to a file";

    public static final String TEXT_LICENCE = "Copyright © 2010 by Bill Shakespeare\n" +
            "\n" +
            "All rights reserved. No part of this publication may be reproduced, distributed, or transmitted in any form or by any means, " +
            "including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the publisher, " +
            "except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law. " +
            "For permission requests, write to the publisher, addressed “Attention: Permissions Coordinator,” at the address below.\n" +
            "\n" +
            "Imaginary Press\n" +
            "1233 Pennsylvania Avenue\n" +
            "San Francisco, CA 94909\n" +
            "www.imaginarypress.com";

    private FileChannel channel;
    private FileLock lock;
    private Path path;

    public static TextFileProcessor of(String filePath) throws IOException, PessimisticFileLockException {

        TextFileProcessor textFileProcessor = new TextFileProcessor();

        if (filePath == null) {
            throw new IllegalArgumentException(MESSAGE_ERROR_PATH_IS_NULL);
        }

        textFileProcessor.path = Paths.get(filePath);
        File file = textFileProcessor.path.toFile();
        if (!file.isFile()) {
            throw new IllegalArgumentException(MESSAGE_ERROR_NOT_A_FILE);
        }

        try {
            textFileProcessor.channel = FileChannel.open(textFileProcessor.path, StandardOpenOption.READ);
            textFileProcessor.lock = textFileProcessor.channel.lock(0, Long.MAX_VALUE, true);
        } catch (OverlappingFileLockException exception) {
            throw new PessimisticFileLockException(exception);
        }

        return textFileProcessor;

    }

    @Override
    public synchronized void process() throws IOException {

        this.lock.release();
        this.channel.close();

        try (FileWriter fileWriter = new FileWriter(path.toFile(), true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             PrintWriter out = new PrintWriter(bufferedWriter)) {
            out.write(TEXT_LICENCE);
        }
        this.notify();
    }


}

