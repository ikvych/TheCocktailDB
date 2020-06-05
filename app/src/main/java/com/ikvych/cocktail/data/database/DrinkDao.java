package com.ikvych.cocktail.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ikvych.cocktail.data.entity.Drink;

import java.util.List;

@Dao
public interface DrinkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Drink drink);

    @Query("SELECT * FROM drink ORDER BY created DESC")
    LiveData<List<Drink>> getAllDrink();
}
