package com.ikvych.cocktail.data.local.source

import androidx.lifecycle.MutableLiveData

interface AppSettingLocalSource {
    val showNavigationTitleLiveData: MutableLiveData<Boolean>
    val showBatteryStateLiveData: MutableLiveData<Boolean>
    val selectedLanguageLiveData: MutableLiveData<Int>
}