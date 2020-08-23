package com.ikvych.cocktail.data.repository.impl.mapper.cocktail

import com.ikvych.cocktail.data.db.model.cocktail.LocalizedCocktailDbModel
import com.ikvych.cocktail.data.db.model.cocktail.entity.*
import com.ikvych.cocktail.data.network.model.cocktail.CocktailNetModel
import com.ikvych.cocktail.data.repository.impl.mapper.base.BaseRepoModelMapper
import com.ikvych.cocktail.data.repository.model.cocktail.IngredientRepoModel
import com.xtreeivi.cocktailsapp.data.repository.model.CocktailRepoModel
import com.xtreeivi.cocktailsapp.data.repository.model.LocalizedStringRepoModel
import java.text.SimpleDateFormat
import java.util.*

class CocktailRepoModelMapper(
    private val localizedStringRepoModelMapper: LocalizedStringRepoModelMapper
) : BaseRepoModelMapper<CocktailRepoModel, LocalizedCocktailDbModel, CocktailNetModel>() {
    override fun mapDbToRepo(db: LocalizedCocktailDbModel): CocktailRepoModel = with(db) {
        CocktailRepoModel(
            id = cocktailDbModel.id,
            names = LocalizedStringRepoModel(
                defaultName = localizedNameDbModel.defaultName,
                defaultAlternate = localizedNameDbModel.defaultAlternate,
                zhHant = localizedNameDbModel.zhHant,
                zhHans = localizedNameDbModel.zhHans,
                fr = localizedNameDbModel.fr,
                es = localizedNameDbModel.es,
                de = localizedNameDbModel.de
            ),
            category = cocktailDbModel.category,
            alcoholType = cocktailDbModel.alcoholType,
            glass = cocktailDbModel.glass,
            image = cocktailDbModel.image,
            instructions = LocalizedStringRepoModel(
                defaultName = localizedInstructionDbModel.defaultsName,
                defaultAlternate = localizedInstructionDbModel.defaultAlternate,
                zhHant = localizedInstructionDbModel.zhHant,
                zhHans = localizedInstructionDbModel.zhHans,
                fr = localizedInstructionDbModel.fr,
                es = localizedInstructionDbModel.es,
                de = localizedInstructionDbModel.de
            ),
            ingredients = ingredientsWithMeasures.map {
                IngredientRepoModel(
                    it.ingredient
                )
            },
            measures = ingredientsWithMeasures.map { it.measure },
            cocktailOfTheDay = cocktailDbModel.cocktailOfDay,
            isFavorite = cocktailDbModel.isFavorite,
            dateModified = cocktailDbModel.dateModified ?: Date(),
            dateSaved = cocktailDbModel.dateSaved
        )
    }

    override fun mapRepoToDb(repo: CocktailRepoModel): LocalizedCocktailDbModel = with(repo) {
        LocalizedCocktailDbModel(
            cocktailDbModel = CocktailDbModel(
                id = id,
                category = category,
                alcoholType = alcoholType,
                glass = glass,
                image = image,
                cocktailOfDay = cocktailOfTheDay,
                isFavorite = isFavorite,
                dateModified = dateModified,
                dateSaved = dateSaved
            ),
            localizedInstructionDbModel = LocalizedInstructionDbModel(
                defaultsName = instructions.defaultName!!,
                cocktailOwnerId = id,
                de = instructions.de,
                defaultAlternate = instructions.defaultAlternate,
                es = instructions.es,
                fr = instructions.fr,
                zhHans = instructions.zhHans,
                zhHant = instructions.zhHant
            ),
            localizedNameDbModel = LocalizedNameDbModel(
                defaultName = names.defaultName!!,
                cocktailOwnerId = id,
                de = names.de,
                defaultAlternate = names.defaultAlternate,
                es = names.es,
                fr = names.fr,
                zhHans = names.zhHans,
                zhHant = names.zhHant
            ),
            ingredientsWithMeasures = ingredients.mapIndexed { index, ingredientRepoModel ->
                IngredientMeasureDbModel(
                    this.id,
                    ingredientRepoModel.ingredient,
                    measures[index]
                )
            }
        )
    }

    override fun mapNetToRepo(net: CocktailNetModel) = with(net) {
        CocktailRepoModel(
            id = id,
            names = LocalizedStringRepoModel(
                names.defaultName,
                names.defaultAlternate,
                names.es,
                names.de,
                names.fr,
                names.zhHans,
                names.zhHant
            ),
            category = category,
            alcoholType = alcoholType,
            glass = glass,
            image = image,
            instructions = LocalizedStringRepoModel(
                instructions.defaultName,
                null,
                instructions.defaultAlternate,
                instructions.de,
                instructions.fr,
                instructions.zhHans,
                instructions.zhHant
            ),
            ingredients = ingredientsWithMeasures.map {
                IngredientRepoModel(
                    it.key
                )
            },
            measures = ingredientsWithMeasures.map { it.value },
            dateModified = date
        )
    }

    override fun mapRepoToNet(repo: CocktailRepoModel) = with(repo) {
        CocktailNetModel().apply { }
    }
}