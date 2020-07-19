package com.ikvych.cocktail.data.repository.impl.mapper

import com.ikvych.cocktail.data.db.model.LocalizedCocktailDbModel
import com.ikvych.cocktail.data.network.model.CocktailNetModel
import com.ikvych.cocktail.data.repository.impl.mapper.base.BaseRepoModelMapper
import com.xtreeivi.cocktailsapp.data.repository.model.LocalizedStringRepoModel

class LocalizedStringRepoModelMapper :
    BaseRepoModelMapper<LocalizedStringRepoModel, LocalizedCocktailDbModel, CocktailNetModel>() {
/*    override fun mapDbToRepo(db: LocalizedStringDbModel) = with(db) {
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
            defaults = defaultName,
            defaultAlternate = defaultAlternate,
            es = es,
            de = de,
            fr = fr,
            zhHans = zhHans,
            zhHant = zhHant
        )
    }*/

/*    override fun mapNamesNetToRepo(net: CocktailNetModel): LocalizedStringRepoModel = with(net) {
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

    override fun mapNamesRepoToNet(repo: LocalizedStringRepoModel) = with(repo) {
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

    override fun mapInstructionNetToRepo(net: CocktailNetModel): LocalizedStringRepoModel = with(net) {
        LocalizedStringRepoModel(
            defaults = strInstructions,
            es = strInstructionsES,
            de = strInstructionsDE,
            fr = strInstructionsFR,
            zhHans = strInstructionsZHHANS,
            zhHant = strInstructionsZHHANT
        )
    }

    override fun mapInstructionRepoToNet(repo: LocalizedStringRepoModel) = with(repo) {
        CocktailNetModel().apply {
            strInstructions = defaults
            strInstructionsES = es
            strInstructionsDE = de
            strInstructionsFR = fr
            strInstructionsZHHANS = zhHans
            strInstructionsZHHANT = zhHant
        }
    }*/
}