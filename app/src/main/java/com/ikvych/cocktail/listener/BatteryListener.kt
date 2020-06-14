package com.ikvych.cocktail.listener

import android.content.Intent

interface BatteryListener {
    fun onBatteryChange(intent: Intent)
}