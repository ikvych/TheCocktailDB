package com.ikvych.cocktail.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "ingredient")
data class Ingredient(

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @SerializedName("strIngredient1")
    @ColumnInfo(name = "str_ingredient")
    var strIngredient1: String? = null
)