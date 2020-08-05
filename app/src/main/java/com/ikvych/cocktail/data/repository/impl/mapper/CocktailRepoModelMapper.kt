package com.ikvych.cocktail.data.repository.impl.mapper

import com.ikvych.cocktail.data.db.model.LocalizedCocktailDbModel
import com.ikvych.cocktail.data.db.model.entity.*
import com.ikvych.cocktail.data.network.model.CocktailNetModel
import com.ikvych.cocktail.data.repository.impl.mapper.base.BaseRepoModelMapper
import com.ikvych.cocktail.data.repository.model.IngredientRepoModel
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
            ingredients = ingredientsWithMeasures.map { IngredientRepoModel(it.ingredient) },
            measures = ingredientsWithMeasures.map { it.measure },
            cocktailOfTheDay = cocktailDbModel.cocktailOfDay,
            isFavorite = cocktailDbModel.isFavorite,
            dateModified = cocktailDbModel.dateModified ?: Date(),
            dateSaved = cocktailDbModel.dateSaved ?: Date()
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
                dateSaved = Date()
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
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        CocktailRepoModel(
            id = idDrink!!,
            names = LocalizedStringRepoModel(
                strDrink,
                strDrinkAlternate,
                strDrinkES,
                strDrinkDE,
                strDrinkFR,
                strDrinkZHHANS,
                strDrinkZHHANT
            ),
            category = strCategory!!,
            alcoholType = strAlcoholic!!,
            glass = strGlass!!,
            image = strDrinkThumb!!,
            instructions = LocalizedStringRepoModel(
                strInstructions,
                null,
                strInstructionsES,
                strInstructionsDE,
                strInstructionsFR,
                strInstructionsZHHANS,
                strInstructionsZHHANT
            ),
            ingredients = getAllIngredients().map { IngredientRepoModel(it.key) },
            measures = getAllIngredients().values.toList(),
            dateModified = formatter.parse(dateModified ?: "2020-02-12 13:45:30"),
            dateSaved = Date()
        )
    }

    override fun mapRepoToNet(repo: CocktailRepoModel) = with(repo) {
        CocktailNetModel().apply { }
    }
}