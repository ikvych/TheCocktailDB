package com.ikvych.cocktail.dataTest.repository.impl

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.dataTest.local.impl.source.AppSettingLocalSourceImpl
import com.ikvych.cocktail.dataTest.local.source.AppSettingLocalSource
import com.ikvych.cocktail.dataTest.repository.AppSettingRepository
import com.ikvych.cocktail.util.SingletonHolder

class AppSettingRepositoryImpl private constructor(
    private val localSource: AppSettingLocalSource
) : AppSettingRepository {

    override val mutableLiveData: MutableLiveData<Boolean> = localSource.showNavigationTitleLiveData

    companion object: SingletonHolder<AppSettingRepository, Context>(
        {
            AppSettingRepositoryImpl(localSource = AppSettingLocalSourceImpl.instance(it))
        }
    )

}
