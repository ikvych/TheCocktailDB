package com.ikvych.cocktail.dataTest.repository

import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.dataTest.local.source.AppSettingLocalSource

interface AppSettingRepository {
    val showNavigationBarTitleLiveData: MutableLiveData<Boolean>
    val showBatteryStateLiveData: MutableLiveData<Boolean>
}