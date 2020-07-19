package com.ikvych.cocktail.data.db.impl

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ikvych.cocktail.data.db.impl.dao.CocktailDao
import com.ikvych.cocktail.data.db.impl.typeconverter.DateConverter
import com.ikvych.cocktail.data.db.impl.typeconverter.StringListToStringConverter
import com.ikvych.cocktail.data.db.model.entity.*


@Database(entities = [
    CocktailDbModel::class,
    LocalizedNameDbModel::class,
    LocalizedInstructionDbModel::class,
    MeasureDbModel::class,
    IngredientDbModel::class,
    CocktailIngredientCrossRef::class,
    CocktailMeasureCrossRef::class
], version = 6, exportSchema = false)
@TypeConverters(DateConverter::class, StringListToStringConverter::class)
abstract class DrinkDataBase : RoomDatabase() {

    abstract fun drinkDao(): CocktailDao

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