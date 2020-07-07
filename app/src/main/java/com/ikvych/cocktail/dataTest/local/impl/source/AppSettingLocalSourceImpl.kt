package com.ikvych.cocktail.dataTest.local.impl.source

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.dataTest.local.impl.SharedPrefsHelper
import com.ikvych.cocktail.dataTest.local.source.AppSettingLocalSource
import com.ikvych.cocktail.util.SingletonHolder

const val EXTRA_KEY_SHOW_NAV_BAR_TITLE = "EXTRA_KEY_SHOW_NAV_BAR_TITLE"

class AppSettingLocalSourceImpl(
    private val sharedPrefsHelper: SharedPrefsHelper
) : AppSettingLocalSource{

    override val showNavigationTitleLiveData: MutableLiveData<Boolean> = object : MutableLiveData<Boolean>(sharedPrefsHelper.getBoolean(EXTRA_KEY_SHOW_NAV_BAR_TITLE, false)) {

        override fun getValue(): Boolean? {
            return sharedPrefsHelper.getBoolean(EXTRA_KEY_SHOW_NAV_BAR_TITLE, false)
        }

        override fun setValue(value: Boolean?) {
            super.setValue(value)
            sharedPrefsHelper.putBoolean(EXTRA_KEY_SHOW_NAV_BAR_TITLE, value ?: false)
        }
    }

    companion object: SingletonHolder<AppSettingLocalSourceImpl, Context>(
        {
            AppSettingLocalSourceImpl(SharedPrefsHelper(it.getSharedPreferences("Some", Context.MODE_PRIVATE)))
        }
    )
}