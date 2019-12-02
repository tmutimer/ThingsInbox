package com.android.example.thingsinbox;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TagDao {
    @Insert
    void insert(Tag tag);

    @Query("DELETE FROM tag_table")
    void deleteAll();

    @Delete
    void deleteTag(Tag tag);

    @Query("SELECT tag FROM tag_table ORDER BY UPPER(tag) ASC")
    LiveData<List<Tag>> getAlphabetisedTags();

}
