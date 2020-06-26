package com.isaev.ee.implicitlock.fileprocessor;

import com.isaev.ee.implicitlock.fileprocessor.exceptions.OptimisticFileLockException;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Appends a text file with a demo licence text.
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

    private Path path;

    // Constructors

    protected TextFileProcessor(Path path) throws IOException {
        this.path = path;
    }

    protected static Path createPath(String filePath) {

        if (filePath == null) {
            throw new IllegalArgumentException(MESSAGE_ERROR_PATH_IS_NULL);
        }

        Path path = Paths.get(filePath);
        File file = path.toFile();
        if (!file.isFile()) {
            throw new IllegalArgumentException(MESSAGE_ERROR_NOT_A_FILE);
        }
        return path;
    }

    @Override
    public synchronized void process() throws IOException, OptimisticFileLockException {

        try (FileWriter fileWriter = new FileWriter(path.toFile(), true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             PrintWriter out = new PrintWriter(bufferedWriter)) {
            out.write(TEXT_LICENCE);
        }
        this.notify();
    }

    // Getters and setters

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
