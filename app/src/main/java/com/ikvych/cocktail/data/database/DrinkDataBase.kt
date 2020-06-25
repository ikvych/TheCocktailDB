package com.ikvych.cocktail.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ikvych.cocktail.convertor.db.DateConverter
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.entity.Ingredient


@Database(entities = [Drink::class, Ingredient::class], version = 3, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class DrinkDataBase : RoomDatabase() {

    abstract fun drinkDao(): DrinkDao

    companion object {

        private var instance: DrinkDataBase? = null

        @Synchronized
        fun getInstance(context: Context): DrinkDataBase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    DrinkDataBase::class.java, "CocktailsApp"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }

}