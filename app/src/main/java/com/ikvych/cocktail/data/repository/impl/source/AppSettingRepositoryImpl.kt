package com.ikvych.cocktail.data.repository.impl.source

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.data.local.impl.source.AppSettingLocalSourceImpl
import com.ikvych.cocktail.data.local.source.AppSettingLocalSource
import com.ikvych.cocktail.data.repository.impl.source.base.BaseRepositoryImpl
import com.ikvych.cocktail.data.repository.source.AppSettingRepository
import com.ikvych.cocktail.data.repository.source.base.BaseRepository
import com.ikvych.cocktail.util.SingletonHolder

class AppSettingRepositoryImpl private constructor(
    private val localSource: AppSettingLocalSource
) : BaseRepositoryImpl(), AppSettingRepository {

    override val showNavigationBarTitleLiveData: MutableLiveData<Boolean> = localSource.showNavigationTitleLiveData
    override val showBatteryStateLiveData: MutableLiveData<Boolean> = localSource.showBatteryStateLiveData
    override val selectedLanguageLiveData: MutableLiveData<Int> = localSource.selectedLanguageLiveData

    companion object: SingletonHolder<AppSettingRepository, Context>(
        {
            AppSettingRepositoryImpl(localSource = AppSettingLocalSourceImpl.instance(it))
        }
    )

}
