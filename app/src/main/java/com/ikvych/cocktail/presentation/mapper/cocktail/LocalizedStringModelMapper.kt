package com.ikvych.cocktail.presentation.mapper.cocktail

import com.ikvych.cocktail.presentation.mapper.base.BaseModelMapper
import com.ikvych.cocktail.presentation.model.cocktail.LocalizedStringModel
import com.xtreeivi.cocktailsapp.data.repository.model.LocalizedStringRepoModel

class LocalizedStringModelMapper : BaseModelMapper<LocalizedStringModel, LocalizedStringRepoModel>() {
    override fun mapTo(model: LocalizedStringRepoModel) = with(model) {
        LocalizedStringModel(
            defaultName = defaultName,
            defaultAlternate = defaultAlternate,
            es = es,
            de = de,
            fr = fr,
            zhHans = zhHans,
            zhHant = zhHant
        )
    }

    override fun mapFrom(model: LocalizedStringModel) = with(model) {
        LocalizedStringRepoModel(
            defaultName = defaultName,
            defaultAlternate = defaultAlternate,
            es = es,
            de = de,
            fr = fr,
            zhHans = zhHans,
            zhHant = zhHant
        )
    }
}