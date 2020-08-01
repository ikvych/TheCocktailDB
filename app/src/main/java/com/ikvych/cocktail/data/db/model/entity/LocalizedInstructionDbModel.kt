package com.ikvych.cocktail.data.db.model.entity

import androidx.room.*
import com.ikvych.cocktail.data.db.Table

@Entity(
    tableName = Table.INSTRUCTION,
    foreignKeys = [
        ForeignKey(
            entity = CocktailDbModel::class,
            parentColumns = ["id"],
            childColumns = ["cocktailOwnerId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["defaultsName", "cocktailOwnerId"]
)
data class LocalizedInstructionDbModel(

    @ColumnInfo(name = "defaultsName")
    var defaultsName: String = "",

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