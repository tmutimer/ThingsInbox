package com.android.example.thingsinbox;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tag_table")
public class Tag {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "tag")
    private String mTag;

    public Tag(@NonNull String tag) {mTag = tag;}

    public String getTag() {
        return mTag;
    }
}
