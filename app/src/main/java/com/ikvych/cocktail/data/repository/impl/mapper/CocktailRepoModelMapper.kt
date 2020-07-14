package com.ikvych.cocktail.data.repository.impl.mapper

import com.ikvych.cocktail.data.network.model.CocktailNetModel
import com.ikvych.cocktail.data.repository.impl.mapper.base.BaseRepoModelMapper
import com.xtreeivi.cocktailsapp.data.db.model.CocktailDbModel
import com.xtreeivi.cocktailsapp.data.repository.model.CocktailRepoModel

class CocktailRepoModelMapper(
    private val localizedStringRepoModelMapper: LocalizedStringRepoModelMapper
): BaseRepoModelMapper<CocktailRepoModel, CocktailDbModel, CocktailNetModel>() {
    override fun mapDbToRepo(db: CocktailDbModel): CocktailRepoModel = with(db) {
        CocktailRepoModel(
            id = id,
            names = names.run(localizedStringRepoModelMapper::mapDbToRepo),
            category = category,
            alcoholType = alcoholType,
            glass = glass,
            image = image,
            instructions = instructions.run(localizedStringRepoModelMapper::mapDbToRepo),
            ingredients = ingredients,
            measures = measures
        )
    }

    override fun mapRepoToDb(repo: CocktailRepoModel): CocktailDbModel = with(repo) {
        CocktailDbModel(
            id = id,
            names = names.run(localizedStringRepoModelMapper::mapRepoToDb),
            category = category,
            alcoholType = alcoholType,
            glass = glass,
            image = image,
            instructions = instructions.run(localizedStringRepoModelMapper::mapRepoToDb),
            ingredients = ingredients,
            measures = measures
        )
    }

    override fun mapNetToRepo(net: CocktailNetModel) = with(net) {
        CocktailRepoModel(
            id = idDrink!!,
            names = this.run(localizedStringRepoModelMapper::mapNetToRepo),
            category = strCategory!!,
            alcoholType = strAlcoholic!!,
            glass = strGlass!!,
            image = strDrinkThumb!!,
            instructions = this.run(localizedStringRepoModelMapper::mapNetToRepo),
            ingredients = getAllIngredients().keys.toList(),
            measures = getAllIngredients().values.toList()
        )
    }

    override fun mapRepoToNet(repo: CocktailRepoModel) = with(repo) {
        CocktailNetModel().apply {  }
    }
}