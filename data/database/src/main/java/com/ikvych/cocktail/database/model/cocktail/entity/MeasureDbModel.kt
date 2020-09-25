package com.ikvych.cocktail.database.model.cocktail.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ikvych.cocktail.database.Table

@Entity(tableName = Table.MEASURE)
data class MeasureDbModel(
    @PrimaryKey
    @ColumnInfo(name = "measure")
    var measure: String
)