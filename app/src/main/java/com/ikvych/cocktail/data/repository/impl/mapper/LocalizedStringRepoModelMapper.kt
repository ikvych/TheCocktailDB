package com.ikvych.cocktail.data.repository.impl.mapper

import com.ikvych.cocktail.data.network.model.CocktailNetModel
import com.ikvych.cocktail.data.repository.impl.mapper.base.BaseRepoModelMapper
import com.xtreeivi.cocktailsapp.data.db.model.LocalizedStringDbModel
import com.xtreeivi.cocktailsapp.data.repository.model.LocalizedStringRepoModel

class LocalizedStringRepoModelMapper :
    BaseRepoModelMapper<LocalizedStringRepoModel, LocalizedStringDbModel, CocktailNetModel>() {
    override fun mapDbToRepo(db: LocalizedStringDbModel) = with(db) {
        LocalizedStringRepoModel(
            defaults = defaults,
            defaultAlternate = defaultAlternate,
            es = es,
            de = de,
            fr = fr,
            zhHans = zhHans,
            zhHant = zhHant
        )
    }

    override fun mapRepoToDb(repo: LocalizedStringRepoModel) = with(repo) {
        LocalizedStringDbModel(
            defaults = defaults,
            defaultAlternate = defaultAlternate,
            es = es,
            de = de,
            fr = fr,
            zhHans = zhHans,
            zhHant = zhHant
        )
    }

    override fun mapNetToRepo(net: CocktailNetModel): LocalizedStringRepoModel = with(net) {
        LocalizedStringRepoModel(
            defaults = strDrink,
            defaultAlternate = strDrinkAlternate,
            es = strDrinkES,
            de = strDrinkDE,
            fr = strDrinkFR,
            zhHans = strDrinkZHHANS,
            zhHant = strDrinkZHHANT
        )
    }

    override fun mapRepoToNet(repo: LocalizedStringRepoModel) = with(repo) {
        CocktailNetModel().apply {
            strDrink = defaults
            strDrinkAlternate = defaultAlternate
            strDrinkES = es
            strDrinkDE = de
            strDrinkFR = fr
            strDrinkZHHANS = zhHans
            strDrinkZHHANT = zhHant
        }
    }
}