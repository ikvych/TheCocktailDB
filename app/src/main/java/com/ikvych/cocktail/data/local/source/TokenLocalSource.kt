package com.ikvych.cocktail.data.local.source

import androidx.lifecycle.LiveData

interface TokenLocalSource {
    val tokenLiveData: LiveData<String?>
    var token: String?
}