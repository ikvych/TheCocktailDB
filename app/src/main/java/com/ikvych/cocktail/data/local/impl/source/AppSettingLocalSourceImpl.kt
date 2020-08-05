package com.ikvych.cocktail.data.local.impl.source

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.data.local.impl.SharedPrefsHelper
import com.ikvych.cocktail.data.local.impl.source.base.BaseLocalSourceImpl
import com.ikvych.cocktail.data.local.source.AppSettingLocalSource
import com.ikvych.cocktail.util.SingletonHolder

const val EXTRA_KEY_SHOW_NAV_BAR_TITLE = "EXTRA_KEY_SHOW_NAV_BAR_TITLE"
const val EXTRA_KEY_SHOW_BATTERY_STATE = "EXTRA_KEY_SHOW_BATTERY_STATE"
const val EXTRA_KEY_SELECTED_SYSTEM_LANGUAGE = "EXTRA_KEY_SELECTED_SYSTEM_LANGUAGE"

class AppSettingLocalSourceImpl(
    sharedPrefsHelper: SharedPrefsHelper
) : BaseLocalSourceImpl(sharedPrefsHelper), AppSettingLocalSource{

    override val showNavigationTitleLiveData: MutableLiveData<Boolean> = object : MutableLiveData<Boolean>() {

        init { value = value }

        override fun getValue(): Boolean? {
            return sharedPrefsHelper.getBoolean(EXTRA_KEY_SHOW_NAV_BAR_TITLE, true)
        }

        override fun setValue(value: Boolean?) {
            super.setValue(value)
            sharedPrefsHelper.putBoolean(EXTRA_KEY_SHOW_NAV_BAR_TITLE, value ?: true)
        }
    }

    override val showBatteryStateLiveData: MutableLiveData<Boolean> = object : MutableLiveData<Boolean>() {

        init { value = value }

        override fun getValue(): Boolean? {
            return sharedPrefsHelper.getBoolean(EXTRA_KEY_SHOW_BATTERY_STATE, true)
        }

        override fun setValue(value: Boolean?) {
            super.setValue(value)
            sharedPrefsHelper.putBoolean(EXTRA_KEY_SHOW_BATTERY_STATE, value ?: true)
        }
    }

    override val selectedLanguageLiveData: MutableLiveData<Int> = object : MutableLiveData<Int>() {

        init { value = value }

        override fun getValue(): Int? {
            return sharedPrefsHelper.getInt(EXTRA_KEY_SELECTED_SYSTEM_LANGUAGE, 0)
        }

        override fun setValue(value: Int?) {
            super.setValue(value)
            sharedPrefsHelper.putInt(EXTRA_KEY_SELECTED_SYSTEM_LANGUAGE, value ?: 0)
        }
    }

    companion object: SingletonHolder<AppSettingLocalSourceImpl, Context>(
        {
            AppSettingLocalSourceImpl(SharedPrefsHelper(it.getSharedPreferences("Some", Context.MODE_PRIVATE)))
        }
    )
}