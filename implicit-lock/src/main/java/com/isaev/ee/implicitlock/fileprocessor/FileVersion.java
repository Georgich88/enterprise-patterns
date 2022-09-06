package com.isaev.ee.implicitlock.fileprocessor;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Objects;
import java.util.zip.CRC32;

public class FileVersion {

    private FileTime lastModifiedTime;
    private long size;
    private long crc32;

    public static FileVersion of(Path path) throws IOException {

        FileVersion fileVersion = new FileVersion();

         try (FileChannel channel = FileChannel.open(path)) {
            var crc = new CRC32();
            long length = channel.size();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, length);
            for (int p = 0; p < length; p++) {
                int c = buffer.get(p);
                crc.update(c);
            }
             fileVersion.setCrc32(crc.getValue());
        }

        BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
        fileVersion.setLastModifiedTime(attributes.lastModifiedTime());
        fileVersion.setSize(attributes.size());

        return fileVersion;

    }

    // Getters and setters

    public FileTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(FileTime lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getCrc32() {
        return crc32;
    }

    public void setCrc32(long crc32) {
        this.crc32 = crc32;
    }

    // Object inherited methods

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileVersion that = (FileVersion) o;
        return size == that.size &&
                crc32 == that.crc32 &&
                lastModifiedTime.equals(that.lastModifiedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastModifiedTime, size, crc32);
    }
}
