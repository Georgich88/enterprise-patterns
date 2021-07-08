package com.isaev.ee.implicitlock.fileprocessor;

public interface Lockable {
    void tryLock() throws Exception;
}
