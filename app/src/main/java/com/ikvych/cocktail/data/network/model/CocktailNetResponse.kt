package com.ikvych.cocktail.data.network.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class CocktailNetResponse {

    @SerializedName("drinks")
    @Expose
    var cocktails: List<CocktailNetModel> = arrayListOf()
}