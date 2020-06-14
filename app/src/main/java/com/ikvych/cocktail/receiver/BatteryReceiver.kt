package com.ikvych.cocktail.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ikvych.cocktail.listener.BatteryListener

class BatteryReceiver(
    private val batteryListener: BatteryListener
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        batteryListener.onBatteryChange(intent!!)
    }

}