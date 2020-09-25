package com.ikvych.cocktail.prresentation.mapper.cocktail

import com.ikvych.cocktail.prresentation.mapper.base.BaseModelMapper
import com.ikvych.cocktail.prresentation.model.cocktail.LocalizedStringModel
import com.ikvych.cocktail.repository.model.cocktail.LocalizedStringRepoModel

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