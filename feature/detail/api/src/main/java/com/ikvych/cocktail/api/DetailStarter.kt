package com.ikvych.cocktail.api


interface DetailStarter {
    fun startDetailById(extraKey: String, cocktailId: Long, vararg extras: Pair<String, String>)
    fun startDetailWithModel(extraKey: String, cocktail: Any, vararg extras: Pair<String, String>)
}