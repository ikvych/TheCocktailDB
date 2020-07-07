package com.ikvych.cocktail.dataTest.local.source

import androidx.lifecycle.MutableLiveData

interface AppSettingLocalSource {
    val showNavigationTitleLiveData: MutableLiveData<Boolean>
    val showBatteryStateLiveData: MutableLiveData<Boolean>
}