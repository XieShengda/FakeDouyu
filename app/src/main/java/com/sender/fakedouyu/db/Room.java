package com.sender.fakedouyu.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by XieShengda on 2017/4/17.
 */

@Table(database = RoomDatabase.class)
public class Room extends BaseModel{
    @PrimaryKey
    public int roomId;
    @Column
    public String roomUrl;
}
