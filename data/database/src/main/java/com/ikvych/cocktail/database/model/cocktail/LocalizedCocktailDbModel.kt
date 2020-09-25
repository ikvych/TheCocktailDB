package com.ikvych.cocktail.database.model.cocktail

import androidx.room.Embedded
import androidx.room.Relation
import com.ikvych.cocktail.database.model.cocktail.entity.CocktailDbModel
import com.ikvych.cocktail.database.model.cocktail.entity.IngredientMeasureDbModel
import com.ikvych.cocktail.database.model.cocktail.entity.LocalizedInstructionDbModel
import com.ikvych.cocktail.database.model.cocktail.entity.LocalizedNameDbModel

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