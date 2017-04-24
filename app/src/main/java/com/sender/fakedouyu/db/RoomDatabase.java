package com.sender.fakedouyu.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by XieShengda on 2017/4/17.
 */
@Database(name = RoomDatabase.NAME, version = RoomDatabase.VERSION)
public class RoomDatabase {

    public static final int VERSION = 1;
    public static final String NAME = "RoomDatabase";
}
