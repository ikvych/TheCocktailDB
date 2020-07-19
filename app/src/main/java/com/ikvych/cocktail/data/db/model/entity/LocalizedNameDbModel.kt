package com.ikvych.cocktail.data.db.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ikvych.cocktail.data.db.Table

@Entity(tableName = Table.NAME)
data class LocalizedNameDbModel(

    @PrimaryKey
    @ColumnInfo(name = "default_name")
    var defaultsName: String,

    val cocktailOwnerId: Long,

    @ColumnInfo(name = "default_alternate")
    var defaultAlternate: String? = null,

    @ColumnInfo(name = "es")
    var es: String? = null,

    @ColumnInfo(name = "de")
    var de: String? = null,

    @ColumnInfo(name = "fr")
    var fr: String? = null,

    @ColumnInfo(name = "zn_hans")
    var zhHans: String? = null,

    @ColumnInfo(name = "zn_hant")
    var zhHant: String? = null
)