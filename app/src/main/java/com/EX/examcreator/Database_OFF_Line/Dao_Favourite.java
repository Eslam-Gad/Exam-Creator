package com.EX.examcreator.Database_OFF_Line;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface Dao_Favourite {


    // COMPLETED (2) Wrap the return type with LiveData
    @Query("SELECT * FROM Model_Favourite")
    LiveData<List<Model_Favourite>> loadAllTasks();

    @Insert
    void insertTask(Model_Favourite taskEntry);

    @Delete
    void deleteTask(Model_Favourite taskEntry);
}
