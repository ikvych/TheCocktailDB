package com.ikvych.cocktail.ui.base

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ikvych.cocktail.receiver.FlyModeReceiver


abstract class BaseActivity : AppCompatActivity() {

    private val flyModeReceiver: FlyModeReceiver = FlyModeReceiver()

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        }
        registerReceiver(flyModeReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(flyModeReceiver)
    }
}