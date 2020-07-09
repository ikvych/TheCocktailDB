package com.ikvych.cocktail.dataTest.repository

import androidx.lifecycle.MutableLiveData

interface AppSettingRepository {
    val showNavigationBarTitleLiveData: MutableLiveData<Boolean>
    val showBatteryStateLiveData: MutableLiveData<Boolean>
    val selectedLanguageLiveData: MutableLiveData<Int>
}