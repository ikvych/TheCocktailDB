package com.ikvych.cocktail.data.db.model

import androidx.room.Embedded
import androidx.room.Relation
import com.ikvych.cocktail.data.db.model.entity.CocktailDbModel
import com.ikvych.cocktail.data.db.model.entity.LocalizedInstructionDbModel
import com.ikvych.cocktail.data.db.model.entity.LocalizedNameDbModel

data class LocalizedCocktailDbModel(
    @Embedded val cocktailDbModel: CocktailDbModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "cocktailOwnerId"
    )
    val localizedInstructionDbModel: LocalizedInstructionDbModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "cocktailOwnerId"
    )
    val localizedNameDbModel: LocalizedNameDbModel
)