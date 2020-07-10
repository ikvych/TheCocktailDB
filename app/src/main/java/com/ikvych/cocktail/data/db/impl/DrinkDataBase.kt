package com.ikvych.cocktail.data.db.impl

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ikvych.cocktail.data.db.impl.dao.DrinkDao
import com.ikvych.cocktail.data.db.impl.typeconverter.DateConverter
import com.ikvych.cocktail.data.db.model.Drink


@Database(entities = [Drink::class], version = 3, exportSchema = false)
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