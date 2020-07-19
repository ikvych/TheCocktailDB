package com.ikvych.cocktail.data.repository.impl.mapper

import com.ikvych.cocktail.data.db.model.LocalizedCocktailDbModel
import com.ikvych.cocktail.data.db.model.entity.*
import com.ikvych.cocktail.data.network.model.CocktailNetModel
import com.ikvych.cocktail.data.repository.impl.mapper.base.BaseRepoModelMapper
import com.xtreeivi.cocktailsapp.data.repository.model.CocktailRepoModel
import com.xtreeivi.cocktailsapp.data.repository.model.LocalizedStringRepoModel

class CocktailRepoModelMapper(
    private val localizedStringRepoModelMapper: LocalizedStringRepoModelMapper
) : BaseRepoModelMapper<CocktailRepoModel, LocalizedCocktailDbModel, CocktailNetModel>() {
    override fun mapDbToRepo(db: LocalizedCocktailDbModel): CocktailRepoModel = with(db) {
        CocktailRepoModel(
            id = cocktailDbModel.id,
            names = LocalizedStringRepoModel(
                defaultName = localizedNameDbModel.defaultsName,
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
            ingredients = ingredients.map{it.ingredient},
            measures = measures.map{it.measure},
            cocktailOfTheDay = cocktailDbModel.cocktailOfDay,
            isFavorite = cocktailDbModel.isFavorite
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
                isFavorite = isFavorite
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
                defaultsName = names.defaultName!!,
                cocktailOwnerId = id,
                de = names.de,
                defaultAlternate = names.defaultAlternate,
                es = names.es,
                fr = names.fr,
                zhHans = names.zhHans,
                zhHant = names.zhHant
            ),
            ingredients = ingredients.map{IngredientDbModel(it)},
            measures = measures.map{MeasureDbModel(it)}
        )
    }

    override fun mapNetToRepo(net: CocktailNetModel) = with(net) {
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
            ingredients = getAllIngredients().keys.toList(),
            measures = getAllIngredients().values.toList()
        )
    }

    override fun mapRepoToNet(repo: CocktailRepoModel) = with(repo) {
        CocktailNetModel().apply { }
    }
}