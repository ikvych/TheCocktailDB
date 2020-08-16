package com.ikvych.cocktail.data.network.model.cocktail

import com.google.gson.annotations.SerializedName


class CocktailNetResponse {
    @SerializedName("drinks")
    var cocktails: List<CocktailNetModel> = arrayListOf()
}