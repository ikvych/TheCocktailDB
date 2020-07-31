package com.ikvych.cocktail.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.lifecycle.LiveData

class BatteryStateLiveData(
    val context: Context
) : LiveData<CachedBatteryState>() {

    private var receiver: BroadcastReceiver? = null
    val cachedBatteryState = CachedBatteryState.instance

    private fun prepareReceiver(context: Context) {
        val batteryReceiverFilter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_CHANGED)
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(Intent.ACTION_BATTERY_OKAY)
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    Intent.ACTION_BATTERY_LOW -> cachedBatteryState.isLow = true
                    Intent.ACTION_BATTERY_OKAY -> cachedBatteryState.isLow = false
                    Intent.ACTION_BATTERY_CHANGED -> {
                        val status: Int = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                        cachedBatteryState.isCharging =
                            status == BatteryManager.BATTERY_STATUS_CHARGING
                                    || status == BatteryManager.BATTERY_STATUS_FULL
                        cachedBatteryState.percent = intent.let {
                            val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                            val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                            level * 100 / scale.toFloat()
                        }.toInt()
                    }
                }
                value = cachedBatteryState
            }
        }
        context.registerReceiver(receiver, batteryReceiverFilter)
    }

    override fun onActive() {
        super.onActive()
        prepareReceiver(context)
    }

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(receiver)
        receiver = null
    }
}