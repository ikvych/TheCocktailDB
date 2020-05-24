package com.ikvych.cocktail.service;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ikvych.cocktail.model.Drink;

import java.util.List;

@Dao
public interface DrinkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Drink drink);

    @Update
    void update(Drink drink);

    @Delete
    void delete(Drink drink);

    @Query("SELECT * FROM drink")
    LiveData<List<Drink>> getAllDrink();
}
