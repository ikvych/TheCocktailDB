package com.ikvych.cocktail.repository.impl.source

import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.locale.source.AppSettingLocalSource
import com.ikvych.cocktail.repository.impl.source.base.BaseRepositoryImpl
import com.ikvych.cocktail.repository.source.AppSettingRepository

class AppSettingRepositoryImpl (
    private val localSource: AppSettingLocalSource
) : BaseRepositoryImpl(), AppSettingRepository {

    override val showNavigationBarTitleLiveData: MutableLiveData<Boolean> = localSource.showNavigationTitleLiveData
    override val showBatteryStateLiveData: MutableLiveData<Boolean> = localSource.showBatteryStateLiveData
    override val selectedLanguageLiveData: MutableLiveData<Int> = localSource.selectedLanguageLiveData

}
