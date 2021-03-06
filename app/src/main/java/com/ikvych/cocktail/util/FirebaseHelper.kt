package com.ikvych.cocktail.util

import android.content.Context
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.ikvych.cocktail.R

class FirebaseHelper private constructor(
    private val context: Context
) {

    //FirebaseInstanceId
    private val firebaseInstanceId = FirebaseInstanceId.getInstance()

    //FirebaseAnalytics
    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    //FirebaseRemoteConfig
    private val remoteConfig = FirebaseRemoteConfig.getInstance()
    private val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 0
    }
    init {
        firebaseInstanceId.instanceId.addOnCompleteListener {
            if (!it.isSuccessful) {
                Log.w("TOKEN_LOGS", "getInstanceId failed", it.exception)
                return@addOnCompleteListener
            }

            // Get new Instance ID token
            val token = it.result?.token

            // Log and toast
            Log.d("TOKEN_LOGS", token ?: "")
        }
        Log.d("TOKEN_LOGS", firebaseInstanceId.token ?: "")


        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_default)
    }

    fun fetchAndActivate(onCompleteListener: (Boolean, FirebaseRemoteConfig) -> Unit) {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val updated = it.result
                    onCompleteListener(updated, remoteConfig)
                } else {
                    Log.d("MyLOGS", "Fetch Failed")
                }
            }
    }

    fun logEvent(event: String, value: Bundle) {
        firebaseAnalytics.logEvent(
            event,
            value
        )
    }

    companion object {

        private var instance: FirebaseHelper? = null

        @Synchronized
        fun getInstance(context: Context): FirebaseHelper {
            if (instance == null) {
                instance = FirebaseHelper(context)
            }
            return instance!!
        }
    }
}