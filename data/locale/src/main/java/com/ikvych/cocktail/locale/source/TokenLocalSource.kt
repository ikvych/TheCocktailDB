package com.ikvych.cocktail.locale.source

import androidx.lifecycle.LiveData

interface TokenLocalSource {
    val tokenLiveData: LiveData<String?>
    var token: String?
}