package com.ikvych.cocktail.ui.mapper

import com.ikvych.cocktail.ui.mapper.base.BaseModelMapper
import com.ikvych.cocktail.ui.model.cocktail.LocalizedStringModel
import com.xtreeivi.cocktailsapp.data.repository.model.LocalizedStringRepoModel

class LocalizedStringModelMapper : BaseModelMapper<LocalizedStringModel, LocalizedStringRepoModel>() {
    override fun mapTo(model: LocalizedStringRepoModel) = with(model) {
        LocalizedStringModel(
            defaults = defaults,
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
            defaults = defaults,
            defaultAlternate = defaultAlternate,
            es = es,
            de = de,
            fr = fr,
            zhHans = zhHans,
            zhHant = zhHant
        )
    }
}