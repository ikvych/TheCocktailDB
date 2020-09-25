package com.ikvych.cocktail.repository.source

import androidx.lifecycle.LiveData
import com.ikvych.cocktail.repository.source.base.BaseRepository

interface TokenRepository : BaseRepository {

    val tokenLiveData: LiveData<String?>
    var token: String?

}