package com.ikvych.cocktail.database.model.cocktail.entity

import androidx.room.*
import com.ikvych.cocktail.database.Table
import java.util.*

@Entity(
    tableName = Table.COCKTAIL
)
data class CocktailDbModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long = -1L,

    @ColumnInfo(name = "cocktail_of_day")
    var cocktailOfDay: String = "",

    @ColumnInfo(name = "category")
    val category: String = "",

    @ColumnInfo(name = "alcohol_type")
    val alcoholType: String = "",

    @ColumnInfo(name = "glass")
    val glass: String = "",

    @ColumnInfo(name = "image")
    val image: String = "",

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,

    @ColumnInfo(name = "date_modified")
    val dateModified: Date? = Date(),

    @ColumnInfo(name = "date_saved")
    val dateSaved: Date? = Date()
)