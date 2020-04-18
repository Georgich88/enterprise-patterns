package com.isaev.ee.activerecord.database.activerecord;

import com.isaev.ee.activerecord.database.exception.ActiveRecordException;

public interface ActiveRecord {

    void update() throws ActiveRecordException;
    void insert() throws ActiveRecordException;
    void delete() throws ActiveRecordException;

}
