package com.ikvych.cocktail.data.db.model.cocktail.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ikvych.cocktail.data.db.Table

@Entity(tableName = Table.MEASURE)
data class MeasureDbModel(
    @PrimaryKey
    @ColumnInfo(name = "measure")
    var measure: String
)