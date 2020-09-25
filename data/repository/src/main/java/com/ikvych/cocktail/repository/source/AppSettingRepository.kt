package com.ikvych.cocktail.repository.source

import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.repository.source.base.BaseRepository

interface AppSettingRepository : BaseRepository {
    val showNavigationBarTitleLiveData: MutableLiveData<Boolean>
    val showBatteryStateLiveData: MutableLiveData<Boolean>
    val selectedLanguageLiveData: MutableLiveData<Int>
}