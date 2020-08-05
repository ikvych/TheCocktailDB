package com.ikvych.cocktail.data.db.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.ikvych.cocktail.data.db.model.entity.*

data class LocalizedCocktailDbModel(
    @Embedded val cocktailDbModel: CocktailDbModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "cocktailOwnerId"
    )
    val localizedInstructionDbModel: LocalizedInstructionDbModel = LocalizedInstructionDbModel(
        cocktailOwnerId = cocktailDbModel.id
    ),

    @Relation(
        parentColumn = "id",
        entityColumn = "cocktailOwnerId"
    )
    val localizedNameDbModel: LocalizedNameDbModel = LocalizedNameDbModel(
        cocktailOwnerId = cocktailDbModel.id
    ),


    @Relation(
        parentColumn = "id",
        entityColumn = "cocktail"
    )
    val ingredientsWithMeasures: List<IngredientMeasureDbModel> = arrayListOf()

)