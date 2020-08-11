package com.ikvych.cocktail.data.repository.impl.mapper.cocktail

import com.ikvych.cocktail.data.db.model.cocktail.LocalizedCocktailDbModel
import com.ikvych.cocktail.data.network.model.cocktail.CocktailNetModel
import com.ikvych.cocktail.data.repository.impl.mapper.base.BaseRepoModelMapper
import com.xtreeivi.cocktailsapp.data.repository.model.LocalizedStringRepoModel

class LocalizedStringRepoModelMapper :
    BaseRepoModelMapper<LocalizedStringRepoModel, LocalizedCocktailDbModel, CocktailNetModel>() {

}