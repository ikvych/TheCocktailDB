package com.ikvych.cocktail.repository.impl.mapper.cocktail

import com.ikvych.cocktail.database.model.cocktail.LocalizedCocktailDbModel
import com.ikvych.cocktail.network.model.cocktail.CocktailNetModel
import com.ikvych.cocktail.repository.impl.mapper.base.BaseRepoModelMapper
import com.ikvych.cocktail.repository.model.cocktail.LocalizedStringRepoModel

class LocalizedStringRepoModelMapper :
    BaseRepoModelMapper<LocalizedStringRepoModel, LocalizedCocktailDbModel, CocktailNetModel>() {

}