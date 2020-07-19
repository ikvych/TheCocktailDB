package com.ikvych.cocktail.data.db.model.entity

import androidx.room.*
import com.ikvych.cocktail.data.db.Table

@Entity(
    tableName = Table.NAME,
    foreignKeys = [
        ForeignKey(
            entity = CocktailDbModel::class,
            parentColumns = ["id"],
            childColumns = ["cocktailOwnerId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["cocktailOwnerId"], unique = true)]
)
data class LocalizedNameDbModel(

    @PrimaryKey
    @ColumnInfo(name = "defaults_name")
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