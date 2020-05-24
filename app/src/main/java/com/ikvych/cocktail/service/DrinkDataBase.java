package com.ikvych.cocktail.service;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ikvych.cocktail.convertor.DateConverter;
import com.ikvych.cocktail.model.Drink;

@Database(entities = {Drink.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class DrinkDataBase extends RoomDatabase {

    private static DrinkDataBase instance;

    public abstract DrinkDao getDrinkDao();

    public static synchronized DrinkDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    DrinkDataBase.class, "drinkDB")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
