package com.ikvych.cocktail.preference.source

import androidx.lifecycle.LiveData
import com.ikvych.cocktail.locale.source.TokenLocalSource
import com.ikvych.cocktail.preference.SharedPrefsHelper
import com.ikvych.cocktail.preference.source.base.BaseLocalSourceImpl

class TokenLocalSourceImpl(
    sharedPrefsHelper: SharedPrefsHelper
) : BaseLocalSourceImpl(sharedPrefsHelper), TokenLocalSource {

    override val tokenLiveData: LiveData<String?> = sharedPrefLiveData(TOKEN, "")

    override var token: String? = sharedPrefsHelper.get(TOKEN, "")
        get() = sharedPrefsHelper.get(TOKEN, field)
        set(value) {
            sharedPrefsHelper.set(TOKEN, value)
        }

    companion object {
        const val TOKEN = "TOKEN"
    }
}