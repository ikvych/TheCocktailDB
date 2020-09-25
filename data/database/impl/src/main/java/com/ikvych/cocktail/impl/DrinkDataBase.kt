package com.ikvych.cocktail.impl

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.annotation.NonNull
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ikvych.cocktail.database.Table
import com.ikvych.cocktail.database.model.cocktail.entity.*
import com.ikvych.cocktail.database.model.notification.NotificationDbModel
import com.ikvych.cocktail.database.model.user.entity.UserDbModel
import com.ikvych.cocktail.impl.dao.CocktailDao
import com.ikvych.cocktail.impl.dao.NotificationDao
import com.ikvych.cocktail.impl.dao.UserDao
import com.ikvych.cocktail.impl.typeconverter.DateConverter

@Database(
    entities = [
        CocktailDbModel::class,
        LocalizedNameDbModel::class,
        LocalizedInstructionDbModel::class,
        MeasureDbModel::class,
        IngredientDbModel::class,
        IngredientMeasureDbModel::class,
        UserDbModel::class,
        NotificationDbModel::class
    ], version = 4, exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class DrinkDataBase : RoomDatabase() {

    abstract fun drinkDao(): CocktailDao
    abstract fun userDao(): UserDao
    abstract fun notificationDao(): NotificationDao

    companion object {

        private var instance: DrinkDataBase? = null

        @Synchronized
        fun getInstance(context: Context): DrinkDataBase {
            if (instance == null) {

                val MIGRATION_1_2 = object : Migration(1, 2) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("ALTER TABLE ${Table.COCKTAIL} ADD COLUMN date_modified INTEGER")
                        database.execSQL("ALTER TABLE ${Table.COCKTAIL} ADD COLUMN date_saved INTEGER")
                    }
                }

                instance = Room.databaseBuilder(
                    context.applicationContext,
                    DrinkDataBase::class.java, "CocktailDb"
                )
                    .fallbackToDestructiveMigration()
                    //вставля в базу даних значення None для інгредієнтів

                    .addMigrations(MIGRATION_1_2)
                    .addCallback(object : Callback() {
                        override fun onCreate(@NonNull db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val values = ContentValues()
                            values.put(
                                "ingredient",
                                "None"
                            )
                            db.insert(
                                Table.INGREDIENT,
                                SQLiteDatabase.CONFLICT_REPLACE,
                                values
                            )
                        }
                    })
                    .build()
                    .also { instance = it }
            }
            return instance!!
        }
    }
}
